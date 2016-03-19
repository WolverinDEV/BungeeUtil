package dev.wolveringer.BungeeUtil;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RamStatistics {
	@AllArgsConstructor
	public static class RamStatistic {
		private RamStatistics handle;
		@Getter
		private long timestamp;
		@Getter
		private long usedMemory;
		@Getter
		private long reservedMemory;
		@Getter
		private long maxMemory;
		
		public RamStatistic getPreviousStatistic(int time,TimeUnit untit){
			return handle.getPreviousStatistic(this,time,untit);
		}
	}
	private LinkedList<RamStatistic> last = new LinkedList<RamStatistic>();
	private Runtime runtime;
	private ScheduledTask task;
	
	public RamStatistics() {
		runtime = Runtime.getRuntime();
	}
	
	public void start(){
		if(task == null){
			task = BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
				@Override
				public void run() {
					while (true) {
						logCurruntRam();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
			});
		}
	}
	
	protected RamStatistic getPreviousStatistic(RamStatistic currunt){
		int index = last.indexOf(currunt);
		if(index == -1)
			return null;
		if(index-1<0)
			return null;
		return last.get(index);
	}
	
	protected RamStatistic getPreviousStatistic(RamStatistic currunt, int time, TimeUnit untit) {
		int index = last.indexOf(currunt);
		if(index == -1)
			return null;
		index = index-1;
		while (index>0) {
			if(last.get(index).timestamp<currunt.timestamp-untit.toMillis(time))
				return last.get(index);
			index--;
		}
		return null;
	}

	
	public void stop(){
		if(task != null)
			task.cancel();
		task = null;
	}
	
	private void logCurruntRam(){
		last.add(new RamStatistic(this,System.currentTimeMillis(),runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory(), runtime.maxMemory()));
		recalculate();
	}
	
	private void recalculate(){
		while (last.size()> 60*60*5) { //Log for 5 Houers
			last.pollFirst();
		}
	}
	
	public RamStatistic getLastState(){
		return last.getLast();
	}
}
