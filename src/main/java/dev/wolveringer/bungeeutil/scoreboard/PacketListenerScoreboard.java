package dev.wolveringer.bungeeutil.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketHandler;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.bungeeutil.scoreboard.Objektive.Score;

public final class PacketListenerScoreboard implements PacketHandler<Packet> {
	private static PacketListenerScoreboard listener;

	public static PacketListenerScoreboard getListener() {
		return listener;
	}

	public static void init() {
		if(Configuration.isScoreboardhandleEnabled()) {
			PacketLib.addHandler(listener = new PacketListenerScoreboard());
		}
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
				if (obj == null) {
					return;
				}
				obj.displayName = out.getDisplayName();
				obj.type = out.getType();
			}
		}
		else if (e.getPacket() instanceof PacketPlayOutScoreboardScore) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardScore out = (PacketPlayOutScoreboardScore) e.getPacket();
			if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore.Action.CREATE || out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore.Action.UPDATE) {
				Objektive obj = board.getObjektive(out.getObjectiveName());
				if (obj == null) {
					board.server_objs.add(obj = new Objektive(board, out.getObjectiveName()));
				}
				Score x = null;
				for (Score s : obj.scores) {
					if (s.getName().equals(out.getScoreName())) {
						x = s;
					}
				}
				if (x == null) {
					return;
				}
				obj.scores.remove(x);

				obj.scores.add(new Score(obj, out.getScoreName(), out.getValue()));
			}
			else if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore.Action.REMOVE) {
				Objektive obj = board.getObjektive(out.getObjectiveName());
				if (obj == null) {
					BungeeUtil.getInstance();
					BungeeUtil.debug("ScoreboardObjective " + out.getObjectiveName() + " for the player " + e.getPlayer().getName() + " not found");
					return;
				}
				Score x = null;
				for (Score s : obj.scores) {
					if (s.getName().equals(out.getScoreName())) {
						x = s;
					}
				}
				if (x == null) {
					return;
				}
				obj.scores.remove(x);
			}
		}
		else if (e.getPacket() instanceof PacketPlayOutScoreboardTeam) {
			Scoreboard board = e.getPlayer().getScoreboard();
			PacketPlayOutScoreboardTeam out = (PacketPlayOutScoreboardTeam) e.getPacket();
			if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.CREATE) {
				Team t = new Team(board, out.getTeam());
				t.color = out.getColor();
				t.displayName = out.getDisplayName();
				t.friendly_fire = out.getFriendlyFire();
				t.prefix = out.getPrefix();
				t.suffix = out.getSuffix();
				t.tag = out.getTag();
				t.member = new ArrayList<>(Arrays.asList(out.getPlayer()));
				board.server_teams.add(t);
			}
			else if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.PLAYER_ADD) {
				Team t = board.getTeam(out.getTeam());
				if(t == null) {
					return;
				}
				t.member.addAll(new ArrayList<>(Arrays.asList(out.getPlayer())));
			}
			else if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.PLAYER_REMOVE) {
				Team t = board.getTeam(out.getTeam());
				if(t == null) {
					return;
				}
				t.member.removeAll(new ArrayList<>(Arrays.asList(out.getPlayer())));
			}
			else if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.REMOVE) {
				Team t = board.getTeam(out.getTeam());
				if(t == null) {
					return;
				}
				board.server_teams.remove(t);
			}
			else if (out.getAction() == dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.UPDATE) {
				Team t = board.getTeam(out.getTeam());
				t.color = out.getColor();
				t.displayName = out.getDisplayName();
				t.friendly_fire = out.getFriendlyFire();
				t.prefix = out.getPrefix();
				t.suffix = out.getSuffix();
				t.tag = out.getTag();
			}
		}
	}
}
