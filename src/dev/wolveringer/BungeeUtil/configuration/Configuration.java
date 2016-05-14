package dev.wolveringer.BungeeUtil.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.wolveringer.BungeeUtil.AsyncCatcher.AsyncCatcherMode;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.configuration.file.YamlConfiguration;
import dev.wolveringer.strings.Messages;

public class Configuration {
	private static YamlConfiguration conf;
	private static List<String> versionsFuture;
	
	public static YamlConfiguration getConfig() {
		return conf;
	}
	
	@SuppressWarnings("deprecation")
	public static void init(){
		conf = YamlConfiguration.loadConfiguration(new File(Main.getMain().getDataFolder(),Messages.getString("configuration.name")));
		conf.setDefaults(YamlConfiguration.loadConfiguration(Configuration.class.getResourceAsStream("/"+Messages.getString("configuration.name"))));
		conf.options().copyHeader(true);
		conf.options().copyDefaults(true);
		try{
			conf.save(new File(Main.getMain().getDataFolder(),Messages.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
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
			conf.save(new File(Main.getMain().getDataFolder(),Messages.getString("configuration.name")));
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
	public static boolean isUpdaterActive(){
		return conf.getBoolean(Messages.getString("configuration.updater"));
	}
	public static List<String> getVersionsFeature(){
		if(conf.isList("versionsFeatures"))
			return conf.getStringList("versionsFeatures");
		return new ArrayList<String>();
	}
	public static void setVersionFeature(List<String> out){
		if(out == null || out.size() == 0){
			conf.set("versionsFeatures", null);
		}
		else
			conf.set("versionsFeatures", out);
		try {
			conf.save(new File(Main.getMain().getDataFolder(),Messages.getString("configuration.name")));
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
}
