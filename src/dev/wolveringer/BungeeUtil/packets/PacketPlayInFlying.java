package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInFlying extends Packet implements PacketPlayIn {

	protected boolean onground;
	protected boolean hasLook = false;
	protected boolean hasPos = false;
	protected Location loc = new Location(0, 0, 0);
	protected double stance;

	public PacketPlayInFlying() {
		super(-1);
	}

	public PacketPlayInFlying(int i) {
		super(i);
	}

	public void setLocation(Location loc) {
		if(loc.getYaw() != 0 || loc.getPitch() != 0)
			hasLook = true;
		if(loc.getX() != 0 && loc.getY() != 0 && loc.getZ() != 0)
			hasPos = true;
		this.loc = loc;
	}

	public Location getLocation() {
		return loc.clone();
	}

	public boolean hasLook() {
		return this.hasLook;
	}

	public boolean hasPos() {
		return this.hasPos;
	}

	public boolean onGound() {
		return this.onground;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.onground = s.readUnsignedByte() != 0;
	}

	public double stance() {
		return this.stance;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.onground ? 1 : 0);
	}

	protected void setOnground(boolean flag) {
		this.onground = flag;
	}

	@Override
	public String toString() {
		return "PacketPlayInFlying [onground=" + onground + ", hasLook=" + hasLook + ", hasPos=" + hasPos + ", loc=" + loc + ", stance=" + stance + "]";
	}
	
}