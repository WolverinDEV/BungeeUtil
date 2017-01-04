package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketLoginDisconnect extends Packet {
	@Getter
	private ByteString rawReason;

	public PacketLoginDisconnect() {
		super(-1);
	}

	public PacketLoginDisconnect(String reson) {
		this.rawReason = new ByteString(reson);
	}

	public BaseComponent getReson() {
		return ComponentSerializer.parse(this.rawReason.toString())[0];
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.rawReason = s.readStringBytes();
	}

	public void setReson(BaseComponent reson) {
		this.rawReason = new ByteString(ComponentSerializer.toString(reson));
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.write(this.rawReason);
	}
}
