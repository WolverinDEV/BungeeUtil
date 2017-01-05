package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutSpawnEntity extends Packet implements PacketPlayOut{
	private Location location;
	private int eid;
	private int type;
	private DataWatcher data;

	public PacketPlayOutSpawnEntity(Location location, int eid, int type, DataWatcher data) {
		this.location = location.multiply(32D);
		this.eid = eid;
		this.type = type;
		this.data = data;
	}

	public Location getLocation() {
		return this.location.clone().dividide(32D);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		this.eid = s.readVarInt();
		this.type = s.readByte();
		this.location = new Location(s.readInt(), s.readInt(), s.readInt());
		this.data = DataWatcher.createDataWatcher(this.getBigVersion(),s);
	}

	public void setLocation(Location location) {
		this.location = location.multiply(32D);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.eid);
		s.writeByte(this.type);
		s.writeInt(this.location.getBlockX());
		s.writeInt(this.location.getBlockY());
		s.writeInt(this.location.getBlockZ());
		this.data.write(s);
	}
}
