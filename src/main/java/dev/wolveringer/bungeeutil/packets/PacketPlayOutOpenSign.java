package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PacketPlayOutOpenSign extends Packet implements PacketPlayOut{
	private BlockPosition location;
	
	@Override
	public void read(PacketDataSerializer s) {
		location = s.readBlockPosition();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(location);
	}

}
