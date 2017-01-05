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
public class PacketPlayOutUpdateSign extends Packet implements PacketPlayOut{
	private BlockPosition location;
	private String[] lines = new String[4];

	@Override
	public void read(PacketDataSerializer s) {
		this.location = s.readBlockPosition();
		this.lines = new String[4];
		for(int i = 0;i<4;i++) {
			this.lines[i] = s.readString(-1);
		}
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(this.location);
		for(int i = 0;i<4;i++) {
			s.writeString(this.lines[i]);
		}
	}
}
