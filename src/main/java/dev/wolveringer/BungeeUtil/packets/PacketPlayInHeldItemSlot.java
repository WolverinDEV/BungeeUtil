package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInHeldItemSlot extends PacketPlayXXXHeldItemSlot implements PacketPlayIn{
	public PacketPlayInHeldItemSlot() {
		super(0x09);
	}
	
	public PacketPlayInHeldItemSlot(int slot) {
		this();
		setSlot(slot);
	}

	@Override
	public void read(PacketDataSerializer s) {
		setSlot(s.readShort());
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeShort(getSlot());
	}
}
