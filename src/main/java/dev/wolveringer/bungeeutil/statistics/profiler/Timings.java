package dev.wolveringer.bungeeutil.statistics.profiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang3.ArrayUtils;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.item.MultiClickItemStack;

public class Timings {
	private LinkedList<Long> times = new LinkedList<Long>();
	private LinkedList<Long> period_times = new LinkedList<Long>();
	private Long start = -1L;

	@SuppressWarnings("unused")
	private Long last = 0L;

	private String name;
	@SuppressWarnings("unused")
	private MethodProfiler profiler;
	private ItemStack item;

	public Timings(String key, MethodProfiler p) {
		this.name = key;
		this.profiler = p;
		for(int i = 0;i < 100;i++){
			this.period_times.add(0L);
		}
		this.item = new MultiClickItemStack(Material.CAKE);
		this.rebuild();
	}

	synchronized void addTiming(long time) {
		this.times.add(time);
		this.recalculate();
	}

	private ArrayList<String> addVertical(ArrayList<String> base, String... obj) {
		for(int i = 0;i < obj.length;i++){
			String t = obj[i];
			base.set(i, base.get(i) + t);
		}
		return base;
	}

	private String[] createColum(long min, long max, int steps, long var) {
		long d = max - min;
		double count_step = d / steps;
		String[] out = new String[steps];
		for(int i = steps - 1;i >= 0;i--){
			if(count_step * i <= var) {
				if(count_step * i + count_step * 0.5D <= var) {
					out[i] = ChatColorUtils.COLOR_CHAR+"aX";//""+ChatColorUtils.COLOR_CHAR+"a▅";
				}
				else {
					out[i] = ChatColorUtils.COLOR_CHAR+"aX";//ChatColorUtils.COLOR_CHAR+"a█";
				}
			}
			else {
				out[i] = ChatColorUtils.COLOR_CHAR+"0X";//ChatColorUtils.COLOR_CHAR+"0█";
			}
		}
		ArrayUtils.reverse(out);
		return out;
		//PMINGLIU
	}

	private Long durchschnitt(LinkedList<Long> zahlen) {
		if(zahlen.size() == 0) {
			return 0L;
		}
		Long ges = 0L;
		Iterator<Long> i = zahlen.iterator();
		while (i.hasNext()){
			ges += i.next();
		}
		return ges / zahlen.size();
	}

	private String fromat(double d) {
		String out = Profiler.TIME_FORMAT.format(d).replaceAll(",", ".");
		if(out.indexOf(".") != -1) {
			out = out.substring(0, out.indexOf(".")) + ChatColorUtils.COLOR_CHAR+"c" + out.substring(out.indexOf("."), out.length());
		}
		return out;
	}

	public Long getAverageScore() {
		Long all = 0L;
		ArrayList<Long> t = new ArrayList<>(this.period_times);
		for(Long s : t) {
			all += s;
		}
		return all / t.size();
	}

	private Long getHighestValue(Long[] in) {
		Long high = Long.MIN_VALUE;
		for(Long l : in) {
			if(l > high) {
				high = l;
			}
		}
		return high;
	}

	public ItemStack getItemStack() {
		return this.item;
	}

	public Long getLastTiming() {
		if(this.times.size() == 0) {
			return -1L;
		}
		return this.times.get(this.times.size() - 1);
	}

	public String getName() {
		return this.name;
	}

	public Long[] getSmalTimings() {
		return this.times.toArray(new Long[0]);
	}

	public Long[] getTimings() {
		return this.period_times.toArray(new Long[0]);
	}

	public void rebuild() {
		int steps = 10;
		this.item.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR+"bTiming: "+ChatColorUtils.COLOR_CHAR+"b" + this.getName());

		Long max = this.getHighestValue(this.getTimings()) + 10;
		Long min = 0L;
		Long d = max - min;

		//INIT ARRAYLIST
		ArrayList<String> out = new ArrayList<String>();
		for(int i = 0;i < steps;i++) {
			out.add("");
		}

		//COLLUM NUMBERS
		ArrayList<String> a = new ArrayList<String>();
		double count_step = d / steps;
		for(int i = 0;i < steps;i++) {
			a.add(ChatColorUtils.COLOR_CHAR+"c" + this.fromat(count_step * i) + " ms"+ChatColorUtils.COLOR_CHAR+"7: ");
		}
		String[] var1 = a.toArray(new String[a.size()]);
		ArrayUtils.reverse(var1);
		this.addVertical(out, var1);

		for(int i = 50;i < 100;i++){
			this.addVertical(out, this.createColum(0, max, steps, this.getTimings()[i]));
		}
		this.item.getItemMeta().setLore(out);
	}

	private synchronized void recalculate() {
		if(this.times.size() >= 100){
			Long l = this.durchschnitt(this.times);
			this.period_times.add(l);
			if(this.period_times.size() > 100) {
				this.period_times.pollFirst();
			}
			this.times.clear();
		}
	}

	public void resetTimings() {
		this.times.clear();
		this.period_times.clear();
		for(int i = 0;i < 100;i++){
			this.period_times.add(0L);
		}
		this.rebuild();
	}

	public synchronized void start() {
		this.start = System.nanoTime();
	}

	public synchronized void stop() {
		if(this.start == -1L) {
			return;
		}
		this.addTiming(System.nanoTime() - this.start);
		this.start = -1L;
		this.last = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "Timings@" + System.identityHashCode(this) + "{timings=" + this.period_times + "}";
	}
}
