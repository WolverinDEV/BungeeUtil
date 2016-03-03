package dev.wolveringer.BungeeUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.injector.InjectFiles;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.api.scoreboard.PacketListenerScoreboard;
import dev.wolveringer.chat.ChatColor.BukkitColorFormater;
import dev.wolveringer.commands.BungeeTimings;
import dev.wolveringer.network.IIInitialHandler;
import dev.wolveringer.network.ProxiedPlayerUserConnection;
import dev.wolveringer.network.channel.init.ChannelInizializer;
import dev.wolveringer.updater.Updater;

public class Main extends Plugin {
	private static boolean costumPromtLine = false;
	private static String costumPormtLineMessage = "";
	private static String costumPormtLineRawMessage = "";
	private static SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	private static Main main;
	public Thread plugin_thread;
	public Updater updater;
	
	@SuppressWarnings("rawtypes")
	public static Class conn;
	
	public static Main getMain() {
		return main;
	}
	
	@Override
	public void onLoad() {
		PacketHandler.class.getName();
		PacketLib.class.getName();
		try {
			ChannelInizializer.init();
		}
		catch (Exception e) {
			e.printStackTrace();
			sendMessage("§7[§eBungeeUntil§7] §cError while loading ProtocolLIB §4Code: 002");
			sendMessage("§7[§eBungeeUntil§7] §cDisable ProtocolLIB");
		}
		super.onLoad();
	}
	
