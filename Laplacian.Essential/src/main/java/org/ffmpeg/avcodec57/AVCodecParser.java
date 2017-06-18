package org.ffmpeg.avcodec57;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : .\libavcodec\avcodec.h:1952</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVCodecParser extends Structure {
	/**
	 * several codec IDs are permitted<br>
	 * C type : int[5]
	 */
	public int[] codec_ids = new int[5];
	public int priv_data_size;
	/** C type : parser_init_callback* */
	public parser_init_callback parser_init;
	/** C type : parser_parse_callback* */
	public parser_parse_callback parser_parse;
	/** C type : parser_close_callback* */
	public parser_close_callback parser_close;
	/** C type : split_callback* */
	public split_callback split;
	/** C type : AVCodecParser* */
	public ByReference next;
	/** <i>native declaration : .\libavcodec\avcodec.h:1948</i> */
	public interface parser_init_callback extends Callback {
		int apply(AVCodecParserContext s);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1949</i> */
	public interface parser_parse_callback extends Callback {
		int apply(AVCodecParserContext s, AVCodecContext avctx, PointerByReference poutbuf, IntByReference poutbuf_size, Pointer buf, int buf_size);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1950</i> */
	public interface parser_close_callback extends Callback {
		void apply(AVCodecParserContext s);
	};
	/** <i>native declaration : .\libavcodec\avcodec.h:1951</i> */
	public interface split_callback extends Callback {
		int apply(AVCodecContext avctx, Pointer buf, int buf_size);
	};
	public AVCodecParser() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("codec_ids", "priv_data_size", "parser_init", "parser_parse", "parser_close", "split", "next");
	}
	/**
	 * @param codec_ids several codec IDs are permitted<br>
	 * C type : int[5]<br>
	 * @param parser_init C type : parser_init_callback*<br>
	 * @param parser_parse C type : parser_parse_callback*<br>
	 * @param parser_close C type : parser_close_callback*<br>
	 * @param split C type : split_callback*<br>
	 * @param next C type : AVCodecParser*
	 */
	public AVCodecParser(int codec_ids[], int priv_data_size, parser_init_callback parser_init, parser_parse_callback parser_parse, parser_close_callback parser_close, split_callback split, ByReference next) {
		super();
		if ((codec_ids.length != this.codec_ids.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.codec_ids = codec_ids;
		this.priv_data_size = priv_data_size;
		this.parser_init = parser_init;
		this.parser_parse = parser_parse;
		this.parser_close = parser_close;
		this.split = split;
		this.next = next;
	}
	public AVCodecParser(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVCodecParser implements Structure.ByReference {
		
	};
	public static class ByValue extends AVCodecParser implements Structure.ByValue {
		
	};
}
