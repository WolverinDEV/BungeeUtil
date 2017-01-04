package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutCloseWindow extends Packet implements PacketPlayOut{

	private int window;

	@Override
	public void read(PacketDataSerializer s) {
		this.window = s.readUnsignedByte();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.window);
	}
}
