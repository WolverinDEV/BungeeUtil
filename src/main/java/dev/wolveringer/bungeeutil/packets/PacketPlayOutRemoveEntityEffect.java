package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutRemoveEntityEffect extends Packet implements PacketPlayOut {

	private int entity;
	private int effect;

	@Override
	public void read(PacketDataSerializer s) {
		this.entity = s.readVarInt();
		this.effect = s.readUnsignedByte();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.entity);
		s.writeByte(this.effect);
	}
}
