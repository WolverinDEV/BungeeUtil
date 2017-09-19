package dev.wolveringer.BungeeUtil.packets.Abstract;

import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.packet.PacketDataSerializer;

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
