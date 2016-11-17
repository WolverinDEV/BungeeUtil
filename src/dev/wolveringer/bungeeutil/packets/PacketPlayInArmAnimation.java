package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.HandType;

public class PacketPlayInArmAnimation extends Packet implements PacketPlayIn {
	private int id;
	private int type;

	public PacketPlayInArmAnimation() {
		super(0x0A);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_7:
			this.id = s.readInt();
			this.type = s.readByte();
			break;
		case v1_8:
			type = 0;
			break;
		case v1_9:
		case v1_10:
		case v1_11:
			this.type = s.readVarInt();
			break;
		default:
			break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		switch (getBigVersion()) {
		case v1_7:
			s.writeInt(id);
			s.writeByte(type);
			break;
		case v1_8:
			type = 0;
			break;
		case v1_9:
		case v1_10:
		case v1_11:
			s.writeVarInt(type);
			break;
		default:
			break;
		}
	}
	
	public HandType getArmType() {
		return HandType.values()[type];
	}

	@Override
	public String toString() {
		return "PacketPlayInArmAnimation [id=" + id + ", type=" + type + "]";
	}
}
