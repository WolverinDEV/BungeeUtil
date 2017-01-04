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
public class PacketPlayOutScoreboardScore extends Packet implements PacketPlayOut{

	public static enum Action {
		CREATE(0),
		REMOVE(1),
		UPDATE(0);

		public static Action fromInt(int i){
			for(Action a : values()) {
				if(a.i == i) {
					return a;
				}
			}
			return null;
		}
		int i;

		private Action(int i) {
			this.i = i;
		}
	}

	private String objectiveName;
	private String scoreName;
	private int value;
	private Action action;

	@Override
	public void read(PacketDataSerializer s) {
		this.objectiveName = s.readString(-1);
		this.action = Action.fromInt(s.readByte());
		this.scoreName = s.readString(-1);
		if(this.action.i == 0) {
			this.value = PacketDataSerializer.readVarInt(s);
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(this.objectiveName);
		s.writeByte(this.action.i);
		s.writeString(this.scoreName);
		if(this.action.i == 0) {
			PacketDataSerializer.writeVarInt(this.value, s);
		}

	}
}
