package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayOutSetSlot extends Packet implements PacketPlayOut{
	private Item ItemStack;
	private int slot;
	private int window;

	public PacketPlayOutSetSlot(Item itemstack, int window, int slot) {
		this.ItemStack = itemstack == null ? null : itemstack;
		this.window = window;
		this.slot = slot;
	}

	public Item getItemStack() {
		return ItemStack;
	}

	public int getSlot() {
		return slot;
	}

	public int getWindow() {
		return window;
	}

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readByte();
		this.slot = packetdataserializer.readShort();
		this.ItemStack = packetdataserializer.readItem();
	}

	public void setItemStack(Item itemStack) {
		ItemStack = itemStack;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
		packetdataserializer.writeShort(this.slot);
		packetdataserializer.writeItem(this.ItemStack);
	}

	@Override
	public String toString() {
		return "PacketPlayOutSetSlot [ItemStack=" + ItemStack + ", slot=" + slot + ", window=" + window + "]";
	}
}
