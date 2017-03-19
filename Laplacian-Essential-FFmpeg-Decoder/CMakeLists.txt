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
        laplacian_essential-ffmpeg_decoder_impl.cpp
        PacketQueue.cpp
        PacketQueue.h
        shortcuts.h error_msgs.h decode.h)

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
        libavcodec.dll.a
        libavformat.dll.a
        libavdevice.dll.a
        libavfilter.dll.a
        libavutil.dll.a
        libpostproc.dll.a
        libswresample.dll.a
        libswscale.dll.a
        -lmingw32
        -lSDL2main
        -lSDL2
        -lm
        -ldinput8
        -ldxguid
        -ldxerr8
        -luser32
        -lgdi32
        -lwinmm
        -limm32
        -lole32
        -loleaut32
        -lshell32
        -lversion
        -luuid
)