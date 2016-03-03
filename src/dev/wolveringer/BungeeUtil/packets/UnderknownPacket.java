package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.packet.PacketDataSerializer;

@SuppressWarnings("deprecation")
public class UnderknownPacket extends Packet {

	byte[] data;

	@Override
	public void read(PacketDataSerializer s) {
		data = new byte[s.readableBytes()];
		s.readBytes(data);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBytes(data);
	}
}
