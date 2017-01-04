package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketPlayOutUpdateHealth extends Packet implements PacketPlayOut{

	private float health;
	private int food;
	private float food_indicase;

	@Override
	public void read(PacketDataSerializer s) {
		health = s.readFloat();
		food = s.readVarInt();
		food_indicase = s.readFloat();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(health);
		s.writeVarInt(food);
		s.writeFloat(food_indicase);
	}

}
