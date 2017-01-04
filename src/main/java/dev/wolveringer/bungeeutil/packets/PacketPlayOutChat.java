package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutChat extends Packet implements PacketPlayOut {

	private byte modus = 0;
	private ByteString rawMessage;

	public PacketPlayOutChat(BaseComponent msg) {
		this.rawMessage = new ByteString(ComponentSerializer.toString(msg));
	}

	public BaseComponent getMessage() {
		return ComponentSerializer.parse(this.rawMessage.getString())[0];
	}


	public byte[] getRawMessage() {
		return this.rawMessage.getBytes();
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.rawMessage = s.readStringBytes();
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			this.modus = s.readByte();
			break;
		case v1_7:
			break;
		}
	}

	public void setMessage(BaseComponent c) {
		this.rawMessage = new ByteString(ComponentSerializer.toString(c));
	}

	public void setRawMessage(byte[] raw) {
		this.rawMessage = new ByteString(raw);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeStringBytes(this.rawMessage);
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeByte(this.modus);
			break;
		case v1_7:
			break;
		}
	}
}
