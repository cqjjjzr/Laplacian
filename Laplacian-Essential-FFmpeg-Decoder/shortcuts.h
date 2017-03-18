#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#include <jni.h>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
}

#define CHECK_RETVAL(retval, errorMsgRoot) if (retval < 0) {throw_retval_exception(env, retval, errorMsgRoot);return;}

AVCodecContext* getAVCodecContext(JNIEnv * env, jobject javaThis) {
    return (AVCodecContext*)
            env->GetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVCodecContext", "L"));
}

void setAVCodecContext(JNIEnv * env, jobject javaThis, AVCodecContext *avCodecContext) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVCodecContext", "L")
            , (jlong) avCodecContext);
}

AVFormatContext* getAVFormatContext(JNIEnv * env, jobject javaThis) {
    return (AVFormatContext*)
            env->GetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVFormatContext", "L"));
}

void setAVFormatContext(JNIEnv * env, jobject javaThis, AVFormatContext *AVFormatContext) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVFormatContext", "L")
            , (jlong) AVFormatContext);
}

bool paused(JNIEnv * env, jobject javaThis) {
    return (bool)
            env->GetBooleanField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "paused", "Z"));
}

bool setPaused(JNIEnv * env, jobject javaThis, bool paused) {
    env->SetBooleanField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "paused", "Z")
            , (jboolean) paused);
}

void throw_retval_exception(JNIEnv *env, int retval, const char *errorMsgRoot) {
    stringstream errorStream;
    errorStream << errorMsgRoot << retval;

    env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                  errorStream.str().c_str());
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
