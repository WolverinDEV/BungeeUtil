package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;

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
