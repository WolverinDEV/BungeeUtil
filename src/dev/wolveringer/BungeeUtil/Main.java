package dev.wolveringer.BungeeUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import jline.TerminalFactory;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import dev.wolveringer.BungeeUtil.RamStatistics.RamStatistic;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.injector.InjectFiles;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.api.bossbar.BossBarListener;
import dev.wolveringer.api.scoreboard.PacketListenerScoreboard;
import dev.wolveringer.chat.ChatColor.BukkitColorFormater;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.commands.BungeeTimings;
import dev.wolveringer.network.IIInitialHandler;
import dev.wolveringer.network.ProxiedPlayerUserConnection;
import dev.wolveringer.network.channel.init.ChannelInizializer;
import dev.wolveringer.terminal.table.TerminalTable;
import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;
import dev.wolveringer.updater.Updater;

public class Main extends Plugin {
	public Updater updater;
	private static Main main = null;
	
	public static Main getMain() {
		return main;
	}
	
	@Override
	public void onLoad() {
		main = this;

	}
	
	@Override
	public void onEnable() {
		main = this;
		BungeeUtil.createInstance(main);
		Configuration.init();
		BungeeUtil.getInstance().setInformation("Check for updates");
		
		try {
			updater = new Updater("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/versions.json");
			updater.loadData();
			if (Configuration.isUpdaterActive() && updater.checkUpdate()) {
				BungeeUtil.getInstance().setInformation("§cRestarting bungeecord");
				BungeeUtil.getInstance().sleep(1000);
				BungeeUtil.getInstance().setInformation(null);
				System.exit(-1);
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (Configuration.getLastVersion() != null && updater != null) {
			BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aBungeeUtil successful updated!");
			BungeeUtil.getInstance().sendMessage(ChatColorUtils.COLOR_CHAR + "aUpdates:");
			TerminalTable table = new TerminalTable(new TerminalTable.TerminalColumn[]{
					new TerminalTable.TerminalColumn("Version", Align.LEFT),
					new TerminalTable.TerminalColumn("Changes", Align.LEFT)
			});
			HashMap<String, List<String>> _changes = updater.createChanges(Configuration.getLastVersion());
			List<Entry<String, List<String>>> changes = new ArrayList<>(_changes.entrySet());
			Collections.sort(changes, new Comparator<Entry<String, List<String>>>() {
				@Override
				public int compare(Entry<String, List<String>> o1, Entry<String, List<String>> o2) {
					return Long.compare(Long.parseLong(o2.getKey().replaceAll("\\.", "")), Long.parseLong(o1.getKey().replaceAll("\\.", "")));
				}
			});
			for(Entry<String, List<String>> e : changes){
				TerminalRow row = new TerminalRow(2);
				row.getColumns()[1].addAll(e.getValue());
				row.setText(0, e.getKey());
				table.addRow(row);
			}
			for(String message : table.buildLines())
				BungeeUtil.getInstance().sendMessage(message);
		}
		
		if(!BungeeUtil.getInstance().isInjected()){
			switch (BungeeUtil.getInstance().inject()) {
			case 0:
				System.exit(-1);
				break;
			case -1:
				break;
			default:
				return;
			}
		}
		BungeeUtil.getInstance().load();
		Packet.listPackets();
	}
	
	@Override
	public void onDisable() {
		BungeeUtil.getInstance().disable();
	}
}
