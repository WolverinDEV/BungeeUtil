package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.packet.PacketDataSerializer;

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
		s.readInt();
	}

	public void setMessage(String c) {
		this.msg = c;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(msg);
	}
}
