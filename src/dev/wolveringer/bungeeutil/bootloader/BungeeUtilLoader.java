package dev.wolveringer.bungeeutil.bootloader;

import dev.wolveringer.bungeeutil.plugin.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeUtilLoader {
	public static enum MajorBungeeUtilVersion {
		v1_x,
		v2_x;
	}
	/**
	 * @return Returns null if BungeeUtil isnt loaded
	 */
	public static String getLoadedBungeeUtilVersion(){
		Plugin plugin = BungeeCord.getInstance().getPluginManager().getPlugin("BungeeUtil");
		if(plugin == null){
			try{
				Class.forName("dev.wolveringer.bungeeutil.BungeeUtil");
				plugin = dev.wolveringer.bungeeutil.BungeeUtil.getPluginInstance();
			}catch(Exception e){
				return null;
			}
		}
		else
		{
			dev.wolveringer.bungeeutil.plugin.Main butilMain = (Main) plugin;
			return butilMain.getDescription().getVersion();
		}
		return null;
	}
}
