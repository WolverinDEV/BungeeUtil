package dev.wolveringer.bungeeutil.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.bungeeutil.AsyncCatcher;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.bungeeutil.player.Player;

public final class Scoreboard {
	@SuppressWarnings({"unused" })
	private static class ScoreboardAlredyExistException extends RuntimeException {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

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
		if(this.getObjektive(name) != null) {
			return this.getObjektive(name);
		}
		Objektive o = new Objektive(this, name);
		this.objs.add(o);
		this.player.sendPacket(new PacketPlayOutScoreboardObjective(name, Action.CREATE, o.getDisplayName(), t));
		return o;
	}

	public Team createTeam(String name) {
		if(this.getTeam(name) != null) {
			return this.getTeam(name);
		}
		Team t = new Team(this, name);
		this.teams.add(t);
		return t;
	}

	public Objektive getObjektive(String name) {
		for(Objektive o : this.objs) {
			if(o.getName().equals(name)) {
				return o;
			}
		}
		for(Objektive o : this.server_objs) {
			if(o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}

	public List<Objektive> getObjektives(){
		return Collections.unmodifiableList(this.objs);
	}

	public Team getTeam(String name) {
		for(Team t : this.teams) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		for(Team t : this.server_teams) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	public List<Team> getTeams(){
		return Collections.unmodifiableList(this.teams);
	}

	public void removeObjektive(String name) {
		Objektive o = this.getObjektive(name);
		if(o == null) {
			return;
		}
		AsyncCatcher.catchOp("Async scoreboard changing");
		this.player.sendPacket(new PacketPlayOutScoreboardObjective(name, Action.REMOVE, o.getDisplayName(), Type.INTEGER));
		if(this.objs.remove(o) && !this.server_objs.remove(o)){ //Check if proxy side board
			for(Objektive var0 : this.server_objs) {
				if(var0.getPosition() == o.getPosition()){
					var0.display(o.getPosition());
					break;
				}
			}
		}
	}

	public void removeTeam(String name) {
		Team t = this.getTeam(name);
		if(t != null){
			this.teams.remove(t);
			PacketPlayOutScoreboardTeam x = new PacketPlayOutScoreboardTeam(t);
			x.setAction(dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action.REMOVE);
			this.player.sendPacket(x);
		}
	}

	@Override
	public String toString() {
		return "Scoreboard [Owner="+this.player.getName()+",Objekt-Count="+(this.objs.size()+this.server_objs.size())+"(Bungee: "+this.objs.size()+"/Server: "+this.server_objs.size()+"),Team-Count="+(this.teams.size()+this.server_teams.size())+"(Bungee:"+this.teams.size()+"/Server:"+this.server_teams.size()+")]";
	}
}
