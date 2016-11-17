package dev.wolveringer.bungeeutil.profile;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerInfoData {
	private int ping;
	private int gamemode;
	private GameProfile gameprofile;
	private BaseComponent name;
	private String username;
	
	public PlayerInfoData(GameProfile gameprofile, int ping, int gamemode, BaseComponent tab) {
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

	public BaseComponent getName() {
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

	public void setName(BaseComponent name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PlayerInfoData [b=" + ping + ", gamemode=" + gamemode + ", gameprofile=" + gameprofile + ", name=" + TextComponent.toLegacyText(name) + "§r]";
	}
	public String getUsername() {
		return username;
	}
}