	@SuppressWarnings({ "deprecation" })
	@Override
	public void onEnable() {
		main = this;
		setInformation("§aGeneral Loading");
		Configuration.init();
		AsyncCatcher.disableAll();
		AsyncCatcher.catchOp("null");
		
		setInformation("Check for updates");
		
		try {
			updater = new Updater("http://www.mcgalaxy.de/updater/updates.json");
			if (Configuration.isUpdaterActive()) updater.loadData();
			if (Configuration.isUpdaterActive()) if (updater.check()) return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		sleep(1000);
		
		setInformation("Loading PluginData");
		long start = System.currentTimeMillis();
		switch (InjectFiles.inject()) {
			case -1:
				break;
			case 0:
				sendMessage("§7[§eBungeeUntil§7] §cA fatal error has blocked in the injection of BungeeUtil.");
				sendMessage("§7[§eBungeeUntil§7] §cDisable BungeeUtil");
				return;
			case 1:
				long diff = System.currentTimeMillis() - start;
				DecimalFormat format = new DecimalFormat("#,000");
				sendMessage("§7[§eBungeeUntil§7] §aBungeeUtil injection successful (" + format.format(diff).replaceAll(",", ".") + "s). Restarting BungeeCord.");
				BungeeCord.getInstance().stop();
				return;
		};
		IIInitialHandler.init(ProxiedPlayerUserConnection.class);
		sleep(1000);
		
		sendMessage("§7[§eBungeeUntil§7] §aRegister Commands:");
		sendMessage("§7[§eBungeeUntil§7]   §7- §aBungeeTimings");
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new BungeeTimings());
		sendMessage("§7[§eBungeeUntil§7] §aLoading ProtocolLIB.");
		sendMessage("§7[§eBungeeUntil§7] §aProtocolLIB is loaded.");
		sendMessage("§7[§eBungeeUntil§7] §aLoadet Packets: ");
		Packet.listPackets();
		sleep(1000);
		setInformation("Register Schedullers");
		PacketListenerScoreboard.init();
		BungeeCord.getInstance().getScheduler().runAsync(this, new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5 * 1000);
					}
					catch (InterruptedException e) {
					}
					System.gc();
				}
			}
		});
		if (DebugProperties.RAM_LOGGER && Configuration.ramStatistics()) BungeeCord.getInstance().getScheduler().runAsync(this, new Runnable() {
			@Override
			public void run() {
				while (DebugProperties.RAM_LOGGER && Configuration.ramStatistics()) {
					try {
						Thread.sleep(10 * 1000);
					}
					catch (InterruptedException e) {
					}
					
					int mb = 1024 * 1024;
					Runtime runtime = Runtime.getRuntime();
					System.out.println();
					System.out.println("§7##### §6Heap utilization statistics [MB] §7#####");
					
					String var1 = (runtime.totalMemory() - runtime.freeMemory()) / mb + "";
					String var2 = runtime.freeMemory() / mb + "";
					String var3 = runtime.totalMemory() / mb + "";
					String var4 = runtime.maxMemory() / mb + "";
					
					int var5 = 5;
					var1 = format(var1, var5);
					var2 = format(var2, var5);
					var3 = format(var3, var5);
					var4 = format(var4, var5);
					
					System.out.println("§7#     §aReserved Used Memory:      §e" + var1 + "M    §7#");
					System.out.println("§7#     §aReserved Free Memory:      §e" + var2 + "M    §7#");
					System.out.println("§7#     §aReserved Memory:           §e" + var3 + "M    §7#");
					System.out.println("§7#     §a-----------------------------" + format("", var5).replaceAll(" ", "-") + "   §7#");
					System.out.println("§7#     §aAllowed Reservable Memory: §e" + var4 + "M    §7#");
					System.out.println("§7############################################");
					System.out.println();
				}
			}
			
			private String format(String in, int space) {
				while (in.length() < space) {
					in = in + " ";
				}
				return in;
			}
		});
		setInformation("Plugin loadet");
		sleep(1000);
		setInformation(null);
	}
	
	private void sleep(int millis) {
		if (!Configuration.isFastBoot()) {
			int procent = millis / 100;
			int now = 0;
			while (now * procent < millis) {
				setPromt(costumPormtLineRawMessage + " [" + now + "%]");
				try {
					Thread.sleep(procent);
				}
				catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				now++;
			}
			setPromt(costumPormtLineRawMessage + " [100%]");
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	/*
	 * 100 = 20 ? = 1
	 */
	
	public static void setInformation(String info) {
		if ("".equalsIgnoreCase(info) || info == null) costumPromtLine = false;
		else costumPromtLine = true;
		costumPormtLineRawMessage = info;
		setPromt(info);
	}
	
	private static void setPromt(String info) {
		try {
			if (costumPromtLine) BungeeCord.getInstance().getConsoleReader().resetPromptLine(costumPormtLineMessage = BukkitColorFormater.getFormater().format("§aLoading BungeeUtil >> §b" + info), "", 0);
			else BungeeCord.getInstance().getConsoleReader().resetPromptLine(">", "", 1);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void sendMessage(String message) {
		if (!message.startsWith("§7[§eBungeeUntil§7] ")) message = "§7[§eBungeeUntil§7] " + message;
		message = "\r" + date_format.format(new Date()) + " " + message;
		if (costumPromtLine) {
			try {
				while (message.length() < costumPormtLineMessage.length()) {
					message += " ";
				}
				// 15:00:37
				
				BungeeCord.getInstance().getConsoleReader().resetPromptLine("", "", 0);
				BungeeCord.getInstance().getConsole().sendMessage(message);
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(costumPormtLineMessage, "", 0);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else BungeeCord.getInstance().getConsole().sendMessage(message + ":" + costumPromtLine);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		BungeeCord.getInstance().getConsole().sendMessage("§7[§eBungeeUntil§7] §aThank you for using BungeeUntil");
	}
	/*
	
	public static void main(String[] args) {
		Runtime.getRuntime().load(new File("/home/wolverindev/workspaces/BungeeUtil/Native BungeeUtil/Debug/NativeLib.so").getAbsolutePath());
		NativePacketCreator p = new NativePacketCreator();
		p.registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x01, PacketPlayOutChat.class);
		p.registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x02, PacketPlayOutEntityAbstract.class);
		System.out.println(p.getRegisteredPackets());
	}
	*/
}
