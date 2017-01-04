package dev.wolveringer.bungeeutil.commands;

import java.util.Map.Entry;

import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.chat.AnsiColorFormater;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.creator.CachedPacketCreator;
import dev.wolveringer.bungeeutil.statistics.RamStatistics.RamStatistic;
import dev.wolveringer.bungeeutil.terminal.TerminalListener;
import dev.wolveringer.terminal.graph.TerminalGraph;
import dev.wolveringer.terminal.string.ColoredString;
import jline.TerminalFactory;
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
			if(args.length == 1 && (args[0].equalsIgnoreCase("graph") || args[0].equalsIgnoreCase("g"))){
				TerminalListener.getInstance().setTerminalEnabled(false);
				TerminalGraph graph = BungeeUtil.getInstance().ramStatistiks.createGrath(120, 1024*1024);
				graph.setYAxisName(new ColoredString("§amb"));
				graph.setXAxisName(new ColoredString("§aseconds"));
				for(ColoredString line : graph.buildLines(TerminalFactory.get().getWidth()-1, TerminalFactory.get().getHeight()-4, false)) {
					AnsiConsole.out.print("\r"+AnsiColorFormater.getFormater().format(line.toString())+"\n");
				}
				AnsiConsole.out.flush();
				TerminalListener.getInstance().setTerminalEnabled(true);
				return;
			}
		}

		RamStatistic last = BungeeUtil.getInstance().ramStatistiks.getLastState();
		int mb = 1024*1024;
		cs.sendMessage("");
		cs.sendMessage("§aReserved Used Memory: §7"+(int)(last.getUsedMemory()/mb)+"M");
		cs.sendMessage("§aReserved Free Memory: §7"+(int)((last.getReservedMemory()-last.getUsedMemory())/mb)+"M");
		cs.sendMessage("§aReserved Memory: §7"+(int)(last.getReservedMemory()/mb)+"M");
		cs.sendMessage("§aAllowed Memory: §7"+(int)(last.getMaxMemory()/mb)+"M");
		cs.sendMessage("");

		if(Packet.getCreator() instanceof CachedPacketCreator){
			CachedPacketCreator c = (CachedPacketCreator) Packet.getCreator();
			cs.sendMessage("");
			cs.sendMessage("§aPacket Heap Statistics:");
			cs.sendMessage("  §aWaiting for processing: §7"+c.getPacketsToProcessing());
			cs.sendMessage("  §aPacket waiting for reusing:");
			for(Entry<Class<? extends Packet>, Integer> e : c.getProcessedPackets().entrySet()) {
				cs.sendMessage("    §7- §a"+e.getKey().getName()+" §7-> §b"+e.getValue());
			}
			cs.sendMessage("");
		}
	}

}
