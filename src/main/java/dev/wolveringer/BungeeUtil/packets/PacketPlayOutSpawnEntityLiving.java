package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.api.position.Vector;
import dev.wolveringer.packet.PacketDataSerializer;

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
		this();
		this.type = type;
		this.location = location;
		this.yaw = yaw;
		this.pitch = pitch;
		this.headRotation = headRotation;
		this.vector = vector;
	}

	public PacketPlayOutSpawnEntityLiving() {
		super(0x0F);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		id = s.readVarInt();
		type = s.readByte();
		location = new Location(s.readInt(), s.readInt(), s.readInt());
		yaw = s.readByte();
		pitch = s.readByte();
		headRotation = s.readByte();
		if(headRotation > 0)
			vector = new Vector(s.readShort(), s.readShort(), s.readShort());
		meta = DataWatcher.createDataWatcher(getBigVersion(),s);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(id);
		s.writeByte(type);
		s.writeInt(location.getBlockX());
		s.writeInt(location.getBlockY());
		s.writeInt(location.getBlockZ());
		s.writeByte(yaw);
		s.writeByte(pitch);
		s.writeByte(headRotation);
		if(headRotation > 0){
			s.writeShort(vector.getBlockX());
			s.writeShort(vector.getBlockY());
			s.writeShort(vector.getBlockZ());
		}
		if(meta == null)
			meta = DataWatcher.createDataWatcher(getBigVersion());
		meta.write(s);
	}

	public boolean isItemFrame() {
		return type == 71;
	}

	public boolean isFalingBlock() {
		return type == 70;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location.multiply(32.0D);
	}

	public int getYaw() {
		return yaw;
	}

	public void setYaw(int yaw) {
		this.yaw = yaw;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getHeadRotation() {
		return headRotation;
	}

	public void setHeadRotation(int headRotation) {
		this.headRotation = headRotation;
	}

	public Vector getVector() {
		return vector;
	}

	public void setVector(Vector vector) {
		this.vector = vector;
	}
	public void setEntityID(int id) {
		this.id = id;
	}
	public int getEnityID() {
		return id;
	}
	public void setMeta(DataWatcher meta) {
		this.meta = meta;
	}
	public DataWatcher getMeta() {
		return meta;
	}
}
