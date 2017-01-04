package dev.wolveringer.bungeeutil.animations.inventory;

import java.util.concurrent.TimeUnit;

import dev.wolveringer.bungeeutil.BungeeUtil;
import net.md_5.bungee.BungeeCord;

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

	public void done() {

	}

	@Override
	public void run() {
		if(this.count > this.limit){
			BungeeCord.getInstance().getScheduler().cancel(this.ID);
			this.done();
		}
		this.run(this.count);
		this.count++;
	}

	public abstract void run(int count);

	public void start() {
		this.ID = BungeeCord.getInstance().getScheduler().schedule(BungeeUtil.getPluginInstance(), this, 0, this.repeat_time, this.unit).getId();
	}

	public void stop() {
		BungeeCord.getInstance().getScheduler().cancel(this.ID);
		this.done();
	}
}