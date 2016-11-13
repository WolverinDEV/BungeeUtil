package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutSetExperience extends Packet implements PacketPlayOut{
	float exp;
	int level;
	int total;
	
	public PacketPlayOutSetExperience() {
		super(0x1F);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		exp = s.readFloat();
		level = s.readVarInt();
		total = s.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(exp);
		s.writeVarInt(level);
		s.writeVarInt(total);
	}
	
	public float getExp() {
		return exp;
	}
	public int getLevel() {
		return level;
	}
	public int getTotal() {
		return total;
	}
	public PacketPlayOutSetExperience setExp(float exp) {
		this.exp = exp;
		return this;
	}
	public PacketPlayOutSetExperience setLevel(int level) {
		this.level = level;
		return this;
	}
	public PacketPlayOutSetExperience setTotal(int total) {
		this.total = total;
		return this;
	}
}
