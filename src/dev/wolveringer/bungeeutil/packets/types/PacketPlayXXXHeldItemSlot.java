package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.bungeeutil.packets.Packet;

public abstract class PacketPlayXXXHeldItemSlot extends Packet{
	private int slot;
	
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
}
