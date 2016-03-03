package dev.wolveringer.animations.text;

public class TickedTextFormater extends TextFormater{
	private final int ft;
	private int t;
	private String now;

	public TickedTextFormater(String raw, int ticks) {
		super(raw);
		t = ft = ticks;
		now = super.getNextString();
	}

	public TickedTextFormater runTick() {
		t--;
		if(t <= 0){
			t = ft;
			now = super.getNextString();
		}
		return this;
	}

	public String getMessage() {
		return now;
	}
	
	public int getTickRate(){
		return ft;
	}

	@Override
	public String getNextString() {
		throw new OperationNotSupportedException();
	}
}
