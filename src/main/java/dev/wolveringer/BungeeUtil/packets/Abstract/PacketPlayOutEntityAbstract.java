package dev.wolveringer.BungeeUtil.packets.Abstract;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutEntityAbstract extends PacketAbstract {

	private int id;

	public PacketPlayOutEntityAbstract() {}
	
	public PacketPlayOutEntityAbstract(int id) {
		super(id);
	}

	@Override
	public void read(PacketDataSerializer s) {
		id = getVersion().getBigVersion() == BigClientVersion.v1_8 ? s.readVarInt() : s.readInt();
		readUnusedBytes(s);
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_8)
			s.writeVarInt(id);
		else
			s.writeInt(id);
		writeUnusedBytes(s);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
