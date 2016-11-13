package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

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
	
	
	public PacketPlayOutScoreboardScore(String name, String obj_name, int value, Action action) {
		this();
		this.name = name;
		this.string_name = obj_name;
		this.value = value;
		this.action = action;
	}

	public PacketPlayOutScoreboardScore() {
		super(0x3C);
	}
	
	String name;
	String string_name;
	int value;
	Action action;
	
	@Override
	public void read(PacketDataSerializer s) {
		name = s.readString(-1);
		action = Action.fromInt(s.readByte());
		string_name = s.readString(-1);
		if(action.i == 0) value = PacketDataSerializer.readVarInt(s);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(name);
		s.writeByte(action.i);
		s.writeString(string_name);
		if(action.i == 0) PacketDataSerializer.writeVarInt(value, s);
		
	}

	public String getObjektiveName() {
		return name;
	}

	public void setObjektiveName(String name) {
		this.name = name;
	}

	public String getScoreName() {
		return string_name;
	}

	public void setScoreName(String obj_name) {
		this.string_name = obj_name;
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
