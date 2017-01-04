package dev.wolveringer.bungeeutil.animations.text;

public class TickedTextFormater extends TextFormater{
	private final int ft;
	private int t;
	private String now;

	public TickedTextFormater(String raw, int ticks) {
		super(raw);
		this.t = this.ft = ticks;
		this.now = super.getNextString();
	}

	public String getMessage() {
		return this.now;
	}

	@Override
	public String getNextString() {
		throw new OperationNotSupportedException();
	}

	public int getTickRate(){
		return this.ft;
	}

	public TickedTextFormater runTick() {
		this.t--;
		if(this.t <= 0){
			this.t = this.ft;
			this.now = super.getNextString();
		}
		return this;
	}
}
