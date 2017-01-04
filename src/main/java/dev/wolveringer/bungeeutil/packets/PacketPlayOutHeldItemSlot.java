package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayXXXHeldItemSlot;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayOutHeldItemSlot extends PacketPlayXXXHeldItemSlot implements PacketPlayOut{
	public PacketPlayOutHeldItemSlot(byte slot) {
		this.setSlot(slot);
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.setSlot(s.readByte());
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.getSlot());
	}
}
