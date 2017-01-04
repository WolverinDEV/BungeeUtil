package dev.wolveringer.bungeeutil.chat;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.fusesource.jansi.Ansi;

public class AnsiColorFormater {

	public static AnsiColorFormater formater = new AnsiColorFormater();

	public static AnsiColorFormater getFormater() {
		if (formater == null)
			formater = new AnsiColorFormater();
		return formater;
	}

	public static void setFormater(AnsiColorFormater formater) {
		AnsiColorFormater.formater = formater;
	}

	private final Map<String, String> replacements = new HashMap<String, String>();

	public AnsiColorFormater() {
		this.replacements.put(ChatColor.BLACK.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
		this.replacements.put(ChatColor.DARK_BLUE.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
		this.replacements.put(ChatColor.DARK_GREEN.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
		this.replacements.put(ChatColor.DARK_AQUA.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
		this.replacements.put(ChatColor.DARK_RED.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
		this.replacements.put(ChatColor.DARK_PURPLE.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
		this.replacements.put(ChatColor.GOLD.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
		this.replacements.put(ChatColor.GRAY.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
		this.replacements.put(ChatColor.DARK_GRAY.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
		this.replacements.put(ChatColor.BLUE.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
		this.replacements.put(ChatColor.GREEN.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
		this.replacements.put(ChatColor.AQUA.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
		this.replacements.put(ChatColor.RED.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
		this.replacements.put(ChatColor.LIGHT_PURPLE.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
		this.replacements.put(ChatColor.YELLOW.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
		this.replacements.put(ChatColor.WHITE.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
		this.replacements.put(ChatColor.MAGIC.toString(), Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
		this.replacements.put(ChatColor.BOLD.toString(), Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
		this.replacements.put(ChatColor.STRIKETHROUGH.toString(), Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
		this.replacements.put(ChatColor.UNDERLINE.toString(), Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
		this.replacements.put(ChatColor.ITALIC.toString(), Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
		this.replacements.put(ChatColor.COLOR_CHAR + "z", Ansi.ansi().newline().toString());
		this.replacements.put(ChatColor.RESET.toString(), Ansi.ansi().a(Ansi.Attribute.RESET).toString());
	}

	public String format(String s) {
		for (String color : replacements.keySet()) {
			s = s.replaceAll("(?i)" + color, (String) this.replacements.get(color));
		}
		return s + Ansi.ansi().a(Ansi.Attribute.RESET).toString();
	}

	public String stripAnsi(String message) {
		message = message.replaceAll("\033\\[m", "");
		return message;
	}
}