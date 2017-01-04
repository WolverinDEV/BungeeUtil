package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacketPlayOutKeepAlive extends Packet implements PacketPlayOut{
	private int id;
	@Override
	public void read(PacketDataSerializer s) {
		this.id = s.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.id);
	}
}
