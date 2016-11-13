package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutCloseWindow extends Packet implements PacketPlayOut{

	private int window;

	public PacketPlayOutCloseWindow() {
		super(0x2E);
	}

	public PacketPlayOutCloseWindow(int window) {
		super(0x2E);
		this.window = window;
	}

	public int getWindow() {
		return window;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.window = s.readUnsignedByte();
	}

	public void setWindow(int window) {
		this.window = window;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.window);
	}
}
