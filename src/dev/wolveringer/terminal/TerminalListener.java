package dev.wolveringer.terminal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;

import dev.wolveringer.BungeeUtil.BungeeUtil;
import dev.wolveringer.chat.ChatColor.AnsiColorFormater;

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiRenderWriter;
import org.fusesource.jansi.AnsiString;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.log.BungeeLogger;
import net.md_5.bungee.log.ColouredWriter;

public class TerminalListener {
	@Getter
	@Setter
	private static TerminalListener instance;

	public static interface Listener {
		void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight);

		void onLinesPrinted();
	}

	static class ColouredWriterAdapter extends Handler {
		private static final ChatColor[] colors = ChatColor.values();
		private final Map<ChatColor, String> replacements = new EnumMap<>(ChatColor.class);
		private ConsoleReader console;
		private TerminalListener listener;
		protected boolean writed = false;

		public ColouredWriterAdapter(TerminalListener listener, ConsoleReader console) {
			replacements.put(ChatColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
			replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
			replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
			replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
			replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
			replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
			replacements.put(ChatColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
			replacements.put(ChatColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
			replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
			replacements.put(ChatColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
			replacements.put(ChatColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
			replacements.put(ChatColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
			replacements.put(ChatColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
			replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
			replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
			replacements.put(ChatColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
			replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
			replacements.put(ChatColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
			replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
			replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
			replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
			replacements.put(ChatColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());

			this.console = console;
			this.listener = listener;
		}

		public void print(String s) {
			for (ChatColor color : colors) {
				s = s.replaceAll("(?i)" + color.toString(), replacements.get(color));
			}
			s = Ansi.ansi().eraseLine(Erase.ALL).toString() + ConsoleReader.RESET_LINE + s + Ansi.ansi().reset().toString();
			listener.addMessage(s);
			if (listener.terminalEnabled || true) {
				writed = true;
				try {
					console.print(s);
					console.drawLine();
					console.flush();
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				listener.lineBffer.add(s);
			}
		}

		@Override
		public void publish(LogRecord record) {
			if (isLoggable(record)) {
				if(getFormatter()!= null)
					print(getFormatter().format(record));
				else
					print(record.getMessage());
			}
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() throws SecurityException {
		}
	}

	private ArrayList<String> lineBffer = new ArrayList<>();
	@Getter
	private boolean terminalEnabled = true;
	private ScheduledTask task;
	private ColouredWriterAdapter writer;
	private List<String> lines = new ArrayList<>();
	@Getter
	private CopyOnWriteArrayList<Listener> listener = new CopyOnWriteArrayList<>();

	public TerminalListener() {
		BungeeLogger logger = (BungeeLogger) BungeeCord.getInstance().getLogger();
		ColouredWriter org = null;
		for (Handler h : logger.getHandlers())
			if (h instanceof ColouredWriter) {
				logger.removeHandler(h);
				org = (ColouredWriter) h;
			}
		logger.addHandler(writer = new ColouredWriterAdapter(this, BungeeCord.getInstance().getConsoleReader()));
		writer.setLevel( Level.INFO );
		writer.setFormatter(org.getFormatter());
		
		task = BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			int oldWidth = -1, oldHeight = -1;

			@Override
			public void run() {
				while (BungeeUtil.getInstance().isActive()) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (TerminalFactory.get().getHeight() != oldHeight || TerminalFactory.get().getWidth() != oldWidth) {
						for (Listener l : listener)
							l.onResize(oldWidth, oldHeight, TerminalFactory.get().getWidth(), TerminalFactory.get().getHeight());
						oldHeight = TerminalFactory.get().getHeight();
						oldWidth = TerminalFactory.get().getWidth();
					}
					if (writer.writed) {
						writer.writed = false;
						for (Listener l : listener)
							l.onLinesPrinted();
					}
				}
			}
		});
	}

	public void setTerminalEnabled(boolean terminalEnabled) {
		this.terminalEnabled = terminalEnabled;
		if (terminalEnabled) {
			try {
				for (String s : lineBffer)
					BungeeCord.getInstance().getConsoleReader().print(s);
				BungeeCord.getInstance().getConsoleReader().drawLine();
				BungeeCord.getInstance().getConsoleReader().flush();
				writer.writed = true;
			} catch (Exception e) {
			}
			lineBffer.clear();
		}
	}

	protected void addMessage(String message) {
		lines.add(message);
		while (lines.size() > 200)
			lines.remove(0);
	}

	public void repaintTerminal() {
		try {
			AnsiConsole.out.print("\033[0;0H");
			int h = TerminalFactory.get().getHeight();
			int w = TerminalFactory.get().getWidth();
			for (int i = h; i > 0; i--) {
				if(lines.size() > i){
					String message = lines.get(i);
					while(AnsiColorFormater.getFormater().stripAnsi(message).length() > w){
						message = message.substring(0,message.length()-1); //TODO Ansi color chars not count
					}
					AnsiConsole.out.print("\033["+(i)+";0H"+message);
					AnsiConsole.out.flush();
				}
				else
					continue;
			}
			BungeeCord.getInstance().getConsoleReader().drawLine();
			//BungeeCord.getInstance().getConsoleReader().flush();
			AnsiConsole.out.print("\033["+h+";"+(2+BungeeCord.getInstance().getConsoleReader().getCursorBuffer().buffer.length())+"H");
			AnsiConsole.out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
