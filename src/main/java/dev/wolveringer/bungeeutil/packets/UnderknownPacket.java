package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnderknownPacket extends Packet {

	private byte[] data;

	@Override
	public void read(PacketDataSerializer s) {
		this.data = new byte[s.readableBytes()];
		s.readBytes(this.data);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBytes(this.data);
	}
}
