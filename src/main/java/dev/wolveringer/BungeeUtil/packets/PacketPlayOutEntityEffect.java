package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutEntityEffect extends Packet implements PacketPlayOut {
	
	int entity;
	int effect;
	int amplifier;
	int duration;
	boolean hidden = false;
	
	public PacketPlayOutEntityEffect(int entity, int effect, int amplifier, int duration, boolean hidden) {
		this();
		this.entity = entity;
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
		this.hidden = hidden;
	}
	
	public PacketPlayOutEntityEffect() {
		super(0x1D);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		entity = getVersion().getBigVersion() == BigClientVersion.v1_7 ? s.readInt() : s.readVarInt();
		effect = s.readByte();
		amplifier = s.readByte();
		duration = getVersion().getBigVersion() == BigClientVersion.v1_7 ? s.readShort() : s.readVarInt();
		hidden = getVersion().getBigVersion() == BigClientVersion.v1_8 ? s.readBoolean() : (getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) ? s.readByte() == 1 : false;
	}
	
	public void write(PacketDataSerializer s) {
		if (getVersion().getBigVersion() == BigClientVersion.v1_8 || getVersion().getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) {
			s.writeVarInt(entity);
			s.writeByte(effect);
			s.writeByte(amplifier);
			s.writeVarInt(duration);
			if (getBigVersion() == BigClientVersion.v1_8) s.writeBoolean(hidden);
			else if (getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) s.writeByte(hidden == true ? 1 : 0);
		}
		else if (getVersion().getBigVersion() == BigClientVersion.v1_7) {
			s.writeInt(entity);
			s.writeByte(effect);
			s.writeByte(amplifier);
			s.writeShort(duration);
		}
	}
	
	@Override
	public String toString() {
		return "PacketPlayOutEntityEffect [entity=" + entity + ", effect=" + effect + ", amplifier=" + amplifier + ", duration=" + duration + ", hidden=" + hidden + "]";
	}
	
}
