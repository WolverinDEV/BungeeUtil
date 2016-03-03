package dev.wolveringer.animations.text;

import net.md_5.bungee.api.ChatColor;

public class FormatTest {
	public static void main(String[] args) {
		new scroler().start();
	}
}

class scroler extends Thread {
	TextFormater s = new TextFormater("<scroller width=10  space=0 time=100>HELL§nO THIS IS A§a TEST§a STRING!</scroller>XX<scroller width=10  space=0 time=150>HELLO THIS IS A TEST STRING!</scroller>XX<scroller width=10  space=0 time=200>HELLO THIS IS A TEST STRING!</scroller>");

	@Override
	public void run() {
		while (true){
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println("\"" + ChatColor.stripColor(s.getNextString()) + "\"");
			try{
				Thread.sleep(s.getTick());
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}