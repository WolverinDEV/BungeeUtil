package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.util.ByteString;

public class PacketLoginDisconnect extends Packet {
	private ByteString reson;

	public PacketLoginDisconnect(String reson) {
		this.reson = new ByteString(reson);
	}

	public PacketLoginDisconnect() {
		super(-1);
	}

	@Override
	public void read(PacketDataSerializer s) {
		reson = s.readStringBytes();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.write(reson);
	}

	public IChatBaseComponent getReson() {
		return ChatSerializer.fromJSON(reson.getString());
	}

	public void setReson(IChatBaseComponent reson) {
		this.reson = new ByteString(ChatSerializer.toJSONString(reson));
	}
}
