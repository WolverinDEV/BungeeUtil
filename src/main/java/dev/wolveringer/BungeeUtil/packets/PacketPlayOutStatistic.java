package dev.wolveringer.BungeeUtil.packets;

import java.util.HashMap;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutStatistic extends Packet implements PacketPlayOut{

	public PacketPlayOutStatistic() {
		super(0x37);
	}

	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private int x = -2;

	@Override
	public void read(PacketDataSerializer s) {
		int max = s.readVarInt();
		while (max > 0){
			stats.put(s.readString(-1), s.readVarInt());
			max--;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(stats.size());
		for(String x : stats.keySet()){
			s.writeString(x);
			if(this.x == -2)
				s.writeVarInt(stats.get(x));
			else
				s.writeVarInt(this.x);
		}
	}

	@Deprecated
	public void a(int i) {
		x = i;
	}

	@Override
	public String toString() {
		return "PacketPlayOutStatistic [stats=" + stats + "]";
	}
}
