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
public class PacketPlayOutUpdateHealth extends Packet implements PacketPlayOut{

	private float health;
	private int food;
	private float food_indicase;

	@Override
	public void read(PacketDataSerializer s) {
		this.health = s.readFloat();
		this.food = s.readVarInt();
		this.food_indicase = s.readFloat();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(this.health);
		s.writeVarInt(this.food);
		s.writeFloat(this.food_indicase);
	}

}
