package dev.wolveringer.bungeeutil.plugin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class BukkitMain extends JavaPlugin{
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY+"[] ----------------------------------------------- []");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"7["+ChatColor.GREEN+"BungeeUntil"+ChatColor.GRAY+"] "+ChatColor.RED+"BungeeUtil is only a plugin for the BungeeCord");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY+"[] ----------------------------------------------- []");
	}

}
