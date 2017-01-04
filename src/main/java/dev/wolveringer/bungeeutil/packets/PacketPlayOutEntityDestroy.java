package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutEntityDestroy extends Packet implements PacketPlayOut{
	private int[] entitys;

	@Override
	public void read(PacketDataSerializer paramPacketDataSerializer) {
		this.entitys = new int[this.getVersion().getBigVersion() == BigClientVersion.v1_7?paramPacketDataSerializer.readByte():paramPacketDataSerializer.readVarInt()];
		for(int i = 0;i < this.entitys.length;i++){
			if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
				this.entitys[i] = paramPacketDataSerializer.readInt();
			} else {
				this.entitys[i] = paramPacketDataSerializer.readVarInt();
			}
		}
	}

	@Override
	public void write(PacketDataSerializer paramPacketDataSerializer) {
		if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
			paramPacketDataSerializer.writeByte(this.entitys.length);
		} else {
			paramPacketDataSerializer.writeVarInt(this.entitys.length);
		}
		for (int entity : this.entitys) {
			if(this.getVersion().getBigVersion() == BigClientVersion.v1_7) {
				paramPacketDataSerializer.writeInt(entity);
			} else {
				paramPacketDataSerializer.writeVarInt(entity);
			}
		}
	}
}