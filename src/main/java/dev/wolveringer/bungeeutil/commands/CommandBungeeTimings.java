package dev.wolveringer.bungeeutil.commands;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.statistics.profiler.ProfileMenue;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CommandBungeeTimings extends Command {
	public CommandBungeeTimings() {
		super("BungeeTimings");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(final CommandSender cs, String[] args) {
		if(!cs.hasPermission("BungeeUtil.timings") && !cs.getName().equalsIgnoreCase("WolverinDEV") && !cs.getName().equalsIgnoreCase("WolverinGER")){
			cs.sendMessage(ChatColor.RED+"> Access denied (Insufficient permissions).");
			return;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("on")){
				if(!Profiler.isEnabled()){
					Profiler.setEnabled(true);
					cs.sendMessage("Timings "+ChatColor.GREEN+"enabled");
					return;
				}
				cs.sendMessage(ChatColor.RED+"Error: Timings alredy enabled");
				return;
			}else if(args[0].equalsIgnoreCase("off")){
				if(Profiler.isEnabled()){
					Profiler.setEnabled(false);
					cs.sendMessage("Timings "+ChatColor.COLOR_CHAR+"cdisabled");
					return;
				}
				cs.sendMessage(ChatColor.RED+"Error: Timings alredy disabled");
				return;
			}else if(args[0].equalsIgnoreCase("reset")){
				if(Profiler.isEnabled()){
					Profiler.reset();
					cs.sendMessage("Timings "+ChatColor.DARK_PURPLE+"reseted");
					return;
				}
				cs.sendMessage(ChatColor.RED+"Error: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("paste")){
				if(Profiler.isEnabled()){
					cs.sendMessage(ChatColor.GOLD+"> Creating timings....");
					FutureTask<String> task = Profiler.pasteToHastebin();
					cs.sendMessage(ChatColor.GOLD+"> Uploading timings....");
					BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), () -> {
						try {
							cs.sendMessage(ChatColor.GREEN+"> Timings uploaded. URL: "+task.get(15, TimeUnit.SECONDS));
						}catch (TimeoutException e) {
							cs.sendMessage(ChatColor.RED+"> Failed to upload timings. (Timeout)");
						}catch(InterruptedException e){
							cs.sendMessage(ChatColor.RED+"> Failed to upload timings. (Interrupt)");
						}catch(ExecutionException e){
							cs.sendMessage(ChatColor.RED+"> Failed to upload timings. (Exception: "+e.getMessage()+")");
						}
					});
					return;
				}
				cs.sendMessage(ChatColor.RED+"> Error: Timings are disabled");
				return;
			}else if(args[0].equalsIgnoreCase("status")){
				if(Profiler.isEnabled()) {
					cs.sendMessage("Timings are "+ChatColor.GREEN+"enabled");
				} else {
					cs.sendMessage("Timings are "+ChatColor.RED+"disabled");
				}
				return;
			}else if(args[0].equalsIgnoreCase("view")){
				if(cs instanceof Player){
					((Player)cs).openInventory(ProfileMenue.getProfilerMenue().getMenue());
				} else {
					cs.sendMessage(ChatColor.RED+"You must be a player to open an inventory.");
				}
				return;
			}
		}
		cs.sendMessage("/bungeetimings <"+(cs instanceof Player ? "view/" : "")+"on/off/reset/paste/status>");
	}
}
