package dev.wolveringer.bungeeutil.scoreboard;

import java.util.ArrayList;

import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.NameTag;
import net.md_5.bungee.api.ChatColor;

public final class Team {
	protected Scoreboard owner;
	protected ChatColor color = ChatColor.WHITE;
	protected String name;
	protected String displayName;
	protected String prefix = "";
	protected String suffix = "";
	protected NameTag tag = NameTag.VISIABLE;
	protected int friendly_fire = 0;
	protected ArrayList<String> member = new ArrayList<>();

	public Team(Scoreboard owner,String name) {
		this.owner = owner;
		this.name = name;
	}

	public void addMember(String name){
		this.member.add(name);
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.PLAYER_ADD);
		team.setPlayer(new String[]{name});
		this.owner.player.sendPacket(team);
	}
	public ChatColor getColor() {
		return this.color;
	}
	public String getDisplayName() {
		return this.displayName;
	}
	public int getFriendlyFire() {
		return this.friendly_fire;
	}
	public ArrayList<String> getMember() {
		return this.member;
	}
	public String getName() {
		return this.name;
	}
	public String getPrefix() {
		return this.prefix;
	}
	public String getSuffix() {
		return this.suffix;
	}

	public NameTag getTagVisibility() {
		return this.tag;
	}
	public void removeMember(String name){
		this.member.remove(name);
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.PLAYER_REMOVE);
		team.setPlayer(new String[]{name});
		this.owner.player.sendPacket(team);
	}
	private void sendUpdate(){
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.UPDATE);
		this.owner.player.sendPacket(team);
	}
	public void setColor(ChatColor color) {
		this.color = color;
		this.sendUpdate();
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		this.sendUpdate();
	}
	public void setFriendlyFire(int friendly_fire) {
		this.friendly_fire = friendly_fire;
		this.sendUpdate();
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		this.sendUpdate();
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		this.sendUpdate();
	}
	public void setTagVisibility(NameTag tag) {
		this.tag = tag;
		this.sendUpdate();
	}
}
