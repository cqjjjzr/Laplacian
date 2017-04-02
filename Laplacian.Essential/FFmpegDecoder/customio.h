#pragma once
#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H


#include <jni.h>

typedef unsigned char byte;

extern "C" {
#include <libavformat/avio.h>
}

#define CHECK_EXCEPTION if (env->ExceptionCheck()) { env->ExceptionClear(); return -1; }

static JavaVM *javaVM = nullptr;
static jint javaVMVersion;

static jclass clazzSizeKnownStream = nullptr;
static jclass clazzSeekableStream = nullptr;
static jclass clazzTrackStream = nullptr;

static jmethodID readMethod = nullptr;
static jmethodID positionMethod = nullptr;
static jmethodID seekMethod = nullptr;
static jmethodID forceSeekMethod = nullptr;
static jmethodID sizeMethod = nullptr;

const int iBufSize = 1024 * 1024;

int IOReadFunc(void *data, byte *buf, int buf_size) {
    jobject trackStream = *(jobject*) data;
    JNIEnv *env = nullptr;
	javaVM->GetEnv((void**) &env, javaVMVersion);
    jbyteArray tempBuf = env->NewByteArray(buf_size);
    int retval = env->CallIntMethod(trackStream, readMethod, tempBuf, 0, buf_size);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        return -1;
    }

    env->GetByteArrayRegion(tempBuf, 0, buf_size, (jbyte *) buf);
    return retval == -1 ? AVERROR_EOF : retval;
}

int64_t IOSeekFunc(void* data, int64_t pos, int whence) {
    int force = whence & AVSEEK_FORCE;
    whence &= ~AVSEEK_FORCE;

    jobject trackStream = *(jobject*) data;
    JNIEnv *env = nullptr;
    javaVM->AttachCurrentThread((void **) &env, nullptr);

    int64_t retval = -1;

    jmethodID intlSeekMethod;
    switch (whence) {
        case SEEK_SET: {
            if (!env->IsInstanceOf(trackStream, clazzSeekableStream)) {
                if (force) intlSeekMethod = forceSeekMethod;
                else return -1;
            } else {
                intlSeekMethod = seekMethod;
            }
            env->CallVoidMethod(trackStream, intlSeekMethod, pos);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, positionMethod);
            CHECK_EXCEPTION;
        }
            break;
        case SEEK_CUR: {
            if (!env->IsInstanceOf(trackStream, clazzSeekableStream)) {
                if (force) intlSeekMethod = forceSeekMethod;
                else return -1;
            } else {
                intlSeekMethod = seekMethod;
            }
            int64_t absPos = env->CallIntMethod(trackStream, positionMethod) + pos;
            CHECK_EXCEPTION;
            env->CallVoidMethod(trackStream, intlSeekMethod, absPos);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, positionMethod);
            CHECK_EXCEPTION;
        }
            break;
        case SEEK_END: {
            if (!env->IsInstanceOf(trackStream, clazzSeekableStream)) {
                if (force) intlSeekMethod = forceSeekMethod;
                else return -1;
            } else {
                intlSeekMethod = seekMethod;
            }
            if (!env->IsInstanceOf(trackStream, clazzSizeKnownStream))
                return -1;
            int64_t absPos2 = env->CallIntMethod(trackStream, sizeMethod) - pos;
            CHECK_EXCEPTION;
            env->CallVoidMethod(trackStream, intlSeekMethod, absPos2);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, positionMethod);
            CHECK_EXCEPTION;
        }
            break;
        case AVSEEK_SIZE: {
            if (!env->IsInstanceOf(trackStream, clazzSizeKnownStream))
                return -1;
            retval = env->CallIntMethod(trackStream, sizeMethod);
        }
            break;
        default:
            retval = -1;
            break;
    }
    javaVM->DetachCurrentThread();
    return retval;
}

AVIOContext *getIOContext(JNIEnv * env, jobject javaThis, jobject trackStream) {
    byte *pBuffer = (byte *) av_malloc(iBufSize * sizeof(byte));
	jobject globalTrackStream = env->NewGlobalRef(trackStream);
	jobject *pStream = new jobject;
	*pStream = globalTrackStream;
    AVIOContext *pIOCtx = avio_alloc_context(pBuffer, iBufSize,
                                             false,
                                             pStream,
                                             IOReadFunc,
                                             nullptr, // Write disabled
                                             IOSeekFunc);
    if (!pIOCtx) return nullptr;

    pIOCtx->seekable = env->IsInstanceOf(trackStream, clazzSeekableStream);
    return pIOCtx;
}

void initClassesAndMethods(JNIEnv * env) {
    if (!clazzTrackStream)
        clazzTrackStream =
                (jclass) env->NewGlobalRef(env->FindClass("charlie/laplacian/stream/TrackStream"));
    if (!clazzSeekableStream)
        clazzSeekableStream =
                (jclass) env->NewGlobalRef(env->FindClass("charlie/laplacian/stream/SeekableTrackStream"));
    if (!clazzSizeKnownStream)
        clazzSizeKnownStream =
                (jclass) env->NewGlobalRef(env->FindClass("charlie/laplacian/stream/SizeKnownTrackStream"));
    if (!readMethod)
        readMethod = env->GetMethodID(clazzTrackStream, "read", "([BII)I");
    if (!seekMethod)
        seekMethod = env->GetMethodID(clazzSeekableStream, "seek", "(I)V");
    if (!forceSeekMethod)
        forceSeekMethod = env->GetMethodID(clazzTrackStream, "forceSeek", "(I)V");
    if (!positionMethod)
        positionMethod = env->GetMethodID(clazzTrackStream, "position", "()I");
    if (!sizeMethod)
        sizeMethod = env->GetMethodID(clazzSizeKnownStream, "size", "()I");
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H
