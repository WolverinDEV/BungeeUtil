package dev.wolveringer.bungeeutil.packets;

import java.util.List;

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
public class PacketPlayOutWindowItems extends Packet implements PacketPlayOut {
	private int window;
	private Item[] items;

	public PacketPlayOutWindowItems(int window, List<Item> list) {
		this.window = window;
		this.items = new Item[list.size()];
		for(int i = 0;i < this.items.length;++i){
			this.items[i] = list.get(i);
		}
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.window = s.readUnsignedByte();
		short items_length = s.readShort();

		this.items = new Item[items_length];

		for(int i = 0;i < items_length;++i){
			this.items[i] = s.readItem();
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.window);
		s.writeShort(this.items.length);
		for (Item item : this.items) {
			s.writeItem(item);
		}
	}
}
