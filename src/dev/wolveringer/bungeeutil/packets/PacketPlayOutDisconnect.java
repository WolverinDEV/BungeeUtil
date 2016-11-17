package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PacketPlayOutDisconnect extends Packet implements PacketPlayOut{

	private BaseComponent message;
	
	@Override
	public void read(PacketDataSerializer s) {
		message = s.readRawString();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeRawString(message);
	}
}
