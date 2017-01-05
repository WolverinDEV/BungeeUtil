package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutEntityEquipment extends Packet implements PacketPlayOut {
	public static enum Slot {
		MAIN_HAND, OFF_HAND, BOOTS, LEGGINS, CHESTPLATE, HEMELT;
		private Slot() {
		}
	}

	private int eid;
	private Item item;
	private Slot slot;

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		this.eid = s.readVarInt();
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			this.slot = Slot.values()[s.readVarInt()];
			break;
		case v1_8:
			this.slot = Slot.values()[s.readShort()];
			break;
		default:
			break;
		}
		this.item = s.readItem();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.eid);
		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			s.writeVarInt(this.slot.ordinal());
			break;
		case v1_8:
			s.writeShort(this.slot.ordinal());
			break;
		default:
			break;
		}
		s.writeItem(this.item);
	}
}
