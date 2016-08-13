package dev.wolveringer.BungeeUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.BungeeUtil.RamStatistics.RamStatistic;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.injector.InjectFiles;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.chat.ChatColor.BukkitColorFormater;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.commands.BungeeTimings;
import dev.wolveringer.listener.InventoryResetListener;
import dev.wolveringer.network.IIInitialHandler;
import dev.wolveringer.network.ProxiedPlayerUserConnection;
import dev.wolveringer.network.channel.init.BungeeConnectionInit;
import dev.wolveringer.network.channel.init.ChannelInizializer;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;
import jline.TerminalFactory;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.command.ConsoleCommandSender;

public final class BungeeUtil {
	@Getter
	private static Plugin pluginInstance;
	@Getter
	private static BungeeUtil instance;
	
	public static BungeeUtil createInstance(Plugin plugin){
		if(instance == null){
			pluginInstance = plugin;
			instance = new BungeeUtil();
		}
		return instance;
	}
	
	private BigInteger state = new BigDecimal(0).toBigInteger();
	@Getter
	private boolean active;
	private boolean costumPromtLine = false;
	private String costumPormtLineMessage = "";
	private String costumPormtLineRawMessage = "";
	private SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
	public RamStatistics ramStatistiks;
	
	private BungeeUtil() {}
	
