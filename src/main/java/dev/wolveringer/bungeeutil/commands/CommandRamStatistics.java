package dev.wolveringer.bungeeutil.commands;

import java.util.Map.Entry;

import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.chat.AnsiColorFormater;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.creator.CachedPacketCreator;
import dev.wolveringer.bungeeutil.statistics.RamStatistics.RamStatistic;
import dev.wolveringer.bungeeutil.terminal.TerminalListener;
import dev.wolveringer.terminal.graph.TerminalGraph;
import dev.wolveringer.terminal.string.ColoredString;
import jline.TerminalFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class CommandRamStatistics extends Command{

	public CommandRamStatistics() {
		super("ramstatistics",null,"rm");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!cs.hasPermission("bungeeutil.ramstats")){
			cs.sendMessage(ChatColor.RED+"> Access denied (Insufficient permissions).");
			return;
		} else if(!Configuration.ramStatistics()){
			cs.sendMessage(ChatColor.RED+"> Ram statistics are disabled. Enable it by changing the entry in the config.");
			return;
		}
		
		if(cs instanceof ConsoleCommandSender){
			if(args.length == 1 && (args[0].equalsIgnoreCase("graph") || args[0].equalsIgnoreCase("g"))){
				TerminalListener.getInstance().setTerminalEnabled(false);
				TerminalGraph graph = BungeeUtil.getInstance().ramStatistiks.createGrath(120, 1024*1024);
				graph.setYAxisName(new ColoredString(ChatColor.GREEN+"mb"));
				graph.setXAxisName(new ColoredString(ChatColor.GREEN+"seconds"));
				
				StringBuilder sb = new StringBuilder();
				for(ColoredString line : graph.buildLines(TerminalFactory.get().getWidth()-1, TerminalFactory.get().getHeight()-4, false)) {
					//AnsiConsole.out.print("\r"+AnsiColorFormater.getFormater().format(line.toString())+"\n");
					sb.append("\r"+AnsiColorFormater.getFormater().format(line.toString())+"\n");
				}
				AnsiConsole.out.print(sb.toString());
				AnsiConsole.out.flush();
				TerminalListener.getInstance().setTerminalEnabled(true);
				return;
			}
		}

		RamStatistic last = BungeeUtil.getInstance().ramStatistiks.getLastState();
		int mb = 1024*1024;
		cs.sendMessage("");
		cs.sendMessage(ChatColor.GREEN+"Reserved Used Memory: "+ChatColor.GRAY+(int)(last.getUsedMemory()/mb)+"M");
		cs.sendMessage(ChatColor.GREEN+"Reserved Free Memory: "+ChatColor.GRAY+(int)((last.getReservedMemory()-last.getUsedMemory())/mb)+"M");
		cs.sendMessage(ChatColor.GREEN+"Reserved Memory: "+ChatColor.GRAY+(int)(last.getReservedMemory()/mb)+"M");
		cs.sendMessage(ChatColor.GREEN+"Allowed Memory: "+ChatColor.GRAY+(int)(last.getMaxMemory()/mb)+"M");
		cs.sendMessage("");

		if(Packet.getCreator() instanceof CachedPacketCreator){
			CachedPacketCreator c = (CachedPacketCreator) Packet.getCreator();
			cs.sendMessage("");
			cs.sendMessage(ChatColor.GREEN+"Packet Heap Statistics:");
			cs.sendMessage("  "+ChatColor.GREEN+"Waiting for processing: "+ChatColor.GRAY+c.getPacketsToProcessing());
			cs.sendMessage("  "+ChatColor.GREEN+"Packet waiting for reusing:");
			for(Entry<Class<? extends Packet>, Integer> e : c.getProcessedPackets().entrySet()) {
				cs.sendMessage("    "+ChatColor.GRAY+"- "+ChatColor.GREEN+e.getKey().getName()+" "+ChatColor.GRAY+"-> "+ChatColor.BLUE+e.getValue());
			}
			cs.sendMessage("");
		}
	}

}
