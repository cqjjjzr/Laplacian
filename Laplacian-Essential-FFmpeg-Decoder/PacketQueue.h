#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_PACKETQUEUE_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_PACKETQUEUE_H
extern "C" {
#include <libavcodec/avcodec.h>
};

#include <SDL2/SDL.h>
#include <queue>
using namespace std;

class PacketQueue {
public:
    bool enQueue(const AVPacket *packet);
    bool deQueue(AVPacket *packet, bool block);

    PacketQueue(bool *quit);

private:
    queue<AVPacket> theQueue;

    int nb_packets;
    int size;
    bool *quit;

    SDL_mutex *mutex;
    SDL_cond *cond;
};

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_PACKETQUEUE_H
