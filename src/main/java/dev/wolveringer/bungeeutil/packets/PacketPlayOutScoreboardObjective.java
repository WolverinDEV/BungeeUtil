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
public class PacketPlayOutScoreboardObjective extends Packet implements PacketPlayOut {

	public static enum Action {
		CREATE(0), REMOVE(1), UPDATE(2);

		public static Action fromInt(int i) {
			for (Action a : values()) {
				if (a.i == i) {
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

	public static enum Type {
		INTEGER("integer"), HEARTS("hearts");

		public static Type fromString(String s) {
			for (Type t : values()) {
				if (t.s.equalsIgnoreCase(s)) {
					return t;
				}
			}
			return null;
		}

		private String s;

		private Type(String s) {
			this.s = s;
		}

		public String getIdentifire() {
			return this.s;
		}
	}

	private String scorebordName;
	private Action action;
	private String displayName = "";
	private Type type = Type.INTEGER;

	@Override
	public void read(PacketDataSerializer s) {
		this.scorebordName = s.readString(-1);
		this.action = Action.fromInt(s.readByte());
		if (this.action.i != 1) {
			this.displayName = s.readString(-1);
			this.type = Type.fromString(s.readString(-1));
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(this.scorebordName);
		s.writeByte(this.action.i);
		if (this.action.i != 1) {
			s.writeString(this.displayName);
			s.writeString(this.type.getIdentifire());
		}
	}
}
