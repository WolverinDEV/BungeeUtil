package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.Packet;

public abstract class PacketAbstract extends Packet{
	private byte[] b;

	public PacketAbstract() {}

	public PacketAbstract(int id) {
		super(id);
	}

	public void readUnusedBytes(PacketDataSerializer s){
		this.b = new byte[s.readableBytes()];
		s.readBytes(this.b);
	}
	public void writeUnusedBytes(PacketDataSerializer s){
		s.writeBytes(this.b);
	}
}
