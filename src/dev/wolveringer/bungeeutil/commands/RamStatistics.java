package dev.wolveringer.bungeeutil.commands;

import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.chat.AnsiColorFormater;
import dev.wolveringer.bungeeutil.statistics.RamStatistics.RamStatistic;
import dev.wolveringer.bungeeutil.terminal.TerminalListener;
import dev.wolveringer.string.ColoredString;
import dev.wolveringer.terminal.graph.TerminalGraph;
import jline.TerminalFactory;
import jline.internal.TerminalLineSettings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class RamStatistics extends Command{

	public RamStatistics() {
		super("ramstatistics",null,"rm");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!cs.hasPermission("bungeeutil.ramstats")){
			cs.sendMessage("§c> Permission denied.");
			return;
		}
		if(cs instanceof ConsoleCommandSender){
			TerminalListener.getInstance().setTerminalEnabled(false);
			TerminalGraph graph = BungeeUtil.getInstance().ramStatistiks.createGrath(120, 1024*1024);
			graph.setYAxisName(new ColoredString("§amb"));
			graph.setXAxisName(new ColoredString("§aseconds"));
			for(ColoredString line : graph.buildLines(TerminalFactory.get().getWidth()-1, TerminalFactory.get().getHeight()-4, false))
				AnsiConsole.out.print("\r"+AnsiColorFormater.getFormater().format(line.toString())+"\n");
			AnsiConsole.out.flush();
			TerminalListener.getInstance().setTerminalEnabled(true);
		}
		else
		{
			RamStatistic last = BungeeUtil.getInstance().ramStatistiks.getLastState();
			int mb = 1024*1024;
			cs.sendMessage("");
			cs.sendMessage("§aReserved Used Memory: §7"+((int)(last.getUsedMemory()/mb))+"M");
			cs.sendMessage("§aReserved Free Memory: §7"+((int)((last.getReservedMemory()-last.getUsedMemory())/mb))+"M");
			cs.sendMessage("§aReserved Memory: §7"+((int)(last.getReservedMemory()/mb))+"M");
			cs.sendMessage("§aAllowed Memory: §7"+((int)(last.getMaxMemory()/mb))+"M");
			cs.sendMessage("");
		}
	}
	
}
