package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInUpdateSign extends Packet implements PacketPlayIn {
	private BlockPosition location;
	private BaseComponent comps[] = new BaseComponent[4];

	@Override
	public void read(PacketDataSerializer s) {
		this.location = s.readBlockPosition();
		this.comps[0] = s.readRawString();
		this.comps[1] = s.readRawString();
		this.comps[2] = s.readRawString();
		this.comps[3] = s.readRawString();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(this.location);
		s.writeRawString(this.comps[0]);
		s.writeRawString(this.comps[1]);
		s.writeRawString(this.comps[2]);
		s.writeRawString(this.comps[3]);
	}
}
