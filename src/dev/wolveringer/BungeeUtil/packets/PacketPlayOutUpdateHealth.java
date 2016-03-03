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
		food = getVersion().getBigVersion() == BigClientVersion.v1_7?s.readShort():s.readVarInt();
		food_indicase = s.readFloat();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeFloat(health);
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			s.writeShort(food);
		else
			s.writeVarInt(food);
		s.writeFloat(food_indicase);
	}

}
