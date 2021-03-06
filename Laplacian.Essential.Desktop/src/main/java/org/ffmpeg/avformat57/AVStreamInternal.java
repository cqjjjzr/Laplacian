package org.ffmpeg.avformat57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavformat\avformat.h:180</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVStreamInternal extends Structure {
	public int dummy;
	public AVStreamInternal() {
		super();
	}
	protected List<String> getFieldOrder() {
		return java.util.Arrays.asList("dummy");
	}
	public AVStreamInternal(int dummy) {
		super();
		this.dummy = dummy;
	}
	public AVStreamInternal(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVStreamInternal implements Structure.ByReference {
		
	};
	public static class ByValue extends AVStreamInternal implements Structure.ByValue {
		
	};
}
