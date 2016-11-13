package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.bungeeutil.packets.Packet;

public abstract class PacketPlayXXXHeldItemSlot extends Packet{
	private int slot;
	
	public PacketPlayXXXHeldItemSlot(byte id) {
		super(id);
		
	}
	public PacketPlayXXXHeldItemSlot(int i) {
		super(i);
		
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
}
