package dev.wolveringer.bungeeutil.statistics.profiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.xml.ws.Holder;

import org.apache.commons.lang3.ArrayUtils;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemBuilder;
import dev.wolveringer.bungeeutil.item.Material;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class Timings {
	private LinkedList<Long> times = new LinkedList<Long>();
	private LinkedList<Long> period_times = new LinkedList<Long>();
	private Long start = -1L;

	@SuppressWarnings("unused")
	private Long last = 0L;

	private String name;
	@SuppressWarnings("unused")
	private MethodProfiler profiler;
	@Getter
	private Item item;

	public Timings(String key, MethodProfiler p) {
		this.name = key;
		this.profiler = p;
		for(int i = 0;i < 100;i++){
			this.period_times.add(0L);
		}
		this.item = ItemBuilder.create(Material.CAKE).build();
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
					out[i] = ChatColor.GREEN+"X";
				}
				else {
					out[i] = ChatColor.GREEN+"X";
				}
			}
			else {
				out[i] = ChatColor.BLACK+"X";
			}
		}
		ArrayUtils.reverse(out);
		return out;
	}

	private Long average(LinkedList<Long> zahlen, boolean countEmpty) {
		if(zahlen.isEmpty()) return 0L;
		
		Stream<Long> stream = zahlen.stream().filter(e -> (countEmpty || e != 0)).parallel();
		long count = stream.count();
		if(count == 0) return 0L;
		
		Holder<BigDecimal> sum = new Holder<>(new BigDecimal(0));
		stream.forEach(e -> sum.value = sum.value.add(new BigDecimal(e)));
		
		return sum.value.divide(new BigDecimal(count), BigDecimal.ROUND_HALF_UP).longValue();
	}

	private String fromat(double d) {
		return Profiler.TIME_FORMAT.format(d);
	}

	public Long getAverageScore() {
		return average(this.period_times, false);
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
		return this.times.toArray(new Long[times.size()]);
	}

	public Long[] getTimings() {
		return this.period_times.toArray(new Long[period_times.size()]);
	}

	public void rebuild() {
		int steps = 10;
		this.item.getItemMeta().setDisplayName(ChatColor.BLUE+"Timing: "+ChatColor.YELLOW + this.getName());

		Long max = this.getHighestValue(this.getTimings()) + 10;
		Long min = 0L;
		Long difference = max - min;

		//INIT ARRAYLIST
		ArrayList<String> out = new ArrayList<String>();
		for(int i = 0;i < steps;i++) {
			out.add("");
		}

		//COLLUM NUMBERS
		ArrayList<String> collums = new ArrayList<String>();
		double count_step = difference / steps;
		for(int i = 0;i < steps;i++) {
			collums.add(ChatColor.RED + this.fromat((count_step * i) / (double) 1000000) + " ms"+ChatColor.GRAY+": ");
		}
		String[] var1 = collums.toArray(new String[collums.size()]);
		ArrayUtils.reverse(var1);
		this.addVertical(out, var1);

		for(int i = 50;i < 100;i++){
			this.addVertical(out, this.createColum(0, max, steps, this.getTimings()[i]));
		}
		this.item.getItemMeta().setLore(out);
	}

	private void recalculate() {
		synchronized (times) {
			if(this.times.size() >= 100){
				this.period_times.add(this.average(this.times, true));
				while(this.period_times.size() > 100) this.period_times.pollFirst();
				this.times.clear();
			}
		}
	}

	public void resetTimings() {
		synchronized (times) {
			this.times.clear();
			this.period_times.clear();
			for(int i = 0;i < 100;i++){
				this.period_times.add(0L);
			}
			this.rebuild();
		}
	}

	public synchronized void start() {
		this.start = System.nanoTime();
	}

	public synchronized void stop() {
		if(this.start == -1L){
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
