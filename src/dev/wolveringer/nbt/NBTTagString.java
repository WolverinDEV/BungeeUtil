package dev.wolveringer.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.google.common.base.Charsets;

public class NBTTagString extends NBTBase {
	private byte[] final_data;

	public NBTTagString() {
		final_data = "".getBytes();
	}

	public NBTTagString(String string) {
		if(string == null || string.length() == 0)
			throw new IllegalArgumentException("Empty string not allowed"); //Todo remove?
		final_data = string.getBytes(Charsets.UTF_8);
	}

	void write(DataOutput paramDataOutput) {
		try{
			paramDataOutput.writeShort(final_data.length);
			paramDataOutput.write(final_data, 0, final_data.length);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	void load(DataInput paramDataInput, int paramInt, NBTReadLimiter paramNBTReadLimiter) {
		try{
			this.final_data = new byte[paramDataInput.readUnsignedShort()];
			paramDataInput.readFully(this.final_data);
		}catch (IOException e){
			e.printStackTrace();
		}
		paramNBTReadLimiter.readBytes(final_data.length+2);
	}

	public byte getTypeId() {
		return 8;
	}

	public String toString() {
		return "\"" + charsetEncoding(this.final_data) + "\"";
	}

	private String charsetEncoding(byte[] in) {
		return new String(in, Charsets.UTF_8);
	}

	@Override
	String toFormatedString(String in) {
		return in + toString();
	}

	public NBTBase clone() {
		NBTTagString string =new NBTTagString();
		string.final_data = final_data;
		return string;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(this.final_data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(!super.equals(obj))
			return false;
		if(getClass() != obj.getClass())
			return false;
		NBTTagString other = (NBTTagString) obj;
		if(!Arrays.equals(this.final_data, other.final_data))
			return false;
		return true;
	}

	public String getData() {
		return this.charsetEncoding(final_data);
	}
}
