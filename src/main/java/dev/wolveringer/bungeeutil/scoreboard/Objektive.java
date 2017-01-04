package dev.wolveringer.bungeeutil.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardScore;

public final class Objektive {
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

		public String getName() {
			return this.name;
		}

		public int getValue() {
			return this.value;
		}

		private void sendUpdate() {
			this.owner.owner.player.sendPacket(new PacketPlayOutScoreboardScore(this.name, this.owner.getName(), this.value, PacketPlayOutScoreboardScore.Action.UPDATE));
		}

		public void setValue(int value) {
			this.value = value;
			this.sendUpdate();
		}
	}
	protected Scoreboard owner;
	protected String name;
	protected String displayName;
	protected Type type;
	protected ArrayList<Score> scores = new ArrayList<>();

	protected Position pos;

	protected Objektive(Scoreboard owner, String name) {
		this.owner = owner;
		this.name = this.displayName = name;
		this.type = Type.INTEGER;
	}

	public void display(Position position) {
		this.pos = position;
		this.owner.player.sendPacket(new PacketPlayOutScoreboardDisplayObjective(this.name, position));
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getName() {
		return this.name;
	}

	public Position getPosition() {
		return this.pos;
	}

	public int getScore(String name) {
		for(Score s : this.scores) {
			if(s.getName().equals(name)){
				return s.getValue();
			}
		}
		return 0;
	}

	public List<String> getScores(){
		ArrayList<String> out = new ArrayList<>();
		for(Score s : this.scores) {
			out.add(s.name);
		}
		return Collections.unmodifiableList(out);
	}
	public void removeScore(String scoreName) {
		Score x = null;
		for(Score s : this.scores) {
			if(s.getName().equals(scoreName)){
				x = s;
			}
		}
		if(x == null){
			BungeeUtil.getInstance();
			BungeeUtil.debug("Removing not existing score ("+scoreName+ChatColorUtils.COLOR_CHAR+"r)");
			return;
		}
		this.scores.remove(x);
		this.owner.player.sendPacket(new PacketPlayOutScoreboardScore(x.name, this.getName(), -1, PacketPlayOutScoreboardScore.Action.REMOVE));
	}

	private void sendUpdate() {
		this.owner.player.sendPacket(new PacketPlayOutScoreboardObjective(this.name, Action.UPDATE, this.displayName, this.type));
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		this.sendUpdate();
	}

	public void setScore(String name, int value) {
		for(Score s : this.scores) {
			if(s.getName().equals(name)){
				s.setValue(value);
				return;
			}
		}
		this.scores.add(new Score(this, name, 0));
		this.setScore(name, value);
	}

	public void setType(Type type) {
		this.type = type;
		this.sendUpdate();
	}
}
