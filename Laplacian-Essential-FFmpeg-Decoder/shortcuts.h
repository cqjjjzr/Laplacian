#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
#include <jni.h>
#include "PacketQueue.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
}

#define CHECK_RETVAL(retval, errorMsgRoot) if (retval < 0) {throw_retval_exception(env, retval, errorMsgRoot);return;}

AVCodecContext* getAVCodecContext(JNIEnv * env, jobject javaThis) {
    return (AVCodecContext*)
            env->GetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVCodecContext", "J"));
}

void setAVCodecContext(JNIEnv * env, jobject javaThis, AVCodecContext *avCodecContext) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVCodecContext", "J")
            , (jlong) avCodecContext);
}

AVFormatContext* getAVFormatContext(JNIEnv * env, jobject javaThis) {
    return (AVFormatContext*)
            env->GetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVFormatContext", "J"));
}

void setAVFormatContext(JNIEnv * env, jobject javaThis, AVFormatContext *avFormatContext) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerAVFormatContext", "J")
            , (jlong) avFormatContext);
}

PacketQueue* getPacketQueue(JNIEnv * env, jobject javaThis) {
    return (PacketQueue*)
            env->GetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerPacketQueue", "J"));
}

void setPacketQueue(JNIEnv * env, jobject javaThis, PacketQueue *packetQueue) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "pointerPacketQueue", "J")
            , (jlong) packetQueue);
}

int getAudioStreamIndex(JNIEnv * env, jobject javaThis) {
    return (int)
            env->GetIntField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "audioStreamIndex", "I"));
}

void setAudioStreamIndex(JNIEnv * env, jobject javaThis, int audioStreamIndex) {
    env->SetIntField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "audioStreamIndex", "I")
            , (jint) audioStreamIndex);
}

bool paused(JNIEnv * env, jobject javaThis) {
    return (bool)
            env->GetBooleanField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "paused", "Z"));
}

void setPaused(JNIEnv * env, jobject javaThis, bool paused) {
    env->SetBooleanField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "paused", "Z")
            , (jboolean) paused);
}

int getVolume(JNIEnv * env, jobject javaThis) {
    return (int)
            env->GetIntField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "volume", "I"));
}

void setVolume(JNIEnv * env, jobject javaThis, int volume) {
    env->SetIntField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "volume", "I")
            , (jint) volume);
}

void setPosition(JNIEnv * env, jobject javaThis, long position) {
    env->SetLongField(javaThis, env->GetFieldID(env->GetObjectClass(javaThis), "position", "J")
            , (jlong) position);
}

void throw_retval_exception(JNIEnv *env, int retval, const char *errorMsgRoot) {
    stringstream errorStream;
    errorStream << errorMsgRoot << retval;

    env->ThrowNew(env->FindClass("charlie/laplacian/decoder/essential/FFmpegException"),
                  errorStream.str().c_str());
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_JNI_OPER_H
