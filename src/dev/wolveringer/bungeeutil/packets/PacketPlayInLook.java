package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.position.Location;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayInLook extends PacketPlayInFlying implements PacketPlayIn{
	@Override
	public void read(PacketDataSerializer s) {
		super.setLocation(new Location(0, 0, 0,s.readFloat(),s.readFloat()));
		super.read(s);
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(super.getLocation().getYaw());
		s.writeFloat(super.getLocation().getPitch());
		super.write(s);
	}
}
