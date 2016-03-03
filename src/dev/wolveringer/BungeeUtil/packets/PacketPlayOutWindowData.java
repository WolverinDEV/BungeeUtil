package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutWindowData extends Packet implements PacketPlayOut{
	
	private int window;
	private short action;
	private short value;
	
	public PacketPlayOutWindowData() {
		super(0x31);
	}
	
	
	
	public PacketPlayOutWindowData(int window, short action, short value) {
		super(0x31);
		this.window = window;
		this.action = action;
		this.value = value;
	}

	@Override
	public void read(PacketDataSerializer s) {
		window = s.readUnsignedByte();
		action = s.readShort();
		value = s.readShort();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(window);
		s.writeShort(action);
		s.writeShort(value);
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}
}
