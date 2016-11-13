package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PacketPlayOutChat extends BetaPacket implements PacketPlayOut {

	private byte modus = 0;
	private ByteString rawMessage;

	public PacketPlayOutChat(BaseComponent msg) {
		rawMessage = new ByteString(ComponentSerializer.toString(msg));
	}

	public BaseComponent getMessage() {
		return ComponentSerializer.parse(rawMessage.getString())[0];
	}

	public void setModus(byte modus) {
		this.modus = modus;
	}

	public byte getModus() {
		return modus;
	}

	@Override
	public void read(PacketDataSerializer s) {
		rawMessage = s.readStringBytes();
		switch (getBigVersion()) {
		case v1_10:
		case v1_9:
		case v1_8:
			modus = s.readByte();
			break;
		case v1_7:
			break;
		}
	}

	public void setMessage(BaseComponent c) {
		this.rawMessage = new ByteString(ComponentSerializer.toString(c));
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(rawMessage);
		switch (getBigVersion()) {
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeByte(modus);
			break;
		case v1_7:
			break;
		}
	}

	public void setRawMessage(byte[] raw) {
		this.rawMessage = new ByteString(raw);
	}

	public byte[] getRawMessage() {
		return this.rawMessage.getBytes();
	}
}
