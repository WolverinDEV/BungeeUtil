package dev.wolveringer.bungeeutil.terminal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;
import org.fusesource.jansi.AnsiConsole;

import dev.wolveringer.bungeeutil.BungeeUtil;
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
	static class ColouredWriterAdapter extends Handler {
		private static final ChatColor[] colors = ChatColor.values();
		private final Map<ChatColor, String> replacements = new EnumMap<>(ChatColor.class);
		private ConsoleReader console;
		private TerminalListener listener;
		protected boolean writed = false;

		public ColouredWriterAdapter(TerminalListener listener, ConsoleReader console) {
			this.replacements.put(ChatColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
			this.replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
			this.replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
			this.replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
			this.replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
			this.replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
			this.replacements.put(ChatColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
			this.replacements.put(ChatColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
			this.replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
			this.replacements.put(ChatColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
			this.replacements.put(ChatColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
			this.replacements.put(ChatColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
			this.replacements.put(ChatColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
			this.replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
			this.replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
			this.replacements.put(ChatColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
			this.replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
			this.replacements.put(ChatColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
			this.replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
			this.replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
			this.replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
			this.replacements.put(ChatColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());

			this.console = console;
			this.listener = listener;
		}

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {
		}

		public void print(String s) {
			for (ChatColor color : colors) {
				s = s.replaceAll("(?i)" + color.toString(), this.replacements.get(color));
			}
			s = Ansi.ansi().eraseLine(Erase.ALL).toString() + ConsoleReader.RESET_LINE + s + Ansi.ansi().reset().toString();
			this.listener.addMessage(s);
			if (this.listener.terminalEnabled || true) {
				this.writed = true;
				try {
					this.console.print(s);
					this.console.drawLine();
					this.console.flush();
				}catch(Exception e){
					e.printStackTrace();
				}
			} else {
				this.listener.lineBffer.add(s);
			}
		}

		@Override
		public void publish(LogRecord record) {
			if (this.isLoggable(record)) {
				if(this.getFormatter()!= null) {
					this.print(this.getFormatter().format(record));
				} else {
					this.print(record.getMessage());
				}
			}
		}
	}

	public static interface Listener {
		void onLinesPrinted();

		void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight);
	}

	@Getter
	@Setter
	private static TerminalListener instance;

	private ArrayList<String> lineBffer = new ArrayList<>();
	@Getter
	private boolean terminalEnabled = true;
	private ScheduledTask task;
	private ColouredWriterAdapter writer;
	private LinkedList<String> lines = new LinkedList<>();
	@Getter
	private CopyOnWriteArrayList<Listener> listener = new CopyOnWriteArrayList<>();

	public TerminalListener() {
		BungeeLogger logger = (BungeeLogger) BungeeCord.getInstance().getLogger();
		ColouredWriter org = null;
		for (Handler h : logger.getHandlers()) {
			if (h instanceof ColouredWriter) {
				logger.removeHandler(h);
				org = (ColouredWriter) h;
			}
		}
		logger.addHandler(this.writer = new ColouredWriterAdapter(this, BungeeCord.getInstance().getConsoleReader()));
		this.writer.setLevel( Level.INFO );
		if(org != null) {
			this.writer.setFormatter(org.getFormatter());
		} else {
			this.addMessage("§cCant find BungeeCord Terminal handler!");
		}
		this.task = BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			int oldWidth = -1, oldHeight = -1;

			@Override
			public void run() {
				while (BungeeUtil.getInstance().isActive()) {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
					}
					if (TerminalFactory.get().getHeight() != this.oldHeight || TerminalFactory.get().getWidth() != this.oldWidth) {
						for (Listener l : TerminalListener.this.listener) {
							l.onResize(this.oldWidth, this.oldHeight, TerminalFactory.get().getWidth(), TerminalFactory.get().getHeight());
						}
						this.oldHeight = TerminalFactory.get().getHeight();
						this.oldWidth = TerminalFactory.get().getWidth();
					}
					if (TerminalListener.this.writer.writed) {
						TerminalListener.this.writer.writed = false;
						for (Listener l : TerminalListener.this.listener) {
							l.onLinesPrinted();
						}
					}
				}
			}
		});
	}

	protected void addMessage(String message) {
		this.lines.push(message);
		while (this.lines.size() > 1000) {
			this.lines.removeLast();
		}
	}

	public void repaintTerminal() {
		try {
			AnsiConsole.out.print("\033[H\033[2J");
			AnsiConsole.out.print("\033[0;0H");
			int h = TerminalFactory.get().getHeight();
			int w = TerminalFactory.get().getWidth();
			int fs = Math.max(0, h-this.lines.size());

			for (int i = 0; i < h; i++) {
				if(h-i > -1 && this.lines.size() > h-i){
					String message = this.lines.get(h-i);
					//while(AnsiColorFormater.getFormater().stripAnsi(message).length() > w){
					//	message = message.substring(0,message.length()-1); //TODO Ansi color chars not count
					//}
					AnsiConsole.out.print("\033["+i+";0H"+message);
				}
				//else
				//	AnsiConsole.out.print("\033["+(i)+";0H"+Ansi.ansi().a(Ansi.Erase.ALL).toString());
				AnsiConsole.out.flush();
			}
			BungeeCord.getInstance().getConsoleReader().drawLine();
			//BungeeCord.getInstance().getConsoleReader().flush();
			AnsiConsole.out.print("\033["+h+";"+(2+BungeeCord.getInstance().getConsoleReader().getCursorBuffer().buffer.length())+"H");
			AnsiConsole.out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setTerminalEnabled(boolean terminalEnabled) {
		this.terminalEnabled = terminalEnabled;
		if (terminalEnabled) {
			try {
				for(String line : this.lineBffer) {
					AnsiConsole.out.println(line);
				}
				this.writer.writed = true;
			} catch (Exception e) {
			}
			this.lineBffer.clear();
		}
	}
}
