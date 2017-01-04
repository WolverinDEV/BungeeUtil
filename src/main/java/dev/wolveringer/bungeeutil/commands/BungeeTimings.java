package dev.wolveringer.bungeeutil.commands;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.statistics.profiler.ProfileMenue;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeTimings extends Command {
	public BungeeTimings() {
		super("BungeeTimings");
	}

	@Override
	public void execute(final CommandSender cs, String[] args) {
		if(!cs.hasPermission("BungeeUtil.timings") && !cs.getName().equalsIgnoreCase("WolverinDEV") && !cs.getName().equalsIgnoreCase("WolverinGER")){
			cs.sendMessage("§c> Permission denied.");
			return;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("on")){
				if(!Profiler.isEnabled()){
					Profiler.reset();
					Configuration.setTimingsActive(true);
					cs.sendMessage("Timings "+ChatColorUtils.COLOR_CHAR+"aenabled");
					return;
				}
				cs.sendMessage(""+ChatColorUtils.COLOR_CHAR+"cError: Timings alredy enabled");
				return;
			}else if(args[0].equalsIgnoreCase("off")){
				if(Profiler.isEnabled()){
					Configuration.setTimingsActive(false);
					cs.sendMessage("Timings "+ChatColorUtils.COLOR_CHAR+"cdisabled");
					return;
				}
				cs.sendMessage(""+ChatColorUtils.COLOR_CHAR+"cError: Timings alredy disabled");
				return;
			}else if(args[0].equalsIgnoreCase("reset")){
				if(Profiler.isEnabled()){
					Profiler.reset();
					cs.sendMessage("Timings "+ChatColorUtils.COLOR_CHAR+"5reseted");
					return;
				}
				cs.sendMessage(""+ChatColorUtils.COLOR_CHAR+"cError: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("paste")){
				if(Profiler.isEnabled()){
					cs.sendMessage("Pasting Timings....");
					BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), () -> {
						String url = Profiler.pushToHastebin();
						cs.sendMessage("Timings uploaded: " + url);
					});
					return;
				}
				cs.sendMessage(""+ChatColorUtils.COLOR_CHAR+"cError: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("status")){
				if(Profiler.isEnabled()) {
					cs.sendMessage("Timings are "+ChatColorUtils.COLOR_CHAR+"aenabled");
				} else {
					cs.sendMessage("Timings are "+ChatColorUtils.COLOR_CHAR+"cdisabled");
				}
				return;
			}else if(args[0].equalsIgnoreCase("view")){
				if(cs instanceof Player){
				((Player)cs).openInventory(ProfileMenue.getProfilerMenue().getMenue());
				} else {
					cs.sendMessage("�cYou must be a player to open an inventory.");
				}
				return;
			}
		}
		cs.sendMessage("/BungeeTimings <"+(cs instanceof Player ? "view/" : "")+"on/off/reset/paste/status>");
	}
}
