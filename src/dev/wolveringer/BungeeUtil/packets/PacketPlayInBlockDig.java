package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.packet.PacketDataSerializer;

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
	private BlockPosition loc;
	private int face;
	
	public PacketPlayInBlockDig() {
		super(0x07);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		state = State.values()[s.readByte()];
		loc = s.readBlockPosition();
		face = s.readUnsignedByte();
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(state.ordinal());
		s.writeBlockPosition(loc);
		s.writeByte(face);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public BlockPosition getLocation() {
		return loc;
	}

	public void setLocation(BlockPosition loc) {
		this.loc = loc;
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	@Override
	public String toString() {
		return "PacketPlayInBlockDig [state=" + state + ", loc=" + loc + ", face=" + face + "]";
	}
}
