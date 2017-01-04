package dev.wolveringer.bungeeutil.animations.text;

import java.util.concurrent.TimeUnit;

import dev.wolveringer.bungeeutil.BungeeUtil;
import net.md_5.bungee.BungeeCord;

public abstract class RunningTextFormater {
	public static void main(String[] args) {
		//Starting an scroler animation with the HTML tag <scroller>
		//Needet arguments are:
		//  - width --> The maximung with of the scroler
		//  - space --> Space between repeating
		//  - time --> time per step in ms
		RunningTextFormater scrooler = new RunningTextFormater("<scroller width=10  space=0 time=100>HELL"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"nO THIS IS A"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"a TEST"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"a STRING!</scroller>XX<scroller width=10  space=0 time=150>HELLO THIS IS A TEST STRING!</scroller>XX<scroller width=10  space=0 time=200>HELLO THIS IS A TEST STRING!</scroller>") {
			@Override
			public void update(String newText) {
				//this methode will run when the text is updating
			}
		};
	}
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
