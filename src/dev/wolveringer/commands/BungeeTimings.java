package dev.wolveringer.commands;

import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.profiler.Profiler;
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
			cs.sendMessage("§cYou are not allowed to perform this command.");
			return;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("on")){
				if(!Profiler.isEnabled()){
					Profiler.reset();
					Configuration.setTimingsActive(true);
					cs.sendMessage("Timings §aenabled");
					return;
				}
				cs.sendMessage("§cError: Timings alredy enabled");
				return;
			}else if(args[0].equalsIgnoreCase("off")){
				if(Profiler.isEnabled()){
					Configuration.setTimingsActive(false);
					cs.sendMessage("Timings §cdisabled");
					return;
				}
				cs.sendMessage("§cError: Timings alredy disabled");
				return;
			}else if(args[0].equalsIgnoreCase("reset")){
				if(Profiler.isEnabled()){
					Profiler.reset();
					cs.sendMessage("Timings §5reseted");
					return;
				}
				cs.sendMessage("§cError: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("paste")){
				if(Profiler.isEnabled()){
					cs.sendMessage("Pasting Timings....");
					BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
						@Override
						public void run() {
							String url = Profiler.pushToHastebin();
							cs.sendMessage("Timings uploaded: " + url);
						}
					});
					return;
				}
				cs.sendMessage("§cError: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("status")){
				if(Profiler.isEnabled())
					cs.sendMessage("Timings are §aenabled");
				else
					cs.sendMessage("Timings are §cdisabled");
				return;
			}
		}
		cs.sendMessage("/BungeeTimings <on/off/reset/paste/status>");
	}
}
