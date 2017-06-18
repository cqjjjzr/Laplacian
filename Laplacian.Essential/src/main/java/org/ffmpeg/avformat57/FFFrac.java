package org.ffmpeg.avformat57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavformat\avformat.h:5</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class FFFrac extends Structure {
	public int dummy;
	public FFFrac() {
		super();
	}
	protected List<String> getFieldOrder() {
		return java.util.Arrays.asList("dummy");
	}
	public FFFrac(int dummy) {
		super();
		this.dummy = dummy;
	}
	public FFFrac(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends FFFrac implements Structure.ByReference {
		
	};
	public static class ByValue extends FFFrac implements Structure.ByValue {
		
	};
}
