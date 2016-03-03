package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

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
		if(action.i == 0 || (getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9)) string_name = s.readString(-1);
		if(action.i == 0 && (getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9)) value = PacketDataSerializer.readVarInt(s);
		if(action.i == 0 && getVersion().getBigVersion() == BigClientVersion.v1_7) value = s.readInt();
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(name);
		s.writeByte(action.i);
		if(action.i == 0 || (getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9)) s.writeString(string_name);
		if(action.i == 0 && (getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9)) PacketDataSerializer.writeVarInt(value, s);
		if(action.i == 0 && getVersion().getBigVersion() == BigClientVersion.v1_7) s.writeInt(value);
		
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
