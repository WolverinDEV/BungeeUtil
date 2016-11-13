package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.util.ByteString;

public class PacketPlayOutPlayerListHeaderFooter extends Packet implements PacketPlayOut {
	private ByteString header;
	private ByteString footer;

	public PacketPlayOutPlayerListHeaderFooter() {
	}
	
	public PacketPlayOutPlayerListHeaderFooter(String header,String footer) {
		setHeader(ChatSerializer.fromMessage(header));
		setFooter(ChatSerializer.fromMessage(footer));
	}
	public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent header,IChatBaseComponent footer) {
		setHeader(header);
		setFooter(footer);
	}

	@Override
	public void read(PacketDataSerializer s) {
		header = s.readStringBytes();
		footer = s.readStringBytes();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(header);
		s.writeStringBytes(footer);
	}

	public IChatBaseComponent getHeader() {
		return ChatSerializer.fromJSON(header.getString());
	}

	public IChatBaseComponent getFooter() {
		return ChatSerializer.fromJSON(footer.getString());
	}

	public void setHeader(IChatBaseComponent header) {
		this.header = new ByteString(ChatSerializer.toJSONString(header));
	}

	public void setFooter(IChatBaseComponent footer) {
		this.footer = new ByteString(ChatSerializer.toJSONString(footer));
	}
}
