#include "charlie_laplacian_decoder_essential_FFmpegDecoder.h"

#include <queue>
#include <cassert>
#include <string>
#include <sstream>
using namespace std;

#include <SDL2/SDL.h>
#include <SDL2/SDL_thread.h>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libswresample/swresample.h>
}

#define CHECK_RETVAL(retval, errorMsgRoot) if (retval < 0) {throw_retval_exception(env, retval, errorMsgRoot);return;}

static const char* ERROR_MESSAGE_OPEN_INPUT = "Failed to open input, avformat_open_input returned error code ";
static const char* ERROR_MESSAGE_FIND_STREAM = "Failed to find stream info, avformat_find_stream_info returned error code ";
static const char* ERROR_MESSAGE_NO_AUDIO_STREAM = "Can't find audio stream in the input resource!";
static const char* ERROR_MESSAGE_UNSUPPORTED_CODEC = "Unsupported codec!";
static const char* ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE = "Failed opening audio device! SDL_OpenAudio returned error code ";
static const char* ERROR_MESSAGE_UNKNOWN = "Failed to open input, unknown error! FFmpeg function returned error code ";

static const int MAX_AUDIO_FRAME_SIZE = 192000;
static const int SDL_AUDIO_BUFFER_SIZE = 1024;

void throw_retval_exception(JNIEnv *env, int retval, const char *errorMsgRoot) {
    stringstream errorStream;
    errorStream << errorMsgRoot << retval;

    env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                      errorStream.str().c_str());
}

void audio_callback(void* userdata, Uint8 *stream, int len) {

}

extern "C"{

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

    retval = avformat_find_stream_info(pFormatCtx, nullptr);
    if (retval != 0) {
        env->ReleaseStringUTFChars(url, urlCString);
        throw_retval_exception(env, retval, ERROR_MESSAGE_FIND_STREAM);
        return;
    }

    av_dump_format(pFormatCtx, 0, urlCString, 0);

    env->ReleaseStringUTFChars(url, urlCString);

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

    retval = avcodec_parameters_to_context(pCodecCtx, pCodecPara);
    if (retval != 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_UNKNOWN);
        return;
    }

    SDL_AudioSpec wanted_spec, spec;
    wanted_spec.freq     = pCodecCtx->sample_rate;
    wanted_spec.format   = AUDIO_S16SYS;
    wanted_spec.channels = (uint8_t) pCodecCtx->channels;
    wanted_spec.silence  = 0;
    wanted_spec.samples  = SDL_AUDIO_BUFFER_SIZE;
    wanted_spec.callback = audio_callback;
    wanted_spec.userdata = pCodecCtx;

    retval = SDL_OpenAudio(&wanted_spec, &spec);
    if (retval < 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE);
        return;
    }

    retval = avcodec_open2(pCodecCtx, pCodec, nullptr);
    if (retval < 0) {
        throw_retval_exception(env, retval, ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE);
        return;
    }
    CHECK_RETVAL(avcodec_open2(pCodecCtx, pCodec, nullptr), ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE);

}

JNIEXPORT void JNICALL Java_charlie_laplacian_decoder_essential_FFmpegDecoder_globalInit
        (JNIEnv * env, jclass javaClass) {
    av_register_all();
    SDL_Init(SDL_INIT_AUDIO | SDL_INIT_TIMER);
}
}