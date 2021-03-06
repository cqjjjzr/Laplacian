package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:553</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVPanScan extends Structure {
	public int id;
	public int width;
	public int height;
	/** C type : int16_t[3][2] */
	public short[] position = new short[((3) * (2))];
	public AVPanScan() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("id", "width", "height", "position");
	}
	/** @param position C type : int16_t[3][2] */
	public AVPanScan(int id, int width, int height, short position[]) {
		super();
		this.id = id;
		this.width = width;
		this.height = height;
		if ((position.length != this.position.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.position = position;
	}
	public AVPanScan(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVPanScan implements Structure.ByReference {
		
	};
	public static class ByValue extends AVPanScan implements Structure.ByValue {
		
	};
}
