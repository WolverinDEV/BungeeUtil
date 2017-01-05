package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PacketPlayOutTitle extends Packet{
	public static enum Action {
		SET_TITLE,
		SET_SUBTITLE,
		ACTION_BAR,
		UPDATE_TIMINGS,
		HIDE,
		RESET;
	}
	private static final Action[] oldValues = {Action.SET_TITLE, Action.SET_SUBTITLE, Action.UPDATE_TIMINGS, Action.HIDE, Action.RESET};

	private Action action;
	private String title;
	private int fadeIn;
	private int stay;
	private int fadeOut;

	@SuppressWarnings("deprecation")
	private Action getAction(int index){
		switch (this.getBigVersion()) {
		case v1_11:
			return Action.values()[index];
		case v1_10:
		case v1_9:
		case v1_8:
			return oldValues[index];
		default:
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private int getActionId(Action action){
		switch (this.getBigVersion()) {
		case v1_11:
			return action.ordinal();
		case v1_10:
		case v1_9:
		case v1_8:
			for(int i = 0;i<oldValues.length;i++) {
				if(oldValues[i] == action) {
					return i;
				}
			}
		default:
			throw new NullPointerException("Cant find action "+action);
		}
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.action = this.getAction(s.readVarInt());
		switch (this.action) {
		case SET_TITLE:
		case SET_SUBTITLE:
		case ACTION_BAR:
			this.title = s.readString(-1);
			break;
		case UPDATE_TIMINGS:
			this.fadeIn = s.readInt();
			this.stay = s.readInt();
			this.fadeOut = s.readInt();
		case HIDE:
		case RESET:
			break;
		default:
			break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.getActionId(this.action));
		switch (this.action) {
		case SET_SUBTITLE:
		case SET_TITLE:
		case ACTION_BAR:
			s.writeString(this.title);
			break;
		case UPDATE_TIMINGS:
			s.writeInt(this.fadeIn);
			s.writeInt(this.stay);
			s.writeInt(this.fadeOut);
			break;
		default:
			break;
		}
	}

}
