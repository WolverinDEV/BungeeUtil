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
		if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
			this.stance = packetdataserializer.readDouble();
		}
		Double z = packetdataserializer.readDouble();
		this.setLocation(new Location(x, y, z));
		super.read(packetdataserializer);
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeDouble(this.getLocation().getX());
		packetdataserializer.writeDouble(this.getLocation().getY());
		if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
			packetdataserializer.writeDouble(this.stance);
		}
		packetdataserializer.writeDouble(this.getLocation().getZ());
		super.write(packetdataserializer);
	}
}