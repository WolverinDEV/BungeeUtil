package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.nbt.NBTTagCompound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutTileData extends Packet implements PacketPlayOut {
	private BlockPosition location;
	private int type;
	private NBTTagCompound nbt;
	
	@Override
	public void read(PacketDataSerializer s) {
		location = s.readBlockPosition();
		type = s.readByte();
		nbt = s.readNBT();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(location);
		s.writeByte(type);
		s.writeNBT(nbt);
	}

}
