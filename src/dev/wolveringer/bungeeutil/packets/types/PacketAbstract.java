package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.Packet;

@SuppressWarnings("deprecation")
public abstract class PacketAbstract extends Packet{
	private byte[] b;
	
	public PacketAbstract(int id) {
		super(id);
	}
	
	public PacketAbstract() {}
	
	public void readUnusedBytes(PacketDataSerializer s){
		b = new byte[s.readableBytes()];
		s.readBytes(b);
	}
	public void writeUnusedBytes(PacketDataSerializer s){
		s.writeBytes(b);
	}
}
