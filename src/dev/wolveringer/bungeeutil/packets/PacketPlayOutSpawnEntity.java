package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.Location;

public class PacketPlayOutSpawnEntity extends Packet implements PacketPlayOut{
	Location location;
	int eid;
	int type;
	DataWatcher data;
	
	public PacketPlayOutSpawnEntity() {
		super(0x0E);
	}
	
	public PacketPlayOutSpawnEntity(Location location, int eid, int type, DataWatcher data) {
		this();
		this.location = location.multiply(32D);
		this.eid = eid;
		this.type = type;
		this.data = data;
	}



	@Override
	public void read(PacketDataSerializer s) {
		eid = s.readVarInt();
		type = s.readByte();
		location = new Location(s.readInt(), s.readInt(), s.readInt());
		data = DataWatcher.createDataWatcher(getBigVersion(),s);
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(eid);
		s.writeByte(type);
		s.writeInt(location.getBlockX());
		s.writeInt(location.getBlockY());
		s.writeInt(location.getBlockZ());
		data.write(s);
	}

	public Location getLocation() {
		return location.clone().dividide(32D);
	}

	public void setLocation(Location location) {
		this.location = location.multiply(32D);
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public DataWatcher getData() {
		return data;
	}

	public void setData(DataWatcher data) {
		this.data = data;
	}
}
