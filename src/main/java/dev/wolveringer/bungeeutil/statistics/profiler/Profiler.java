package dev.wolveringer.bungeeutil.statistics.profiler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.hastebin.HastebinPost;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.ScrolingInventory;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.plugin.Main;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTTagCompound;
import dev.wolveringer.nbt.NBTTagList;
import dev.wolveringer.nbt.NBTTagLong;
import net.md_5.bungee.BungeeCord;

public class Profiler {
	protected static final DecimalFormat TIME_FORMAT = new DecimalFormat("#.000000000");

	public static void setEnabled(boolean enabled) {
		Configuration.setTimingsActive(enabled);
	}

	private static int cachedBoolean = -1;
	public static boolean isEnabled() {
		if(cachedBoolean == -1)
			cachedBoolean = Configuration.isTimingsActive() ? 1 : 0;
		return cachedBoolean == 1;
	}

	public static void reset() {
		for(Profiler p : getProfilers())
			p.resetTimings();
	}

	private static ArrayList<Profiler> profilers = new ArrayList<Profiler>();

	public static ArrayList<Profiler> getProfilers() {
		return profilers;
	}

	public static String pushToHastebin() {
		long start = System.nanoTime();
		HastebinPost post = new HastebinPost();
		post.addLine("Timings for Bungeecord-Server  : " + getHostAdress());
		post.addLine("General Information:");
		post.addLine("  Bungeecord version           : " + BungeeCord.getInstance().getVersion());
		post.addLine("  Bungeecord protocol version  : " + BungeeCord.getInstance().getProtocolVersion());
		post.addLine("  Bungeecord game version      : " + BungeeCord.getInstance().getGameVersion());
		post.addLine("  BungeeUtil author            : " + Main.getMain().getDescription().getAuthor());
		if(Main.getMain().updater.isNewstVersion())
			post.addLine("  BungeeUtil version           : " + Main.getMain().getDescription().getVersion() + " (up to date)");
		else
			post.addLine("  BungeeUtil version      	 : " + Main.getMain().getDescription().getVersion() + " (new version avariable: " + Main.getMain().updater.getNewestVersion() + ")");
		post.addLine("  BungeeUtil ByteBuffType      : " + Configuration.getByteBuffType().toUpperCase());
		post.addLine("");
		post.addLine("Memory:");

		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;

		post.addLine("  Reserved Used Memory     : " + (runtime.totalMemory() - runtime.freeMemory()) / mb + "MB");
		post.addLine("  Reserved Free Memory     : " + runtime.freeMemory() / mb + "MB");
		post.addLine("  Reserved Memory          : " + runtime.totalMemory() / mb + "MB");
		post.addLine("  Allowed Reservable Memory: " + runtime.maxMemory() / mb + "MB");

		post.addLine("");
		post.addLine("Profiler: (All times in NanoSeconds!)");
		for(Profiler p : getProfilers()){
			post.addLine("  " + p.getName() + ":");
			for(MethodProfiler m : p.getProfiles().values()){
				post.addLine("    Method \"" + m.getName() + "\":");
				int max = 0;
				for(Timings s : m.getTimings().values()){
					if(s.getName().length() > max)
						max = s.getName().length();
				}
				max+=1;
				for(Timings s : m.getTimings().values()){
					post.addLine("      Timing \"" + format(s.getName()+"\"",max) + ": " + format(s));
				}
			}
		}
		post.addLine("");
		post.addLine("Details: (Base64 NBTTag Structure)");
		String s = details();
		for(String x : s.split("(?<=\\G.{100})"))
			post.addLine(x);
		post.addLine("");
		long end = System.nanoTime() - start;
		post.addLine("File created in " + TIME_FORMAT.format(end).replaceAll(",", ".") + " NanoSeconds (" + ((int) (end / 1000000)) + " MilliSeconds).");
		return post.getTextUrl();
	}

	private static String format(String in, int length) {
		while (in.length() < length){
			in += " ";
		}
		return in;
	}

