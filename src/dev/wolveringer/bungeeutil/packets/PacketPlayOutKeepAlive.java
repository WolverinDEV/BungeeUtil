package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutKeepAlive extends Packet implements PacketPlayOut{
	int id;
	public PacketPlayOutKeepAlive() {
		super(0x00);
	}
	@Override
	public void read(PacketDataSerializer s) {
		id = s.readVarInt();
	}
	public void write(PacketDataSerializer s) {
		s.writeVarInt(id);
	};
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
