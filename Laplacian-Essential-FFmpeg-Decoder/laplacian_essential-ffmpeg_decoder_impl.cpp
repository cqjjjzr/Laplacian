#include "charlie_laplacian_decoder_essential_FFmpegDecoder.h"

#include <queue>
#include <cassert>
#include <string>
#include <sstream>
using namespace std;

#include "shortcuts.h"
#include "error_msgs.h"
#include "customio.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
}

static const int MAX_AUDIO_FRAME_SIZE = 192000;
static const int SDL_AUDIO_BUFFER_SIZE = 1024;

extern "C"{
JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_seek
        (JNIEnv * env, jobject javaThis, jlong positionMillis) {
    jclass clazz = env->GetObjectClass(javaThis);

    AVRational position = {
            1, (int) (positionMillis / 1000)
    };
    AVCodecContext *pCodecCtx = getAVCodecContext(env, javaThis, clazz);
    int64_t timestamp = (int64_t) av_q2d(av_mul_q(pCodecCtx->time_base, position));
    av_seek_frame(getAVFormatContext(env, javaThis, clazz),
                  getAudioStreamIndex(env, clazz, javaThis), timestamp , AVSEEK_FLAG_BACKWARD);
    setPosition(env, javaThis, clazz, (long) positionMillis);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_closeInternal
        (JNIEnv * env, jobject javaThis) {
    //TODO close

    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis, env->GetObjectClass(javaThis));
    AVIOContext *pIOCtx = pFormatCtx->pb;
    avformat_close_input(&pFormatCtx);
    byte *pBuffer = pIOCtx->buffer;
    delete pBuffer;
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_playThread
        (JNIEnv * env, jobject javaThis) {
    jclass clazz = env->GetObjectClass(javaThis);
    jmethodID mixMethod = env->GetMethodID(clazz, "internalMix", "([BII)V");
    jclass lockClazz = env->FindClass("java/util/concurrent/locks/Lock");
    jmethodID lockMethod = env->GetMethodID(lockClazz, "lock", "()V");
    jmethodID unlockMethod = env->GetMethodID(lockClazz, "unlock", "()V");
    jmethodID waitMethod = env->GetMethodID(env->FindClass("java/util/concurrent/locks/Condition"), "await", "()V");
    jobject lockObject = env->GetObjectField(javaThis, env->GetFieldID(clazz, "pauseLock", "Ljava/util/concurrent/locks/Lock;"));
    jobject condObject = env->GetObjectField(javaThis, env->GetFieldID(clazz, "pauseCondition", "Ljava/util/concurrent/locks/Condition;"));
    jfieldID pausedField = env->GetFieldID(env->GetObjectClass(javaThis), "paused", "Z");
    jfieldID closedField = env->GetFieldID(env->GetObjectClass(javaThis), "closed", "Z");

    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis, clazz);
    AVCodecContext *pCodecCtx = getAVCodecContext(env, javaThis, clazz);
    SwrContext *pSwrCtx = swr_alloc();

    int bitDepth = env->GetIntField(javaThis, env->GetFieldID(clazz, "bitDepth", "I"));
    AVSampleFormat sampleFormat = bitDepth == 16 ? AV_SAMPLE_FMT_S16 : (bitDepth == 32 ? AV_SAMPLE_FMT_S32 : AV_SAMPLE_FMT_S64);

    pSwrCtx = swr_alloc_set_opts(pSwrCtx,
                                 av_get_default_channel_layout(env->GetIntField(javaThis, env->GetFieldID(clazz, "numChannel", "I"))),
                                 sampleFormat,
                                 (int) env->GetFloatField(javaThis, env->GetFieldID(clazz, "sampleRateHz", "F")),
                                 av_get_default_channel_layout(pCodecCtx->channels),
                                 pCodecCtx->sample_fmt,
                                 pCodecCtx->sample_rate,
                                 0, nullptr
    );
    swr_init(pSwrCtx);

    AVPacket packet;
    AVFrame *frame;
    int data_size;
    uint8_t *audio_buf = new uint8_t[(MAX_AUDIO_FRAME_SIZE * 3) / 2];
    //jbyteArray jBuf = env->NewByteArray((MAX_AUDIO_FRAME_SIZE * 3) / 2);
    int audioStreamIndex = getAudioStreamIndex(env, clazz, javaThis);
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (env->GetBooleanField(javaThis, closedField)) return;
        if (packet.stream_index == audioStreamIndex) {
            int ret = avcodec_send_packet(pCodecCtx, &packet);
            if (ret < 0 && ret != AVERROR(EAGAIN) && ret != AVERROR_EOF) {
                throw_retval_exception(env, ret, ERROR_MESSAGE_DECODE);
                return;
            }

            frame = av_frame_alloc();
            ret = avcodec_receive_frame(pCodecCtx, frame);
            if (ret < 0 && ret != AVERROR_EOF) {
                throw_retval_exception(env, ret, ERROR_MESSAGE_DECODE);
                return;
            }

            if (frame->channels > 0 && frame->channel_layout == 0)
                frame->channel_layout = (uint64_t) av_get_default_channel_layout(frame->channels);
            else if (frame->channels == 0 && frame->channel_layout > 0)
                frame->channels = av_get_channel_layout_nb_channels(frame->channel_layout);

            int64_t dst_nb_samples =
                    av_rescale_rnd(swr_get_delay(pSwrCtx, frame->sample_rate) +
                                           frame->nb_samples, frame->sample_rate, frame->sample_rate, AVRounding(1));
            int nb = swr_convert(pSwrCtx, &audio_buf, (int) dst_nb_samples, (const uint8_t**)frame->data, frame->nb_samples);
            data_size = frame->channels * nb * av_get_bytes_per_sample(sampleFormat);

            jbyteArray jBuf = env->NewByteArray(data_size);
            env->SetByteArrayRegion(jBuf, 0, data_size, (const jbyte *) audio_buf);
            if (env->ExceptionCheck()) {
                return;
            }

            if (env->GetBooleanField(javaThis, closedField)) return;
            env->CallVoidMethod(javaThis, mixMethod, jBuf, 0, data_size);
            av_frame_free(&frame);

            if (env->ExceptionCheck()) {
                return;
            }
        }
        else
            av_packet_unref(&packet);
        while (env->GetBooleanField(javaThis, pausedField)) {
            env->CallVoidMethod(lockObject, lockMethod);
            env->CallVoidMethod(condObject, waitMethod);
            env->CallVoidMethod(lockObject, unlockMethod);
        }

        if (env->GetBooleanField(javaThis, closedField)) return;
    }
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_initWithStream
        (JNIEnv * env, jobject javaThis, jobject stream) {
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    AVIOContext *pIOCtx = getIOContext(env, javaThis, stream);
    AVInputFormat *pInputFormat = nullptr;
    if (!pIOCtx) env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                               ERROR_MESSAGE_ALLOC_AVIOCONTEXT);

    pFormatCtx->flags |= AVFMT_FLAG_CUSTOM_IO | AVFMT_NOFILE;
	pFormatCtx->pb = pIOCtx;
    pFormatCtx->probesize = iBufSize;
    int retval = av_probe_input_buffer(pIOCtx, &pInputFormat, "", NULL, 0, iBufSize); // 亲妈爆炸啊，这里要把probe部分拆出来，不能让avformat_open_input去probe
    if (retval < 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_PROBE);
        return;
    }

    retval = avformat_open_input(&pFormatCtx, "", pInputFormat, nullptr);
    if (retval < 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_OPEN_INPUT);
        return;
    }

    setAVFormatContext(env, javaThis, env->GetObjectClass(javaThis), pFormatCtx);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_initWithURL
        (JNIEnv * env, jobject javaThis, jstring url) {
    const char* urlCString;
    urlCString = env->GetStringUTFChars(url, JNI_FALSE);
    AVFormatContext *pFormatCtx = nullptr;
    int retval = avformat_open_input(&pFormatCtx, urlCString, nullptr, nullptr);
    env->ReleaseStringUTFChars(url, urlCString);
    if (retval != 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_OPEN_INPUT);
    } else {
        setAVFormatContext(env, javaThis, env->GetObjectClass(javaThis), pFormatCtx);
    }
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_startupNativeLibs
        (JNIEnv * env, jobject javaThis, jfloat sampleRateHz, jint bitDepth, jint numChannels) {
    jclass clazz = env->GetObjectClass(javaThis);

    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis, clazz);

    CHECK_RETVAL(avformat_find_stream_info(pFormatCtx, nullptr), ERROR_MESSAGE_FIND_STREAM)

    int audioStreamIndex = -1;
    for (int i = 0;i < pFormatCtx->nb_streams;i++)
        if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioStreamIndex = i;
            break;
        }

    if (audioStreamIndex == -1) {
        env->ThrowNew(env->FindClass("charlie/laplacian/decoder/InvalidInputMediaException"),
                      ERROR_MESSAGE_NO_AUDIO_STREAM);
        return;
    }

    AVCodecParameters* pCodecPara = nullptr;
    AVCodecContext* pCodecCtx = nullptr;

    AVCodec* pCodec = nullptr;
    pCodecPara = pFormatCtx->streams[audioStreamIndex]->codecpar;

    pCodec = avcodec_find_decoder(pCodecPara->codec_id);

    if (!pCodec) {
        env->ThrowNew(env->FindClass("charlie/laplacian/decoder/UnsupportedCodecException"),
                      ERROR_MESSAGE_UNSUPPORTED_CODEC);
        return;
    }

    pCodecCtx = avcodec_alloc_context3(pCodec);

	CHECK_RETVAL(avcodec_parameters_to_context(pCodecCtx, pCodecPara), ERROR_MESSAGE_UNKNOWN);

    CHECK_RETVAL(avcodec_open2(pCodecCtx, pCodec, nullptr), ERROR_MESSAGE_UNKNOWN);

    setAVCodecContext(env, javaThis, clazz, pCodecCtx);
    setAudioStreamIndex(env, javaThis, clazz, audioStreamIndex);
    env->SetLongField(javaThis, env->GetFieldID(clazz, "duration", "J"),
                      getAVFormatContext(env, javaThis, clazz)->duration / 1000);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_globalInit
        (JNIEnv * env, jclass javaClass) {
    av_register_all();
    env->GetJavaVM(&javaVM);
    javaVMVersion = env->GetVersion();
    initClassesAndMethods(env);
}
}