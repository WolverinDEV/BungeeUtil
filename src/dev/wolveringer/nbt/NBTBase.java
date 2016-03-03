package dev.wolveringer.nbt;

import java.io.DataInput;
import java.io.DataOutput;

public abstract class NBTBase {
	private final static int id_min = 0;
	private final static int id_max = 11;//edit i8f you wont add your Tags
	public static final String[] types = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };

	protected static NBTBase createTag(byte type) {
		if(type> id_max || type < id_min)
			throw new IllegalArgumentException("type must be: 11 <= type >= 0");
		switch (type) {
			case 0:
				return new NBTTagEnd();

			case 1:
				return new NBTTagByte();

			case 2:
				return new NBTTagShort();

			case 3:
				return new NBTTagInt();

			case 4:
				return new NBTTagLong();

			case 5:
				return new NBTTagFloat();

			case 6:
				return new NBTTagDouble();

			case 7:
				return new NBTTagByteArray();

			case 8:
				return new NBTTagString();

			case 9:
				return new NBTTagList();

			case 10:
				return new NBTTagCompound();

			case 11:
				return new NBTTagIntArray();
				//add case for your NBT
			default:
				return null;
		}
	}

	protected NBTBase() {}

	@Override
	public abstract NBTBase clone();

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof NBTBase)){
			return false;
		}else{
			NBTBase nbtbase = (NBTBase) object;

			return this.getTypeId() == nbtbase.getTypeId();
		}
	}

	public abstract byte getTypeId();

	@Override
	public int hashCode() {
		return this.getTypeId();
	}

	abstract void load(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws Exception;

	@Override
	public abstract String toString();
	abstract String toFormatedString(String prefix);
	
	public String toFormatedString(){
		return toFormatedString("  ");
	}
	
	abstract void write(DataOutput dataoutput) throws Exception;
}
