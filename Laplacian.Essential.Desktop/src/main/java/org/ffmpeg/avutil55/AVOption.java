package org.ffmpeg.avutil55;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : libavutil\opt.h:206</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class AVOption extends Structure {
	/** C type : const char* */
	public Pointer name;
	/** C type : const char* */
	public Pointer help;
	public int offset;
	/**
	 * @see AVOptionType<br>
	 * C type : AVOptionType
	 */
	public int type;
	/** C type : default_val_union */
	public default_val_union default_val;
	/** < minimum valid value for the option */
	public double min;
	/** < maximum valid value for the option */
	public double max;
	public int flags;
	/** C type : const char* */
	public Pointer unit;
	/** <i>native declaration : libavutil\opt.h:205</i> */
	public static class default_val_union extends Union {
		public long i64;
		public double dbl;
		/** C type : const char* */
		public Pointer str;
		/** C type : AVRational */
		public AVRational q;
		public default_val_union() {
			super();
		}
		public default_val_union(long i64) {
			super();
			this.i64 = i64;
			setType(Long.TYPE);
		}
		public default_val_union(double dbl) {
			super();
			this.dbl = dbl;
			setType(Double.TYPE);
		}
		/** @param str C type : const char* */
		public default_val_union(Pointer str) {
			super();
			this.str = str;
			setType(Pointer.class);
		}
		/** @param q C type : AVRational */
		public default_val_union(AVRational q) {
			super();
			this.q = q;
			setType(AVRational.class);
		}
		public static class ByReference extends default_val_union implements Structure.ByReference {
			
		};
		public static class ByValue extends default_val_union implements Structure.ByValue {
			
		};
	};
	public AVOption() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("name", "help", "offset", "type", "default_val", "min", "max", "flags", "unit");
	}
	/**
	 * @param name C type : const char*<br>
	 * @param help C type : const char*<br>
	 * @param type @see AVOptionType<br>
	 * C type : AVOptionType<br>
	 * @param default_val C type : default_val_union<br>
	 * @param min < minimum valid value for the option<br>
	 * @param max < maximum valid value for the option<br>
	 * @param unit C type : const char*
	 */
	public AVOption(Pointer name, Pointer help, int offset, int type, default_val_union default_val, double min, double max, int flags, Pointer unit) {
		super();
		this.name = name;
		this.help = help;
		this.offset = offset;
		this.type = type;
		this.default_val = default_val;
		this.min = min;
		this.max = max;
		this.flags = flags;
		this.unit = unit;
	}
	public AVOption(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends AVOption implements Structure.ByReference {
		
	};
	public static class ByValue extends AVOption implements Structure.ByValue {
		
	};
}
