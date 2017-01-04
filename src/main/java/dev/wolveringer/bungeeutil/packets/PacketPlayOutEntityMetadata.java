package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
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
public class PacketPlayOutEntityMetadata extends Packet implements PacketPlayOut {
	private int id;
	private DataWatcher meta;

	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		this.id = packetdataserializer.readInt();
		this.meta = DataWatcher.createDataWatcher(this.getBigVersion(),packetdataserializer);
	}

	@Override
	public String toString() {
		return "PacketPlayOutEntityMetadata@" + System.identityHashCode(this) + "[id=" + this.id + ", meta=" + this.meta + "]";
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		if(this.getVersion().getVersion() < 16){
			packetdataserializer.writeInt(this.id);
		}else{
			packetdataserializer.writeVarInt(this.id);
		}
		this.meta.write(packetdataserializer);
	}

}
