package org.ffmpeg.avutil;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : libavutil\opt.h:221</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("avutil-55") 
public class AVOptionRanges extends StructObject {
	static {
		BridJ.register();
	}
	/** C type : AVOptionRange** */
	@Field(0) 
	public Pointer<Pointer<AVOptionRange > > range() {
		return this.io.getPointerField(this, 0);
	}
	/** C type : AVOptionRange** */
	@Field(0) 
	public AVOptionRanges range(Pointer<Pointer<AVOptionRange > > range) {
		this.io.setPointerField(this, 0, range);
		return this;
	}
	@Field(1) 
	public int nb_ranges() {
		return this.io.getIntField(this, 1);
	}
	@Field(1) 
	public AVOptionRanges nb_ranges(int nb_ranges) {
		this.io.setIntField(this, 1, nb_ranges);
		return this;
	}
	@Field(2) 
	public int nb_components() {
		return this.io.getIntField(this, 2);
	}
	@Field(2) 
	public AVOptionRanges nb_components(int nb_components) {
		this.io.setIntField(this, 2, nb_components);
		return this;
	}
	public AVOptionRanges() {
		super();
	}
	public AVOptionRanges(Pointer pointer) {
		super(pointer);
	}
}
