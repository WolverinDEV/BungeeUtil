package dev.wolveringer.bungeeutil.packets;

import java.util.HashMap;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PacketPlayOutStatistic extends Packet implements PacketPlayOut{

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
