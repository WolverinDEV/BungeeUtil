package dev.wolveringer.bungeeutil.profile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.json.JSONObject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.OperationCalback;
import net.md_5.bungee.BungeeCord;

public class SkinCache {
	private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";

	private static LoadingCache<UUID, Skin> profileCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<UUID, Skin>() {
		public Skin load(UUID name) throws Exception {
			return loadSkin(name);
		};
	});

	@Deprecated
	public static Skin getSkin(UUID uuid) {
		if(uuid == null)
			throw new IllegalArgumentException("UUID cant be null");
		try{
			return profileCache.get(uuid);
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
		if("".equalsIgnoreCase(s) || s == null)
			throw new IOException("Player skin not found (" + uuid + ")");
		return new Skin(new JSONObject(s));
	}
}