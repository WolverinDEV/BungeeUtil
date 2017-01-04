package dev.wolveringer.bungeeutil.packets;

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
public class PacketPlayOutScoreboardDisplayObjective extends Packet implements PacketPlayOut{
	public static enum Position {
		LIST(0),
		SIDEBAR(1),
		BELOW_NAME(2);

		public static Position getPosition(int i){
			for(Position p : values()) {
				if(p.i == i) {
					return p;
				}
			}
			return null;
		}
		private int i;
		private Position(int i) {
			this.i = i;
		}
		public int getInt(){
			return this.i;
		}
	}
	private String name;
	private Position position;

	@Override
	public void read(PacketDataSerializer s) {
		this.position = Position.getPosition(s.readByte());
		this.name = s.readString(-1);
	}
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.position.i);
		s.writeString(this.name);
	}
}
