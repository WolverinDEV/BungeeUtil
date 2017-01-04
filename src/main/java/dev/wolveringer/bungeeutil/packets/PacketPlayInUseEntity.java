package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInUseEntity extends Packet implements PacketPlayIn {
	public static enum Action {
		INTERACT,
		ATTACK,
		INTERACT_AT;
	}

	private int target;
	private Action action;
	private Vector3f location;
	private int hand = 0;

	@Override
	public void read(PacketDataSerializer s) {

		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:

			this.target = s.readVarInt();
			this.action = Action.values()[s.readVarInt()];
			if(this.action == Action.INTERACT_AT) {
				this.location = new Vector3f(s.readFloat(), s.readFloat(), s.readFloat());
			}
			if(this.action != Action.ATTACK && this.getBigVersion() != BigClientVersion.v1_8) {
				this.hand = s.readVarInt();
			}
			break;
		case v1_7:
			this.target = s.readInt();
			this.action = Action.values()[s.readByte()];
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "PacketPlayInUseEntity [target=" + this.target + ", action=" + this.action + ", location=" + this.location + ", hand=" + this.hand + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeVarInt(this.target);
			s.writeVarInt(this.action.ordinal());
			if (this.action == Action.INTERACT_AT) {
				s.writeFloat(this.location.getX());
				s.writeFloat(this.location.getY());
				s.writeFloat(this.location.getZ());
			}
			if(this.action != Action.ATTACK && this.getBigVersion() != BigClientVersion.v1_8) {
				s.writeVarInt(this.hand);
			}
			break;
		case v1_7:
			s.writeInt(this.target);
			s.writeByte(this.action.ordinal());
			break;
		default:
			break;
		}
	}
}
