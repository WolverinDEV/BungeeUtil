package dev.wolveringer.bungeeutil.terminal;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.chat.AnsiColorFormater;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.statistics.RamStatistics.RamStatistic;
import dev.wolveringer.bungeeutil.terminal.TerminalListener.Listener;
import jline.TerminalFactory;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;

public class RamStatisticsPainter implements Runnable, Listener {
	@Override
	public void run() {
		while (Configuration.ramStatistics()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if (!BungeeUtil.getInstance().isActive())
				return;
			if (BungeeUtil.getInstance().ramStatistiks.getLastState() == null)
				continue;
			paint();
		}
	}
	
	private void paint(){
		int mb = 1024 * 1024;

		RamStatistic state = BungeeUtil.getInstance().ramStatistiks.getLastState();
		String var1 = (state.getUsedMemory()) / mb + "";
		String var2 = (state.getReservedMemory() - state.getUsedMemory()) / mb + "";
		String var3 = state.getReservedMemory() / mb + "";
		String var4 = state.getMaxMemory() / mb + "";

		int var5 = 5;
		var1 = format(var1, var5);
		var2 = format(var2, var5);
		var3 = format(var3, var5);
		var4 = format(var4, var5);

		String diffSpace = "";
		List<String> lines = new ArrayList<>();
		lines.add(ChatColorUtils.COLOR_CHAR + "7#####" + diffSpace.substring(0, diffSpace.length() / 2).replaceAll(" ", "#") + " " + ChatColorUtils.COLOR_CHAR + "6Heap utilization statistics [MB] " + ChatColorUtils.COLOR_CHAR + "7#####" + diffSpace.substring(0, diffSpace.length() / 2).replaceAll(" ", "#") + (diffSpace.length() % 2 != 0 ? "#" : ""));
		lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Used Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var1 + "M    " + ChatColorUtils.COLOR_CHAR + "7#");
		lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Free Memory:      " + ChatColorUtils.COLOR_CHAR + "e" + var2 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
		lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aReserved Memory:           " + ChatColorUtils.COLOR_CHAR + "e" + var3 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
		lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "a-----------------------------" + format("", var5).replaceAll(" ", "-") + "   " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
		lines.add(ChatColorUtils.COLOR_CHAR + "7#     " + ChatColorUtils.COLOR_CHAR + "aAllowed Reservable Memory: " + ChatColorUtils.COLOR_CHAR + "e" + var4 + "M    " + diffSpace + ChatColorUtils.COLOR_CHAR + "7#");
		lines.add(ChatColorUtils.COLOR_CHAR + "7############################################" + diffSpace.replaceAll(" ", "#"));
		int h = 1;
		int w = 0;
		for (String m : lines)
			if (ChatColor.stripColor(m).length() > w)
				w = ChatColor.stripColor(m).length();
		w = TerminalFactory.get().getWidth() - w + 1;
		for (int i = 0; i < lines.size(); i++, h++) {
			AnsiConsole.out.print("\033[" + h + ";" + w + "H" + AnsiColorFormater.getFormater().format(lines.get(i)));
		}
		int cw = 2 + BungeeCord.getInstance().getConsoleReader().getCursorBuffer().cursor; //2 = Promt = ' >'
		AnsiConsole.out.print("\033[" + TerminalFactory.get().getHeight() + ";" + cw + "H");
		AnsiConsole.out.flush();
	}

	private String format(String in, int space) {
		while (in.length() < space) {
			in = in + " ";
		}
		return in;
	}

	@Override
	public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
		TerminalListener.getInstance().repaintTerminal();
		paint();
	}

	@Override
	public void onLinesPrinted() {
		paint();
	}
}
