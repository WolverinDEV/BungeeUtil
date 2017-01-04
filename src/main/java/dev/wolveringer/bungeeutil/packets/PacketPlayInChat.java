package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketPlayInChat extends Packet implements PacketPlayIn {
	private String message;

	@Override
	public void read(PacketDataSerializer s) {
		message = s.readString(-1);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(message);
	}
}