	public void load(){
		if(state.testBit(0))
			throw new RuntimeException("Alredy loading plugin.");
		if(state.testBit(1))
			throw new RuntimeException("BungeeUtil alredy loaded!");
		if(!isInjected() && !state.testBit(3))
			throw new RuntimeException("BungeeUtil isnt injected!");
		state.setBit(0);
		try{
			active =  true;
			setInformation("§aLoading configuration.");
			sendMessage("§aLoading configuration.");
			Configuration.init();
			sleep(500);
			setInformation("§aValidate configuration.");
			sendMessage("§aValidate configuration.");
			if(Configuration.getHandleExceptionAction() == null){
				sendMessage("§cCant find the NetworkExceptionAction for "+Configuration.getConfig().getString("network.exception")+". §6Using default ("+HandleErrorAction.DISCONNECT+")");
				Configuration.getConfig().set("network.exception", HandleErrorAction.DISCONNECT.name().toUpperCase());
			}
			sleep(500);
			setInformation("§aConfiguration valid.");
			sendMessage("§aConfiguration valid.");
			sleep(500);
			setInformation("§aLoading AsyncCatcher");
			sendMessage("§aLoading AsyncCatcher");
			AsyncCatcher.init();
			AsyncCatcher.disable(pluginInstance);
			AsyncCatcher.catchOp("Async test failed");
			sleep(500);
			setInformation("§aAsyncCatcher successfull loaded");
			sendMessage("§aAsyncCatcher successfull loaded");
			sleep(500);
			setInformation("§aLoading ChannelInizializer");
			sendMessage("§aLoading ChannelInizializer");
			boolean flag = true;
			try {
				ChannelInizializer.init();
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cError while loading ProtocolLIB " + ChatColorUtils.COLOR_CHAR + "4Code: 002");
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cDisable ProtocolLIB");
			}
			sleep(500);
			if(!flag){
				setInformation("§cAn error happend while loading aChannelInizializer.");
				sendMessage("§cAn error happend while loading aChannelInizializer.");
				sleep(500);
				return;
			}
			setInformation("§aChannelInizializer successfull loaded.");
			sendMessage("§aChannelInizializer successfull loaded.");
			sleep(500);
			setInformation("§aLoading player class");
			sendMessage("§aLoading player class");
			IIInitialHandler.init(ProxiedPlayerUserConnection.class);
			sleep(500);
			setInformation("§aPlayer class loaded");
			sendMessage("§aPlayer class loaded");
			
			sleep(500);
			setInformation("§aRegister commands and scheduler");
			sendMessage("§aRegister commands and scheduler");
			BungeeCord.getInstance().getPluginManager().registerListener(pluginInstance, new InventoryResetListener());
			BungeeCord.getInstance().getPluginManager().registerCommand(pluginInstance, new BungeeTimings());
			BungeeCord.getInstance().getScheduler().runAsync(pluginInstance, new Runnable() {
				@Override
				public void run() {
					sendMessage("§eSystem.gc() -> Enabled: " + Configuration.isGCEnabled());
					while (Configuration.isGCEnabled()) {
						try {
							Thread.sleep(5 * 1000);
						}
						catch (InterruptedException e) {
						}
						if (!active) return;
						System.gc();
					}
				}
			});
			if (Configuration.ramStatistics()) {
				ramStatistiks = new RamStatistics();
				ramStatistiks.start();
				BungeeCord.getInstance().getScheduler().runAsync(pluginInstance, new Runnable() {
					@Override
					public void run() {
						while (Configuration.ramStatistics()) {
							try {
								Thread.sleep(100);
							}
							catch (InterruptedException e) {
							}
							if (!isActive()) return;
							if (ramStatistiks.getLastState() == null) continue;
							
							int mb = 1024 * 1024;
							
							RamStatistic state = ramStatistiks.getLastState();
							String var1 = (state.getUsedMemory()) / mb + "";
							String var2 = (state.getReservedMemory() - state.getUsedMemory()) / mb + "";
							String var3 = state.getReservedMemory() / mb + "";
							String var4 = state.getMaxMemory() / mb + "";
							
							int var5 = 5;
							var1 = format(var1, var5);
							var2 = format(var2, var5);
							var3 = format(var3, var5);
							var4 = format(var4, var5);
							
							int diff = 0;
							if (state.getPreviousStatistic(10, TimeUnit.SECONDS) != null) diff = (int) (((int) (state.getUsedMemory() / mb)) - ((int) (state.getPreviousStatistic(10, TimeUnit.SECONDS).getUsedMemory() / mb)));
							String diffSpace = "";
							for (int i = 0; i < ("(*" + Math.abs(diff) + ")").length(); i++)
								diffSpace += " ";
							List<String> lines = new ArrayList<>();
							lines.add(ChatColorUtils.COLOR_CHAR + "7#####" + diffSpace.substring(0, diffSpace.length() / 2).replaceAll(" ", "#") + " " + ChatColorUtils.COLOR_CHAR + "6Heap utilization statistics [MB] " + ChatColorUtils.COLOR_CHAR + "7#####" + diffSpace.substring(0, diffSpace.length() / 2).replaceAll(" ", "#") + (diffSpace.length() % 2 != 0 ? "#" : ""));
							lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Used Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var1 + "M " + ChatColorUtils.COLOR_CHAR + "7(" + (diff > 0 ? ChatColorUtils.COLOR_CHAR + "a+" : diff < 0 ? ChatColorUtils.COLOR_CHAR + "c-" : ChatColorUtils.COLOR_CHAR + "6±") + Math.abs(diff) + ChatColorUtils.COLOR_CHAR + "7)   " + ChatColorUtils.COLOR_CHAR + "7#");
							lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Free Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var2 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
							lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Memory:           " + ChatColorUtils.COLOR_CHAR + "e" + var3 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
							lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "a-----------------------------" + format("", var5).replaceAll(" ", "-") + "   " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
							lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aAllowed Reservable Memory: " + ChatColorUtils.COLOR_CHAR + "e" + var4 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
							lines.add(ChatColorUtils.COLOR_CHAR + "7############################################" + diffSpace.replaceAll(" ", "#"));
							int h = 2;
							int w = 0;
							for(String m : lines)
								if(ChatColor.stripColor(m).length()>w)
									w = ChatColor.stripColor(m).length();
							w = TerminalFactory.get().getWidth()-w+2;
							for(int i = 0;i<lines.size();i++,h++){
								AnsiConsole.out.print("\033["+h+";"+w+"H"+BukkitColorFormater.getFormater().format(lines.get(i)));
							}
							int cw = 2+BungeeCord.getInstance().getConsoleReader().getCursorBuffer().cursor; //2 = Promt = ' >'
							AnsiConsole.out.print("\033["+TerminalFactory.get().getHeight()+";"+cw+"H");
							AnsiConsole.out.flush();
						}
					}
					
					private String format(String in, int space) {
						while (in.length() < space) {
							in = in + " ";
						}
						return in;
					}
				});
				BungeeCord.getInstance().getPluginManager().registerCommand(pluginInstance, new dev.wolveringer.commands.RamStatistics());
			}
			
			sleep(500);
			setInformation("§aRegister packets");
			sendMessage("§aRegister packets");
			Packet.countPackets();
			sleep(500);
			setInformation("§aPackets registered");
			sendMessage("§aPackets registered");
			sleep(500);
			setInformation(null);
			state.setBit(1);
			active = true;
		}catch(Exception e){
			active = false;
			throw e;
		}finally{
			state.clearBit(0);
		}
	}
	
	public int inject(){
		if(state.testBit(2))
			throw new RuntimeException("Alredy injecting plugin.");
		if(state.testBit(3))
			throw new RuntimeException("BungeeUtil alredy injected!");
		state.setBit(2);
		try{
			if(isInjected())
				return -1;
			setInformation("§aInjecting patches");
			sendMessage("§aInjecting patches");
			switch (InjectFiles.inject()) {
			case -1:
				return -1;
			case 0:
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cA fatal error has blocked in the injection of BungeeUtil.");
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cDisable BungeeUtil");
				setInformation(null);
				return 2;
			case 1:
				sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aBungeeUtil injection successful. Need a BungeeCord restart.");
				setInformation(null);
				state.setBit(3);
				return 0;
		};
		}finally{
			state.clearBit(2);
		}
		return 2;
	}
	
	public boolean isInjected(){
		return InjectFiles.isInjected() || state.testBit(3);
	}
	
	public static void debug(String string) {
		if (pluginInstance == null || Configuration.isDebugEnabled()) System.out.println(string); // Debug if this not a plugin
	}
	
	public static void debug(Exception e, String otherMessage) {
		if (pluginInstance == null || Configuration.isDebugEnabled()) e.printStackTrace(); // Debug if this not a plugin
	}
	
	public static void debug(Exception e) {
		debug(e, "Exception message -> " + e.getMessage());
	}
	
	void sleep(int millis) {
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
	
	
	public void disable(){
		active = false;
		BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "aThank you for using BungeeUntil");
	}
	
	public void setInformation(String info) {
		if ("".equalsIgnoreCase(info) || info == null) costumPromtLine = false;
		else costumPromtLine = true;
		costumPormtLineRawMessage = info;
		setPromt(info);
	}
	
	private void setPromt(String info) {
		try {
			if (costumPromtLine) BungeeCord.getInstance().getConsoleReader().resetPromptLine(costumPormtLineMessage = BukkitColorFormater.getFormater().format(ChatColorUtils.COLOR_CHAR + "aLoading BungeeUtil >> " + ChatColorUtils.COLOR_CHAR + "b" + info), "", 0);
			else BungeeCord.getInstance().getConsoleReader().resetPromptLine(">", "", 1);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void sendMessage(String message) {
		if (!message.startsWith(ChatColorUtils.PREFIX + " ")) message = ChatColorUtils.PREFIX + " " + message;
		message = "\r" + date_format.format(new Date()) + " " + message;
		if (costumPromtLine) {
			try {
				while (message.length() < costumPormtLineMessage.length()) {
					message += " ";
				}
				BungeeCord.getInstance().getConsoleReader().resetPromptLine("", "", 0);
				BungeeCord.getInstance().getConsole().sendMessage(message);
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(costumPormtLineMessage, "", 0);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			BungeeCord.getInstance().getConsole().sendMessage(message);
		}
	}
}
