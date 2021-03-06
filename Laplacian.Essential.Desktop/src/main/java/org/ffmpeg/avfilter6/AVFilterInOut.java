package org.ffmpeg.avfilter6;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavfilter\avfilter.h:485</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVFilterInOut extends Structure {
	/** C type : char* */
	public Pointer name;
	/** C type : AVFilterContext* */
	public org.ffmpeg.avfilter6.AVFilterContext.ByReference filter_ctx;
	public int pad_idx;
	/** C type : AVFilterInOut* */
	public ByReference next;
	public AVFilterInOut() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("name", "filter_ctx", "pad_idx", "next");
	}
	/**
	 * @param name C type : char*<br>
	 * @param filter_ctx C type : AVFilterContext*<br>
	 * @param next C type : AVFilterInOut*
	 */
	public AVFilterInOut(Pointer name, org.ffmpeg.avfilter6.AVFilterContext.ByReference filter_ctx, int pad_idx, ByReference next) {
		super();
		this.name = name;
		this.filter_ctx = filter_ctx;
		this.pad_idx = pad_idx;
		this.next = next;
	}
	public AVFilterInOut(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVFilterInOut implements Structure.ByReference {
		
	};
	public static class ByValue extends AVFilterInOut implements Structure.ByValue {
		
	};
}
