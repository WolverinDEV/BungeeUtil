package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayXXXHeldItemSlot;

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
