package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.BlockPosition;

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
