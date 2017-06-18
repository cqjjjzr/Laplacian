package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:601</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVPacketSideData extends Structure {
	/** C type : uint8_t* */
	public Pointer data;
	public int size;
	/**
	 * @see AVPacketSideDataType<br>
	 * C type : AVPacketSideDataType
	 */
	public int type;
	public AVPacketSideData() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("data", "size", "type");
	}
	/**
	 * @param data C type : uint8_t*<br>
	 * @param type @see AVPacketSideDataType<br>
	 * C type : AVPacketSideDataType
	 */
	public AVPacketSideData(Pointer data, int size, int type) {
		super();
		this.data = data;
		this.size = size;
		this.type = type;
	}
	public AVPacketSideData(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVPacketSideData implements Structure.ByReference {
		
	};
	public static class ByValue extends AVPacketSideData implements Structure.ByValue {
		
	};
}
