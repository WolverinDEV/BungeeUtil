package dev.wolveringer.api.scoreboard;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.NameTag;

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
	
	public void setColor(ChatColor color) {
		this.color = color;
		sendUpdate();
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		sendUpdate();
	}
	public void addMember(String name){
		member.add(name);
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.PLAYER_ADD);
		team.setPlayers(new String[]{name});
		owner.player.sendPacket(team);
	}
	public void removeMember(String name){
		member.remove(name);
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.PLAYER_REMOVE);
		team.setPlayers(new String[]{name});
		owner.player.sendPacket(team);
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		sendUpdate();
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		sendUpdate();
	}
	public void setTagVisibility(NameTag tag) {
		this.tag = tag;
		sendUpdate();
	}
	public void setFriendlyFire(int friendly_fire) {
		this.friendly_fire = friendly_fire;
		sendUpdate();
	}
	
	public ChatColor getColor() {
		return this.color;
	}
	public String getDisplayName() {
		return this.displayName;
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
	public int getFriendlyFire() {
		return this.friendly_fire;
	}
	private void sendUpdate(){
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam(this);
		team.setAction(Action.UPDATE);
		owner.player.sendPacket(team);
	}
}
