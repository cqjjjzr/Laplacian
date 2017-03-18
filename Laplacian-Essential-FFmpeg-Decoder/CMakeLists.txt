cmake_minimum_required(VERSION 3.6)
project(Laplacian_Essential_FFmpeg_Decoder)
set(PROJECT_VERSION_MAJOR 1)
set(PROJECT_VERSION_MINOR 0)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_VERBOSE_MAKEFILE on)

set(SOURCE_FILES charlie_laplacian_decoder_essential_FFmpegDecoder.h charlie_laplacian_decoder_essential_FFmpegDecoder_Companion.h laplacian_essential-ffmpeg_decoder_impl.cpp PacketQueue.cpp PacketQueue.h)
add_library(FFmpegDecoder SHARED ${SOURCE_FILES})

set(FFMPEG_INCLUDE "${PROJECT_SOURCE_DIR}/ffmpeg-win64-dev/include")
set(FFMPEG_LIBRARY "${PROJECT_SOURCE_DIR}/ffmpeg-win64-dev/lib")
include_directories(${FFMPEG_INCLUDE})
link_directories(Laplacian_Essential_FFmpeg_Decoder ${FFMPEG_LIBRARY})
target_link_libraries(
        FFmpegDecoder
        libavcodec
)

set(SDL2_INCLUDE "${PROJECT_SOURCE_DIR}/SDL2-devel-mingw/x86_64-w64-mingw32/include")
set(SDL2_LIBRARY "${PROJECT_SOURCE_DIR}/SDL2-devel-mingw/x86_64-w64-mingw32/lib")
include_directories(${SDL2_INCLUDE})
link_directories(Laplacian_Essential_FFmpeg_Decoder SDL2_LIBRARY)

find_package(JNI REQUIRED)
include_directories("${JAVA_INCLUDE_PATH}")
include_directories("${JAVA_INCLUDE_PATH2}")