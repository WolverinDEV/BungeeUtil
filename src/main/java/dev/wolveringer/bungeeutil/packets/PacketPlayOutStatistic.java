package dev.wolveringer.bungeeutil.packets;

import java.util.HashMap;

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
public class PacketPlayOutStatistic extends Packet implements PacketPlayOut{

	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private int x = -2;

	@Deprecated
	public void a(int i) {
		this.x = i;
	}

	@Override
	public void read(PacketDataSerializer s) {
		int max = s.readVarInt();
		while (max > 0){
			this.stats.put(s.readString(-1), s.readVarInt());
			max--;
		}
	}

	@Override
	public String toString() {
		return "PacketPlayOutStatistic [stats=" + this.stats + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.stats.size());
		for(String x : this.stats.keySet()){
			s.writeString(x);
			if(this.x == -2) {
				s.writeVarInt(this.stats.get(x));
			} else {
				s.writeVarInt(this.x);
			}
		}
	}
}
