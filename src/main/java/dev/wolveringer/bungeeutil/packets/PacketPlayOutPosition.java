package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutPosition extends Packet implements PacketPlayOut{
	private boolean ground;
	private Location location;
	private byte flag;
	private int teleportId;

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		this.location = new Location(s.readDouble(), s.readDouble(), s.readDouble(), s.readFloat(), s.readFloat());
		this.flag = (byte) s.readUnsignedByte();
		if(this.getBigVersion() != BigClientVersion.v1_8) {
			this.teleportId = s.readVarInt();
		}
	}

	@Override
	public String toString() {
		return "PacketPlayOutPosition [ground=" + this.ground + ", loc=" + this.location + ", flag=" + this.flag + ", teleportId=" + this.teleportId + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		s.writeDouble(this.location.getX());
		s.writeDouble(this.location.getY());
		s.writeDouble(this.location.getZ());
		s.writeFloat(this.location.getYaw());
		s.writeFloat(this.location.getPitch());
		s.writeByte(this.flag);
		if(this.getBigVersion() != BigClientVersion.v1_8) {
			s.writeVarInt(this.teleportId);
		}
	}
}
