package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.connection.ProtocollVersion;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PacketPlayOutKeepAlive extends Packet implements PacketPlayOut {
	private long id;

	@Override
	public void read(PacketDataSerializer s) {
		if(getVersion().getVersion() >= ProtocollVersion.v1_12_2.getBasedVersionInt()) this.id = s.readLong();
		else this.id = s.readVarInt();
	}
	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getVersion() >= ProtocollVersion.v1_12_2.getBasedVersionInt()) s.writeLong(id);
		else s.writeVarInt((int) this.id);
	}
}
