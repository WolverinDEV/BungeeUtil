package dev.wolveringer.bungeeutil.animations.inventory;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.plugin.Main;

public abstract class LimetedScheduller implements Runnable {
	protected int limit;
	protected int count;
	private int repeat_time;
	private TimeUnit unit;
	private int ID;
	
	public LimetedScheduller(int limit, int repeat_time, TimeUnit unit) {
		this.limit = limit;
		this.repeat_time = repeat_time;
		this.unit = unit;
	}
	
	public LimetedScheduller(int persiod,TimeUnit untim, int repeat_time, TimeUnit unit) {
		this.limit = (int) (untim.toMillis(persiod)/unit.toMillis(repeat_time));
		this.repeat_time = repeat_time;
		this.unit = unit;
	}

	@Override
	public void run() {
		if(count > limit){
			BungeeCord.getInstance().getScheduler().cancel(ID);
			done();
		}
		run(count);
		count++;
	}

	public abstract void run(int count);

	public void done() {

	}

	public void start() {
		ID = BungeeCord.getInstance().getScheduler().schedule(BungeeUtil.getPluginInstance(), this, 0, repeat_time, unit).getId();
	}

	public void stop() {
		BungeeCord.getInstance().getScheduler().cancel(ID);
		done();
	}
}