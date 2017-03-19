#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H


#include <jni.h>

typedef unsigned char byte;

extern "C" {
#include <libavformat/avio.h>
}

static JavaVM *javaVM = nullptr;
static jint javaVMVersion;

const int iBufSize = 32 * 1024;

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
    jobject trackStream = *(jobject*) data;
    JNIEnv *env = nullptr;
    javaVM->AttachCurrentThread((void **) &env, nullptr);
    int64_t retval;
    switch (whence) {
        case SEEK_SET:
            if (!env->IsInstanceOf(trackStream, env->FindClass("charlie.laplacian.decoder.SeekableTrackStream")))
                return -1;
            env->CallVoidMethod(trackStream, env->GetMethodID(env->GetObjectClass(trackStream), "seek", "(I)V"));
            // TODO !
    }
    javaVM->DetachCurrentThread();
    return retval;
}

AVIOContext *getContext(JNIEnv * env, jobject javaThis, jobject trackStream) {
    byte *pBuffer = new byte[iBufSize];
    env->GetJavaVM(&javaVM);
    javaVMVersion = env->GetVersion();
    AVIOContext *pIOCtx = avio_alloc_context(pBuffer, iBufSize,
                                             false,
                                             env->NewGlobalRef(trackStream),
                                             IOReadFunc,
                                             nullptr, // Write disabled
                                            IOSeekFunc);
    return pIOCtx;
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_CUSTOMIO_H
