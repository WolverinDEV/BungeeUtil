package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutTransaction extends Packet implements PacketPlayOut{

	private int window;
	private short id;
	private boolean cancel;


	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readUnsignedByte();
		this.id = packetdataserializer.readShort();
		this.cancel = packetdataserializer.readBoolean();
	}

	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
		packetdataserializer.writeShort(this.id);
		packetdataserializer.writeBoolean(this.cancel);
	}

	public String toString() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] { Integer.valueOf(this.window), Short.valueOf(this.id), Boolean.valueOf(this.cancel) });
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
}