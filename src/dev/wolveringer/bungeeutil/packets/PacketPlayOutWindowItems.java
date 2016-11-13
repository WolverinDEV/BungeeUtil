package dev.wolveringer.bungeeutil.packets;

import java.util.List;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutWindowItems extends Packet implements PacketPlayOut {
	private int window;
	private Item[] items;

	public PacketPlayOutWindowItems() {
		super(0x30);
	}

	public PacketPlayOutWindowItems(int window, List<Item> list) {
		super(0x30);
		this.window = window;
		this.items = new Item[list.size()];
		for(int i = 0;i < this.items.length;++i){
			this.items[i] = list.get(i);
		}
	}

	public PacketPlayOutWindowItems(int i, Item... items) {
		super(0x30);
		this.window = i;
		this.items = items;
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
		for(int i = 0;i < this.items.length;++i){
			s.writeItem(this.items[i]);
		}
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}

}
