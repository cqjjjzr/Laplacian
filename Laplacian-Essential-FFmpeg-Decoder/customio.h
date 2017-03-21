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

const int iBufSize = 5 * 1024 * 1024;

int IOReadFunc(void *data, byte *buf, int buf_size) {
    jobject trackStream = *(jobject*) data;
    JNIEnv *env = nullptr;
    javaVM->AttachCurrentThread((void **) &env, nullptr);
    jbyteArray tempBuf = env->NewByteArray(buf_size);
    int retval = env->CallIntMethod(trackStream, env->GetMethodID(env->GetObjectClass(trackStream), "read", "([BII)I"),
                       tempBuf, buf_size);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        return -1;
    }

    env->GetByteArrayRegion(tempBuf, 0, buf_size, (jbyte *) buf);

    javaVM->DetachCurrentThread();
    return retval == -1 ? 0 : retval;
}

int64_t IOSeekFunc(void* data, int64_t pos, int whence) {
    int force = whence & AVSEEK_FORCE;
    whence &= ~AVSEEK_FORCE;

    jobject trackStream = *(jobject*) data;
    JNIEnv *env = nullptr;
    javaVM->AttachCurrentThread((void **) &env, nullptr);

    int64_t retval = -1;
    jclass clazz = env->GetObjectClass(trackStream);

    jmethodID seekMethod;
    jmethodID positionMethod;
    switch (whence) {
        case SEEK_SET: {
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SeekableTrackStream"))) {
                if (force) seekMethod = env->GetMethodID(clazz, "position", "()I");
                else return -1;
            } else {
                seekMethod = env->GetMethodID(env->GetObjectClass(trackStream), "seek", "(I)V");
            }
            env->CallVoidMethod(trackStream, seekMethod, pos);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, env->GetMethodID(clazz, "position", "()I"));
        }
            break;
        case SEEK_CUR: {
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SeekableTrackStream"))) {
                if (force) seekMethod = env->GetMethodID(clazz, "position", "()I");
                else return -1;
            } else {
                seekMethod = env->GetMethodID(env->GetObjectClass(trackStream), "seek", "(I)V");
            }
            positionMethod = env->GetMethodID(clazz, "position", "()I");
            int64_t absPos = env->CallIntMethod(trackStream, positionMethod) + pos;
            CHECK_EXCEPTION;
            env->CallVoidMethod(trackStream, seekMethod , absPos);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, positionMethod);
        }
            break;
        case SEEK_END: {
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SeekableTrackStream"))) {
                if (force) seekMethod = env->GetMethodID(clazz, "position", "()I");
                else return -1;
            } else {
                seekMethod = env->GetMethodID(env->GetObjectClass(trackStream), "seek", "(I)V");
            }
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SizeKnownStream")))
                return -1;
            positionMethod = env->GetMethodID(clazz, "position", "()I");
            int64_t absPos2 = env->CallIntMethod(trackStream, env->GetMethodID(clazz, "size", "()I")) - pos;
            CHECK_EXCEPTION;
            env->CallVoidMethod(trackStream, seekMethod, absPos2);
            CHECK_EXCEPTION;
            retval = env->CallIntMethod(trackStream, positionMethod);
        }
            break;
        case AVSEEK_SIZE: {
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SizeKnownStream")))
                return -1;
            retval = env->CallIntMethod(trackStream, env->GetMethodID(clazz, "position", "()I"));
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
    byte *pBuffer = new byte[iBufSize];
    AVIOContext *pIOCtx = avio_alloc_context(pBuffer, iBufSize,
                                             false,
                                             env->NewGlobalRef(trackStream),
                                             IOReadFunc,
                                             nullptr, // Write disabled
                                            IOSeekFunc);
    if (!pIOCtx) return nullptr;
    pIOCtx->seekable = env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SeekableTrackStream"));
    return pIOCtx;
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H
