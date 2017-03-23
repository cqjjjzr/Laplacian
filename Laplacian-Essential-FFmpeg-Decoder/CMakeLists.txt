cmake_minimum_required(VERSION 3.6)
project(Laplacian_Essential_FFmpeg_Decoder)
set(PROJECT_VERSION_MAJOR 1)
set(PROJECT_VERSION_MINOR 0)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_VERBOSE_MAKEFILE 1)

set(
        SOURCE_FILES
        charlie_laplacian_decoder_essential_FFmpegDecoder.h
        charlie_laplacian_decoder_essential_FFmpegDecoder_Companion.h
        charlie_laplacian_decoder_essential_FFmpegDecoder_SDLVolumeController.h
        laplacian_essential-ffmpeg_decoder_impl.cpp
        shortcuts.h error_msgs.h decode.h customio.h)

set(FFMPEG_INCLUDE "$ENV{FFMPEG_DEV_HOME}/include/")
set(FFMPEG_LIBRARY "$ENV{FFMPEG_DEV_HOME}/lib/")
include_directories(${FFMPEG_INCLUDE})
link_directories(${FFMPEG_LIBRARY})

set(SDL2_INCLUDE "$ENV{SDL2_DEV_HOME}/x86_64-w64-mingw32/include")
set(SDL2_LIBRARY "$ENV{SDL2_DEV_HOME}/x86_64-w64-mingw32/lib/")
include_directories(${SDL2_INCLUDE})

link_directories(${SDL2_LIBRARY})

find_package(JNI REQUIRED)
include_directories("${JAVA_INCLUDE_PATH}")
include_directories("${JAVA_INCLUDE_PATH2}")

add_library(FFmpegDecoder SHARED ${SOURCE_FILES})

target_link_libraries(
        FFmpegDecoder
        avcodec
        avformat
        avdevice
        avfilter
        avutil
        postproc
        swresample
        libswscale
        #mingw32
        SDL2main
        libSDL2.dll.a
        dinput8
        dxguid
        user32
        gdi32
        winmm
        imm32
        ole32
        oleaut32
        shell32
        version
        uuid
)