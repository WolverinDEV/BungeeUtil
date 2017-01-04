package dev.wolveringer.bungeeutil.item;

import dev.wolveringer.bungeeutil.item.ItemStack.Click;

@FunctionalInterface
public interface ClickListener {
	public void click(Click click);
}
