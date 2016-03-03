package dev.wolveringer.profiler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.item.MultiClickItemStack;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.util.apache.ArrayUtils;

public class Timings {
	private LinkedList<Long> times = new LinkedList<Long>();
	private LinkedList<Long> period_times = new LinkedList<Long>();
	private Long start = -1L;

	private Long last = 0L;

	private String name;
	private MethodProfiler profiler;
	private ItemStack item;

	public Timings(String key, MethodProfiler p) {
		this.name = key;
		this.profiler = p;
		for(int i = 0;i < 100;i++){
			period_times.add(0L);
		}
		item = new MultiClickItemStack(Material.CAKE);
		rebuild();
	}

	public void start() {
		start = System.nanoTime();
	}

	public void stop() {
		if(start == -1L)
			return;
		addTiming(System.nanoTime() - start);
		start = -1L;
		last = System.currentTimeMillis();
	}

	synchronized void addTiming(long time) {
		times.add(time);
		recalculate();
	}

	public Long[] getTimings() {
		return period_times.toArray(new Long[0]);
	}

	public Long[] getSmalTimings() {
		return times.toArray(new Long[0]);
	}

	public Long getAverageScore() {
		Long all = 0L;
		ArrayList<Long> t = new ArrayList<>(period_times);
		for(Long s : t)
			all += s;
		return all / t.size();
	}

	public Long getLastTiming() {
		if(times.size() == 0)
			return -1L;
		return times.get(times.size() - 1);
	}

	private void recalculate() {
		if(times.size() >= 100){
			Long l = durchschnitt(times);
			period_times.add(l);
			if(period_times.size() > 100)
				period_times.pollFirst();
			times.clear();
		}
	}

	@Override
	public String toString() {
		return "Timings@" + System.identityHashCode(this) + "{timings=" + period_times + "}";
	}

	private Long durchschnitt(LinkedList<Long> zahlen) {
		if(zahlen.size() == 0)
			return 0L;
		Long ges = 0L;
		Iterator<Long> i = zahlen.iterator();
		while (i.hasNext()){
			ges += i.next();
		}
		return (ges / zahlen.size());
	}

	public String getName() {
		return name;
	}

	public ItemStack getItemStack() {
		return item;
	}

	public void rebuild() {
		int steps = 10;
		item.getItemMeta().setDisplayName("§bTiming: §b" + getName());

		Long max = getHighestValue(getTimings()) + 10;
		Long min = 0L;
		Long d = max - min;

		//INIT ARRAYLIST
		ArrayList<String> out = new ArrayList<String>();
		for(int i = 0;i < steps;i++)
			out.add("");

		//COLLUM NUMBERS
		ArrayList<String> a = new ArrayList<String>();
		double count_step = d / steps;
		for(int i = 0;i < steps;i++)
			a.add("§c" + fromat(count_step * i) + " ms§7: ");
		String[] var1 = a.toArray(new String[a.size()]);
		ArrayUtils.reverse(var1);
		addVertical(out, var1);

		for(int i = 50;i < 100;i++){
			addVertical(out, createColum(0, max, steps, getTimings()[i]));
		}
		item.getItemMeta().setLore(out);
	}

	private String fromat(double d) {
		String out = Profiler.TIME_FORMAT.format(d).replaceAll(",", ".");
		if(out.indexOf(".") != -1)
			out = out.substring(0, out.indexOf(".")) + "§c" + out.substring(out.indexOf("."), out.length());
		return out;
	}

	private Long getHighestValue(Long[] in) {
		Long high = Long.MIN_VALUE;
		for(Long l : in)
			if(l > high)
				high = l;
		return high;
	}

	private String[] createColum(long min, long max, int steps, long var) {
		long d = max - min;
		double count_step = d / steps;
		String[] out = new String[steps];
		for(int i = steps - 1;i >= 0;i--){
			if(count_step * i <= var)
				if(((count_step * i) + count_step * 0.5D) <= var)
					out[i] = "§a█";
				else
					out[i] = "§a▄";
			else
				out[i] = "§0█";
		}
		ArrayUtils.reverse(out);
		return out;
		//PMINGLIU
	}

	private ArrayList<String> addVertical(ArrayList<String> base, String... obj) {
		for(int i = 0;i < obj.length;i++){
			String t = obj[i];
			base.set(i, base.get(i) + t);
		}
		return base;
	}

	public void resetTimings() {
		times.clear();
		period_times.clear();
		for(int i = 0;i < 100;i++){
			period_times.add(0L);
		}
		rebuild();
	}
}
