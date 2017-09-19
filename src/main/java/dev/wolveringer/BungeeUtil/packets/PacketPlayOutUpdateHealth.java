package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutUpdateHealth extends Packet implements PacketPlayOut{

	float health;
	int food;
	float food_indicase;
	
	public PacketPlayOutUpdateHealth(float health, int food, float food_indicase) {
		this();
		this.health = health;
		this.food = food;
		this.food_indicase = food_indicase;
	}
	
	public PacketPlayOutUpdateHealth() {
		super(0x06);
	}

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
