package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;

public class PacketPlayInChat extends Packet implements PacketPlayIn {
	String msg;

	//public PacketPlayInChat() {
	//	super((byte) 0x01);
	//}
	
	public PacketPlayInChat() {
		super();
	}

	public PacketPlayInChat(String c) {
		super((byte) 0x01);
		this.msg = c;
	}

	public String getMessage() {
		return msg;
	}

	@Override
	public void read(PacketDataSerializer s) {
		msg = s.readString(-1);
	}

	public void setMessage(String c) {
		this.msg = c;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(msg);
	}
}
