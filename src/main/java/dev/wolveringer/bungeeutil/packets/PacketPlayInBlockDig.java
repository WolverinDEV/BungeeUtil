package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInBlockDig extends Packet implements PacketPlayIn {
	public static enum State {
		START_DIGGING,
		END_DIGGING,
		FINISH_DIGGING,
		DROP_ITEMSTACK,
		DROP_ITEM,
		SHOT_ARROW,
		SWARM_HAND;
	}
	private State state;
	private BlockPosition location;
	private int face;

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		if (this.getVersion().getVersion() <= ClientVersion.v1_8_0.getVersion()) {
			this.state = State.values()[s.readByte()];
		} else {
			this.state = State.values()[s.readVarInt()];
		}
		this.location = s.readBlockPosition();
		this.face = s.readUnsignedByte();
	}

	@Override
	public String toString() {
		return "PacketPlayInBlockDig [state=" + this.state + ", location=" + this.location + ", face=" + this.face + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		if (this.getVersion().getVersion() <= ClientVersion.v1_8_0.getVersion()) {
			s.writeByte(this.state.ordinal());
		} else {
			s.writeVarInt(this.state.ordinal());
		}
		s.writeBlockPosition(this.location);
		s.writeByte(this.face);
	}
}
