package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

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
