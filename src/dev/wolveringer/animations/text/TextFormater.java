package dev.wolveringer.animations.text;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

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
		this.raw = loadElement(Jsoup.parse(raw.replaceAll("&", ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"")));
	}
	private String loadElement(Element e) {
		long[] l = new long[e.getElementsByTag("scroller").size()];
		for(int o = 0;o<e.getElementsByTag("scroller").size();o++)
			l[o] = Integer.parseInt(e.getElementsByTag("scroller").get(o).attr("time"));
		kgn = NumericUntil.ggt(l);
		for(Element x : e.getElementsByTag("scroller")){
			x.attr("sid", scroler.size() + "");
			scroler.add(new SpecScroll(x.text(), Integer.parseInt(x.attr("width")), Integer.parseInt(x.attr("space")), (int) (Integer.parseInt(x.attr("time"))/kgn)));
		}
		return e.html();
	}
	public String getNextString() {
		for(SpecScroll s : scroler)
			s.runTick();
		Element e = Jsoup.parse(raw);
		for(Element x : e.getElementsByTag("scroller")){
			x.html(scroler.get(Integer.parseInt(x.attr("sid"))).getMessage().replaceAll(" ", "&nbsp;"));
		}
		return e.text().replaceAll("&nbsp;", " ");
	}

	public long getTick() {
		return kgn;
	}

	public String getRawMessage() {
		return raw;
	}
}

class SpecScroll extends Scroller {
	private final int ft;
	private int t;
	private String now;

	public SpecScroll(String message, int width, int spaceBetween, int tics) {
		super(message, width, spaceBetween, dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR);
		t = ft = tics;
		now = super.next();
	}

	public SpecScroll runTick() {
		t--;
		if(t <= 0){
			t = ft;
			now = super.next();
		}
		return this;
	}

	public String getMessage() {
		return now;
	}
	
	public int getTickRate(){
		return ft;
	}

	@Deprecated
	@Override
	public String next() {
		throw new OperationNotSupportedException();
	}
}

@SuppressWarnings("serial")
class OperationNotSupportedException extends RuntimeException {
	public OperationNotSupportedException() {
		super();
	}

	public OperationNotSupportedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public OperationNotSupportedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OperationNotSupportedException(String arg0) {
		super(arg0);
	}

	public OperationNotSupportedException(Throwable arg0) {
		super(arg0);
	}
}