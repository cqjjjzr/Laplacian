package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:1111</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVSubtitleRect extends Structure {
	/** < top left corner  of pict, undefined when pict is not set */
	public int x;
	/** < top left corner  of pict, undefined when pict is not set */
	public int y;
	/** < width            of pict, undefined when pict is not set */
	public int w;
	/** < height           of pict, undefined when pict is not set */
	public int h;
	/** < number of colors in pict, undefined when pict is not set */
	public int nb_colors;
	/** C type : AVPicture */
	public AVPicture pict;
	/** C type : uint8_t*[4] */
	public Pointer[] data = new Pointer[4];
	/** C type : int[4] */
	public int[] linesize = new int[4];
	/**
	 * @see AVSubtitleType<br>
	 * C type : AVSubtitleType
	 */
	public int type;
	/**
	 * < 0 terminated plain UTF-8 text<br>
	 * C type : char*
	 */
	public Pointer text;
	/** C type : char* */
	public Pointer ass;
	public int flags;
	public AVSubtitleRect() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("x", "y", "w", "h", "nb_colors", "pict", "data", "linesize", "type", "text", "ass", "flags");
	}
	public AVSubtitleRect(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVSubtitleRect implements Structure.ByReference {
		
	};
	public static class ByValue extends AVSubtitleRect implements Structure.ByValue {
		
	};
}