	private static String details() {
		NBTTagCompound nbt = new NBTTagCompound();
		for(Profiler p : getProfilers()){
			NBTTagCompound profiles = new NBTTagCompound();
			for(MethodProfiler m : p.getProfiles().values()){
				NBTTagCompound methode = new NBTTagCompound();
				for(Timings s : m.getTimings().values()){
					NBTTagCompound timings = new NBTTagCompound();
					timings.setString("name", s.getName());
					timings.setLong("AverageScore", s.getAverageScore());
					NBTTagList period_timings = new NBTTagList();
					for(Long l : s.getTimings())
						period_timings.add(new NBTTagLong(l));
					NBTTagList times = new NBTTagList();
					for(Long l : s.getSmalTimings())
						times.add(new NBTTagLong(l));
					timings.set("period_times", period_timings);
					timings.set("times", times);
					methode.set(s.getName(), timings);
				}
				profiles.set(m.getName(), methode);
			}
			nbt.set(p.getName(), profiles);
		}
		try{
			return NBTCompressedStreamTools.toString(nbt);
		}catch (Exception ex){
			ex.printStackTrace();
			return "DetailParadiseError";
		}
	}

	private static String format(Timings in) {
		String out = TIME_FORMAT.format(in.getAverageScore()).replaceAll(",", ".") + " Last 20 Timings: ";
		out += " [";
		Long[] x = in.getTimings();
		for(int i = x.length - 1;i > (x.length - 20 > 0 ? x.length - 20 : 0);i--)
			out += " ," + TIME_FORMAT.format(x[i]).replaceAll(",", ".") + "";
		return out.replaceFirst("\\[ ,", "\\[") + "]";
	}

	private static String getHostAdress() {
		try{
			return InetAddress.getLocalHost().getHostAddress();
		}catch (UnknownHostException ex){
			return "underknown";
		}
	}

	@SuppressWarnings("serial")
	private HashMap<String, MethodProfiler> profiles = new HashMap<String, MethodProfiler>() {
		public MethodProfiler get(Object key) {
			if(super.get(key) == null)
				super.put((String) key, new MethodProfiler(Profiler.this, (String) key));
			return super.get(key);
		};
	};

	public static final Profiler decoder_timings = new Profiler("timings.decoder");
	public static final Profiler encoder_timings = new Profiler("timings.encoder");
	public static final Profiler profiler = new Profiler("timings.profiler");
	public static final Profiler packet_handle = new Profiler("timings.handle");
	
	String name;
	private ScrolingInventory inv;

	public Profiler(String name) {
		profilers.add(this);
		this.name = name;
		String n = ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"aTimings "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7("+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"5"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"l" + getName() + ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7)";
		inv = new ScrolingInventory(4, n);
	}

	public void start(String name) {
		if(isEnabled())
			profiles.get(getStackMethodeName()).start(name);
	}

	public void stop(String name) {
		if(isEnabled())
			profiles.get(getStackMethodeName()).stop(name);
	}

	public Long getLastTiming(String name) {
		if(isEnabled())
			return profiles.get(getStackMethodeName()).getLastTiming(name);
		else
			return -1L;
	}

	public Long getLastTiming(String method, String name) {
		if(isEnabled())
			return profiles.get(method).getLastTiming(name);
		else
			return -1L;
	}

	public HashMap<String, MethodProfiler> getProfiles() {
		return profiles;
	}

	private String getStackMethodeName() {
		StackTraceElement[] e = Thread.currentThread().getStackTrace();
		for(StackTraceElement et : e){
			if(!et.toString().contains("Profiler") && !et.toString().contains("java.lang"))
				return et.getMethodName();
		}
		return "null";
	}

	protected Inventory getInventory() {
		return inv;
	}

	protected void updateInventory() {
		if(!isEnabled())
			return;
		profiler.start("update");
		inv.disableUpdate();
		inv.clear();
		for(MethodProfiler p : getProfiles().values()){
			p.updateInventory();
			inv.addItem(buildMethodProfiler(p));
		}
		inv.enableUpdate();
		profiler.stop("update");
	}

	private ItemStack buildMethodProfiler(final MethodProfiler profile) {
		ItemStack is = new ItemStack(Material.COMPASS) {
			@Override
			public void click(Click p) {
				p.getPlayer().openInventory(profile.getInventory());
			}
		};
		is.getItemMeta().setDisplayName(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"bMethode: "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"b" + profile.getName());
		return is;
	}

	public void resetTimings() {
		for(MethodProfiler p : getProfiles().values()){
			p.resetTimings();
		}
		updateInventory();
	}

	public String getName() {
		return name;
	}
}
