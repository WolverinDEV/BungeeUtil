package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutWindowData extends Packet implements PacketPlayOut{
	
	private int window;
	private short action;
	private short value;
	

	@Override
	public void read(PacketDataSerializer s) {
		window = s.readUnsignedByte();
		action = s.readShort();
		value = s.readShort();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(window);
		s.writeShort(action);
		s.writeShort(value);
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}
}
