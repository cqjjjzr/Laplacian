#include "charlie_laplacian_decoder_essential_FFmpegDecoder.h"

#include <queue>
#include <cassert>
#include <string>
#include <sstream>
using namespace std;

#include <SDL2/SDL.h>
#include <SDL2/SDL_thread.h>

#include "shortcuts.h"
#include "error_msgs.h"
#include "decode.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
}

static const int MAX_AUDIO_FRAME_SIZE = 192000;
static const int SDL_AUDIO_BUFFER_SIZE = 1024;
static JavaVM *javaVM = nullptr;

typedef struct CallBackHolder {
    AVCodecContext *pCodecCtx;
    PacketQueue *queue;
} CallBackHolder;

void audio_callback(void* userdata, Uint8 *stream, int len) {
    AVCodecContext *pCodecCtx = ((CallBackHolder *)userdata)->pCodecCtx;
    PacketQueue *packetQueue = ((CallBackHolder *)userdata)->queue;

    int len1, audioSize;
    static uint8_t audioBuff[(MAX_AUDIO_FRAME_SIZE * 3) / 2];
    static unsigned int audioBufSize = 0;
    static unsigned int audioBufIndex = 0;

    SDL_memset(stream, 0, (size_t) len);

    while (len > 0) {
        if (audioBufIndex >= audioBufSize) {
            audioSize = audio_decode_frame(pCodecCtx, audioBuff, sizeof(audioBuff), *packetQueue);
            if (audioSize < 0) {
                audioBufSize = 0;
                memset(audioBuff, 0, audioBufSize);
            } else
                audioBufSize = (unsigned int) audioSize;
        }

        len1 = audioBufSize - audioBufIndex;
        if (len1 > len)
            len1 = len;

        SDL_MixAudio(stream, audioBuff + audioBufIndex, (Uint32) len, getVolume(env, javaThis)); // ??????????

        len -= len1;
        stream += len1;
        audioBufIndex += len1;
    }
}

extern "C"{
JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_play
        (JNIEnv * env, jobject javaThis) {
    setPaused(env, javaThis, false);
    SDL_PauseAudio(0);

    CHECK_RETVAL(env->MonitorEnter(javaThis), ERROR_MESSAGE_MONITOR);
    env->CallVoidMethod(javaThis, env->GetMethodID(env->FindClass("java/lang/Object"), "notifyAll", "V()"));
    CHECK_RETVAL(env->MonitorExit(javaThis), ERROR_MESSAGE_MONITOR);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_playThread
        (JNIEnv * env, jobject javaThis) {
    AVFormatContext *pFormatCtx = getAVFormatContext(env, javaThis);

    AVPacket packet;
    int audioStreamIndex = getAudioStreamIndex(env, javaThis);
    PacketQueue packetQueue = *getPacketQueue(env, javaThis);
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (packet.stream_index == audioStreamIndex)
            packetQueue.enQueue(&packet);
        else
            av_packet_unref(&packet);
        while (paused(env, javaThis)) {
            CHECK_RETVAL(env->MonitorEnter(javaThis), ERROR_MESSAGE_MONITOR);
            env->CallVoidMethod(javaThis, env->GetMethodID(env->FindClass("java/lang/Object"), "wait", "V()"));
            CHECK_RETVAL(env->MonitorExit(javaThis), ERROR_MESSAGE_MONITOR);
        }
    }
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_initNativeLib
        (JNIEnv * env, jobject javaThis, jobject stream, jstring url) {
    const char *urlCString = env->GetStringUTFChars(url, JNI_FALSE);
    int retval = 0;

    AVFormatContext *pFormatCtx = nullptr;

    retval = avformat_open_input(&pFormatCtx, urlCString, nullptr, nullptr);
    if (retval != 0) {
        env->ReleaseStringUTFChars(url, urlCString);
        throw_retval_exception(env, retval, ERROR_MESSAGE_OPEN_INPUT);
        return;
    }

    env->ReleaseStringUTFChars(url, urlCString);

    CHECK_RETVAL(avformat_find_stream_info(pFormatCtx, nullptr), ERROR_MESSAGE_FIND_STREAM)

    int audioStreamIndex = -1;
    for (int i = 0;i < pFormatCtx->nb_streams;i++)
        if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
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

    CallBackHolder *holder = new CallBackHolder;
    holder->queue = new PacketQueue;
    holder->pCodecCtx = pCodecCtx;

    SDL_AudioSpec wanted_spec, spec;
    wanted_spec.freq     = pCodecCtx->sample_rate;
    wanted_spec.format   = AUDIO_S16SYS;
    wanted_spec.channels = (uint8_t) pCodecCtx->channels;
    wanted_spec.silence  = 0;
    wanted_spec.samples  = SDL_AUDIO_BUFFER_SIZE;
    wanted_spec.callback = audio_callback;
    wanted_spec.userdata = &holder;

    CHECK_RETVAL(SDL_OpenAudio(&wanted_spec, &spec), ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE);
    CHECK_RETVAL(avcodec_open2(pCodecCtx, pCodec, nullptr), ERROR_MESSAGE_UNKNOWN);

    SDL_PauseAudio(1);
    setAVCodecContext(env, javaThis, pCodecCtx);
    setAVFormatContext(env, javaThis, pFormatCtx);
    setAudioStreamIndex(env, javaThis, audioStreamIndex);
    setPacketQueue(env, javaThis, holder->queue);
}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_globalInit
        (JNIEnv * env, jclass javaClass) {
    av_register_all();
    SDL_Init(SDL_INIT_AUDIO | SDL_INIT_TIMER);
    env->GetJavaVM(&javaVM);
}
}