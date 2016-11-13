package dev.wolveringer.bungeeutil.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.fusesource.jansi.AnsiConsole;

import net.md_5.bungee.api.plugin.Plugin;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.terminal.table.TerminalTable;
import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;

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
		if(BungeeUtil.getInstance() == null)
			BungeeUtil.createInstance(main);
		Configuration.init();
		BungeeUtil.getInstance().sendMessage("Ansi consolen class: "+AnsiConsole.out.getClass());
		BungeeUtil.getInstance().setInformation("Check for updates");
		try {
			updater = new Updater("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/versions.json");
			updater.loadData();
			if(updater.getData() == null){
				BungeeUtil.getInstance().sendMessage("§cCant get versions informations.");
			}
			else
			{
				if (Configuration.isUpdaterActive() && updater.checkUpdate()) {
					BungeeUtil.getInstance().setInformation("§cRestarting bungeecord");
					BungeeUtil.getInstance().displayedSleep(1000);
					BungeeUtil.getInstance().setInformation(null);
					System.exit(-1);
					return;
				}
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
