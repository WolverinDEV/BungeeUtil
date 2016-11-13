package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;

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
