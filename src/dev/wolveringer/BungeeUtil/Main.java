package dev.wolveringer.BungeeUtil;

import io.netty.channel.unix.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import jline.TerminalFactory;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.jni.zlib.NativeCompressImpl;
import dev.wolveringer.BungeeUtil.RamStatistics.RamStatistic;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.injector.InjectFiles;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.api.scoreboard.PacketListenerScoreboard;
import dev.wolveringer.chat.ChatColor.BukkitColorFormater;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.commands.BungeeTimings;
import dev.wolveringer.network.IIInitialHandler;
import dev.wolveringer.network.ProxiedPlayerUserConnection;
import dev.wolveringer.network.channel.init.ChannelInizializer;
import dev.wolveringer.updater.Updater;
import dev.wolveringer.util.MathUtil;

public class Main extends Plugin {
	private static boolean active;
	private static boolean costumPromtLine = false;
	private static String costumPormtLineMessage = "";
	private static String costumPormtLineRawMessage = "";
	private static SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	private static Main main;
	public Thread plugin_thread;
	public Updater updater;
	public RamStatistics ramStatistiks;
	
	public static boolean isPluginActive() {
		return active;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class conn;
	
	public static Main getMain() {
		return main;
	}
	
	@Override
	public void onLoad() {
		main = this;
		ramStatistiks = new RamStatistics();
		// "+ChatColorUtils.COLOR_CHAR+"
		PacketHandler.class.getName();
		PacketLib.class.getName();
		try {
			ChannelInizializer.init();
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cError while loading ProtocolLIB " + ChatColorUtils.COLOR_CHAR + "4Code: 002");
			sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cDisable ProtocolLIB");
		}
	}
	
	@Override
	public void onEnable() {
		main = this;
		active = true;
		System.out.println("Terminal size: " + TerminalFactory.get().getWidth());
		setInformation(ChatColorUtils.COLOR_CHAR + "aGeneral Loading");
		Configuration.init();
		AsyncCatcher.init();
		AsyncCatcher.disable(this);
		AsyncCatcher.catchOp("Async test failed");
		
		if (Configuration.getVersionsFeature().size() != 0) {
			sendMessage("§aBungeeUtil successful updated!");
			sendMessage("§aFeatures:");
			for (String s : Configuration.getVersionsFeature())
				sendMessage("   §e" + s);
			Configuration.setVersionFeature(null);
		}
		
		setInformation("Check for updates");
		
		try {
			updater = new Updater("http://www.mcgalaxy.de/updater/updates.json");
			if (Configuration.isUpdaterActive())
				updater.loadData();
			if (Configuration.isUpdaterActive())
				if (updater.check())
					return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		sleep(1000);
		
		setInformation("Loading PluginData");
		long start = System.currentTimeMillis();
		switch (InjectFiles.inject()) {
			case -1:
				break;
			case 0:
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cA fatal error has blocked in the injection of BungeeUtil.");
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cDisable BungeeUtil");
				return;
			case 1:
				long diff = System.currentTimeMillis() - start;
				DecimalFormat format = new DecimalFormat("#,000");
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aBungeeUtil injection successful (" + format.format(diff).replaceAll(",", ".") + "s). Restarting BungeeCord.");
				BungeeCord.getInstance().stop();
				return;
		}
		;
		IIInitialHandler.init(ProxiedPlayerUserConnection.class);
		sleep(1000);
		
		sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aRegister Commands:");
		sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7]   " + ChatColorUtils.COLOR_CHAR + "7- " + ChatColorUtils.COLOR_CHAR + "aBungeeTimings");
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new BungeeTimings());
		sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aLoading ProtocolLIB.");
		sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aProtocolLIB is loaded.");
		sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aLoadet Packets: ");
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
					} catch (InterruptedException e) {
					}
					if (!active)
						return;
					System.gc();
				}
			}
		});
		if (Configuration.ramStatistics()) {
			ramStatistiks.start();
			BungeeCord.getInstance().getScheduler().runAsync(this, new Runnable() {
				@Override
				public void run() {
					while (Configuration.ramStatistics()) {
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e) {
						}
						if (!active)
							return;
						if(ramStatistiks.getLastState() == null)
							continue;
						
						int mb = 1024 * 1024;
						
						
						RamStatistic state = ramStatistiks.getLastState();
						String var1 = (state.getUsedMemory()) / mb + "";
						String var2 = (state.getReservedMemory()-state.getUsedMemory())/ mb + "";
						String var3 = state.getReservedMemory() / mb + "";
						String var4 = state.getMaxMemory() / mb + "";
						
						int var5 = 5;
						var1 = format(var1, var5);
						var2 = format(var2, var5);
						var3 = format(var3, var5);
						var4 = format(var4, var5);

						
						int diff = 0;
						if(state.getPreviousStatistic(10,TimeUnit.SECONDS) != null)
								diff = (int) (((int)(state.getUsedMemory() / mb))-((int)(state.getPreviousStatistic(10,TimeUnit.SECONDS).getUsedMemory() / mb)));
						String diffSpace = "";
						for(int i = 0;i<("(*"+Math.abs(diff)+")").length();i++)
							diffSpace+=" ";
						Main.sendMessage("");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#####"+diffSpace.substring(0, diffSpace.length()/2).replaceAll(" ", "#")+" " + ChatColorUtils.COLOR_CHAR + "6Heap utilization statistics [MB] " + ChatColorUtils.COLOR_CHAR + "7#####"+diffSpace.substring(0, diffSpace.length()/2).replaceAll(" ", "#")+(diffSpace.length()%2!=0?"#":""));
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Used Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var1 + "M §7("+(diff>0?"§a+":diff<0?"§c-":"§6±")+Math.abs(diff)+"§7)   " + ChatColorUtils.COLOR_CHAR + "7#");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Free Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var2 + "M    "+diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Memory:           " + ChatColorUtils.COLOR_CHAR + "e" + var3 + "M    "+diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "a-----------------------------" + format("", var5).replaceAll(" ", "-") + "   "+diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aAllowed Reservable Memory: " + ChatColorUtils.COLOR_CHAR + "e" + var4 + "M    "+diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
						Main.sendMessage(ChatColorUtils.COLOR_CHAR + "7############################################"+diffSpace.replaceAll(" ", "#"));
						Main.sendMessage("");
					}
				}
				
				private String format(String in, int space) {
					while (in.length() < space) {
						in = in + " ";
					}
					return in;
				}
			});
		}
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
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				now++;
			}
			setPromt(costumPormtLineRawMessage + " [100%]");
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/*
	 * 100 = 20 ? = 1
	 */
	
	public static void setInformation(String info) {
		if ("".equalsIgnoreCase(info) || info == null)
			costumPromtLine = false;
		else
			costumPromtLine = true;
		costumPormtLineRawMessage = info;
		setPromt(info);
	}
	
	private static void setPromt(String info) {
		try {
			if (costumPromtLine)
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(costumPormtLineMessage = BukkitColorFormater.getFormater().format(ChatColorUtils.COLOR_CHAR + "aLoading BungeeUtil >> " + ChatColorUtils.COLOR_CHAR + "b" + info), "", 0);
			else
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(">", "", 1);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void sendMessage(String message) {
		if (!message.startsWith(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] "))
			message = ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + message;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			BungeeCord.getInstance().getConsole().sendMessage(message);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		active = false;
		BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aThank you for using BungeeUntil");
	}
	public Main() throws IOException {
		ServerSocket socket = new ServerSocket(25566);
		while (true) {
			java.net.Socket sock = socket.accept();
			InputStream is = sock.getInputStream();
			while (true) {
				if(is.available() > 0){
					byte[] b = new byte[is.available()];
					is.read(b);
					String out = "";
					for(byte b0 : b)
						out+=" 0x"+Integer.toHexString(b0)+",";
					System.out.println("Readed: "+out);
				}
			}
		}
	}
	/*
	 * 
	 * public static void main(String[] args) { Runtime.getRuntime().load(new
	 * File(
	 * "/home/wolverindev/workspaces/BungeeUtil/Native BungeeUtil/Debug/NativeLib.so"
	 * ).getAbsolutePath()); NativePacketCreator p = new NativePacketCreator();
	 * p.registerPacket(Protocol.GAME, Direction.TO_CLIENT, 0x01,
	 * PacketPlayOutChat.class); p.registerPacket(Protocol.GAME,
	 * Direction.TO_CLIENT, 0x02, PacketPlayOutEntityAbstract.class);
	 * Main.sendMessage(p.getRegisteredPackets()); }
	 */
}
