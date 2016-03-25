package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.HandType;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.packet.PacketDataSerializer;

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
		}else if(getBigVersion() == BigClientVersion.v1_9)
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
		else if(getBigVersion() == BigClientVersion.v1_9)
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
