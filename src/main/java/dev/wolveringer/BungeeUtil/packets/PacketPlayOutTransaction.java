package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutTransaction extends Packet implements PacketPlayOut{

	private int window;
	private short id;
	private boolean cancel;

	public PacketPlayOutTransaction() {
		super(0x32);
	}

	public PacketPlayOutTransaction(int window, short id, boolean flag) {
		super(0x32);
		this.window = window;
		this.id = id;
		this.cancel = flag;
	}

	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readUnsignedByte();
		this.id = packetdataserializer.readShort();
		this.cancel = packetdataserializer.readBoolean();
	}

	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
		packetdataserializer.writeShort(this.id);
		packetdataserializer.writeBoolean(this.cancel);
	}

	public String toString() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] { Integer.valueOf(this.window), Short.valueOf(this.id), Boolean.valueOf(this.cancel) });
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
}