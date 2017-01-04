package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;

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
