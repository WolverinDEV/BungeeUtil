package dev.wolveringer.bungeeutil.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.plugin.Updater.Version;
import dev.wolveringer.bungeeutil.system.ProxyType;
import dev.wolveringer.terminal.table.TerminalTable;
import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	private static Main main = null;
	public static Main getMain() {
		return main;
	}

	public Updater updater;

	@Override
	public void onDisable() {
		BungeeUtil.getInstance().disable();
	}

	@Override
	public void onEnable() {
		main = this;
		if(BungeeUtil.getInstance() == null) {
			BungeeUtil.createInstance(main);
		}

		if(BungeeCord.getInstance().getPluginManager().getPlugin("ViaVersion") != null){
			BungeeUtil.getInstance().sendMessage("§7------------------------------ §c§nAttantion:§r ------------------------------");
			BungeeUtil.getInstance().sendMessage("           §5BungeeUtil detected ViaVersion (Bungee).");
			BungeeUtil.getInstance().sendMessage("           §5BungeeUtil may have conflicts with ViaVersion.");
			BungeeUtil.getInstance().sendMessage("           §5Any bugs/errors with ViaVersion installed will be ignored.");
			BungeeUtil.getInstance().sendMessage("§7------------------------------------------------------------------------");
		}

		Configuration.init();
		BungeeUtil.getInstance().sendMessage(ChatColor.GRAY+"Detected Minecraft proxy type: "+ProxyType.getType().toString());
		BungeeUtil.getInstance().setInformation("Loading update data");
		try {
			this.updater = new Updater("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/versions.json");
			this.updater.loadData();
			BungeeUtil.getInstance().displayedSleep(1000);

			if(this.updater.getData() == null){
				BungeeUtil.getInstance().sendMessage(ChatColor.RED+"Cant get versions informations.");
				this.updater = null;
			}
			else
			{
				if (Configuration.isUpdaterActive() && this.updater.checkUpdate()) {
					BungeeUtil.getInstance().setInformation(ChatColor.RED+"Restarting bungeecord");
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

		if (Configuration.getLastVersion() != null && this.updater != null) {
			HashMap<Updater.Version, List<String>> _changes = this.updater.createChanges(new Version(Configuration.getLastVersion()));
			if(_changes.size() > 0){
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "BungeeUtil successful updated!");
				BungeeUtil.getInstance().sendMessage(ChatColor.GRAY + "Updates:");
				TerminalTable table = new TerminalTable(new TerminalTable.TerminalColumn[]{
						new TerminalTable.TerminalColumn("Version", Align.LEFT),
						new TerminalTable.TerminalColumn("Changes", Align.LEFT)
				});

				List<Entry<Updater.Version, List<String>>> changes = new ArrayList<>(_changes.entrySet());
				Collections.sort(changes, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
				for(Entry<Updater.Version, List<String>> e : changes){
					TerminalRow row = new TerminalRow(2);
					row.getColumns()[1].addAll(e.getValue());
					row.setText(0, e.getKey().getVersion());
					table.addRow(row);
				}
				for(String message : table.buildLines()) {
					BungeeUtil.getInstance().sendMessage(message);
				}
			}
		}
		Configuration.setLastVersion(this.updater.getCurrentVersion().getPlainVersion());

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

		if(!Configuration.isQuietBoot()){
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Registered packets:");
			Packet.listPackets();
		}
	}

	@Override
	public void onLoad() {
		main = this;

	}
}
