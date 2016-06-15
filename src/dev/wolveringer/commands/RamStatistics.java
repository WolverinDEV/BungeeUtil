package dev.wolveringer.commands;

import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.RamStatistics.RamStatistic;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class RamStatistics extends Command{

	public RamStatistics() {
		super("ramstatistics",null,"rm");
	}
	
	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!cs.hasPermission("bungeeutil.ramstats")){
			cs.sendMessage("§c> Permission denied.");
			return;
		}
		if(cs instanceof ConsoleCommandSender){
			
		}
		else
		{
			RamStatistic last = Main.getMain().ramStatistiks.getLastState();
			int mb = 1024*1024;
			cs.sendMessage("");
			cs.sendMessage("§aReserved Used Memory: §7"+((int)(last.getUsedMemory()/mb))+"M");
			cs.sendMessage("§aReserved Free Memory: §7"+((int)(last.getReservedMemory()-last.getUsedMemory()/mb))+"M");
			cs.sendMessage("§aReserved Memory: §7"+((int)(last.getReservedMemory()/mb))+"M");
			cs.sendMessage("§aAllowed Memory: §7"+((int)(last.getMaxMemory()/mb))+"M");
			cs.sendMessage("");
		}
	}
	
}
