package org.ffmpeg.avutil55;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavutil\buffer.h:16</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVBufferRef extends Structure {
	/** C type : AVBuffer* */
	public org.ffmpeg.avutil55.AVBuffer.ByReference buffer;
	/** C type : uint8_t* */
	public Pointer data;
	public int size;
	public AVBufferRef() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("buffer", "data", "size");
	}
	/**
	 * @param buffer C type : AVBuffer*<br>
	 * @param data C type : uint8_t*
	 */
	public AVBufferRef(org.ffmpeg.avutil55.AVBuffer.ByReference buffer, Pointer data, int size) {
		super();
		this.buffer = buffer;
		this.data = data;
		this.size = size;
	}
	public AVBufferRef(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVBufferRef implements Structure.ByReference {
		
	};
	public static class ByValue extends AVBufferRef implements Structure.ByValue {
		
	};
}