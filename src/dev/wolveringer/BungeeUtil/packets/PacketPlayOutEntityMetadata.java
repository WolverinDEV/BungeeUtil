package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutEntityMetadata extends Packet implements PacketPlayOut {
	private int id;
	private DataWatcher meta;

	public PacketPlayOutEntityMetadata() {
		super(0x1C);
	}

	public PacketPlayOutEntityMetadata(int id, DataWatcher datawatcher) {
		this();
		this.id = id;
		this.meta = datawatcher;
	}

	public void read(PacketDataSerializer packetdataserializer) {
		this.id = packetdataserializer.readInt();
		this.meta = DataWatcher.createDataWatcher(getBigVersion(),packetdataserializer);
	}

	public void write(PacketDataSerializer packetdataserializer) {
		if(getVersion().getVersion() < 16){
			packetdataserializer.writeInt(this.id);
		}else{
			packetdataserializer.writeVarInt(this.id);
		}
		meta.write(packetdataserializer);
	}

	@Override
	public String toString() {
		return "PacketPlayOutEntityMetadata@" + System.identityHashCode(this) + "[id=" + id + ", meta=" + meta + "]";
	}
	
}
