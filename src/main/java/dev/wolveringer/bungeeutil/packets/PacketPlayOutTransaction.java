package dev.wolveringer.bungeeutil.packets;

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
public class PacketPlayOutTransaction extends Packet implements PacketPlayOut{

	private int window;
	private short id;
	private boolean cancel;


	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.window = packetdataserializer.readUnsignedByte();
		this.id = packetdataserializer.readShort();
		this.cancel = packetdataserializer.readBoolean();
	}

	@Override
	public String toString() {
		return String.format("id=%d, uid=%d, accepted=%b", new Object[] { Integer.valueOf(this.window), Short.valueOf(this.id), Boolean.valueOf(this.cancel) });
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		packetdataserializer.writeByte(this.window);
		packetdataserializer.writeShort(this.id);
		packetdataserializer.writeBoolean(this.cancel);
	}
}