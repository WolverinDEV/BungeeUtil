package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;

@NoArgsConstructor
public class PacketPlayInUpdateSign extends Packet implements PacketPlayIn {
	private BlockPosition loc;
	private BaseComponent comps[] = new BaseComponent[4];
	
	@Override
	public void read(PacketDataSerializer s) {
		loc = s.readBlockPosition();
		comps[0] = s.readRawString();
		comps[1] = s.readRawString();
		comps[2] = s.readRawString();
		comps[3] = s.readRawString();
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);
		s.writeRawString(comps[0]);
		s.writeRawString(comps[1]);
		s.writeRawString(comps[2]);
		s.writeRawString(comps[3]);
	}
	
	public void setLines(BaseComponent[] comps) {
		this.comps = comps;
	}
	public void setLocation(BlockPosition loc) {
		this.loc = loc;
	}
	public BlockPosition getLoc() {
		return loc;
	}
	public BaseComponent[] getComps() {
		return comps;
	}
}
