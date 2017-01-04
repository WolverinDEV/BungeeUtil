package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketPlayOutEntityEquipment extends Packet implements PacketPlayOut {
	public static enum Slot {
		MAIN_HAND, OFF_HAND, BOOTS, LEGGINS, CHESTPLATE, HEMELT;
		private Slot() {
		}
	}

	private int eid;
	private Item item;
	private Slot slot;

	@Override
	public void read(PacketDataSerializer s) {
		eid = s.readVarInt();
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			slot = Slot.values()[s.readVarInt()];
			break;
		case v1_8:
			slot = Slot.values()[s.readShort()];
			break;
		default:
			break;
		}
		item = s.readItem();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(eid);
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			s.writeVarInt(slot.ordinal());
			break;
		case v1_8:
			s.writeShort(slot.ordinal());
			break;
		default:
			break;
		}
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
