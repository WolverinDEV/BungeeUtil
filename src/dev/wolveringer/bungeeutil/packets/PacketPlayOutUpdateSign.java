package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutUpdateSign extends Packet implements PacketPlayOut{
	private BlockPosition loc;
	private String[] lines = new String[4];

	@Override
	public void read(PacketDataSerializer s) {
		loc = s.readBlockPosition();
		for(int i = 0;i<4;i++)
			lines[i] = s.readString(-1);
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);
		for(int i = 0;i<4;i++)
			s.writeString(lines[i]);
	}
	public BlockPosition getLocation() {
		return loc;
	}
	public void setLocation(BlockPosition loc) {
		this.loc = loc;
	}
	public String[] getLines() {
		return lines;
	}
	public void setLines(String[] lines) {
		this.lines = lines;
	}
}
