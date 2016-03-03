package dev.wolveringer.api.scoreboard;

import java.util.ArrayList;

import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam;

public final class Scoreboard {
	@SuppressWarnings("serial")
	private static class ScoreboardAlredyExistException extends RuntimeException {
		public ScoreboardAlredyExistException(String message) {
			super(message);
		}
	}

	protected Player player;
	private ArrayList<Objektive> objs = new ArrayList<>();
	private ArrayList<Team> teams = new ArrayList<>();

	protected ArrayList<Team> server_teams = new ArrayList<>();
	protected ArrayList<Objektive> server_objs = new ArrayList<>();
	
	public Scoreboard(Player player) {
		//if(player.getScoreboard() != null)
		//	throw new ScoreboardAlredyExistException("Player " + player.getName() + " has alredy a Scoreboard");
		this.player = player;
	}

	public Objektive createObjektive(String name, Type t) {
		if(getObjektive(name) != null)
			return getObjektive(name);
		Objektive o = new Objektive(this, name);
		objs.add(o);
		player.sendPacket(new PacketPlayOutScoreboardObjective(name, Action.CREATE, o.getDisplayName(), t));
		return o;
	}

	public Objektive getObjektive(String name) {
		for(Objektive o : objs)
			if(o.getName().equals(name))
				return o;
		for(Objektive o : server_objs)
			if(o.getName().equals(name))
				return o;
		return null;
	}

	public void removeObjektive(String name) {
		Objektive o = getObjektive(name);
		if(o == null)
			return;
		objs.remove(o);
		server_objs.remove(o);
		player.sendPacket(new PacketPlayOutScoreboardObjective(name, Action.REMOVE, o.getDisplayName(), Type.INTEGER));
	}

	public Team createTeam(String name) {
		if(getTeam(name) != null)
			return getTeam(name);
		Team t = new Team(this, name);
		teams.add(t);
		return t;
	}

	public void removeTeam(String name) {
		Team t = getTeam(name);
		if(t != null){
			teams.remove(t);
			PacketPlayOutScoreboardTeam x = new PacketPlayOutScoreboardTeam(t);
			x.setAction(dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action.REMOVE);
			player.sendPacket(x);
		}
	}

	public Team getTeam(String name) {
		for(Team t : teams)
			if(t.getName().equals(name))
				return t;
		for(Team t : server_teams)
			if(t.getName().equals(name))
				return t;
		return null;
	}

	@Override
	public String toString() {
		return "Scoreboard [Owner="+player.getName()+",Objekt-Count="+(objs.size()+server_objs.size())+"(Bungee: "+objs.size()+"/Server: "+server_objs.size()+"),Team-Count="+(teams.size()+server_teams.size())+"(Bungee:"+teams.size()+"/Server:"+server_teams.size()+")]";
	}
}
