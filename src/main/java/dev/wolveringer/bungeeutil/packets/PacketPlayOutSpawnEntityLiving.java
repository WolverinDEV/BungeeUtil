package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.position.Vector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutSpawnEntityLiving extends BetaPacket implements PacketPlayOut{
	private byte type;
	private Location location;
	private int yaw = 0;
	private int pitch = 0;
	private int headRotation;
	private Vector vector = new Vector();
	private int id;
	private DataWatcher meta;

	public PacketPlayOutSpawnEntityLiving(byte type, Location location, int yaw, int pitch, int headRotation, Vector vector) {
		this.type = type;
		this.location = location;
		this.yaw = yaw;
		this.pitch = pitch;
		this.headRotation = headRotation;
		this.vector = vector;
	}


	public Location getLocation() {
		return this.location.clone().dividide(32D);
	}

	public boolean isFalingBlock() {
		return this.type == 70;
	}

	public boolean isItemFrame() {
		return this.type == 71;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.id = s.readVarInt();
		this.type = s.readByte();
		this.location = new Location(s.readInt(), s.readInt(), s.readInt());
		this.yaw = s.readByte();
		this.pitch = s.readByte();
		this.headRotation = s.readByte();
		if(this.headRotation > 0) {
			this.vector = new Vector(s.readShort(), s.readShort(), s.readShort());
		}
		this.meta = DataWatcher.createDataWatcher(this.getBigVersion(),s);
	}

	public void setLocation(Location location) {
		this.location = location.multiply(32.0D);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.id);
		s.writeByte(this.type);
		s.writeInt(this.location.getBlockX());
		s.writeInt(this.location.getBlockY());
		s.writeInt(this.location.getBlockZ());
		s.writeByte(this.yaw);
		s.writeByte(this.pitch);
		s.writeByte(this.headRotation);
		if(this.headRotation > 0){
			s.writeShort(this.vector.getBlockX());
			s.writeShort(this.vector.getBlockY());
			s.writeShort(this.vector.getBlockZ());
		}
		if(this.meta == null) {
			this.meta = DataWatcher.createDataWatcher(this.getBigVersion());
		}
		this.meta.write(s);
	}
}
