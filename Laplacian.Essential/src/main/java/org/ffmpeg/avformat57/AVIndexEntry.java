package org.ffmpeg.avformat57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavformat\avformat.h:173</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVIndexEntry extends Structure {
	public long pos;
	/**
	 * <<br>
	 * Timestamp in AVStream.time_base units, preferably the time from which on correctly decoded frames are available<br>
	 * when seeking to this entry. That means preferable PTS on keyframe based formats.<br>
	 * But demuxers can choose to store a different timestamp, if it is more convenient for the implementation or nothing better<br>
	 * is known
	 */
	public long timestamp;
	/** Conversion Error : flags:2 (This runtime does not support bit fields : JNA (please use BridJ instead)) */
	/** Conversion Error : size:30 (This runtime does not support bit fields : JNA (please use BridJ instead)) */
	/** < Minimum distance between this and the previous keyframe, used to avoid unneeded searching. */
	public int min_distance;
	public AVIndexEntry() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("pos", "timestamp", "min_distance");
	}
	/**
	 * @param timestamp <<br>
	 * Timestamp in AVStream.time_base units, preferably the time from which on correctly decoded frames are available<br>
	 * when seeking to this entry. That means preferable PTS on keyframe based formats.<br>
	 * But demuxers can choose to store a different timestamp, if it is more convenient for the implementation or nothing better<br>
	 * is known<br>
	 * @param min_distance < Minimum distance between this and the previous keyframe, used to avoid unneeded searching.
	 */
	public AVIndexEntry(long pos, long timestamp, int min_distance) {
		super();
		this.pos = pos;
		this.timestamp = timestamp;
		this.min_distance = min_distance;
	}
	public AVIndexEntry(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVIndexEntry implements Structure.ByReference {
		
	};
	public static class ByValue extends AVIndexEntry implements Structure.ByValue {
		
	};
}
