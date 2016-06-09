package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;
import dev.wolveringer.util.ByteString;

public class PacketPlayOutChat extends BetaPacket implements PacketPlayOut {

	private byte modus = 0;
	private ByteString data;

	public PacketPlayOutChat() {
		super(0x02);
	}

	public PacketPlayOutChat(IChatBaseComponent msg) {
		super(0x02);
		data = new ByteString(ChatSerializer.toJSONString(msg));
	}

	public IChatBaseComponent getMessage() {
		return ChatSerializer.fromJSON(data.getString());
	}

	public void setModus(byte modus) {
		this.modus = modus;
	}

	public byte getModus() {
		return modus;
	}

	@Override
	public void read(PacketDataSerializer s) {
		data = s.readStringBytes();
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			modus = s.readByte();
	}

	public void setMessage(IChatBaseComponent c) {
		this.data = new ByteString(ChatSerializer.toJSONString(c));
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(data);
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			s.writeByte(modus);
	}

	public void setRawMessage(byte[] raw) {
		this.data = new ByteString(raw);
	}

	public byte[] getRawMessage() {
		return this.data.getBytes();
	}
}
