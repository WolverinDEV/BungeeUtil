package dev.wolveringer.bungeeutil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.wolveringer.bungeeutil.AsyncCatcher.AsyncCatcherMode;
import dev.wolveringer.bungeeutil.translation.Messages;
import dev.wolveringer.configuration.file.YamlConfiguration;

public class Configuration {
	private static YamlConfiguration conf;
	private static List<String> versionsFuture;
	
	public static YamlConfiguration getConfig() {
		return conf;
	}
	
	@SuppressWarnings("deprecation")
	public static void init(){
		conf = YamlConfiguration.loadConfiguration(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+Messages.getString("configuration.name")));
		conf.setDefaults(YamlConfiguration.loadConfiguration(Configuration.class.getResourceAsStream("/"+Messages.getString("configuration.name"))));
		conf.options().copyHeader(true);
		conf.options().copyDefaults(true);
		try{
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+Messages.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static boolean isQuietBoot(){
		return conf.getBoolean("loading.quiet");
	}
	
	public static String getByteBuffType() {
		return conf.getString("byteBuffType");
	}
	
	public static boolean ramStatistics() {
		return conf.getBoolean("debug.ram-statistics");
	}
	
	public static boolean isTerminalColored(){
		return conf.getBoolean("terminal.colored");
	}
	
	public static boolean isTimingsActive(){
		return conf.getBoolean(Messages.getString("configuration.timings"));
	}
	
	public static int getLoadingBufferSize(){
		return conf.getInt("loading.inject.buffer-size");
	}

	public static boolean isFastBoot(){
		return conf.getBoolean("loading.fastboot");
	}
	
	public static void setTimingsActive(boolean enabled) {
		conf.set(Messages.getString("configuration.timings"), enabled);
		try{
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+Messages.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
	public static boolean isUpdaterActive(){
		return conf.getBoolean(Messages.getString("configuration.updater"));
	}
	public static String getLastVersion(){
		return conf.getString("lastVersion"); 
	}
	public static void setLastVersion(String oldVerstion){
		if(oldVerstion == null){
			conf.set("lastVersion", null);
		}
		else
			conf.set("lastVersion", oldVerstion);
		try {
			conf.save(new File(BungeeUtil.getPluginInstance().getDataFolder().getParentFile().getAbsolutePath()+"/BungeeUtil/"+Messages.getString("configuration.name")));
		} catch (IOException e) {
		}
	}
	public static AsyncCatcherMode getAsyncMode(){
		if(conf.getBoolean("async-catcher.enabled"))
			return AsyncCatcherMode.valueOf(conf.getString("async-catcher.mode"));
		else
			return AsyncCatcherMode.DISABLED;
	}
	public static boolean isGCEnabled(){
		return conf.getBoolean("system.gc.enabled");
	}
	public static boolean isDebugEnabled(){
		return conf.getBoolean("debug.messages");
	}
	public static boolean isSyncInventoryClickActive(){
		return conf.getBoolean("inventory.synchandle");
	}
	public static boolean isScoreboardhandleEnabled(){
		return conf.getBoolean("utils.scoreboard");
	}
	public static boolean isBossBarhandleEnabled(){
		return conf.getBoolean("utils.bossbar");
	}
	public static HandleErrorAction getHandleExceptionAction(){
		return HandleErrorAction.valueOf(conf.getString("network.exception"));
	}
}
