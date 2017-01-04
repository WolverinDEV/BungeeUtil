package dev.wolveringer.bungeeutil.chat;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;

public class ChatColorUtils {
	private final static Map<Character, ChatColor> BY_CHAR = Maps.newHashMap();
	public static final char COLOR_CHAR = '§';// \u00A7
	public static final String PREFIX = ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "eBungeeUntil"
			+ ChatColorUtils.COLOR_CHAR + "7]";
	private static final Pattern STRIP_FORMAT_PATTERN = Pattern
			.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[A-FK-OR]");
	private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9]");
	static {
		for (ChatColor color : ChatColor.values()) {
			BY_CHAR.put(color.toString().charAt(1), color);
		}
	}

	/**
	 * Gets the color represented by the specified color code
	 *
	 * @param code
	 *            Code to check
	 * @return Associative {@link org.bukkit.ChatColor} with the given code, or
	 *         null if it doesn't exist
	 */
	public static ChatColor getByChar(char code) {
		return BY_CHAR.get(code);
	}

	/**
	 * Gets the color represented by the specified color code
	 *
	 * @param code
	 *            Code to check
	 * @return Associative {@link org.bukkit.ChatColor} with the given code, or
	 *         null if it doesn't exist
	 */
	public static ChatColor getByChar(String code) {
		return BY_CHAR.get(code.charAt(0));
	}

	/**
	 * Gets the ChatColors used at the end of the given input string.
	 *
	 * @param input
	 *            Input string to retrieve the colors from.
	 * @return Any remaining ChatColors to pass onto the next line.
	 */
	public static String getLastColors(String input) {
		String result = "";
		int length = input.length();
		for (int index = length - 1; index > -1; index--) {
			char section = input.charAt(index);
			if (section == COLOR_CHAR && index < length - 1) {
				char c = input.charAt(index + 1);
				ChatColor color = getByChar(c);
				if (color != null) {
					result = color.toString() + result;
					if (isColor(color) || color.equals(ChatColor.RESET)) {
						break;
					}
				}
			}
		}
		return result;
	}

	public static boolean isColor(ChatColor color) {
		if (color == null) {
			return false;
		}
		return !"".equalsIgnoreCase(stripFormat(color.toString()));
	}

	/**
	 * Strips the given message of all color codes
	 *
	 * @param input
	 *            String to strip of color
	 * @return A copy of the input string, without any coloring
	 */
	public static String stripColor(final String input) {
		if (input == null) {
			return null;
		}
		return STRIP_COLOR_PATTERN.matcher(STRIP_FORMAT_PATTERN.matcher(input).replaceAll("")).replaceAll("");
	}

	public static String stripFormat(final String input) {
		if (input == null) {
			return null;
		}
		return STRIP_FORMAT_PATTERN.matcher(input).replaceAll("");
	}

	/**
	 * Translates a string using an alternate color code character into a string
	 * that uses the internal ChatColor.COLOR_CODE color code character. The
	 * alternate color code character will only be replaced if it is immediately
	 * followed by 0-9, A-F, a-f, K-O, k-o, R or r.
	 *
	 * @param altColorChar
	 *            The alternate color code character to replace. Ex: &
	 * @param textToTranslate
	 *            Text containing the alternate color code character.
	 * @return Text containing the ChatColor.COLOR_CODE color code character.
	 */
	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
				b[i] = ChatColorUtils.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}
}