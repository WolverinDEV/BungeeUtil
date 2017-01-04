package dev.wolveringer.bungeeutil.profile;

import net.md_5.bungee.api.chat.BaseComponent;

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

	public int getGamemode() {
		return this.gamemode;
	}

	public GameProfile getGameprofile() {
		return this.gameprofile;
	}

	public BaseComponent getName() {
		return this.name;
	}

	public int getPing() {
		return this.ping;
	}

	public String getUsername() {
		return this.username;
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

	public void setPing(int b) {
		this.ping = b;
	}
	@Override
	public String toString() {
		return "PlayerInfoData [b=" + this.ping + ", gamemode=" + this.gamemode + ", gameprofile=" + this.gameprofile + ", name=" + BaseComponent.toLegacyText(this.name) + "§r]";
	}
}
