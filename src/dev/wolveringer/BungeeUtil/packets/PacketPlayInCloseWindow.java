package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInCloseWindow extends Packet implements PacketPlayIn {

	private int window;

	public PacketPlayInCloseWindow() {
		super(0x0D);
	}

	public int getWindow() {
		return window;
	}

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readByte();
	}

	public void setWindow(int window) {
		this.window = window;
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
	}
}
