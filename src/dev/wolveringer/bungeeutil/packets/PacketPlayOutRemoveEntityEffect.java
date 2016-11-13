package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class PacketPlayOutRemoveEntityEffect extends Packet implements PacketPlayOut {
	
	int entity;
	int effect;
	
	public PacketPlayOutRemoveEntityEffect(int entity, int effect) {
		super(0x1E);
		this.entity = entity;
		this.effect = effect;
	}
	
	public PacketPlayOutRemoveEntityEffect() {
		super(0x1E);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		entity = s.readVarInt();
		effect = s.readUnsignedByte();
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(entity);
		s.writeByte(effect);
	}
	
	public int getEntity() {
		return entity;
	}
	
	public void setEntity(int entity) {
		this.entity = entity;
	}
	
	public int getEffect() {
		return effect;
	}
	
	public void setEffect(int effect) {
		this.effect = effect;
	}
	
}
