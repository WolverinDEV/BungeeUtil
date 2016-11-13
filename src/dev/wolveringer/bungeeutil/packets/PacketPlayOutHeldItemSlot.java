package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayXXXHeldItemSlot;


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
