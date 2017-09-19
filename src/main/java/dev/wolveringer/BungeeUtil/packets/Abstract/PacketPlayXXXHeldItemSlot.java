package dev.wolveringer.BungeeUtil.packets.Abstract;

import dev.wolveringer.BungeeUtil.packets.Packet;

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
