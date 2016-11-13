package dev.wolveringer.bungeeutil.packetlib.reader;

import io.netty.buffer.ByteBuf;

public class PacketDataSerializer_vX_X extends PacketDataSerializer_v1_8 {
	public PacketDataSerializer_vX_X(byte pid) {
		super(pid);
	}

	public PacketDataSerializer_vX_X(ByteBuf bytebuf) {
		super(bytebuf);
	}

	public PacketDataSerializer_vX_X(byte b, ByteBuf buf) {
		super(b,buf);
	}
}
