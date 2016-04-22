package dev.wolveringer.BungeeUtil.gameprofile;

import java.util.UUID;

public class Test {
	public static void main(String[] args) throws Exception {
		UUID uuid = UUIDFetcher.getUUIDOf("WolverinDEV");
		System.out.println("UUID: "+uuid);
		Skin skin = SkinCache.getSkin(uuid);
		System.out.println(skin);
	}
}
