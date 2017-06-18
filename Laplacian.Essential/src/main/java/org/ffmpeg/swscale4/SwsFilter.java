package org.ffmpeg.swscale4;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libswscale\swscale.h:39</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class SwsFilter extends Structure {
	/** C type : SwsVector* */
	public org.ffmpeg.swscale4.SwsVector.ByReference lumH;
	/** C type : SwsVector* */
	public org.ffmpeg.swscale4.SwsVector.ByReference lumV;
	/** C type : SwsVector* */
	public org.ffmpeg.swscale4.SwsVector.ByReference chrH;
	/** C type : SwsVector* */
	public org.ffmpeg.swscale4.SwsVector.ByReference chrV;
	public SwsFilter() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("lumH", "lumV", "chrH", "chrV");
	}
	/**
	 * @param lumH C type : SwsVector*<br>
	 * @param lumV C type : SwsVector*<br>
	 * @param chrH C type : SwsVector*<br>
	 * @param chrV C type : SwsVector*
	 */
	public SwsFilter(org.ffmpeg.swscale4.SwsVector.ByReference lumH, org.ffmpeg.swscale4.SwsVector.ByReference lumV, org.ffmpeg.swscale4.SwsVector.ByReference chrH, org.ffmpeg.swscale4.SwsVector.ByReference chrV) {
		super();
		this.lumH = lumH;
		this.lumV = lumV;
		this.chrH = chrH;
		this.chrV = chrV;
	}
	public SwsFilter(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends SwsFilter implements Structure.ByReference {
		
	};
	public static class ByValue extends SwsFilter implements Structure.ByValue {
		
	};
}
