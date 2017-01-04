package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutSpawnPostition extends Packet implements PacketPlayOut{
	private BlockPosition loc;
	@Override
	public void read(PacketDataSerializer s) {
		this.loc = s.readBlockPosition();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(this.loc);
	}
}
