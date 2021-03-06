package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import org.ffmpeg.avutil55.AVRational;

import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:2388</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVBSFContext extends Structure {
	/** C type : const AVClass* */
	public Pointer av_class;
	/** C type : AVBitStreamFilter* */
	public org.ffmpeg.avcodec57.AVBitStreamFilter.ByReference filter;
	/** C type : AVBSFInternal* */
	public org.ffmpeg.avcodec57.AVBSFInternal.ByReference internal;
	/** C type : void* */
	public Pointer priv_data;
	/** C type : AVCodecParameters* */
	public org.ffmpeg.avcodec57.AVCodecParameters.ByReference par_in;
	/** C type : AVCodecParameters* */
	public org.ffmpeg.avcodec57.AVCodecParameters.ByReference par_out;
	/** C type : AVRational */
	public AVRational time_base_in;
	/** C type : AVRational */
	public AVRational time_base_out;
	public AVBSFContext() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("av_class", "filter", "internal", "priv_data", "par_in", "par_out", "time_base_in", "time_base_out");
	}
	/**
	 * @param av_class C type : const AVClass*<br>
	 * @param filter C type : AVBitStreamFilter*<br>
	 * @param internal C type : AVBSFInternal*<br>
	 * @param priv_data C type : void*<br>
	 * @param par_in C type : AVCodecParameters*<br>
	 * @param par_out C type : AVCodecParameters*<br>
	 * @param time_base_in C type : AVRational<br>
	 * @param time_base_out C type : AVRational
	 */
	public AVBSFContext(Pointer av_class, org.ffmpeg.avcodec57.AVBitStreamFilter.ByReference filter, org.ffmpeg.avcodec57.AVBSFInternal.ByReference internal, Pointer priv_data, org.ffmpeg.avcodec57.AVCodecParameters.ByReference par_in, org.ffmpeg.avcodec57.AVCodecParameters.ByReference par_out, AVRational time_base_in, AVRational time_base_out) {
		super();
		this.av_class = av_class;
		this.filter = filter;
		this.internal = internal;
		this.priv_data = priv_data;
		this.par_in = par_in;
		this.par_out = par_out;
		this.time_base_in = time_base_in;
		this.time_base_out = time_base_out;
	}
	public AVBSFContext(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVBSFContext implements Structure.ByReference {
		
	};
	public static class ByValue extends AVBSFContext implements Structure.ByValue {
		
	};
}
