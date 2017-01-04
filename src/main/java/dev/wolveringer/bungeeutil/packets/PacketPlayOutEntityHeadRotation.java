package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.MathUtil;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayOutEntityHeadRotation extends Packet implements PacketPlayOut{
	@Getter
	private int entityId;
	private byte pitch;
	
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
	
	public float getPitch() {
		return pitch / 256.0F * 360.0F;
	}
}
