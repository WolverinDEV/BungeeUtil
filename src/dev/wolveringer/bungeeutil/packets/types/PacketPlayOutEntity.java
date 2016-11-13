package dev.wolveringer.bungeeutil.packets.types;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;

public class PacketPlayOutEntity extends PacketAbstract {

	private int entityId;

	public PacketPlayOutEntity() {}

	@Override
	public void read(PacketDataSerializer s) {
		entityId = s.readVarInt();
		readUnusedBytes(s);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(entityId);
		writeUnusedBytes(s);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int id) {
		this.entityId = id;
	}
}
