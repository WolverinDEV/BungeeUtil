package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayInPosition extends PacketPlayInFlying {

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		Double x = packetdataserializer.readDouble();
		Double y = packetdataserializer.readDouble();
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			this.stance = packetdataserializer.readDouble();
		Double z = packetdataserializer.readDouble();
		setLocation(new Location(x, y, z));
		super.read(packetdataserializer);
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeDouble(getLocation().getX());
		packetdataserializer.writeDouble(getLocation().getY());
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			packetdataserializer.writeDouble(this.stance);
		packetdataserializer.writeDouble(getLocation().getZ());
		super.write(packetdataserializer);
	}
}