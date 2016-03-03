package dev.wolveringer.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase {

	NBTTagEnd() {
	}

	@Override
	public NBTBase clone() {
		return new NBTTagEnd();
	}

	@Override
	public byte getTypeId() {
		return (byte) 0;
	}

	@Override
	void load(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) {
	}

	@Override
	public String toString() {
		return "END";
	}

	@Override
	String toFormatedString(String in) {
		return in+toString();
	}
	
	@Override
	void write(DataOutput dataoutput) {
	}
}
