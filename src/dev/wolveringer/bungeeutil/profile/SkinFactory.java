package dev.wolveringer.bungeeutil.profile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.OperationCalback;
import dev.wolveringer.bungeeutil.plugin.Main;

public class SkinFactory {
	private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";

	private static LoadingCache<UUID, Skin> profileCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<UUID, Skin>() {
		public Skin load(UUID name) throws Exception {
			Skin skin = new SteveSkin();
			try{
				skin = loadSkin(name);
			}catch(Exception e){
				e.printStackTrace();
			}
			return skin;
		};
	});

	@Deprecated
	public static Skin getSkin(UUID uuid) {
		if(uuid == null)
			throw new IllegalArgumentException("UUID cant be null");
		try{
			Skin s = profileCache.get(uuid);
			if(s instanceof SteveSkin){
				profileCache.refresh(uuid);
				s = profileCache.get(uuid);
			}
			return s.clone();
		}catch (Exception e){
			BungeeCord.getInstance().getLogger().log(Level.WARNING, "Cant loading Skin for " + uuid + " (Reson: " + e.getMessage() + ")");
			return Skin.createEmptySkin();
		}
	}

	@SuppressWarnings("unchecked")
	public static void getSkin(final UUID uuid, final OperationCalback<Skin>... c) {
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public void run() {
				Skin s = getSkin(uuid);
				for(OperationCalback t : c)
					t.done(s);
			}
		});
	}
	
	public static Skin createEmptySkin(){
		return Skin.createEmptySkin();
	}
	
	public static Skin createSkin(String rawValue,String signature){
		JSONObject o = new JSONObject();
		JSONArray props = new JSONArray();
		JSONObject prop = new JSONObject();
		prop.put("name", "textures");
		prop.put("value", rawValue);
		prop.put("signature", signature);
		props.put(prop);
		o.put("properties", props);
		return new Skin(o);
	}
	
	@Deprecated
	public static Skin getSkin(String name) {
		try{
			return getSkin(UUIDFetcher.getUUIDOf(name));
		}catch (Exception e){
			e.printStackTrace();
		}
		return Skin.createEmptySkin();
	}
	
	@SuppressWarnings("unchecked")
	public static void getSkin(final String name, final OperationCalback<Skin>... c) {
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public void run() {
				Skin s = getSkin(name);
				for(OperationCalback t : c)
					t.done(s);
			}
		});
	}

	private static Skin loadSkin(UUID uuid) throws IOException {
		String s = new SkinRequest().performGetRequest(new URL(PROFILE_URL + uuid.toString().replace("-", "") + "?unsigned=false"));
		if("".equalsIgnoreCase(s) || s == null){
			throw new NullPointerException("Player skin not found (" + uuid + ")");
		}
		return new Skin(new JSONObject(s));
	}
	
	public static void main(String[] args) {
		Skin s = Skin.createEmptySkin();
		s.setRawData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjdiYmQwYjI5MTFjOTZiNWQ4N2IyZGY3NjY5MWE1MWI4YjEyYzZmZWZkNTIzMTQ2ZDhhYzVlZjFiOGVlIn19fQ==");
		s.setSkin(SkinFactory.getSkin("WolverinEN").getSkinUrl());
		s.setUUID(UUID.randomUUID());
		
		System.out.print(s.getRawData());
	}
}