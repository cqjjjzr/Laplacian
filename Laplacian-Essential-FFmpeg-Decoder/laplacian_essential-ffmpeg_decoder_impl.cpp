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

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
}

static const int MAX_AUDIO_FRAME_SIZE = 192000;
static const int SDL_AUDIO_BUFFER_SIZE = 1024;

void audio_callback(void* userdata, Uint8 *stream, int len) {

}

void thread_play(AVFormatContext *pFormatCtx) {
    for ()
}

extern "C"{
JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_play
        (JNIEnv * env, jobject javaThis) {
    AVCodecContext *pCodecCtx = getAVCodecContext(env, javaThis);

    setPaused(env, javaThis, false);
    SDL_PauseAudio(0);

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

    SDL_AudioSpec wanted_spec, spec;
    wanted_spec.freq     = pCodecCtx->sample_rate;
    wanted_spec.format   = AUDIO_S16SYS;
    wanted_spec.channels = (uint8_t) pCodecCtx->channels;
    wanted_spec.silence  = 0;
    wanted_spec.samples  = SDL_AUDIO_BUFFER_SIZE;
    wanted_spec.callback = audio_callback;
    wanted_spec.userdata = pCodecCtx;

    CHECK_RETVAL(SDL_OpenAudio(&wanted_spec, &spec), ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE);
    CHECK_RETVAL(avcodec_open2(pCodecCtx, pCodec, nullptr), ERROR_MESSAGE_UNKNOWN);

    SDL_PauseAudio(1);
    setAVCodecContext(env, javaThis, pCodecCtx);
    setAVFormatContext(env, javaThis, pFormatCtx);

}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_globalInit
        (JNIEnv * env, jclass javaClass) {
    av_register_all();
    SDL_Init(SDL_INIT_AUDIO | SDL_INIT_TIMER);
}
}