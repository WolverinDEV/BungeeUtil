package dev.wolveringer.bungeeutil.animations.text;

import net.md_5.bungee.api.ChatColor;

public class StringUntils {
	public static String subStringWithoutChatcolors(String s,int min,int max){
		String out = "";
		int pos = min;
		int rpos = min;
		while (rpos<max){
			if(s.length()<pos) {
				break;
			}
			while (s.charAt(pos) == ChatColor.COLOR_CHAR){
				if(s.length()<pos) {
					break;
				}
				pos++;
				out += ChatColor.COLOR_CHAR+s.charAt(pos);
				pos++;
			}
			out+=s.charAt(pos);
			pos++;
			rpos++;
		}
		return out;
	}
}
