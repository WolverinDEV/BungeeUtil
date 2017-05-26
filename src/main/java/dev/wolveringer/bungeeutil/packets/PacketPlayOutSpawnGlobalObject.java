package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.Location;

public class PacketPlayOutSpawnGlobalObject extends Packet implements PacketPlayOut {
	private int entityId;
	private byte type;
	private Location location;
	
	@Override
	public void read(PacketDataSerializer s) {
		this.entityId = s.readVarInt();
		this.type = s.readByte();
		
		switch (getBigVersion()) {
		case v1_8:
			location = new Location(s.readInt(), s.readInt(), s.readInt()).dividide(32D);
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			location = new Location(s.readDouble(), s.readDouble(), s.readDouble());
			break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.entityId);
		s.writeByte(this.type);
		
		switch (getBigVersion()) {
		case v1_8:
			Location loc = location.clone().multiply(32D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());
			break;
		case v1_9:
		case v1_10:
		case v1_11:
		case v1_12:
			s.writeDouble(location.getX());
			s.writeDouble(location.getY());
			s.writeDouble(location.getZ());
			break;
		}
	}

}
