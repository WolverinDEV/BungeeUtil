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
public class PacketPlayOutWindowData extends Packet implements PacketPlayOut{

	private int window;
	private short action;
	private short value;


	@Override
	public void read(PacketDataSerializer s) {
		this.window = s.readUnsignedByte();
		this.action = s.readShort();
		this.value = s.readShort();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.window);
		s.writeShort(this.action);
		s.writeShort(this.value);
	}
}
