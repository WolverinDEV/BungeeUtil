package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.packet.PacketDataSerializer;
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
		UPDATE_TIMINGS,
		HIDE,
		RESET;
	}
	
	private Action action;
	private String title;
	private int fadeIn;
	private int stay;
	private int fadeOut;
	
	@Override
	public void read(PacketDataSerializer s) {
		action = Action.values()[s.readVarInt()];
		switch (action) {
		case SET_TITLE:
		case SET_SUBTITLE:
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
		s.writeInt(action.ordinal());
		switch (action) {
		case SET_SUBTITLE:
		case SET_TITLE:
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
