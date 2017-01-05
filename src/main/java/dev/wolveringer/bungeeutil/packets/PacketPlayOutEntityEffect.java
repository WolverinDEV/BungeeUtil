package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutEntityEffect extends Packet implements PacketPlayOut {

	private int entity;
	private int effect;
	private int amplifier;
	private int duration;
	private boolean hidden = false;

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			this.entity = s.readVarInt();
			this.effect = s.readByte();
			this.amplifier = s.readByte();
			this.duration = s.readVarInt();
			this.hidden = s.readBoolean(); //Aka s.readByte() == 1
			break;
		case v1_7:
			this.entity = s.readInt();
			this.effect = s.readByte();
			this.amplifier = s.readByte();
			this.duration = s.readShort();
			this.hidden = false;
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "PacketPlayOutEntityEffect [entity=" + this.entity + ", effect=" + this.effect + ", amplifier=" + this.amplifier + ", duration=" + this.duration + ", hidden=" + this.hidden + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeVarInt(this.entity);
			s.writeByte(this.effect);
			s.writeByte(this.amplifier);
			s.writeVarInt(this.duration);
			s.writeBoolean(this.hidden);
			break;
		case v1_7:
			s.writeInt(this.entity);
			s.writeByte(this.effect);
			s.writeByte(this.amplifier);
			s.writeShort(this.duration);
			this.hidden = false;
			break;
		default:
			break;
		}
	}

}
