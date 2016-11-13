package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class PacketPlayOutEntityDestroy extends Packet implements PacketPlayOut{
	private int[] entitys;

	public PacketPlayOutEntityDestroy() {
	}

	public PacketPlayOutEntityDestroy(int... paramVarArgs) {
		this.entitys = paramVarArgs;
	}

	public void read(PacketDataSerializer paramPacketDataSerializer) {
		this.entitys = new int[getVersion().getBigVersion() == BigClientVersion.v1_7?paramPacketDataSerializer.readByte():paramPacketDataSerializer.readVarInt()];
		for(int i = 0;i < this.entitys.length;i++){
			if(getVersion().getBigVersion() == BigClientVersion.v1_7)
				this.entitys[i] = paramPacketDataSerializer.readInt();
			else
				this.entitys[i] = paramPacketDataSerializer.readVarInt();
		}
	}

	public void write(PacketDataSerializer paramPacketDataSerializer) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_7)
			paramPacketDataSerializer.writeByte(this.entitys.length);
		else
			paramPacketDataSerializer.writeVarInt(this.entitys.length);
		for(int i = 0;i < this.entitys.length;i++){
			if(getVersion().getBigVersion() == BigClientVersion.v1_7)
				paramPacketDataSerializer.writeInt(this.entitys[i]);
			else
				paramPacketDataSerializer.writeVarInt(this.entitys[i]);
		}
	}
	
	public int[] getEntitys() {
		return entitys;
	}
	public void setEntitys(int[] entitys) {
		this.entitys = entitys;
	}
}