package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutEntityEquipment extends Packet implements PacketPlayOut{
	public static enum Slot {
		MAIN_HAND,
		OFF_HAND,
		BOOTS,
		LEGGINS,
		CHESTPLATE,
		HEMELT;
		
		private Slot() {}
	}
	private int eid;
	private Item item;
	private Slot slot;

	public PacketPlayOutEntityEquipment() {
		super(0x04);
	}

	
	public PacketPlayOutEntityEquipment(int eid, Item item, Slot slot) {
		this();
		this.eid = eid;
		this.item = item;
		this.slot = slot;
	}



	@Override
	public void read(PacketDataSerializer s) {
		eid = s.readVarInt();
		slot = Slot.values()[(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) ? s.readVarInt() : s.readShort()];
		item= s.readItem();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(eid);
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			s.writeVarInt(slot.ordinal());
		else
			s.writeShort(slot.ordinal());
		s.writeItem(item);
	}

	
	public int getEntityId() {
		return eid;
	}
	public Item getItem() {
		return item;
	}
	public Slot getSlot() {
		return slot;
	}
	
	public void setEntityId(int eid) {
		this.eid = eid;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
}
