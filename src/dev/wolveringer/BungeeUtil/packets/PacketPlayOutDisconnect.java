package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutDisconnect extends Packet implements PacketPlayOut{

	private String c;
	
	public PacketPlayOutDisconnect() {
	}
	
	public PacketPlayOutDisconnect(IChatBaseComponent c) {
		this.c = ChatSerializer.toJSONString(c);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		c = s.readString(-1);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(c);
	}
	
	public IChatBaseComponent getMessage(){
		return ChatSerializer.fromJSON(c);
	}
	
}
