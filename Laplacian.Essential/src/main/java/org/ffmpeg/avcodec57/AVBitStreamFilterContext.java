package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:2367</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVBitStreamFilterContext extends Structure {
	/** C type : void* */
	public Pointer priv_data;
	/** C type : AVBitStreamFilter* */
	public org.ffmpeg.avcodec57.AVBitStreamFilter.ByReference filter;
	/** C type : AVCodecParserContext* */
	public org.ffmpeg.avcodec57.AVCodecParserContext.ByReference parser;
	/** C type : AVBitStreamFilterContext* */
	public ByReference next;
	/** C type : char* */
	public Pointer args;
	public AVBitStreamFilterContext() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("priv_data", "filter", "parser", "next", "args");
	}
	/**
	 * @param priv_data C type : void*<br>
	 * @param filter C type : AVBitStreamFilter*<br>
	 * @param parser C type : AVCodecParserContext*<br>
	 * @param next C type : AVBitStreamFilterContext*<br>
	 * @param args C type : char*
	 */
	public AVBitStreamFilterContext(Pointer priv_data, org.ffmpeg.avcodec57.AVBitStreamFilter.ByReference filter, org.ffmpeg.avcodec57.AVCodecParserContext.ByReference parser, ByReference next, Pointer args) {
		super();
		this.priv_data = priv_data;
		this.filter = filter;
		this.parser = parser;
		this.next = next;
		this.args = args;
	}
	public AVBitStreamFilterContext(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVBitStreamFilterContext implements Structure.ByReference {
		
	};
	public static class ByValue extends AVBitStreamFilterContext implements Structure.ByValue {
		
	};
}