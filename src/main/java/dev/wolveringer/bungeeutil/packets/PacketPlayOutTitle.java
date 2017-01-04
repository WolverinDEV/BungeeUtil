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
	
	private Action getAction(int index){
		switch (getBigVersion()) {
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
	
	private int getActionId(Action action){
		switch (getBigVersion()) {
		case v1_11:
			return action.ordinal();
		case v1_10:
		case v1_9:
		case v1_8:
			for(int i = 0;i<oldValues.length;i++)
				if(oldValues[i] == action)
					return i;
		default:
			throw new NullPointerException("Cant find action "+action);
		}
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		action = getAction(s.readVarInt());
		switch (action) {
		case SET_TITLE:
		case SET_SUBTITLE:
		case ACTION_BAR:
			title = s.readString(-1);
			break;
		case UPDATE_TIMINGS:
			fadeIn = s.readInt();
			stay = s.readInt();
			fadeOut = s.readInt();
		case HIDE:
		case RESET:
			break;
		default:
			break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(getActionId(action));
		switch (action) {
		case SET_SUBTITLE:
		case SET_TITLE:
		case ACTION_BAR:
			s.writeString(title);
			break;
		case UPDATE_TIMINGS:
			s.writeInt(fadeIn);
			s.writeInt(stay);
			s.writeInt(fadeOut);
			break;
		default:
			break;
		}
	}

}
