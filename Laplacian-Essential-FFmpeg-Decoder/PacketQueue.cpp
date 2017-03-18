#include "PacketQueue.h"

PacketQueue::PacketQueue(bool *quit) {
    nb_packets = 0;
    size = 0;
    this->quit = quit;

    mutex = SDL_CreateMutex();
    cond = SDL_CreateCond();
}

bool PacketQueue::enQueue(const AVPacket *packet) {
    AVPacket pkt;
    if (av_packet_ref(&pkt, packet) < 0) return false;

    SDL_LockMutex(mutex);
    theQueue.push(pkt);

    size += pkt.size;
    nb_packets++;

    SDL_CondSignal(cond);
    SDL_UnlockMutex(mutex);

    return true;
}

bool PacketQueue::deQueue(AVPacket *packet, bool block) {
    bool ret = false;

    SDL_LockMutex(mutex);
    while (true) {
        if (*quit) {
            ret = false;
            break;
        }

        if (!theQueue.empty()) {
            if (av_packet_ref(packet, &theQueue.front()) < 0) {
                ret = false;
                break;
            }
            theQueue.pop();
            nb_packets--;
            size -= packet->size;
            break;
        } else if (!block) {
            ret = false;
            break;
        } else {
            SDL_CondWait(cond, mutex);
        }
    }

    SDL_UnlockMutex(mutex);
    return ret;
}
