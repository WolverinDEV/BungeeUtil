package dev.wolveringer.animations.text;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.BungeeUtil.Main;

public abstract class RunningTextFormater {
	private int PID = -1;
	private TextFormater format;

	public RunningTextFormater(String in) {
		format = new TextFormater(in);
	}

	public void start(){
		if(PID == -1){
			BungeeCord.getInstance().getScheduler().schedule(Main.getMain(), new Runnable() {
				@Override
				public void run() {
					update(format.getNextString());
				}
			}, 0, format.getTick(), TimeUnit.MILLISECONDS);
		}
	}

	public void stop() {
		if(PID != -1){
			BungeeCord.getInstance().getScheduler().cancel(PID);
			PID = -1;
		}
	}
	
	public TextFormater getFormater() {
		return this.format;
	}
	public abstract void update(String newText);
	
	public static void main(String[] args) {
		//Starting an scroler animation with the HTML tag <scroller>
		//Needet arguments are:
		//  - width --> The maximung with of the scroler
		//  - space --> Space between repeating
		//  - time --> time per step in ms
		RunningTextFormater scrooler = new RunningTextFormater("<scroller width=10  space=0 time=100>HELL"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"nO THIS IS A"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a TEST"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a STRING!</scroller>XX<scroller width=10  space=0 time=150>HELLO THIS IS A TEST STRING!</scroller>XX<scroller width=10  space=0 time=200>HELLO THIS IS A TEST STRING!</scroller>") {
			@Override
			public void update(String newText) {
				//this methode will run when the text is updating
			}
		};
	}
}
