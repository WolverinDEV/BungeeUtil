package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayOutScoreboardScore extends Packet implements PacketPlayOut{

	public static enum Action {
		CREATE(0),
		REMOVE(1),
		UPDATE(0);
		
		int i;
		private Action(int i) {
			this.i = i;
		}
		
		public static Action fromInt(int i){
			for(Action a : values())
				if(a.i == i)
					return a;
			return null;
		}
	}
	
	private String objectiveName;
	private String scoreName;
	private int value;
	private Action action;
	
	@Override
	public void read(PacketDataSerializer s) {
		objectiveName = s.readString(-1);
		action = Action.fromInt(s.readByte());
		scoreName = s.readString(-1);
		if(action.i == 0) value = PacketDataSerializer.readVarInt(s);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(objectiveName);
		s.writeByte(action.i);
		s.writeString(scoreName);
		if(action.i == 0) PacketDataSerializer.writeVarInt(value, s);
		
	}

	public String getObjektiveName() {
		return objectiveName;
	}

	public void setObjektiveName(String name) {
		this.objectiveName = name;
	}

	public String getScoreName() {
		return scoreName;
	}

	public void setScoreName(String obj_name) {
		this.scoreName = obj_name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
}
