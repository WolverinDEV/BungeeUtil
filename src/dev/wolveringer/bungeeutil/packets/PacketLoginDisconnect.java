package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketLoginDisconnect extends Packet {
	@Getter
	private ByteString rawReason;

	public PacketLoginDisconnect(String reson) {
		this.rawReason = new ByteString(reson);
	}

	public PacketLoginDisconnect() {
		super(-1);
	}

	@Override
	public void read(PacketDataSerializer s) {
		rawReason = s.readStringBytes();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.write(rawReason);
	}

	public BaseComponent getReson() {
		return ComponentSerializer.parse(rawReason.toString())[0];
	}

	public void setReson(BaseComponent reson) {
		this.rawReason = new ByteString(ComponentSerializer.toString(reson));
	}
}
