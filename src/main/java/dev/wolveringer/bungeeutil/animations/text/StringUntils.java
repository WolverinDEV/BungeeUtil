package dev.wolveringer.bungeeutil.animations.text;

public class StringUntils {
	public static String subStringWithoutChatcolors(String s,int min,int max){
		String out = "";
		int pos = min;
		int rpos = min;
		while (rpos<max){
			if(s.length()<pos) {
				break;
			}
			while (s.charAt(pos) == dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR){
				if(s.length()<pos) {
					break;
				}
				pos++;
				out += ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+""+s.charAt(pos);
				pos++;
			}
			out+=s.charAt(pos);
			pos++;
			rpos++;
		}
		return out;
	}
}
