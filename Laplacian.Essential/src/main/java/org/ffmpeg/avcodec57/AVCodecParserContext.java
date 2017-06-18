package org.ffmpeg.avcodec57;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:1939</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVCodecParserContext extends Structure {
	/** C type : void* */
	public Pointer priv_data;
	/** C type : AVCodecParser* */
	public org.ffmpeg.avcodec57.AVCodecParser.ByReference parser;
	/** offset of the current frame */
	public long frame_offset;
	/**
	 * current offset<br>
	 * (incremented by each av_parser_parse())
	 */
	public long cur_offset;
	/** offset of the next frame */
	public long next_frame_offset;
	/** XXX: Put it back in AVCodecContext. */
	public int pict_type;
	/** XXX: Put it back in AVCodecContext. */
	public int repeat_pict;
	/** pts of the current frame */
	public long pts;
	/** dts of the current frame */
	public long dts;
	public long last_pts;
	public long last_dts;
	public int fetch_timestamp;
	public int cur_frame_start_index;
	/** C type : int64_t[4] */
	public long[] cur_frame_offset = new long[4];
	/** C type : int64_t[4] */
	public long[] cur_frame_pts = new long[4];
	/** C type : int64_t[4] */
	public long[] cur_frame_dts = new long[4];
	public int flags;
	/**
	 * Set if the parser has a valid file offset<br>
	 * < byte offset from starting packet start
	 */
	public long offset;
	/** C type : int64_t[4] */
	public long[] cur_frame_end = new long[4];
	public int key_frame;
	public long convergence_duration;
	public int dts_sync_point;
	public int dts_ref_dts_delta;
	public int pts_dts_delta;
	/** C type : int64_t[4] */
	public long[] cur_frame_pos = new long[4];
	public long pos;
	public long last_pos;
	public int duration;
	/**
	 * @see AVFieldOrder<br>
	 * C type : AVFieldOrder
	 */
	public int field_order;
	/**
	 * @see AVPictureStructure<br>
	 * C type : AVPictureStructure
	 */
	public int picture_structure;
	public int output_picture_number;
	public int width;
	public int height;
	public int coded_width;
	public int coded_height;
	public int format;
	public AVCodecParserContext() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("priv_data", "parser", "frame_offset", "cur_offset", "next_frame_offset", "pict_type", "repeat_pict", "pts", "dts", "last_pts", "last_dts", "fetch_timestamp", "cur_frame_start_index", "cur_frame_offset", "cur_frame_pts", "cur_frame_dts", "flags", "offset", "cur_frame_end", "key_frame", "convergence_duration", "dts_sync_point", "dts_ref_dts_delta", "pts_dts_delta", "cur_frame_pos", "pos", "last_pos", "duration", "field_order", "picture_structure", "output_picture_number", "width", "height", "coded_width", "coded_height", "format");
	}
	public AVCodecParserContext(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVCodecParserContext implements Structure.ByReference {
		
	};
	public static class ByValue extends AVCodecParserContext implements Structure.ByValue {
		
	};
}
