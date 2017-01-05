package dev.wolveringer.bungeeutil.packets;

import java.util.Arrays;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.protocol.packet.Team;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutScoreboardTeam extends Packet implements PacketPlayOut {
	public static enum Action {
		CREATE(0), REMOVE(1), UPDATE(2), PLAYER_ADD(3), PLAYER_REMOVE(4);

		public static Action fromInt(int x) {
			for (Action a : values()) {
				if (a.getAction() == x) {
					return a;
				}
			}
			return null;
		}

		private int b;

		private Action(int b) {
			this.b = b;
		}

		public int getAction() {
			return this.b;
		}
	}

	public static enum NameTag {
		VISIABLE("always"), INVISIABLE("never"), TEAM_VISIABLE("hideForOtherTeams"), OTHER_VISIABLE("hideForOwnTeam");

		public static NameTag fromString(String s) {
			for (NameTag t : values()) {
				if (t.s.equalsIgnoreCase(s)) {
					return t;
				}
			}
			return null;
		}

		private String s;

		private NameTag(String s) {
			this.s = s;
		}

		public String getIdentifire() {
			return this.s;
		}
	}

	private String team;
	private Action action;

	private String collisionRule = "always";
	private String displayName;
	private String prefix;
	private String suffix;
	private NameTag tag = NameTag.VISIABLE;
	private int color = -1;
	private int friendlyFire = 0;
	private String[] player;

	public PacketPlayOutScoreboardTeam(dev.wolveringer.bungeeutil.scoreboard.Team team) {
		this.team = team.getName();
		this.displayName = team.getDisplayName();
		this.prefix = team.getPrefix();
		this.suffix = team.getSuffix();
		this.tag = team.getTagVisibility();
		this.color = team.getColor().ordinal();
		this.friendlyFire = team.getFriendlyFire();
		this.player = team.getMember().toArray(new String[0]);
	}

	public PacketPlayOutScoreboardTeam(Team t) {
		this.action = Action.fromInt(t.getMode());
		this.team = t.getName();
		this.displayName = t.getDisplayName();
		this.prefix = t.getPrefix();
		this.suffix = t.getSuffix();
		this.tag = NameTag.fromString(t.getNameTagVisibility());
		this.color = t.getColor();
		this.friendlyFire = t.getFriendlyFire();
		this.player = t.getPlayers();
	}

	public ChatColor getColor() {
		return ChatColor.values()[this.color == -1 ? 0 : this.color];
	}

	@SuppressWarnings("deprecation")
	@Override
	public void read(PacketDataSerializer s) {
		this.team = s.readString(16);
		this.action = Action.fromInt(s.readByte());
		if (this.action.getAction() == 0 || this.action.getAction() == 2) {
			this.displayName = s.readString(32);
			this.prefix = s.readString(16);
			this.suffix = s.readString(16);
			this.friendlyFire = s.readByte();
			this.tag = NameTag.fromString(s.readString(32));
			if (this.getBigVersion() != BigClientVersion.v1_8) {
				this.collisionRule = s.readString(-1);
			}
			this.color = s.readByte();
		}
		if (this.action.getAction() == 0 || this.action.getAction() == 3 || this.action.getAction() == 4) {
			int i = PacketDataSerializer.readVarInt(s);
			this.player = new String[i];
			for (int x = 0; x < i; x++) {
				this.player[x] = s.readString(40);
			}
		}

	}

	public void setColor(ChatColor color) {
		this.color = color.ordinal();
	}

	@Override
	public String toString() {
		return "PacketPlayOutScoreboardTeam [team=" + this.team + ", action=" + this.action + ", displayName=" + this.displayName + ", prefix=" + this.prefix + ", suffix=" + this.suffix + ", tag=" + this.tag + ", color=" + this.color + ", friendly_fire=" + this.friendlyFire + ", player=" + Arrays.toString(this.player) + "]";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void write(PacketDataSerializer s) {
		s.writeString(this.team);
		s.writeByte(this.action.getAction());
		if (this.action.getAction() == 0 || this.action.getAction() == 2) {
			s.writeString(this.displayName);
			s.writeString(this.prefix);
			s.writeString(this.suffix);
			s.writeByte(this.friendlyFire);
			if (this.tag == null) {
				this.tag = NameTag.VISIABLE;
			}
			s.writeString(this.tag.getIdentifire());
			if (this.getBigVersion() != BigClientVersion.v1_8) {
				s.writeString(this.collisionRule);
			}
			s.writeByte(this.color);
		}
		if (this.action.getAction() == 0 || this.action.getAction() == 3 || this.action.getAction() == 4) {
			s.writeVarInt(this.player.length);
			for (String x : this.player) {
				s.writeString(x);
			}
		}
	}
}
