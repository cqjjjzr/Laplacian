package org.ffmpeg.avcodec;
import org.bridj.BridJ;
import org.bridj.Callback;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
import org.ffmpeg.avcodec.AVCodecLibrary.AVCodecID;
import org.ffmpeg.avcodec.AVCodecLibrary.MpegEncContext;
import org.ffmpeg.avutil.AVFrame;
import org.ffmpeg.avutil.AVUtilLibrary.AVMediaType;
import org.ffmpeg.avutil.AVUtilLibrary.AVPixelFormat;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:1069</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("avcodec-57") 
public class AVHWAccel extends StructObject {
	static {
		BridJ.register();
	}
	/** C type : const char* */
	@Field(0) 
	public Pointer<Byte > name() {
		return this.io.getPointerField(this, 0);
	}
	/** C type : const char* */
	@Field(0) 
	public AVHWAccel name(Pointer<Byte > name) {
		this.io.setPointerField(this, 0, name);
		return this;
	}
	/** C type : AVMediaType */
	@Field(1) 
	public IntValuedEnum<AVMediaType > type() {
		return this.io.getEnumField(this, 1);
	}
	/** C type : AVMediaType */
	@Field(1) 
	public AVHWAccel type(IntValuedEnum<AVMediaType > type) {
		this.io.setEnumField(this, 1, type);
		return this;
	}
	/** C type : AVCodecID */
	@Field(2) 
	public IntValuedEnum<AVCodecID > id() {
		return this.io.getEnumField(this, 2);
	}
	/** C type : AVCodecID */
	@Field(2) 
	public AVHWAccel id(IntValuedEnum<AVCodecID > id) {
		this.io.setEnumField(this, 2, id);
		return this;
	}
	/** C type : AVPixelFormat */
	@Field(3) 
	public IntValuedEnum<AVPixelFormat > pix_fmt() {
		return this.io.getEnumField(this, 3);
	}
	/** C type : AVPixelFormat */
	@Field(3) 
	public AVHWAccel pix_fmt(IntValuedEnum<AVPixelFormat > pix_fmt) {
		this.io.setEnumField(this, 3, pix_fmt);
		return this;
	}
	@Field(4) 
	public int capabilities() {
		return this.io.getIntField(this, 4);
	}
	@Field(4) 
	public AVHWAccel capabilities(int capabilities) {
		this.io.setIntField(this, 4, capabilities);
		return this;
	}
	/** C type : AVHWAccel* */
	@Field(5) 
	public Pointer<AVHWAccel > next() {
		return this.io.getPointerField(this, 5);
	}
	/** C type : AVHWAccel* */
	@Field(5) 
	public AVHWAccel next(Pointer<AVHWAccel > next) {
		this.io.setPointerField(this, 5, next);
		return this;
	}
	/** C type : alloc_frame_callback* */
	@Field(6) 
	public Pointer<alloc_frame_callback > alloc_frame() {
		return this.io.getPointerField(this, 6);
	}
	/** C type : alloc_frame_callback* */
	@Field(6) 
	public AVHWAccel alloc_frame(Pointer<alloc_frame_callback > alloc_frame) {
		this.io.setPointerField(this, 6, alloc_frame);
		return this;
	}
	/** C type : start_frame_callback* */
	@Field(7) 
	public Pointer<start_frame_callback > start_frame() {
		return this.io.getPointerField(this, 7);
	}
	/** C type : start_frame_callback* */
	@Field(7) 
	public AVHWAccel start_frame(Pointer<start_frame_callback > start_frame) {
		this.io.setPointerField(this, 7, start_frame);
		return this;
	}
	/** C type : decode_slice_callback* */
	@Field(8) 
	public Pointer<decode_slice_callback > decode_slice() {
		return this.io.getPointerField(this, 8);
	}
	/** C type : decode_slice_callback* */
	@Field(8) 
	public AVHWAccel decode_slice(Pointer<decode_slice_callback > decode_slice) {
		this.io.setPointerField(this, 8, decode_slice);
		return this;
	}
	/** C type : end_frame_callback* */
	@Field(9) 
	public Pointer<end_frame_callback > end_frame() {
		return this.io.getPointerField(this, 9);
	}
	/** C type : end_frame_callback* */
	@Field(9) 
	public AVHWAccel end_frame(Pointer<end_frame_callback > end_frame) {
		this.io.setPointerField(this, 9, end_frame);
		return this;
	}
	@Field(10) 
	public int frame_priv_data_size() {
		return this.io.getIntField(this, 10);
	}
	@Field(10) 
	public AVHWAccel frame_priv_data_size(int frame_priv_data_size) {
		this.io.setIntField(this, 10, frame_priv_data_size);
		return this;
	}
	/** C type : decode_mb_callback* */
	@Field(11) 
	public Pointer<decode_mb_callback > decode_mb() {
		return this.io.getPointerField(this, 11);
	}
	/** C type : decode_mb_callback* */
	@Field(11) 
	public AVHWAccel decode_mb(Pointer<decode_mb_callback > decode_mb) {
		this.io.setPointerField(this, 11, decode_mb);
		return this;
	}
	/** C type : init_callback* */
	@Field(12) 
	public Pointer<org.ffmpeg.avfilter.AVFilter.init_callback > init() {
		return this.io.getPointerField(this, 12);
	}
	/** C type : init_callback* */
	@Field(12) 
	public AVHWAccel init(Pointer<org.ffmpeg.avfilter.AVFilter.init_callback > init) {
		this.io.setPointerField(this, 12, init);
		return this;
	}
	/** C type : uninit_callback* */
	@Field(13) 
	public Pointer<org.ffmpeg.avfilter.AVFilter.uninit_callback > uninit() {
		return this.io.getPointerField(this, 13);
	}
	/** C type : uninit_callback* */
	@Field(13) 
	public AVHWAccel uninit(Pointer<org.ffmpeg.avfilter.AVFilter.uninit_callback > uninit) {
		this.io.setPointerField(this, 13, uninit);
		return this;
	}
	@Field(14) 
	public int priv_data_size() {
		return this.io.getIntField(this, 14);
	}
	@Field(14) 
	public AVHWAccel priv_data_size(int priv_data_size) {
		this.io.setIntField(this, 14, priv_data_size);
		return this;
	}
	@Field(15) 
	public int caps_internal() {
		return this.io.getIntField(this, 15);
	}
	@Field(15) 
	public AVHWAccel caps_internal(int caps_internal) {
		this.io.setIntField(this, 15, caps_internal);
		return this;
	}
	/** <i>native declaration : .\libavcodec\avcodec.h:1062</i> */
	public static abstract class alloc_frame_callback extends Callback<alloc_frame_callback > {
		public int apply(Pointer<AVCodecContext > avctx, Pointer<AVFrame > frame) {
			return apply(Pointer.getPeer(avctx), Pointer.getPeer(frame));
		}
		public int apply(@Ptr long avctx, @Ptr long frame) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class), (Pointer)Pointer.pointerToAddress(frame, AVFrame.class));
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1063</i> */
	public static abstract class start_frame_callback extends Callback<start_frame_callback > {
		public int apply(Pointer<AVCodecContext > avctx, Pointer<Byte > buf, int buf_size) {
			return apply(Pointer.getPeer(avctx), Pointer.getPeer(buf), buf_size);
		}
		public int apply(@Ptr long avctx, @Ptr long buf, int buf_size) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class), (Pointer)Pointer.pointerToAddress(buf, Byte.class), buf_size);
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1064</i> */
	public static abstract class decode_slice_callback extends Callback<decode_slice_callback > {
		public int apply(Pointer<AVCodecContext > avctx, Pointer<Byte > buf, int buf_size) {
			return apply(Pointer.getPeer(avctx), Pointer.getPeer(buf), buf_size);
		}
		public int apply(@Ptr long avctx, @Ptr long buf, int buf_size) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class), (Pointer)Pointer.pointerToAddress(buf, Byte.class), buf_size);
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1065</i> */
	public static abstract class end_frame_callback extends Callback<end_frame_callback > {
		public int apply(Pointer<AVCodecContext > avctx) {
			return apply(Pointer.getPeer(avctx));
		}
		public int apply(@Ptr long avctx) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class));
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1066</i> */
	public static abstract class decode_mb_callback extends Callback<decode_mb_callback > {
		public void apply(Pointer<MpegEncContext > s) {
			apply(Pointer.getPeer(s));
		}
		public void apply(@Ptr long s) {
			apply((Pointer)Pointer.pointerToAddress(s, MpegEncContext.class));
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1067</i> */
	public static abstract class init_callback extends Callback<init_callback > {
		public int apply(Pointer<AVCodecContext > avctx) {
			return apply(Pointer.getPeer(avctx));
		}
		public int apply(@Ptr long avctx) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class));
		}
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1068</i> */
	public static abstract class uninit_callback extends Callback<uninit_callback > {
		public int apply(Pointer<AVCodecContext > avctx) {
			return apply(Pointer.getPeer(avctx));
		}
		public int apply(@Ptr long avctx) {
			return apply((Pointer)Pointer.pointerToAddress(avctx, AVCodecContext.class));
		}
	};
	public AVHWAccel() {
		super();
	}
	public AVHWAccel(Pointer pointer) {
		super(pointer);
	}
}
