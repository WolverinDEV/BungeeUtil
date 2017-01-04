package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayInPositionLook extends PacketPlayInPosition {

	@Override
	public void read(PacketDataSerializer s) {
		Double x = s.readDouble();
		Double y = s.readDouble();
		if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
			this.stance = s.readDouble();
		}
		Double z = s.readDouble();
		this.setLocation(new Location(x, y, z,s.readFloat(),s.readFloat()));
		this.setOnground(s.readByte()==1);
	}

	@Override
	public String toString() {
		return "PacketPlayInPositionLook [onground=" + this.onground + ", hasLook=" + this.hasLook + ", hasPos=" + this.hasPos + ", loc=" + this.location + ", stance=" + this.stance + "]";
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeDouble(this.getLocation().getX());
		packetdataserializer.writeDouble(this.getLocation().getY());
		if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
			packetdataserializer.writeDouble(this.stance);
		}
		packetdataserializer.writeDouble(this.getLocation().getZ());
		packetdataserializer.writeFloat(this.getLocation().getYaw());
		packetdataserializer.writeFloat(this.getLocation().getPitch());
		packetdataserializer.writeByte(this.isOnGound()?1:0);
	}

}
