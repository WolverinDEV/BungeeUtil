package dev.wolveringer.bungeeutil.packets;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.protocol.packet.Team;
import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public class PacketPlayOutScoreboardTeam extends Packet implements PacketPlayOut {
	public static enum Action {
		CREATE(0), REMOVE(1), UPDATE(2), PLAYER_ADD(3), PLAYER_REMOVE(4);
		
		private int b;
		
		private Action(int b) {
			this.b = b;
		}
		
		public int getAction() {
			return b;
		}
		
		public static Action fromInt(int x) {
			for (Action a : values())
				if (a.getAction() == x) return a;
			return null;
		}
	}
	
	public static enum NameTag {
		VISIABLE("always"), INVISIABLE("never"), TEAM_VISIABLE("hideForOtherTeams"), OTHER_VISIABLE("hideForOwnTeam");
		
		private String s;
		
		private NameTag(String s) {
			this.s = s;
		}
		
		public static NameTag fromString(String s) {
			for (NameTag t : values())
				if (t.s.equalsIgnoreCase(s)) return t;
			return null;
		}
		
		public String getIdentifire() {
			return s;
		}
	}
	
	String team;
	Action action;
	
	String collisionRule = "always";
	String displayName;
	String prefix;
	String suffix;
	NameTag tag = NameTag.VISIABLE;
	int color = -1;
	int friendly_fire = 0;
	String[] player;
	
	public PacketPlayOutScoreboardTeam() {
		super(0x3E);
	}
	
	public PacketPlayOutScoreboardTeam(Team t) {
		super(0x3E);
		action = Action.fromInt(t.getMode());
		team = t.getName();
		displayName = t.getDisplayName();
		prefix = t.getPrefix();
		suffix = t.getSuffix();
		tag = NameTag.fromString(t.getNameTagVisibility());
		color = t.getColor();
		friendly_fire = t.getFriendlyFire();
		player = t.getPlayers();
	}
	
	public PacketPlayOutScoreboardTeam(dev.wolveringer.bungeeutil.scoreboard.Team team) {
		super(0x3E);
		this.team = team.getName();
		this.displayName = team.getDisplayName();
		this.prefix = team.getPrefix();
		this.suffix = team.getSuffix();
		this.tag = team.getTagVisibility();
		this.color = team.getColor().ordinal();
		this.friendly_fire = team.getFriendlyFire();
		this.player = team.getMember().toArray(new String[0]);
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(team);
		s.writeByte(action.getAction());
		if (action.getAction() == 0 || action.getAction() == 2) {
			s.writeString(displayName);
			s.writeString(prefix);
			s.writeString(suffix);
			s.writeByte(friendly_fire);
			if (tag == null) tag = NameTag.VISIABLE;
			s.writeString(tag.getIdentifire());
			if (getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) s.writeString(collisionRule);
			s.writeByte(color);
		}
		if (action.getAction() == 0 || action.getAction() == 3 || action.getAction() == 4) {
			s.writeVarInt(player.length);
			for (String x : player)
				s.writeString(x);
		}
	}
	
	public void read(PacketDataSerializer s) {
		team = s.readString(16);
		action = Action.fromInt(s.readByte());
		if (action.getAction() == 0 || action.getAction() == 2) {
			displayName = s.readString(32);
			prefix = s.readString(16);
			suffix = s.readString(16);
			friendly_fire = s.readByte();
			tag = NameTag.fromString(s.readString(32));
			if (getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) collisionRule = s.readString(-1);
			color = s.readByte();
		}
		if (action.getAction() == 0 || action.getAction() == 3 || action.getAction() == 4) {
			int i = PacketDataSerializer.readVarInt(s);
			player = new String[i];
			for (int x = 0; x < i; x++)
				player[x] = s.readString(40);
		}
		
	}
	
	@Override
	public String toString() {
		return "PacketPlayOutScoreboardTeam [team=" + team + ", action=" + action + ", displayName=" + displayName + ", prefix=" + prefix + ", suffix=" + suffix + ", tag=" + tag + ", color=" + color + ", friendly_fire=" + friendly_fire + ", player=" + Arrays.toString(player) + "]";
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public NameTag getTag() {
		return tag;
	}
	
	public void setTag(NameTag tag) {
		this.tag = tag;
	}
	
	public ChatColor getColor() {
		return ChatColor.values()[color == -1 ? 0 : color];
	}
	
	public void setColor(ChatColor color) {
		this.color = color.ordinal();
	}
	
	public int isFriendlyFire() {
		return friendly_fire;
	}
	
	public void setFriendlyFire(int friendly_fire) {
		this.friendly_fire = friendly_fire;
	}
	
	public String[] getPlayers() {
		return player;
	}
	
	public void setPlayers(String[] player) {
		this.player = player;
	}
	
	public String getCollisionRule() {
		return collisionRule;
	}
	
	public void setCollisionRule(String collisionRule) {
		this.collisionRule = collisionRule;
	}
}
