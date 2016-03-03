package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.packet.PacketDataSerializer;


public class PacketPlayOutHeldItemSlot extends PacketPlayXXXHeldItemSlot implements PacketPlayOut{
	public PacketPlayOutHeldItemSlot() {
		super(0x09);
	}
	
	public PacketPlayOutHeldItemSlot(byte slot) {
		this();
		setSlot(slot);
	}

	@Override
	public void read(PacketDataSerializer s) {
		setSlot(s.readByte());
	}
	public void write(PacketDataSerializer s) {
		s.writeByte(getSlot());
	};
}
