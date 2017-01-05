package dev.wolveringer.bungeeutil.animations.text;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

class OperationNotSupportedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperationNotSupportedException() {
		super();
	}

	public OperationNotSupportedException(String arg0) {
		super(arg0);
	}

	public OperationNotSupportedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OperationNotSupportedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public OperationNotSupportedException(Throwable arg0) {
		super(arg0);
	}
}

class SpecScroll extends Scroller {
	private final int ft;
	private int t;
	private String now;

	public SpecScroll(String message, int width, int spaceBetween, int tics) {
		super(message, width, spaceBetween, dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR);
		this.t = this.ft = tics;
		this.now = super.next();
	}

	public String getMessage() {
		return this.now;
	}

	public int getTickRate(){
		return this.ft;
	}

	@Deprecated
	@Override
	public String next() {
		throw new OperationNotSupportedException();
	}

	public SpecScroll runTick() {
		this.t--;
		if(this.t <= 0){
			this.t = this.ft;
			this.now = super.next();
		}
		return this;
	}
}

/**
 * Usage:<br>
 * Base-Text: <br>{@code <scroller width=10  space=0 time=100>HELLO THIS IS A"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a TEST"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a STRING!</scroller>XX<scroller width=10  space=0 time=150>HELLO THIS IS A TEST STRING!</scroller>XX<scroller width=10  space=0 time=200>HELLO THIS IS A TEST STRING!</scroller>}<br>
 * Start scroler with: {@code <scroller width=<Text With>  space=<Space between end and start> time=<time per step>>}<br>
 * @author wolverindev
 *
 */
public class TextFormater {
	private final String raw;
	private ArrayList<SpecScroll> scroler = new ArrayList<SpecScroll>();
	private long kgn;

	public TextFormater(String raw) {
		this.raw = this.loadElement(Jsoup.parse(raw.replaceAll("&", ""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"")));
	}
	public String getNextString() {
		for(SpecScroll s : this.scroler) {
			s.runTick();
		}
		Element e = Jsoup.parse(this.raw);
		for(Element x : e.getElementsByTag("scroller")){
			x.html(this.scroler.get(Integer.parseInt(x.attr("sid"))).getMessage().replaceAll(" ", "&nbsp;"));
		}
		return e.text().replaceAll("&nbsp;", " ");
	}
	public String getRawMessage() {
		return this.raw;
	}

	public long getTick() {
		return this.kgn;
	}

	private String loadElement(Element e) {
		long[] l = new long[e.getElementsByTag("scroller").size()];
		for(int o = 0;o<e.getElementsByTag("scroller").size();o++) {
			l[o] = Integer.parseInt(e.getElementsByTag("scroller").get(o).attr("time"));
		}
		this.kgn = NumericUntil.ggt(l);
		for(Element x : e.getElementsByTag("scroller")){
			x.attr("sid", this.scroler.size() + "");
			this.scroler.add(new SpecScroll(x.text(), Integer.parseInt(x.attr("width")), Integer.parseInt(x.attr("space")), (int) (Integer.parseInt(x.attr("time"))/this.kgn)));
		}
		return e.html();
	}
}