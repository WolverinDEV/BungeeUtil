package dev.wolveringer.bungeeutil.plugin;

import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.plugin.updater.Updater;
import dev.wolveringer.bungeeutil.plugin.updater.UpdaterV2;
import dev.wolveringer.bungeeutil.plugin.updater.Version;
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
			BungeeUtil.getInstance().sendMessage(ChatColor.COLOR_CHAR+"7------------------------------ "+ChatColor.COLOR_CHAR+"c"+ChatColor.COLOR_CHAR+"nAttantion:"+ChatColor.COLOR_CHAR+"r ------------------------------");
			BungeeUtil.getInstance().sendMessage("           "+ChatColor.COLOR_CHAR+"5BungeeUtil detected ViaVersion (Bungee).");
			BungeeUtil.getInstance().sendMessage("           "+ChatColor.COLOR_CHAR+"5BungeeUtil may have conflicts with ViaVersion.");
			BungeeUtil.getInstance().sendMessage("           "+ChatColor.COLOR_CHAR+"5Any bugs/errors with ViaVersion installed will be ignored.");
			BungeeUtil.getInstance().sendMessage(ChatColor.COLOR_CHAR+"7------------------------------------------------------------------------");
		}

		Configuration.init();
		BungeeUtil.getInstance().sendMessage(ChatColor.GRAY+"Detected Minecraft proxy type: "+ProxyType.getType().toString());
		BungeeUtil.getInstance().setInformation("Loading update data");
		try {
			this.updater = new UpdaterV2("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/BungeeUtil.json");
			if(!Configuration.isUpdaterActive()){ //Check async if an update is avariable
				//BungeeCord.getInstance().getScheduler().runAsync(this, ()->{
					this.updater.loadData();
					if(this.updater.isValid()){
						if(this.updater.hasUpdate()){
							BungeeUtil.getInstance().sendMessage(ChatColor.GOLD+"[UPDATER] A newer version is avariable (Version: "+updater.getNewestVersion().getVersion()+"). Enable the updater to update automaticly.");
						}
					}
				//});
			} else {
				this.updater.loadData();
				BungeeUtil.getInstance().displayedSleep(1000);

				if(!this.updater.isValid()){
					BungeeUtil.getInstance().sendMessage(ChatColor.RED+"Cant get versions informations.");
					this.updater = null;
				}
				else
				{
					
					update:
					if (this.updater.hasUpdate()) {
						BungeeUtil.getInstance().setInformation(ChatColor.GREEN+"Update found. Updating.");
						switch (this.updater.update()) {
						case ALREDY_UP_TO_DATE:
							break update;
						case SUCCESSFULL:
							BungeeUtil.getInstance().setInformation(ChatColor.GREEN+"Update was successful!");
							break;
						case FAILED_CHECKSUM:
						case FAILED_DOWNLOAD:
						case FAILED_NO_DATA:
						case FAILED_UNKNOWN:
						case FAILED_FILE:
							BungeeUtil.getInstance().setInformation(ChatColor.RED+"Update failed.");
						default:
							break;
						};
						BungeeUtil.getInstance().setInformation(ChatColor.RED+"Restarting bungeecord");
						BungeeUtil.getInstance().displayedSleep(1000);
						BungeeUtil.getInstance().setInformation(null);
						Configuration.setLastVersion(updater.getOwnVersion().getVersion());
						System.exit(-1);
						return;
					}
					BungeeUtil.getInstance().setInformation(ChatColor.GREEN+"No update found. Version is up to date.");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (Configuration.getLastVersion() != null && this.updater != null) {
			List<Version> versions = this.updater.getVersionsBehind();
			if(versions.size() > 0){
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "BungeeUtil successful updated!");
				BungeeUtil.getInstance().sendMessage(ChatColor.GRAY + "Updates:");
				TerminalTable table = new TerminalTable(new TerminalTable.TerminalColumn[]{
						new TerminalTable.TerminalColumn("Version", Align.LEFT),
						new TerminalTable.TerminalColumn("Changes", Align.LEFT)
				});
				for(Version version : versions){
					TerminalRow row = new TerminalRow(2);
					row.getColumns()[1].addAll(this.updater.getChangeNotes(version));
					row.setText(0, version.getVersion());
					table.addRow(row);
				}
				for(String message : table.buildLines()) {
					BungeeUtil.getInstance().sendMessage(message);
				}
			}
			Configuration.setLastVersion(this.updater.getOwnVersion().getPlainVersion());
		}
		
		if(this.updater != null){
			List<String> motd = this.updater.getMOTD(this.updater.getOwnVersion());
			if(motd.size() > 0){
				BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + "BungeeUtil information message:");
				for(String line : motd)
					BungeeUtil.getInstance().sendMessage(ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', line));
			}
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

		if(!Configuration.isQuietBoot()){
			BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Registered packets:");
			Packet.listPackets();
		}
	}

	@Override
	public void onLoad() {
		main = this;
	}
	
	public static void main(String[] args) {
		Updater updater;
		updater = new UpdaterV2("https://raw.githubusercontent.com/WolverinDEV/BungeeUtil/jars/BungeeUtil.json");
		updater.loadData();
		System.setProperty("updater.version", "2.2.1");
		System.out.println(updater.hasUpdate()+" - "+updater.isValid());
	}
}
