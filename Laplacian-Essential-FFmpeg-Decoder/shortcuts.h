#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#include <jni.h>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswresample/swresample.h>
}

#define CHECK_RETVAL(retval, errorMsgRoot) if (retval < 0) {throw_retval_exception(env, retval, errorMsgRoot);return;}

AVCodecContext* getAVCodecContext(JNIEnv * env, jobject javaThis, jclass clazz) {
    return (AVCodecContext*)
            env->GetLongField(javaThis, env->GetFieldID(clazz, "pointerAVCodecContext", "J"));
}

void setAVCodecContext(JNIEnv * env, jobject javaThis, jclass clazz, AVCodecContext *avCodecContext) {
    env->SetLongField(javaThis, env->GetFieldID(clazz, "pointerAVCodecContext", "J")
            , (jlong) avCodecContext);
}

AVFormatContext* getAVFormatContext(JNIEnv * env, jobject javaThis, jclass clazz) {
    return (AVFormatContext*)
            env->GetLongField(javaThis, env->GetFieldID(clazz, "pointerAVFormatContext", "J"));
}

void setAVFormatContext(JNIEnv * env, jobject javaThis, jclass clazz, AVFormatContext *avFormatContext) {
    env->SetLongField(javaThis, env->GetFieldID(clazz, "pointerAVFormatContext", "J")
            , (jlong) avFormatContext);
}

int getAudioStreamIndex(JNIEnv * env, jclass clazz, jobject javaThis) {
    return (int)
            env->GetIntField(javaThis, env->GetFieldID(clazz, "audioStreamIndex", "I"));
}

void setAudioStreamIndex(JNIEnv * env, jobject javaThis, jclass clazz, int audioStreamIndex) {
    env->SetIntField(javaThis, env->GetFieldID(clazz, "audioStreamIndex", "I")
            , (jint) audioStreamIndex);
}

void setPosition(JNIEnv * env, jobject javaThis, jclass clazz, long position) {
    env->SetLongField(javaThis, env->GetFieldID(clazz, "position", "J")
            , (jlong) position);
}

void throw_retval_exception(JNIEnv *env, int retval, const char *errorMsgRoot) {
    stringstream errorStream;
    errorStream << errorMsgRoot << retval;

    env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                  errorStream.str().c_str());
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
