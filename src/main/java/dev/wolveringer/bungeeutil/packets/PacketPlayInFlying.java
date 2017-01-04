package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayInFlying extends Packet implements PacketPlayIn {
	@Setter
	protected boolean onground;
	protected boolean hasLook = false;
	protected boolean hasPos = false;
	protected Location location = new Location(0, 0, 0);
	@Getter
	protected double stance;

	public Location getLocation() {
		return this.location.clone();
	}

	public boolean hasLook() {
		return this.hasLook;
	}

	public boolean hasPos() {
		return this.hasPos;
	}

	public boolean isOnGound() {
		return this.onground;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.onground = s.readUnsignedByte() != 0;
	}

	public void setLocation(Location loc) {
		if(loc.getYaw() != 0 || loc.getPitch() != 0) {
			this.hasLook = true;
		}
		if(loc.getX() != 0 && loc.getY() != 0 && loc.getZ() != 0) {
			this.hasPos = true;
		}
		this.location = loc;
	}
	@Override
	public String toString() {
		return "PacketPlayInFlying [onground=" + this.onground + ", hasLook=" + this.hasLook + ", hasPos=" + this.hasPos + ", loc=" + this.location + ", stance=" + this.stance + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.onground ? 1 : 0);
	}

}