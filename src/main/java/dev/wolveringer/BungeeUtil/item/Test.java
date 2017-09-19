package dev.wolveringer.BungeeUtil.item;

import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.item.itemmeta.CraftItemMeta;

public class Test {
	public static void main(String[] args) {
		ItemStack is = new ItemStack(Material.STONE){
			@SyncHandle
			public void click(Click click) {};
		};
		System.out.println(((CraftItemMeta)is.getItemMeta()).isClickSync());
	}
}
