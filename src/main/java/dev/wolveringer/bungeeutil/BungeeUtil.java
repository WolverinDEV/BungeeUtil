package dev.wolveringer.bungeeutil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import static dev.wolveringer.bungeeutil.i18n.tr;
import dev.wolveringer.bungeeutil.chat.AnsiColorFormater;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.commands.BungeeTimings;
import dev.wolveringer.bungeeutil.injector.InjectFiles;
import dev.wolveringer.bungeeutil.listener.InventoryResetListener;
import dev.wolveringer.bungeeutil.netty.ChannelInizializer;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.player.ProxiedPlayerUserConnection;
import dev.wolveringer.bungeeutil.player.connection.IIInitialHandler;
import dev.wolveringer.bungeeutil.statistics.RamStatistics;
import dev.wolveringer.bungeeutil.terminal.RamStatisticsPainter;
import dev.wolveringer.bungeeutil.terminal.TerminalListener;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeUtil {
	@Getter
	private static Plugin pluginInstance;
	@Getter
	private static BungeeUtil instance;

	public static BungeeUtil createInstance(Plugin plugin){
		if(instance == null){
			if(System.getProperty("bungeetil.instance") == null){
				System.setProperty("bungeetil.instance", "WolverinDEV");
			} else {
				throw new NullPointerException("BungeeUtil have alredy an instance!");
			}
			pluginInstance = plugin;
			instance = new BungeeUtil();
		}
		return instance;
	}

	public static void debug(Exception e) {
		debug(e, "An error happed. "+e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.'))+" message -> " + e.getMessage());
	}
	public static void debug(Exception e, String otherMessage) {
		if (pluginInstance == null || Configuration.isDebugEnabled()) {
			e.printStackTrace(); // Debug isf this not a plugin
		} else {
			System.out.println(otherMessage);
		}
	}
	public static void debug(String string) {
		if (pluginInstance == null || Configuration.isDebugEnabled())
		 {
			System.out.println(string); // Debug if this not a plugin
		}
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

	public void disable(){
		this.active = false;
		this.sendMessage(ChatColor.GREEN + "Thank you for using BungeeUntil");
	}

	public void displayedSleep(int millis) {
		if (!Configuration.isFastBoot()) {
			int procent = millis / 100;
			int now = 0;
			while (now * procent < millis) {
				this.setPromt(this.costumPormtLineRawMessage + " [" + now + "%]");
				try {
					Thread.sleep(procent);
				}
				catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				now++;
			}
			this.setPromt(this.costumPormtLineRawMessage + " [100%]");
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int inject(){
		if(this.state.testBit(2)) {
			throw new RuntimeException("Alredy injecting plugin.");
		}
		if(this.state.testBit(3)) {
			throw new RuntimeException("BungeeUtil alredy injected!");
		}
		this.state.setBit(2);
		try{
			if(this.isInjected()) {
				return -1;
			}
			this.setInformation("§aInjecting patches");
			this.sendMessage("§aInjecting patches");
			switch (InjectFiles.inject()) {
			case -1:
				return -1;
			case 0:
				this.sendMessage(ChatColor.RED+"A fatal error has blocked in the injection of BungeeUtil.");
				this.sendMessage(ChatColor.RED+"Disable BungeeUtil");
				this.setInformation(null);
				return 2;
			case 1:
				this.sendMessage(ChatColor.GREEN+"BungeeUtil injection successful. Need a BungeeCord restart.");
				this.setInformation(null);
				this.state.setBit(3);
				return 0;
		};
		}finally{
			this.state.clearBit(2);
		}
		return 2;
	}

	public boolean isInjected(){
		return InjectFiles.isInjected() || this.state.testBit(3);
	}


	public void load(){
		if(this.state.testBit(0) || this.active) {
			throw new RuntimeException("Alredy loading plugin.");
		}
		if(this.state.testBit(1)) {
			throw new RuntimeException("BungeeUtil alredy loaded!");
		}
		if(!this.isInjected() && !this.state.testBit(3)) {
			throw new RuntimeException("BungeeUtil isnt injected!");
		}
		this.state.setBit(0);
		try{
			this.active =  true;
			if(!Configuration.isQuietBoot()){
				this.setInformation("§aLoading configuration.");
				this.sendMessage("§aLoading configuration.");
			}
			Configuration.init();

			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aValidate configuration.");
				this.sendMessage("§aValidate configuration.");
			}

			if(Configuration.getHandleExceptionAction() == null){
				this.sendMessage("§cCant find the NetworkExceptionAction for "+Configuration.getConfig().getString("network.exception")+". §6Using default ("+HandleErrorAction.DISCONNECT+")");
				Configuration.getConfig().set("network.exception", HandleErrorAction.DISCONNECT.name().toUpperCase());
			}
			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aConfiguration valid.");
				this.sendMessage("§aConfiguration valid.");
				this.displayedSleep(500);
				this.setInformation("§aLoading AsyncCatcher");
				this.sendMessage("§aLoading AsyncCatcher");
			}

			AsyncCatcher.init();
			AsyncCatcher.disable(pluginInstance);
			AsyncCatcher.catchOp("Async test failed");

			TerminalListener.setInstance(new TerminalListener());

			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aAsyncCatcher successfull loaded");
				this.sendMessage("§aAsyncCatcher successfull loaded");
				this.displayedSleep(500);
				this.setInformation("§aLoading ChannelInizializer");
				this.sendMessage("§aLoading ChannelInizializer");
			}

			boolean flag = true;
			try {
				ChannelInizializer.init();
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
				this.sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cError while loading ProtocolLIB " + ChatColorUtils.COLOR_CHAR + "4Code: 002");
				this.sendMessage(ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil" + ChatColorUtils.COLOR_CHAR + "7] " + ChatColorUtils.COLOR_CHAR + "cDisable ProtocolLIB");
			}

			if(!Configuration.isQuietBoot()) {
				this.displayedSleep(500);
			}
			if(!flag){
				if(!Configuration.isQuietBoot()) {
					this.setInformation("§cAn error happend while loading aChannelInizializer.");
				}
				this.sendMessage("§cAn error happend while loading aChannelInizializer.");
				if(!Configuration.isQuietBoot()) {
					this.displayedSleep(500);
				}
				return;
			}

			if(!Configuration.isQuietBoot()){
				this.setInformation("§aChannelInizializer successfull loaded.");
				this.sendMessage("§aChannelInizializer successfull loaded.");
				this.displayedSleep(500);
				this.setInformation("§aLoading player class");
				this.sendMessage("§aLoading player class");
			}

			IIInitialHandler.init(ProxiedPlayerUserConnection.class);

			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aPlayer class loaded");
				this.sendMessage("§aPlayer class loaded");

				this.displayedSleep(500);
				this.setInformation("§aRegister commands and scheduler");
				this.sendMessage("§aRegister commands and scheduler");
			}

			BungeeCord.getInstance().getPluginManager().registerListener(pluginInstance, new InventoryResetListener());
			BungeeCord.getInstance().getPluginManager().registerCommand(pluginInstance, new BungeeTimings());
			BungeeCord.getInstance().getScheduler().runAsync(pluginInstance, ()->{
				if(!Configuration.isQuietBoot()) {
					this.sendMessage("§eSystem.gc() -> Enabled: " + Configuration.isGCEnabled());
				}
				while (Configuration.isGCEnabled()) {
					try {
						Thread.sleep(60 * 1000);
					}
					catch (InterruptedException e) { }
					if (!this.active) {
						return;
					}
					System.runFinalization();
					System.gc();
				}
			});
			if (Configuration.ramStatistics()) {
				this.ramStatistiks = new RamStatistics();
				this.ramStatistiks.start();
				RamStatisticsPainter tsp = new RamStatisticsPainter();
				TerminalListener.getInstance().getListener().add(tsp);
				BungeeCord.getInstance().getScheduler().runAsync(pluginInstance, tsp);
				BungeeCord.getInstance().getPluginManager().registerCommand(pluginInstance, new dev.wolveringer.bungeeutil.commands.RamStatistics());
			}

			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aRegister packets");
				this.sendMessage("§aRegister packets");
			}
			Packet.countPackets();

			if(!Configuration.isQuietBoot()){
				this.displayedSleep(500);
				this.setInformation("§aPackets registered");
				this.sendMessage("§aPackets registered ("+Packet.countPackets()+" packets are registered)");
			}
			this.sendMessage(ChatColor.GREEN+"BungeeUtil successfully loaded!");
			this.displayedSleep(500);
			this.setInformation(null);
			this.state.setBit(1);
			this.active = true;
		}catch(Exception e){
			this.active = false;
			throw e;
		}finally{
			this.state.clearBit(0);
		}
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(String message) {
		if (!message.startsWith(ChatColorUtils.PREFIX + " ")) {
			message = ChatColorUtils.PREFIX + " " + message;
		}
		message = "\r" + this.date_format.format(new Date()) + " " + message;
		if (this.costumPromtLine) {
			try {
				while (message.length() < this.costumPormtLineMessage.length()) {
					message += " ";
				}
				BungeeCord.getInstance().getConsoleReader().resetPromptLine("", "", 0);
				BungeeCord.getInstance().getConsole().sendMessage(message);
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(this.costumPormtLineMessage, "", 0);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			BungeeCord.getInstance().getConsole().sendMessage(message);
		}
	}

	public void setInformation(String info) {
		if ("".equalsIgnoreCase(info) || info == null) {
			this.costumPromtLine = false;
		} else {
			this.costumPromtLine = true;
		}
		this.costumPormtLineRawMessage = info;
		this.setPromt(info);
	}

	private void setPromt(String info) {
		try {
			if (this.costumPromtLine){
				try{
					BungeeCord.getInstance().getConsoleReader().resetPromptLine(this.costumPormtLineMessage = AnsiColorFormater.getFormater().format(ChatColorUtils.COLOR_CHAR + "aLoading BungeeUtil >> " + ChatColorUtils.COLOR_CHAR + "b" + info), "", 0);
				}catch(Exception e){
					try{
						BungeeCord.getInstance().getConsoleReader().resetPromptLine("", "", 0);
						BungeeCord.getInstance().getConsoleReader().resetPromptLine(this.costumPormtLineMessage = AnsiColorFormater.getFormater().format(ChatColorUtils.COLOR_CHAR + "aLoading BungeeUtil >> " + ChatColorUtils.COLOR_CHAR + "b" + info), "", 0);
					}catch(Exception e2){
						debug(e2);
					}
				}
			}
			else{
				BungeeCord.getInstance().getConsoleReader().resetPromptLine(">", "", 1);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
