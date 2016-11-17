package dev.wolveringer.bungeeutil.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.bungeeutil.plugin.Main;

public final class Objektive {
	protected Scoreboard owner;
	protected String name;
	protected String displayName;
	protected Type type;
	protected ArrayList<Score> scores = new ArrayList<>();
	protected Position pos;
	
	protected Objektive(Scoreboard owner, String name) {
		this.owner = owner;
		this.name = this.displayName = name;
		type = Type.INTEGER;
	}

	public void setScore(String name, int value) {
		for(Score s : scores)
			if(s.getName().equals(name)){
				s.setValue(value);
				return;
			}
		scores.add(new Score(this, name, 0));
		setScore(name, value);
	}

	public int getScore(String name) {
		for(Score s : scores)
			if(s.getName().equals(name)){
				return s.getValue();
			}
		return 0;
	}

	public List<String> getScores(){
		ArrayList<String> out = new ArrayList<>();
		for(Score s : scores)
			out.add(s.name);
		return Collections.unmodifiableList(out);
	}
	
	public void removeScore(String scoreName) {
		Score x = null;
		for(Score s : scores)
			if(s.getName().equals(scoreName)){
				x = s;
			}
		if(x == null){
			BungeeUtil.getInstance().debug("Removing not existing score ("+scoreName+ChatColorUtils.COLOR_CHAR+"r)");
			return;
		}
		scores.remove(x);
		owner.player.sendPacket(new PacketPlayOutScoreboardScore(x.name, getName(), -1, PacketPlayOutScoreboardScore.Action.REMOVE));
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		sendUpdate();
	}

	public void setType(Type type) {
		this.type = type;
		sendUpdate();
	}

	public void display(Position position) {
		pos = position;
		owner.player.sendPacket(new PacketPlayOutScoreboardDisplayObjective(name, position));
	}
	public Position getPosition() {
		return this.pos;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getName() {
		return this.name;
	}

	private void sendUpdate() {
		owner.player.sendPacket(new PacketPlayOutScoreboardObjective(name, Action.UPDATE, displayName, type));
	}

	protected static class Score {
		private Objektive owner;
		protected String name;
		private int value;

		Score(Objektive owner, String name, int value) {
			this.owner = owner;
			this.name = name;
			this.value = value;
			owner.owner.player.sendPacket(new PacketPlayOutScoreboardScore(name, owner.getName(), value, PacketPlayOutScoreboardScore.Action.CREATE));
		}

		public void setValue(int value) {
			this.value = value;
			sendUpdate();
		}

		public int getValue() {
			return this.value;
		}

		public String getName() {
			return this.name;
		}

		private void sendUpdate() {
			owner.owner.player.sendPacket(new PacketPlayOutScoreboardScore(name, owner.getName(), value, PacketPlayOutScoreboardScore.Action.UPDATE));
		}
	}
}
