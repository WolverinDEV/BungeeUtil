package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.HandType;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class PacketPlayInArmAnimation extends Packet implements PacketPlayIn {
	private int id;
	private int type;

	public PacketPlayInArmAnimation() {
		super(0x0A);
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		if(getBigVersion() == BigClientVersion.v1_7){
			this.id = s.readInt();
			this.type = s.readByte();
		}else if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			this.type = s.readVarInt();
		else
			this.type = 0;
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(getBigVersion() == BigClientVersion.v1_7){
			s.writeInt(id);
			s.writeByte(type);
		}
		else if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			s.writeVarInt(type);
	}
	
	public HandType getArmType() {
		return HandType.values()[type];
	}

	@Override
	public String toString() {
		return "PacketPlayInArmAnimation [id=" + id + ", type=" + type + "]";
	}
}
