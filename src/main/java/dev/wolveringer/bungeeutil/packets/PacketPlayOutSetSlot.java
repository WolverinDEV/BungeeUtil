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
public class PacketPlayOutSetSlot extends Packet implements PacketPlayOut{
	private Item ItemStack;
	private int slot;
	private int window;

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readByte();
		this.slot = packetdataserializer.readShort();
		this.ItemStack = packetdataserializer.readItem();
	}

	@Override
	public String toString() {
		return "PacketPlayOutSetSlot [ItemStack=" + this.ItemStack + ", slot=" + this.slot + ", window=" + this.window + "]";
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
		packetdataserializer.writeShort(this.slot);
		packetdataserializer.writeItem(this.ItemStack);
	}
}
