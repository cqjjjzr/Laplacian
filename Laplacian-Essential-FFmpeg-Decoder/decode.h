#ifndef LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_DECODE_H
#define LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_DECODE_H

#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
#include <SDL2/SDL_stdinc.h>
#include "PacketQueue.h"

// FROM https://github.com/brookicv/FFMPEG-study/blob/master/FFmpeg-playAudio.cpp
// BY brookicv
int audio_decode_frame(AVCodecContext *aCodecCtx, uint8_t *audio_buf, int buf_size, PacketQueue queue)
{
	AVFrame *frame = av_frame_alloc();
	int data_size = 0;
	AVPacket pkt;

	SwrContext *swr_ctx = nullptr;

	if (!queue.deQueue(&pkt, true))
		return -1;

	int ret = avcodec_send_packet(aCodecCtx, &pkt);
	if (ret < 0 && ret != AVERROR(EAGAIN) && ret != AVERROR_EOF)
		return -1;

	ret = avcodec_receive_frame(aCodecCtx, frame);
	if (ret < 0 && ret != AVERROR_EOF)
		return -1;

	int index = av_get_channel_layout_channel_index((uint64_t) av_get_default_channel_layout(4), AV_CH_FRONT_CENTER);

	// 设置通道数或channel_layout
	if (frame->channels > 0 && frame->channel_layout == 0)
		frame->channel_layout = (uint64_t) av_get_default_channel_layout(frame->channels);
	else if (frame->channels == 0 && frame->channel_layout > 0)
		frame->channels = av_get_channel_layout_nb_channels(frame->channel_layout);

	AVSampleFormat dst_format = AV_SAMPLE_FMT_S16;//av_get_packed_sample_fmt((AVSampleFormat)frame->format);
	uint64_t dst_layout = (uint64_t) av_get_default_channel_layout(frame->channels);
	// 设置转换参数
	swr_ctx = swr_alloc_set_opts(nullptr, dst_layout, dst_format, frame->sample_rate,
		frame->channel_layout, (AVSampleFormat)frame->format, frame->sample_rate, 0, nullptr);
	if (!swr_ctx || swr_init(swr_ctx) < 0)
		return -1;

	// 计算转换后的sample个数 a * b / c
	int64_t dst_nb_samples = av_rescale_rnd(swr_get_delay(swr_ctx, frame->sample_rate) + frame->nb_samples, frame->sample_rate, frame->sample_rate, AVRounding(1));
	// 转换，返回值为转换后的sample个数
	int nb = swr_convert(swr_ctx, &audio_buf, (int) dst_nb_samples, (const uint8_t**)frame->data, frame->nb_samples);
	data_size = frame->channels * nb * av_get_bytes_per_sample(dst_format);

	av_frame_free(&frame);
	swr_free(&swr_ctx);
	return data_size;
}

#endif //LAPLACIAN_ESSENTIAL_FFMPEG_DECODER_DECODE_H
