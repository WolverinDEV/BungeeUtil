package dev.wolveringer.BungeeUtil.packets;

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
		this.loc = loc.clone().multiply(32D);
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
		loc = new Location(s.readInt(), s.readInt(), s.readInt(), s.readByte(), s.readByte());
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
		s.writeInt(loc.getBlockX());
		s.writeInt(loc.getBlockY());
		s.writeInt(loc.getBlockZ());
		s.writeByte((int) loc.getYaw());
		s.writeByte((int) loc.getPitch());
		if(getVersion().getVersion() >= 22){
			s.writeBoolean(this.onGround);
		}
	}

	public Location getLocation() {
		return loc.dividide(32D);
	}

	public void setLocation(Location loc) {
		this.loc = loc.multiply(32D);
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
	
	

}
