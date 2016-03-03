package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

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
