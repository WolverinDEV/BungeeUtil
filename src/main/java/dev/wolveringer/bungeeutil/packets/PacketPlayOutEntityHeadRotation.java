package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.MathUtil;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutEntityHeadRotation extends Packet implements PacketPlayOut{
	private int entityId;
	private byte pitch;

	public PacketPlayOutEntityHeadRotation(int entityId, float pitch) {
		this.entityId = entityId;
		this.pitch = (byte)(int)(MathUtil.pitchNormalizer(pitch) * 256.0F / 360.0F);
	}

	public float getPitch() {
		return this.pitch / 256.0F * 360.0F;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.entityId = s.readVarInt();
		this.pitch = s.readByte();
	}

	public void setPitch(float pitch) {
		this.pitch = (byte)(int)(MathUtil.pitchNormalizer(pitch) * 256.0F / 360.0F);;
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.entityId);
		s.writeByte(this.pitch);
	}
}
