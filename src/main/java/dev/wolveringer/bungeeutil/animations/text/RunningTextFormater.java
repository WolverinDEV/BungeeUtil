package dev.wolveringer.bungeeutil.animations.text;

import java.util.concurrent.TimeUnit;

import dev.wolveringer.bungeeutil.BungeeUtil;
import net.md_5.bungee.BungeeCord;

public abstract class RunningTextFormater {
	private int PID = -1;

	private TextFormater format;

	public RunningTextFormater(String in) {
		this.format = new TextFormater(in);
	}

	public TextFormater getFormater() {
		return this.format;
	}

	public void start(){
		if(this.PID == -1){
			BungeeCord.getInstance().getScheduler().schedule(BungeeUtil.getPluginInstance(), () -> RunningTextFormater.this.update(RunningTextFormater.this.format.getNextString()), 0, this.format.getTick(), TimeUnit.MILLISECONDS);
		}
	}
	public void stop() {
		if(this.PID != -1){
			BungeeCord.getInstance().getScheduler().cancel(this.PID);
			this.PID = -1;
		}
	}

	public abstract void update(String newText);
}
