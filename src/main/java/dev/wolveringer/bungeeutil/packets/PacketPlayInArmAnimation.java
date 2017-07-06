package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.HandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInArmAnimation extends Packet implements PacketPlayIn {
	private int id;
	private int type;

	public HandType getArmType() {
		return HandType.values()[this.type];
	}

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_7:
			this.id = s.readInt();
			this.type = s.readByte();
			break;
		case v1_8:
			this.type = 0;
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			this.type = s.readVarInt();
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "PacketPlayInArmAnimation [id=" + this.id + ", type=" + this.type + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_7:
			s.writeInt(this.id);
			s.writeByte(this.type);
			break;
		case v1_8:
			this.type = 0;
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			s.writeVarInt(this.type);
			break;
		default:
			break;
		}
	}
}
