package dev.wolveringer.BungeeUtil;

import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;

public class PlayerInfoData {
	private int ping;
	private int gamemode;
	private GameProfile gameprofile;
	private IChatBaseComponent name;
	private String username;
	
	public PlayerInfoData(GameProfile gameprofile, int ping, int gamemode, IChatBaseComponent tab) {
		this.gameprofile = gameprofile;
		this.ping = ping;
		this.gamemode = gamemode;
		this.name = tab;
		this.username = gameprofile.getName();
	}

	public PlayerInfoData(GameProfile gameprofile, int ping, int gamemode2, String tab) {
		this.gameprofile = gameprofile;
		this.ping = ping;
		this.gamemode = gamemode2;
		this.username = tab;
	}

	public int getPing() {
		return ping;
	}

	public int getGamemode() {
		return gamemode;
	}

	public GameProfile getGameprofile() {
		return gameprofile;
	}

	public IChatBaseComponent getName() {
		return name;
	}
	
	public void setPing(int b) {
		this.ping = b;
	}

	public void setGamemode(int gamemode) {
		this.gamemode = gamemode;
	}

	public void setGameprofile(GameProfile gameprofile) {
		this.gameprofile = gameprofile;
	}

	public void setName(IChatBaseComponent name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PlayerInfoData [b=" + ping + ", gamemode=" + gamemode + ", gameprofile=" + gameprofile + ", name=" + ChatSerializer.toMessage(name) + "]";
	}
	public String getUsername() {
		return username;
	}
}
