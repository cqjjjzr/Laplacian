package org.ffmpeg.avformat57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavformat\avformat.h:50</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVProbeData extends Structure {
	/** C type : const char* */
	public Pointer filename;
	/**
	 * < Buffer must have AVPROBE_PADDING_SIZE of extra allocated bytes filled with zero.<br>
	 * C type : unsigned char*
	 */
	public Pointer buf;
	/** < Size of buf except extra allocated bytes */
	public int buf_size;
	/**
	 * < mime_type, when known.<br>
	 * C type : const char*
	 */
	public Pointer mime_type;
	public AVProbeData() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("filename", "buf", "buf_size", "mime_type");
	}
	/**
	 * @param filename C type : const char*<br>
	 * @param buf < Buffer must have AVPROBE_PADDING_SIZE of extra allocated bytes filled with zero.<br>
	 * C type : unsigned char*<br>
	 * @param buf_size < Size of buf except extra allocated bytes<br>
	 * @param mime_type < mime_type, when known.<br>
	 * C type : const char*
	 */
	public AVProbeData(Pointer filename, Pointer buf, int buf_size, Pointer mime_type) {
		super();
		this.filename = filename;
		this.buf = buf;
		this.buf_size = buf_size;
		this.mime_type = mime_type;
	}
	public AVProbeData(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVProbeData implements Structure.ByReference {
		
	};
	public static class ByValue extends AVProbeData implements Structure.ByValue {
		
	};
}