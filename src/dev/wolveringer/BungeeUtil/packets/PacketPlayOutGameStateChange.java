package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutGameStateChange extends Packet implements PacketPlayOut{
	int state;
	float value;
	
	
	public PacketPlayOutGameStateChange(int state, float value) {
		super(0x2B);
		this.state = state;
		this.value = value;
	}
	
	public PacketPlayOutGameStateChange() {
		super(0x2B);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
			state = s.readByte();
			value = s.readFloat();
	}
	
	@Override
	public void write(PacketDataSerializer s) {
			s.writeByte(state);
			s.writeFloat(value);
	}
	public float getValue() {
		return value;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
