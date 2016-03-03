package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutNamedSoundEffect extends Packet implements PacketPlayOut{
	private float e;
	private int f;
	Location loc;
	String name;

	public PacketPlayOutNamedSoundEffect() {
		super(0x29);
	}

	public PacketPlayOutNamedSoundEffect(Location loc, String sound, float vol, float p) {
		super(0x29);
		this.name = sound;
		this.loc = loc.multiply(8);
		this.e = vol;
		this.f = (int) (p * 63.0F);
		if(this.f < 0)
			this.f = 0;
		if(this.f > 255)
			this.f = 255;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.name = s.readString(256);
		loc = new Location(s.readInt(), s.readInt(), s.readInt());
		e = s.readFloat();
		f = s.readUnsignedByte();
	}

	public void setLocation(Location loc) {
		this.loc = loc.multiply(8);
	}

	public void setSoundName(String s) {
		this.name = s;
	}

	@Override
	public void write(PacketDataSerializer p) {
		p.writeString(name);
		p.writeInt((int) loc.getBlockX());
		p.writeInt((int) loc.getBlockY());
		p.writeInt((int) loc.getBlockZ());
		p.writeFloat(e);
		p.writeByte(f);
	}

	@Override
	public String toString() {
		return "PacketPlayOutNamedSoundEffect [e=" + e + ", f=" + f + ", loc=" + loc + ", name=" + name + "]";
	}
}
