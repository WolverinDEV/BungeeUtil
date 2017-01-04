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
public class PacketPlayInClientState extends Packet implements PacketPlayOut{
	private int state;
	@Override
	public void read(PacketDataSerializer serelizer) {
		this.state = serelizer.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer serelizer) {
		serelizer.writeVarInt(this.state);
	}
}
