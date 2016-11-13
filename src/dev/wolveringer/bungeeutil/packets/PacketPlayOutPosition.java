package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
public class PacketPlayOutPosition extends Packet implements PacketPlayOut{
	private boolean ground;
	private Location loc;
	private byte flag;
	private int teleportId;
	
	public PacketPlayOutPosition() {
		super((byte) 0x08);
	}

	public PacketPlayOutPosition(Location loc, boolean b) {
		super((byte) 0x08);
		this.loc = loc;
		this.ground = b;
	}

	public Location getLocation() {
		return loc;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public int getTeleportId() {
		return teleportId;
	}
	public void setTeleportId(int teleportId) {
		this.teleportId = teleportId;
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		loc = new Location(s.readDouble(), s.readDouble(), s.readDouble(), s.readFloat(), s.readFloat());
		flag = (byte) s.readUnsignedByte();
		if(getVersion().getBigVersion() == BigClientVersion.v1_9  || getBigVersion() == BigClientVersion.v1_10)
			teleportId = s.readVarInt();
	}

	public void setLocation(Location loc) {
		this.loc = loc;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeDouble(loc.getX());
		s.writeDouble(loc.getY());
		s.writeDouble(loc.getZ());
		s.writeFloat(loc.getYaw());
		s.writeFloat(loc.getPitch());
		s.writeByte(flag);
		if(getVersion().getBigVersion() == BigClientVersion.v1_9  || getBigVersion() == BigClientVersion.v1_10)
			s.writeVarInt(teleportId);
	}

	@Override
	public String toString() {
		return "PacketPlayOutPosition [ground=" + ground + ", loc=" + loc + ", flag=" + flag + ", teleportId=" + teleportId + "]";
	}
}
