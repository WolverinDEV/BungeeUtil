package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInChat extends Packet implements PacketPlayIn {
	private String message;

	@Override
	public void read(PacketDataSerializer s) {
		this.message = s.readString(-1);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(this.message);
	}
}
