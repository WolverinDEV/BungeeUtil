package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInClientState extends Packet implements PacketPlayOut{
	private int state;
	public PacketPlayInClientState() {
		super(0x16);
	}
	public PacketPlayInClientState(int state) {
		super(0x16);
		this.state = state;
	}
	@Override
	public void read(PacketDataSerializer serelizer) {
		state = serelizer.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer serelizer) {
		serelizer.writeVarInt(state);
	}
	public int getState() {
		return state;
	}
}
