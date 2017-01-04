package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketPlayOutEntityEffect extends Packet implements PacketPlayOut {
	
	int entity;
	int effect;
	int amplifier;
	int duration;
	boolean hidden = false;

	
	@Override
	public void read(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			entity = s.readVarInt();
			effect = s.readByte();
			amplifier = s.readByte();
			duration = s.readVarInt();
			hidden = s.readBoolean(); //Aka s.readByte() == 1
			break;
		case v1_7:
			entity = s.readInt();
			effect = s.readByte();
			amplifier = s.readByte();
			duration = s.readShort();
			hidden = false;
			break;
		default:
			break;
		}
	}
	
	public void write(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeVarInt(entity);
			s.writeByte(effect);
			s.writeByte(amplifier);
			s.writeVarInt(duration);
			s.writeBoolean(hidden);
			break;
		case v1_7:
			s.writeInt(entity);
			s.writeByte(effect);
			s.writeByte(amplifier);
			s.writeShort(duration);
			hidden = false;
			break;
		default:
			break;
		}
	}
	
	@Override
	public String toString() {
		return "PacketPlayOutEntityEffect [entity=" + entity + ", effect=" + effect + ", amplifier=" + amplifier + ", duration=" + duration + ", hidden=" + hidden + "]";
	}
	
}
