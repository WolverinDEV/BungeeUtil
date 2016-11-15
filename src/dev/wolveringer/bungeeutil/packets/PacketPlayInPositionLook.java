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
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			this.stance = s.readDouble();
		Double z = s.readDouble();
		setLocation(new Location(x, y, z,s.readFloat(),s.readFloat()));
		setOnground(s.readByte()==1);
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeDouble(getLocation().getX());
		packetdataserializer.writeDouble(getLocation().getY());
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			packetdataserializer.writeDouble(this.stance);
		packetdataserializer.writeDouble(getLocation().getZ());
		packetdataserializer.writeFloat(getLocation().getYaw());
		packetdataserializer.writeFloat(getLocation().getPitch());
		packetdataserializer.writeByte(onGound()?1:0);
	}

	@Override
	public String toString() {
		return "PacketPlayInPositionLook [onground=" + onground + ", hasLook=" + hasLook + ", hasPos=" + hasPos + ", loc=" + loc + ", stance=" + stance + "]";
	}

}
