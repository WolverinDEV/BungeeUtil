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
public class PacketPlayOutSetExperience extends Packet implements PacketPlayOut{
	private float exp;
	private int level;
	private int total;

	@Override
	public void read(PacketDataSerializer s) {
		this.exp = s.readFloat();
		this.level = s.readVarInt();
		this.total = s.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(this.exp);
		s.writeVarInt(this.level);
		s.writeVarInt(this.total);
	}
}
