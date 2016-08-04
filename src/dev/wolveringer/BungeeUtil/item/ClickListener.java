package dev.wolveringer.BungeeUtil.item;

import dev.wolveringer.BungeeUtil.item.ItemStack.Click;

@FunctionalInterface
public interface ClickListener {
	public void click(Click click);
}
