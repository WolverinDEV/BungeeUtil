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
public class PacketPlayOutGameStateChange extends Packet implements PacketPlayOut {
	private int state;
	private float value;

	@Override
	public void read(PacketDataSerializer s) {
		this.state = s.readByte();
		this.value = s.readFloat();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.state);
		s.writeFloat(this.value);
	}
}
