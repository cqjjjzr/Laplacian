package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:2094</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ReSampleContext extends Structure {
	public int dummy;
	public ReSampleContext() {
		super();
	}
	protected List<String> getFieldOrder() {
		return java.util.Arrays.asList("dummy");
	}
	public ReSampleContext(int dummy) {
		super();
		this.dummy = dummy;
	}
	public ReSampleContext(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends ReSampleContext implements Structure.ByReference {
		
	};
	public static class ByValue extends ReSampleContext implements Structure.ByValue {
		
	};
}
