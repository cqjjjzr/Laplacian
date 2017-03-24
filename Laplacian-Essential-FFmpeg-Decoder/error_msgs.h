#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_ERROR_MSGS_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_ERROR_MSGS_H

static const char* ERROR_MESSAGE_OPEN_INPUT = "Failed to open input, avformat_open_input returned error code ";
static const char* ERROR_MESSAGE_FIND_STREAM = "Failed to find stream info, avformat_find_stream_info returned error code ";
static const char* ERROR_MESSAGE_NO_AUDIO_STREAM = "Can't find audio stream in the input resource!";
static const char* ERROR_MESSAGE_UNSUPPORTED_CODEC = "Unsupported codec!";
static const char* ERROR_MESSAGE_FAILED_OPEN_AUDIO_DEVICE = "Failed opening audio device! SDL_OpenAudio returned error code ";
static const char* ERROR_MESSAGE_UNKNOWN = "Failed to open input, unknown error! FFmpeg function returned error code ";
static const char* ERROR_MESSAGE_ALLOC_AVIOCONTEXT = "Failed to alloc AVIOContext!";
static const char* ERROR_MESSAGE_PROBE = "Failed to probe stream format! av_probe_input_buffer returned error code ";
static const char* ERROR_MESSAGE_DECODE = "Failed to decode! FFmpeg func returned error code ";

static const char* ERROR_MESSAGE_MONITOR = "Failed executing JVM operation! FATAL ERROR! JNIEnv returned error code ";

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_ERROR_MSGS_H
