package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;

public class PacketPlayOutEntity extends PacketAbstract {

	private int entityId;

	public PacketPlayOutEntity() {}

	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.entityId = s.readVarInt();
		this.readUnusedBytes(s);
	}

	public void setEntityId(int id) {
		this.entityId = id;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.entityId);
		this.writeUnusedBytes(s);
	}
}
