package dev.wolveringer.bungeeutil.statistics;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.terminal.graph.TerminalGraph;
import dev.wolveringer.terminal.graph.TerminalGraph.Graph;
import dev.wolveringer.terminal.graph.TerminalGraph.Point;
import dev.wolveringer.terminal.string.ColoredChar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.scheduler.ScheduledTask;

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
			return this.handle.getPreviousStatistic(this,time,untit);
		}
	}
	private LinkedList<RamStatistic> last = new LinkedList<RamStatistic>();
	private Runtime runtime;
	private ScheduledTask task;

	public RamStatistics() {
		this.runtime = Runtime.getRuntime();
	}

	public TerminalGraph createGrath(int seconds,int divisor){
		TerminalGraph base = new TerminalGraph();

		Graph graphUsedMemory = new Graph();
		graphUsedMemory.setReturnZeroByNoData(true);
		graphUsedMemory.setCharacter(new ColoredChar('#'));
		graphUsedMemory.getCharacter().setColor(ChatColor.GREEN);

		Graph graphAllocatedMemory = new Graph();
		graphAllocatedMemory.setReturnZeroByNoData(true);
		graphAllocatedMemory.setCharacter(new ColoredChar('+'));
		graphAllocatedMemory.getCharacter().setColor(ChatColor.GOLD);

		int max = 0;

		for(RamStatistic stats : this.last){
			if(stats.getTimestamp() > System.currentTimeMillis()-seconds*1000){
				graphUsedMemory.addPoint(new Point((int)((System.currentTimeMillis()-stats.getTimestamp())/1000), (int) stats.getUsedMemory()/divisor));
				if(stats.getUsedMemory()/divisor > max) {
					max = (int) stats.getUsedMemory()/divisor;
				}

				graphAllocatedMemory.addPoint(new Point((int)((System.currentTimeMillis()-stats.getTimestamp())/1000), (int) stats.getReservedMemory()/divisor));
				if(stats.getReservedMemory()/divisor > max) {
					max = (int) stats.getReservedMemory()/divisor;
				}
			}
		}

		base.addGraph(graphAllocatedMemory);
		base.addGraph(graphUsedMemory);

		base.setStartX(seconds);
		base.setEndX(0);
		base.setStepX(10);
		base.setStartY(0);
		base.setEndY((int) (max+max*0.1));
		return base;
	}

	public RamStatistic getLastState(){
		return this.last.getLast();
	}

	protected RamStatistic getPreviousStatistic(RamStatistic currunt){
		int index = this.last.indexOf(currunt);
		if(index == -1) {
			return null;
		}
		if(index-1<0) {
			return null;
		}
		return this.last.get(index);
	}


	protected RamStatistic getPreviousStatistic(RamStatistic currunt, int time, TimeUnit untit) {
		int index = this.last.indexOf(currunt);
		if(index == -1) {
			return null;
		}
		index = index-1;
		while (index>0) {
			if(this.last.get(index).timestamp<currunt.timestamp-untit.toMillis(time)) {
				return this.last.get(index);
			}
			index--;
		}
		return null;
	}

	private void logCurruntRam(){
		this.last.add(new RamStatistic(this,System.currentTimeMillis(),this.runtime.totalMemory() - this.runtime.freeMemory(), this.runtime.totalMemory(), this.runtime.maxMemory()));
		this.recalculate();
	}

	private void recalculate(){
		while (this.last.size()> 60*60*5) { //Log for 5 Houers
			this.last.pollFirst();
		}
	}

	public void start(){
		if(this.task == null){
			this.task = BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), () -> {
				while (true) {
					RamStatistics.this.logCurruntRam();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			});
		}
	}

	public void stop(){
		if(this.task != null) {
			this.task.cancel();
		}
		this.task = null;
	}
}
