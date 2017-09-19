package dev.wolveringer.BungeeUtil.packets;

import java.util.Random;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;
import dev.wolveringer.util.MathUtil;

public class PacketPlayOutEntityHeadRotation extends Packet implements PacketPlayOut{
	private int entityId;
	private byte pitch;
	
	public PacketPlayOutEntityHeadRotation() {}
	
	public PacketPlayOutEntityHeadRotation(int entityId, float pitch) {
		this.entityId = entityId;
		this.pitch = (byte)((int)(MathUtil.pitchNormalizer(pitch) * 256.0F / 360.0F));
	}



	@Override
	public void read(PacketDataSerializer s) {
		entityId = s.readVarInt();
		pitch = s.readByte();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(entityId);
		s.writeByte(pitch);
	}
	
	public int getEntityId() {
		return entityId;
	}
	public float getPitch() {
		return pitch / 256.0F * 360.0F;
	}
}
