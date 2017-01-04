package dev.wolveringer.bungeeutil.item;

import java.util.ArrayList;

public class MultiClickItemStack extends ItemStack{
	private ArrayList<ClickListener> listener = new ArrayList<>();
	public MultiClickItemStack(int type, int amount, short damage) {
		super(type, amount, damage);
	}

	public MultiClickItemStack(Item stack) throws IllegalArgumentException {
		super(stack);
	}

	public MultiClickItemStack(Material type) {
		super(type);
	}

	public MultiClickItemStack(Material type, int amount) {
		super(type, amount);
	}

	public MultiClickItemStack(Material type, int amount, short damage) {
		super(type, amount, damage);
	}

	public void addClickListener(ClickListener listener){
		this.listener.add(listener);
	}
	@Override
	public void click(Click p) {
		for(ClickListener c : this.listener) {
			c.click(p);
		}
	}

	public void removeListener(ClickListener listener){
		this.listener.remove(listener);
	}
}
