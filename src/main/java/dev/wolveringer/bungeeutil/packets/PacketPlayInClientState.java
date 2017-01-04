package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketPlayInClientState extends Packet implements PacketPlayOut{
	private int state;
	@Override
	public void read(PacketDataSerializer serelizer) {
		state = serelizer.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer serelizer) {
		serelizer.writeVarInt(state);
	}
}
