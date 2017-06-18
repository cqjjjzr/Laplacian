package org.ffmpeg.avcodec57;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import org.ffmpeg.avutil55.AVFrame;

import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:1031</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVCodec extends Structure {
	/** C type : const char* */
	public Pointer name;
	/** C type : const char* */
	public Pointer long_name;
	/**
	 * @see org.ffmpeg.avutil55.Avutil55Library#AVMediaType<br>
	 * C type : AVMediaType
	 */
	public int type;
	/**
	 * @see AVCodecID<br>
	 * C type : AVCodecID
	 */
	public int id;
	public int capabilities;
	/**
	 * < array of supported framerates, or NULL if any, array is terminated by {0,0}<br>
	 * C type : const AVRational*
	 */
	public org.ffmpeg.avutil55.AVRational.ByReference supported_framerates;
	/**
	 * < array of supported pixel formats, or NULL if unknown, array is terminated by -1<br>
	 * C type : AVPixelFormat*
	 */
	public IntByReference pix_fmts;
	/**
	 * < array of supported audio samplerates, or NULL if unknown, array is terminated by 0<br>
	 * C type : const int*
	 */
	public IntByReference supported_samplerates;
	/**
	 * < array of supported sample formats, or NULL if unknown, array is terminated by -1<br>
	 * C type : AVSampleFormat*
	 */
	public IntByReference sample_fmts;
	/**
	 * < array of support channel layouts, or NULL if unknown. array is terminated by 0<br>
	 * C type : const uint64_t*
	 */
	public LongByReference channel_layouts;
	/** < maximum value for lowres supported by the decoder */
	public byte max_lowres;
	/**
	 * < AVClass for the private context<br>
	 * C type : const AVClass*
	 */
	public Pointer priv_class;
	/**
	 * < array of recognized profiles, or NULL if unknown, array is terminated by {FF_PROFILE_UNKNOWN}<br>
	 * C type : const AVProfile*
	 */
	public org.ffmpeg.avcodec57.AVProfile.ByReference profiles;
	public int priv_data_size;
	/** C type : AVCodec* */
	public ByReference next;
	/** C type : init_thread_copy_callback* */
	public init_thread_copy_callback init_thread_copy;
	/** C type : update_thread_context_callback* */
	public update_thread_context_callback update_thread_context;
	/** C type : const AVCodecDefault* */
	public org.ffmpeg.avcodec57.AVCodecDefault.ByReference defaults;
	/** C type : init_static_data_callback* */
	public init_static_data_callback init_static_data;
	/** C type : init_callback* */
	public org.ffmpeg.avfilter6.AVFilter.init_callback init;
	/** C type : encode_sub_callback* */
	public encode_sub_callback encode_sub;
	/** C type : encode2_callback* */
	public encode2_callback encode2;
	/** C type : decode_callback* */
	public decode_callback decode;
	/** C type : close_callback* */
	public org.ffmpeg.avcodec57.AVBitStreamFilter.close_callback close;
	/** C type : send_frame_callback* */
	public send_frame_callback send_frame;
	/** C type : send_packet_callback* */
	public send_packet_callback send_packet;
	/** C type : receive_frame_callback* */
	public receive_frame_callback receive_frame;
	/** C type : receive_packet_callback* */
	public receive_packet_callback receive_packet;
	/** C type : flush_callback* */
	public flush_callback flush;
	public int caps_internal;
	/** <i>native declaration : .\libavcodec\avcodec.h:1018</i> */
	public interface init_thread_copy_callback extends Callback {
		int apply(AVCodecContext AVCodecContextPtr1);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1019</i> */
	public interface update_thread_context_callback extends Callback {
		int apply(AVCodecContext dst, AVCodecContext src);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1020</i> */
	public interface init_static_data_callback extends Callback {
		void apply(AVCodec codec);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1021</i> */
	public interface init_callback extends Callback {
		int apply(AVCodecContext AVCodecContextPtr1);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1022</i> */
	public interface encode_sub_callback extends Callback {
		int apply(AVCodecContext AVCodecContextPtr1, Pointer buf, int buf_size, AVSubtitle sub);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1023</i> */
	public interface encode2_callback extends Callback {
		int apply(AVCodecContext avctx, AVPacket avpkt, AVFrame frame, IntByReference got_packet_ptr);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1024</i> */
	public interface decode_callback extends Callback {
		int apply(AVCodecContext AVCodecContextPtr1, Pointer outdata, IntByReference outdata_size, AVPacket avpkt);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1025</i> */
	public interface close_callback extends Callback {
		int apply(AVCodecContext AVCodecContextPtr1);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1026</i> */
	public interface send_frame_callback extends Callback {
		int apply(AVCodecContext avctx, AVFrame frame);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1027</i> */
	public interface send_packet_callback extends Callback {
		int apply(AVCodecContext avctx, AVPacket avpkt);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1028</i> */
	public interface receive_frame_callback extends Callback {
		int apply(AVCodecContext avctx, AVFrame frame);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1029</i> */
	public interface receive_packet_callback extends Callback {
		int apply(AVCodecContext avctx, AVPacket avpkt);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1030</i> */
	public interface flush_callback extends Callback {
		void apply(AVCodecContext AVCodecContextPtr1);
	};
	public AVCodec() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("name", "long_name", "type", "id", "capabilities", "supported_framerates", "pix_fmts", "supported_samplerates", "sample_fmts", "channel_layouts", "max_lowres", "priv_class", "profiles", "priv_data_size", "next", "init_thread_copy", "update_thread_context", "defaults", "init_static_data", "init", "encode_sub", "encode2", "decode", "close", "send_frame", "send_packet", "receive_frame", "receive_packet", "flush", "caps_internal");
	}
	public AVCodec(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVCodec implements Structure.ByReference {
		
	};
	public static class ByValue extends AVCodec implements Structure.ByValue {
		
	};
}
