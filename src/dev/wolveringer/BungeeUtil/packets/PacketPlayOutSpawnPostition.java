package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutSpawnPostition extends Packet implements PacketPlayOut{
	BlockPosition loc;
	public PacketPlayOutSpawnPostition() {
		super(0x05);
	}
	public PacketPlayOutSpawnPostition(BlockPosition loc) {
		super(0x05);
		this.loc = loc;
	}
	@Override
	public void read(PacketDataSerializer s) {
		loc = s.readBlockPosition();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);
	}
	public BlockPosition getLocation() {
		return loc;
	}
}
