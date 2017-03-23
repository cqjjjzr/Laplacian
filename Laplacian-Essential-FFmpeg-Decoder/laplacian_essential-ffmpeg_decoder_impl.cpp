#include "charlie_laplacian_decoder_essential_FFmpegDecoder.h"

#include <queue>
#include <cassert>
#include <string>
#include <sstream>
using namespace std;

#include "shortcuts.h"
#include "error_msgs.h"
#include "decode.h"
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
    AVRational position = {
            1, (int) (positionMillis / 1000)
    };
    AVCodecContext *pCodecCtx = getAVCodecContext(env, javaThis);
    int64_t timestamp = (int64_t) av_q2d(av_mul_q(pCodecCtx->time_base, position));
    av_seek_frame(getAVFormatContext(env, javaThis),
                  getAudioStreamIndex(env, javaThis), timestamp , AVSEEK_FLAG_BACKWARD);
    setPosition(env, javaThis, (long) positionMillis);
}

JNIEXPORT jlong JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_durationMillis
        (JNIEnv * env, jobject javaThis) {
    return getAVFormatContext(env, javaThis)->duration / 1000;
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_closeInternal
        (JNIEnv * env, jobject javaThis) {
    setPaused(env, javaThis, true);
    //TODO close

    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis);
    delete pFormatCtx->pb->buffer;
    av_free(pFormatCtx->pb);
    avformat_close_input(&pFormatCtx);
    setAVFormatContext(env, javaThis, nullptr);
    setAVCodecContext(env, javaThis, nullptr);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_playThread
        (JNIEnv * env, jobject javaThis) {
    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis);

    AVPacket packet;
    int audioStreamIndex = getAudioStreamIndex(env, javaThis);
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (packet.stream_index == audioStreamIndex) {

        }
        else
            av_packet_unref(&packet);
        while (paused(env, javaThis)) {
            CHECK_RETVAL(env->MonitorEnter(javaThis), ERROR_MESSAGE_MONITOR);
            env->CallVoidMethod(javaThis, env->GetMethodID(env->FindClass("java/lang/Object"), "wait", "()V"));
            CHECK_RETVAL(env->MonitorExit(javaThis), ERROR_MESSAGE_MONITOR);
        }
    }
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_initWithStream
        (JNIEnv * env, jobject javaThis, jobject stream) {
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    AVIOContext *pIOCtx = getIOContext(env, javaThis, stream);
    if (!pIOCtx) env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                               ERROR_MESSAGE_ALLOC_AVIOCONTEXT);

    pFormatCtx->flags |= AVFMT_FLAG_CUSTOM_IO;
	pFormatCtx->pb = pIOCtx;

    int retval = avformat_open_input(&pFormatCtx, "", nullptr, nullptr);
    if (retval < 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_OPEN_INPUT);
        return;
    }

    setAVFormatContext(env, javaThis, pFormatCtx);
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
        setAVFormatContext(env, javaThis, pFormatCtx);
    }
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_startupNativeLibs
        (JNIEnv * env, jobject javaThis, jfloat sampleRateHz, jint bitDepth, jint numChannels) {
    jclass clazz = env->GetObjectClass(javaThis);

    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis);

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

	CHECK_RETVAL(avcodec_parameters_to_context(pCodecCtx, pCodecPara), ERROR_MESSAGE_UNKNOWN)

	jobject *globalJavaThis = new jobject;
	*globalJavaThis = env->NewGlobalRef(javaThis);

    CHECK_RETVAL(avcodec_open2(pCodecCtx, pCodec, nullptr), ERROR_MESSAGE_UNKNOWN);

    SwrContext *pSwrCtx = swr_alloc();

    pSwrCtx = swr_alloc_set_opts(pSwrCtx,
                                 av_get_default_channel_layout(env->GetIntField(javaThis, env->GetFieldID(clazz, "numChannel", "I")),
                                 AV_SAMPLE_FMT_S16
                                 env->GetIntField(javaThis, env->GetFieldID(clazz, "sampleRateHz", "I"),
                                 av_get_default_channel_layout(pCodecCtx->channels),
                                 )

    setAVCodecContext(env, javaThis, pCodecCtx);
    setAVFormatContext(env, javaThis, pFormatCtx);
    setAudioStreamIndex(env, javaThis, audioStreamIndex);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_globalInit
        (JNIEnv * env, jclass javaClass) {
    av_register_all();
    env->GetJavaVM(&javaVM);
    javaVMVersion = env->GetVersion();
}
}