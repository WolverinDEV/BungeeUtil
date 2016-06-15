package dev.wolveringer.api.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardScore;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.api.scoreboard.Objektive.Score;

public final class PacketListenerScoreboard implements PacketHandler<Packet> {
	private static PacketListenerScoreboard listener;
	
	public static void init() {
		PacketLib.addHandler(listener = new PacketListenerScoreboard());
	}
	
	public static PacketListenerScoreboard getListener() {
		return listener;
	}
	
	@Override
	public void handle(PacketHandleEvent<Packet> e) {
		if (e.getPacket() instanceof PacketPlayOutScoreboardDisplayObjective) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardDisplayObjective out = (PacketPlayOutScoreboardDisplayObjective) e.getPacket();
			Objektive o = board.getObjektive(out.getName());
			o.pos = out.getPosition();
		}
		else if (e.getPacket() instanceof PacketPlayOutScoreboardObjective) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardObjective out = (PacketPlayOutScoreboardObjective) e.getPacket();
			if (out.getAction() == Action.CREATE) {
				Objektive obj;
				board.server_objs.add(obj = new Objektive(board, out.getScorebordName()));
				obj.displayName = out.getDisplayName();
				obj.name = out.getScorebordName();
				obj.type = out.getType();
			}
			else if (out.getAction() == Action.REMOVE) {
				board.server_objs.remove(board.getObjektive(out.getScorebordName()));
			}
			else if (out.getAction() == Action.UPDATE) {
				Objektive obj = board.getObjektive(out.getScorebordName());
				if (obj == null) return;
				obj.displayName = out.getDisplayName();
				obj.type = out.getType();
			}
		}
		else if (e.getPacket() instanceof PacketPlayOutScoreboardScore) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardScore out = (PacketPlayOutScoreboardScore) e.getPacket();
			if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardScore.Action.CREATE || out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardScore.Action.UPDATE) {
				Objektive obj = board.getObjektive(out.getObjektiveName());
				if (obj == null) board.server_objs.add(obj = new Objektive(board, out.getObjektiveName()));
				if (obj == null) {
					Main.debug("ScoreboardObjective " + out.getObjektiveName() + " for the player " + e.getPlayer().getName() + " not found!");
					return;
				}
				Score x = null;
				for (Score s : obj.scores)
					if (s.getName().equals(out.getScoreName())) {
						x = s;
					}
				if (x == null) return;
				obj.scores.remove(x);
				
				obj.scores.add(new Score(obj, out.getScoreName(), out.getValue()));
			}
			else if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardScore.Action.REMOVE) {
				Objektive obj = board.getObjektive(out.getObjektiveName());
				if (obj == null) {
					Main.debug("ScoreboardObjective " + out.getObjektiveName() + " for the player " + e.getPlayer().getName() + " not found");
					return;
				}
				Score x = null;
				for (Score s : obj.scores)
					if (s.getName().equals(out.getScoreName())) {
						x = s;
					}
				if (x == null) return;
				obj.scores.remove(x);
			}
		}
		else if (e.getPacket() instanceof PacketPlayOutScoreboardTeam) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardTeam out = (PacketPlayOutScoreboardTeam) e.getPacket();
			if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.CREATE) {
				Team t = new Team(board, out.getTeam());
				t.color = out.getColor();
				t.displayName = out.getDisplayName();
				t.friendly_fire = out.isFriendlyFire();
				t.prefix = out.getPrefix();
				t.suffix = out.getSuffix();
				t.tag = out.getTag();
				t.member = new ArrayList<>(Arrays.asList(out.getPlayers()));
				board.server_teams.add(t);
			}
			else if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.PLAYER_ADD) {
				Team t = board.getTeam(out.getTeam());
				t.member.addAll(new ArrayList<>(Arrays.asList(out.getPlayers())));
			}
			else if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.PLAYER_REMOVE) {
				Team t = board.getTeam(out.getTeam());
				t.member.removeAll(new ArrayList<>(Arrays.asList(out.getPlayers())));
			}
			else if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.REMOVE) {
				Team t = board.getTeam(out.getTeam());
				board.server_teams.remove(t);
			}
			else if (out.getAction() == dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.UPDATE) {
				Team t = board.getTeam(out.getTeam());
				t.color = out.getColor();
				t.displayName = out.getDisplayName();
				t.friendly_fire = out.isFriendlyFire();
				t.prefix = out.getPrefix();
				t.suffix = out.getSuffix();
				t.tag = out.getTag();
			}
		}
	}
}
