package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInLook extends PacketPlayInFlying implements PacketPlayIn{
	public PacketPlayInLook() {
		super(0x05);
	}
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
