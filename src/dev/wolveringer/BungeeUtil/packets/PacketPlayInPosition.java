package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInPosition extends PacketPlayInFlying {

	public PacketPlayInPosition() {
		super(0x05);
	}
	public PacketPlayInPosition(int id) {
		super(id);
	}

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