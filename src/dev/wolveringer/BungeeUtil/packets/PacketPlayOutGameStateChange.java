package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.ProtocollVersion;
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
		if(getVersion().getProtocollVersion() == ProtocollVersion.v1_9){
			state = s.readVarInt();
			value = s.readByte();
		}
		else
		{
			state = s.readUnsignedByte();
			value = s.readFloat();
		}
	}
	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getProtocollVersion() == ProtocollVersion.v1_9){
			s.writeVarInt(state);
			s.writeByte((int) value);
		}
		else
		{
			s.writeByte(state);
			s.writeFloat(value);
		}
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
