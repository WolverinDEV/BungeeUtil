package dev.wolveringer.bungeeutil.animations.text;

import java.util.ArrayList;
import java.util.List;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import net.md_5.bungee.api.ChatColor;

public class Scroller {
	private int position;
	private List<String> list;
	private String colour = ChatColor.RESET + "";

	public Scroller(String message, int width, int spaceBetween, char colourChar) {
		list = new ArrayList<String>();
		if(ChatColor.stripColor(message).length() < width){
			while (ChatColor.stripColor(message).length() < width)
				message += " ";
		}

		if(width < 1)
			width = 1;
		if(spaceBetween < 0)
			spaceBetween = 0;

		if(colourChar != dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR)
			message = ChatColor.translateAlternateColorCodes(colourChar, message);

		String raw = ChatColor.stripColor(message);
		while (raw.length() < message.length() + spaceBetween){
			raw += " ";
		}
		String msg = message;
		while (msg.length() <= message.length() + spaceBetween){
			msg += " ";
		}
		String color = "";
		for(int i = 0;i < raw.length();i++){
			while (i - 1 >= 0 && msg.charAt(i - 1) == dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR){
				ChatColor g = ChatColor.getByChar(msg.charAt(i));
				if(ChatColorUtils.isColor(g)) //WORK?
					color = g + "";
				else
					color += g;
				i++;
			}
			while (msg.charAt(i % msg.length()) == dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR){
				ChatColor g = ChatColor.getByChar(msg.charAt((i % msg.length()) + 1));
				if(ChatColorUtils.isColor(g)) //WORK?
					color = g + "";
				else
					color += g;
				i += 2;
			}
			list.add(color + StringUntils.subStringWithoutChatcolors(msg, i > msg.length() ? msg.length() : i, i + width > msg.length() ? msg.length() : i + width) + StringUntils.subStringWithoutChatcolors(msg, 0, i + width > msg.length() ? (i + width) - msg.length() : 0));
		}
	}

	public String next() {
		return list.get(position++ % list.size()).substring(0);
	}

	@Override
	public String toString() {
		return "Scroller [position=" + position + ", list=" + list + ", colour=" + colour + "]";
	}
}