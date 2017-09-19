package dev.wolveringer.nbt;

public abstract class NBTNumber extends NBTBase {

	protected NBTNumber() {
	}

	public abstract long asLong();

	public abstract int asInt();

	public abstract short asShort();

	public abstract byte asByte();

	public abstract double asDouble();

	public abstract float asFloat();
}
