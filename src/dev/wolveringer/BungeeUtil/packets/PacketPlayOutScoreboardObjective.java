package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutScoreboardObjective extends Packet implements PacketPlayOut{

	public static enum Action {
		CREATE(0), REMOVE(1), UPDATE(2);

		int i;

		private Action(int i) {
			this.i = i;
		}

		public static Action fromInt(int i) {
			for(Action a : values())
				if(a.i == i)
					return a;
			return null;
		}
	}

	public static enum Type {
		INTEGER("integer"), HEARTS("hearts");

		private String s;

		private Type(String s) {
			this.s = s;
		}

		public static Type fromString(String s) {
			for(Type t : values())
				if(t.s.equalsIgnoreCase(s))
					return t;
			return null;
		}

		public String getIdentifire() {
			return s;
		}
	}
	

	public PacketPlayOutScoreboardObjective(String scorebordName, Action action, String displayName, Type type) {
		this();
		this.scorebordName = scorebordName;
		this.action = action;
		this.displayName = displayName;
		this.type = type;
	}

	public PacketPlayOutScoreboardObjective() {
		super(0x3B);
	}

	String scorebordName;
	Action action;
	String displayName = "";
	Type type = Type.INTEGER;

	@Override
	public void read(PacketDataSerializer s) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9){
			scorebordName = s.readString(-1);
			action = Action.fromInt(s.readByte());
			if(action.i != 1){
				displayName = s.readString(-1);
				type = Type.fromString(s.readString(-1));
			}
		}else{
			scorebordName = s.readString(-1);
			displayName = s.readString(-1);
			action = Action.fromInt(s.readByte());
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(getVersion().getBigVersion() == BigClientVersion.v1_8 || getBigVersion() == BigClientVersion.v1_9){
			s.writeString(scorebordName);
			s.writeByte(action.i);
			if(action.i != 1){
				s.writeString(displayName);
				s.writeString(type.getIdentifire());
			}
		}else{
			s.writeString(scorebordName);
			s.writeString(displayName);
			s.writeByte(action.i);
		}
	}

	public String getScorebordName() {
		return scorebordName;
	}

	public void setScorebordName(String scorebordName) {
		this.scorebordName = scorebordName;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action a) {
		this.action = a;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
