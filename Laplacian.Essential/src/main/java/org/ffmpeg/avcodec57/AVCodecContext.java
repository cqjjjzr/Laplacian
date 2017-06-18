package org.ffmpeg.avcodec57;
import com.sun.jna.Callback;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
import org.ffmpeg.avutil55.AVFrame;
import org.ffmpeg.avutil55.AVRational;

import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:937</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVCodecContext extends Structure {
	/** C type : const AVClass* */
	public Pointer av_class;
	public int log_level_offset;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVMediaType<br>
	 * see AVMEDIA_TYPE_xxx<br>
	 * C type : AVMediaType
	 */
	public int codec_type;
	/** C type : AVCodec* */
	public org.ffmpeg.avcodec57.AVCodec.ByReference codec;
	/** C type : char[32] */
	public byte[] codec_name = new byte[32];
	/**
	 * @see AVCodecID<br>
	 * see AV_CODEC_ID_xxx<br>
	 * C type : AVCodecID
	 */
	public int codec_id;
	public int codec_tag;
	public int stream_codec_tag;
	/** C type : void* */
	public Pointer priv_data;
	/** C type : AVCodecInternal* */
	public org.ffmpeg.avcodec57.AVCodecInternal.ByReference internal;
	/** C type : void* */
	public Pointer opaque;
	public long bit_rate;
	public int bit_rate_tolerance;
	public int global_quality;
	public int compression_level;
	public int flags;
	public int flags2;
	/** C type : uint8_t* */
	public Pointer extradata;
	public int extradata_size;
	/** C type : AVRational */
	public AVRational time_base;
	public int ticks_per_frame;
	public int delay;
	public int width;
	public int height;
	public int coded_width;
	public int coded_height;
	public int gop_size;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVPixelFormat<br>
	 * C type : AVPixelFormat
	 */
	public int pix_fmt;
	public int me_method;
	/** C type : draw_horiz_band_callback* */
	public draw_horiz_band_callback draw_horiz_band;
	public NativeLong get_format;
	public int max_b_frames;
	public float b_quant_factor;
	public int rc_strategy;
	public int b_frame_strategy;
	public float b_quant_offset;
	public int has_b_frames;
	public int mpeg_quant;
	public float i_quant_factor;
	public float i_quant_offset;
	public float lumi_masking;
	public float temporal_cplx_masking;
	public float spatial_cplx_masking;
	public float p_masking;
	public float dark_masking;
	public int slice_count;
	public int prediction_method;
	/** C type : int* */
	public IntByReference slice_offset;
	/** C type : AVRational */
	public AVRational sample_aspect_ratio;
	public int me_cmp;
	public int me_sub_cmp;
	public int mb_cmp;
	public int ildct_cmp;
	public int dia_size;
	public int last_predictor_count;
	public int pre_me;
	public int me_pre_cmp;
	public int pre_dia_size;
	public int me_subpel_quality;
	public int dtg_active_format;
	public int me_range;
	public int intra_quant_bias;
	public int inter_quant_bias;
	public int slice_flags;
	public int xvmc_acceleration;
	public int mb_decision;
	/** C type : uint16_t* */
	public ShortByReference intra_matrix;
	/** C type : uint16_t* */
	public ShortByReference inter_matrix;
	public int scenechange_threshold;
	public int noise_reduction;
	public int me_threshold;
	public int mb_threshold;
	public int intra_dc_precision;
	public int skip_top;
	public int skip_bottom;
	public float border_masking;
	public int mb_lmin;
	public int mb_lmax;
	public int me_penalty_compensation;
	public int bidir_refine;
	public int brd_scale;
	public int keyint_min;
	public int refs;
	public int chromaoffset;
	public int scenechange_factor;
	public int mv0_threshold;
	public int b_sensitivity;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVColorPrimaries<br>
	 * C type : AVColorPrimaries
	 */
	public int color_primaries;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVColorTransferCharacteristic<br>
	 * C type : AVColorTransferCharacteristic
	 */
	public int color_trc;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVColorSpace<br>
	 * C type : AVColorSpace
	 */
	public int colorspace;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVColorRange<br>
	 * C type : AVColorRange
	 */
	public int color_range;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVChromaLocation<br>
	 * C type : AVChromaLocation
	 */
	public int chroma_sample_location;
	public int slices;
	/**
	 * @see AVFieldOrder<br>
	 * C type : AVFieldOrder
	 */
	public int field_order;
	/** < samples per second */
	public int sample_rate;
	/** < number of audio channels */
	public int channels;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVSampleFormat<br>
	 * < sample format<br>
	 * C type : AVSampleFormat
	 */
	public int sample_fmt;
	public int frame_size;
	public int frame_number;
	public int block_align;
	public int cutoff;
	public long channel_layout;
	public long request_channel_layout;
	/**
	 * @see AVAudioServiceType<br>
	 * C type : AVAudioServiceType
	 */
	public int audio_service_type;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVSampleFormat<br>
	 * C type : AVSampleFormat
	 */
	public int request_sample_fmt;
	/** C type : get_buffer2_callback* */
	public get_buffer2_callback get_buffer2;
	public int refcounted_frames;
	/** < amount of qscale change between easy & hard scenes (0.0-1.0) */
	public float qcompress;
	/** < amount of qscale smoothing over time (0.0-1.0) */
	public float qblur;
	public int qmin;
	public int qmax;
	public int max_qdiff;
	public float rc_qsquish;
	public float rc_qmod_amp;
	public int rc_qmod_freq;
	public int rc_buffer_size;
	public int rc_override_count;
	/** C type : RcOverride* */
	public org.ffmpeg.avcodec57.RcOverride.ByReference rc_override;
	/** C type : const char* */
	public Pointer rc_eq;
	public long rc_max_rate;
	public long rc_min_rate;
	public float rc_buffer_aggressivity;
	public float rc_initial_cplx;
	public float rc_max_available_vbv_use;
	public float rc_min_vbv_overflow_use;
	public int rc_initial_buffer_occupancy;
	public int coder_type;
	public int context_model;
	public int lmin;
	public int lmax;
	public int frame_skip_threshold;
	public int frame_skip_factor;
	public int frame_skip_exp;
	public int frame_skip_cmp;
	public int trellis;
	public int min_prediction_order;
	public int max_prediction_order;
	public long timecode_frame_start;
	/** C type : rtp_callback_callback* */
	public rtp_callback_callback rtp_callback;
	/** The size of the RTP payload: the coder will */
	public int rtp_payload_size;
	public int mv_bits;
	public int header_bits;
	public int i_tex_bits;
	public int p_tex_bits;
	public int i_count;
	public int p_count;
	public int skip_count;
	public int misc_bits;
	public int frame_bits;
	/** C type : char* */
	public Pointer stats_out;
	/** C type : char* */
	public Pointer stats_in;
	public int workaround_bugs;
	public int strict_std_compliance;
	public int error_concealment;
	public int debug;
	public int debug_mv;
	public int err_recognition;
	public long reordered_opaque;
	/** C type : AVHWAccel* */
	public org.ffmpeg.avcodec57.AVHWAccel.ByReference hwaccel;
	/** C type : void* */
	public Pointer hwaccel_context;
	/** C type : uint64_t[8] */
	public long[] error = new long[8];
	public int dct_algo;
	public int idct_algo;
	public int bits_per_coded_sample;
	public int bits_per_raw_sample;
	public int lowres;
	/** C type : AVFrame* */
	public AVFrame.ByReference coded_frame;
	public int thread_count;
	public int thread_type;
	public int active_thread_type;
	public int thread_safe_callbacks;
	/** C type : execute_callback* */
	public execute_callback execute;
	/** C type : execute2_callback* */
	public execute2_callback execute2;
	public int nsse_weight;
	public int profile;
	public int level;
	/**
	 * @see AVDiscard<br>
	 * C type : AVDiscard
	 */
	public int skip_loop_filter;
	/**
	 * @see AVDiscard<br>
	 * C type : AVDiscard
	 */
	public int skip_idct;
	/**
	 * @see AVDiscard<br>
	 * C type : AVDiscard
	 */
	public int skip_frame;
	/** C type : uint8_t* */
	public Pointer subtitle_header;
	public int subtitle_header_size;
	public int error_rate;
	public long vbv_delay;
	public int side_data_only_packets;
	public int initial_padding;
	/** C type : AVRational */
	public AVRational framerate;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVPixelFormat<br>
	 * C type : AVPixelFormat
	 */
	public int sw_pix_fmt;
	/** C type : AVRational */
	public AVRational pkt_timebase;
	/** C type : const AVCodecDescriptor* */
	public org.ffmpeg.avcodec57.AVCodecDescriptor.ByReference codec_descriptor;
	/** Number of incorrect PTS values so far */
	public long pts_correction_num_faulty_pts;
	/** Number of incorrect DTS values so far */
	public long pts_correction_num_faulty_dts;
	/** PTS of the last frame */
	public long pts_correction_last_pts;
	/** DTS of the last frame */
	public long pts_correction_last_dts;
	/** C type : char* */
	public Pointer sub_charenc;
	public int sub_charenc_mode;
	public int skip_alpha;
	public int seek_preroll;
	/** C type : uint16_t* */
	public ShortByReference chroma_intra_matrix;
	/** C type : uint8_t* */
	public Pointer dump_separator;
	/** C type : char* */
	public Pointer codec_whitelist;
	public int properties;
	/** C type : AVPacketSideData* */
	public org.ffmpeg.avcodec57.AVPacketSideData.ByReference coded_side_data;
	public int nb_coded_side_data;
	/** C type : AVBufferRef* */
	public Pointer hw_frames_ctx;
	public int sub_text_format;
	public int trailing_padding;
	public long max_pixels;
	/** C type : AVBufferRef* */
	public Pointer hw_device_ctx;
	public int hwaccel_flags;
	/** <i>native declaration : .\libavcodec\avcodec.h:904</i> */
	public interface draw_horiz_band_callback extends Callback {
		void apply(AVCodecContext s, AVFrame src, IntByReference offset, int y, int type, int height);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:923</i> */
	public interface get_buffer2_callback extends Callback {
		int apply(AVCodecContext s, AVFrame frame, int flags);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:924</i> */
	public interface rtp_callback_callback extends Callback {
		void apply(AVCodecContext avctx, Pointer data, int size, int mb_nb);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:925</i> */
	public interface execute_callback_func_callback extends Callback {
		int apply(AVCodecContext c2, Pointer arg);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:926</i> */
	public interface execute_callback extends Callback {
		int apply(AVCodecContext c, execute_callback_func_callback func, Pointer arg2, IntByReference ret, int count, int size);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:927</i> */
	public interface execute2_callback_func_callback extends Callback {
		int apply(AVCodecContext c2, Pointer arg, int jobnr, int threadnr);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:928</i> */
	public interface execute2_callback extends Callback {
		int apply(AVCodecContext c, execute2_callback_func_callback func, Pointer arg2, IntByReference ret, int count);
	};
	public AVCodecContext() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("av_class", "log_level_offset", "codec_type", "codec", "codec_name", "codec_id", "codec_tag", "stream_codec_tag", "priv_data", "internal", "opaque", "bit_rate", "bit_rate_tolerance", "global_quality", "compression_level", "flags", "flags2", "extradata", "extradata_size", "time_base", "ticks_per_frame", "delay", "width", "height", "coded_width", "coded_height", "gop_size", "pix_fmt", "me_method", "draw_horiz_band", "get_format", "max_b_frames", "b_quant_factor", "rc_strategy", "b_frame_strategy", "b_quant_offset", "has_b_frames", "mpeg_quant", "i_quant_factor", "i_quant_offset", "lumi_masking", "temporal_cplx_masking", "spatial_cplx_masking", "p_masking", "dark_masking", "slice_count", "prediction_method", "slice_offset", "sample_aspect_ratio", "me_cmp", "me_sub_cmp", "mb_cmp", "ildct_cmp", "dia_size", "last_predictor_count", "pre_me", "me_pre_cmp", "pre_dia_size", "me_subpel_quality", "dtg_active_format", "me_range", "intra_quant_bias", "inter_quant_bias", "slice_flags", "xvmc_acceleration", "mb_decision", "intra_matrix", "inter_matrix", "scenechange_threshold", "noise_reduction", "me_threshold", "mb_threshold", "intra_dc_precision", "skip_top", "skip_bottom", "border_masking", "mb_lmin", "mb_lmax", "me_penalty_compensation", "bidir_refine", "brd_scale", "keyint_min", "refs", "chromaoffset", "scenechange_factor", "mv0_threshold", "b_sensitivity", "color_primaries", "color_trc", "colorspace", "color_range", "chroma_sample_location", "slices", "field_order", "sample_rate", "channels", "sample_fmt", "frame_size", "frame_number", "block_align", "cutoff", "channel_layout", "request_channel_layout", "audio_service_type", "request_sample_fmt", "get_buffer2", "refcounted_frames", "qcompress", "qblur", "qmin", "qmax", "max_qdiff", "rc_qsquish", "rc_qmod_amp", "rc_qmod_freq", "rc_buffer_size", "rc_override_count", "rc_override", "rc_eq", "rc_max_rate", "rc_min_rate", "rc_buffer_aggressivity", "rc_initial_cplx", "rc_max_available_vbv_use", "rc_min_vbv_overflow_use", "rc_initial_buffer_occupancy", "coder_type", "context_model", "lmin", "lmax", "frame_skip_threshold", "frame_skip_factor", "frame_skip_exp", "frame_skip_cmp", "trellis", "min_prediction_order", "max_prediction_order", "timecode_frame_start", "rtp_callback", "rtp_payload_size", "mv_bits", "header_bits", "i_tex_bits", "p_tex_bits", "i_count", "p_count", "skip_count", "misc_bits", "frame_bits", "stats_out", "stats_in", "workaround_bugs", "strict_std_compliance", "error_concealment", "debug", "debug_mv", "err_recognition", "reordered_opaque", "hwaccel", "hwaccel_context", "error", "dct_algo", "idct_algo", "bits_per_coded_sample", "bits_per_raw_sample", "lowres", "coded_frame", "thread_count", "thread_type", "active_thread_type", "thread_safe_callbacks", "execute", "execute2", "nsse_weight", "profile", "level", "skip_loop_filter", "skip_idct", "skip_frame", "subtitle_header", "subtitle_header_size", "error_rate", "vbv_delay", "side_data_only_packets", "initial_padding", "framerate", "sw_pix_fmt", "pkt_timebase", "codec_descriptor", "pts_correction_num_faulty_pts", "pts_correction_num_faulty_dts", "pts_correction_last_pts", "pts_correction_last_dts", "sub_charenc", "sub_charenc_mode", "skip_alpha", "seek_preroll", "chroma_intra_matrix", "dump_separator", "codec_whitelist", "properties", "coded_side_data", "nb_coded_side_data", "hw_frames_ctx", "sub_text_format", "trailing_padding", "max_pixels", "hw_device_ctx", "hwaccel_flags");
	}
	public AVCodecContext(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVCodecContext implements Structure.ByReference {
		
	};
	public static class ByValue extends AVCodecContext implements Structure.ByValue {
		
	};
}
