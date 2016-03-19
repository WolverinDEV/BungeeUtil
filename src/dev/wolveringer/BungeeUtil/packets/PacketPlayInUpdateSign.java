package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInUpdateSign extends Packet implements PacketPlayIn {
	private BlockPosition loc;
	private IChatBaseComponent comps[] = new IChatBaseComponent[4];
	
	public PacketPlayInUpdateSign() {
		// TODO Auto-generated constructor stub
	}
	
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
	
	public void setLines(IChatBaseComponent[] comps) {
		this.comps = comps;
	}
	public void setLocation(BlockPosition loc) {
		this.loc = loc;
	}
	public BlockPosition getLoc() {
		return loc;
	}
	public IChatBaseComponent[] getComps() {
		return comps;
	}
}
