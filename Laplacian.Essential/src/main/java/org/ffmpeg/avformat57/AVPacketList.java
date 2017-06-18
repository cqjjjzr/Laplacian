package org.ffmpeg.avformat57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import org.ffmpeg.avcodec57.AVPacket;
/**
 * <i>native declaration : libavformat\avformat.h:496</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVPacketList extends Structure {
	/** C type : AVPacket */
	public AVPacket pkt;
	/** C type : AVPacketList* */
	public ByReference next;
	public AVPacketList() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("pkt", "next");
	}
	/**
	 * @param pkt C type : AVPacket<br>
	 * @param next C type : AVPacketList*
	 */
	public AVPacketList(AVPacket pkt, ByReference next) {
		super();
		this.pkt = pkt;
		this.next = next;
	}
	public AVPacketList(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVPacketList implements Structure.ByReference {
		
	};
	public static class ByValue extends AVPacketList implements Structure.ByValue {
		
	};
}
