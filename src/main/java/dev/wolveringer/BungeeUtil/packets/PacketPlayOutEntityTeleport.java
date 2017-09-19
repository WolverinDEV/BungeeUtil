package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutEntityTeleport extends Packet implements PacketPlayOut {
	Location loc;
	int id;
	private boolean onGround;

	public PacketPlayOutEntityTeleport() {
		super(0x18);
	}

	public PacketPlayOutEntityTeleport(int id, Location loc) {
		this();
		this.loc = loc.clone();
		this.id = id;
	}

	public PacketPlayOutEntityTeleport(int id, Location loc, boolean onGround) {
		this(id, loc);
		this.onGround = onGround;
	}

	public void read(PacketDataSerializer s) {
		if(getVersion().getVersion() < 16)
			id = s.readInt();
		else
			id = s.readVarInt();
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			loc = new Location(s.readDouble(), s.readDouble(), s.readDouble(),((float)s.readByte())/ 256.0F * 360.0F,((float)s.readByte())/ 256.0F * 360.0F);
		else
			loc = new Location(s.readInt(), s.readInt(), s.readInt(), ((float)s.readByte())/ 256.0F * 360.0F,((float)s.readByte())/ 256.0F * 360.0F).dividide(32D);
		if(getVersion().getVersion() >= 22){
			onGround = s.readBoolean();
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getVersion() < 16){
			s.writeInt(id);
		}else{
			s.writeVarInt(id);
		}
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10){
			s.writeDouble(loc.getX());
			s.writeDouble(loc.getY());
			s.writeDouble(loc.getZ());
			
			s.writeByte((int) (loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (loc.getPitch() * 256.0F / 360.0F));
		}
		else
		{
			loc = loc.multiply(32D);
			s.writeInt(loc.getBlockX());
			s.writeInt(loc.getBlockY());
			s.writeInt(loc.getBlockZ());

			s.writeByte((int) (loc.getYaw() * 256.0F / 360.0F));
			s.writeByte((int) (loc.getPitch() * 256.0F / 360.0F));
		}
		if(getVersion().getVersion() >= 22)
			s.writeBoolean(this.onGround);
	}

	public Location getLocation() {
		return loc.clone();
	}

	public void setLocation(Location loc) {
		this.loc = loc.clone();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	public static void main(String[] args) {
		float a = (int) (180F * 256.0F / 360.0F);
		float b = (int) (a / 256.0F * 360.0F);
		System.out.println("X: "+a+" Y: "+b);
	}

}
