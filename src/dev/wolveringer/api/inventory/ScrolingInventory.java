package dev.wolveringer.api.inventory;

import java.util.ArrayList;
import java.util.List;

import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;

public class ScrolingInventory extends Inventory {

	public ScrolingInventory(int rows, String name) {
		super(rows * 9 + 18, name);
		this.rows = rows;
		nextitem.getItemMeta().setDisplayName("§7next Side §b-->");
		backitem.getItemMeta().setDisplayName("§b<-- §7previous Side");
		lineitem.getItemMeta().setDisplayName("§a");
		nothinkitem.getItemMeta().setDisplayName("§cKein Item verfügbar");
		update();
	}

	private ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	private ItemStack[] toolitem = new ItemStack[7];
	private int side = 0;
	private int rows = -1;
	private boolean autoUpdate = true;
	private ItemStack fill;
	private ItemStack nextitem = new ItemStack(Material.STICK) {
		@Override
		public void click(Click p) {
			if(!getItemMeta().hasGlow())
				nextSide();
		}
	};
	private ItemStack backitem = new ItemStack(Material.STICK) {
		@Override
		public void click(Click p) {
			if(!getItemMeta().hasGlow())
				backSide();
		}
	};
	@SuppressWarnings("deprecation")
	private ItemStack lineitem = new ItemStack(Material.getMaterial(160), 1, (short) 15) {
		public void click(Click p) {
		}
	};
	private ItemStack nothinkitem = new ItemStack(Material.FENCE) {
		public void click(Click p) {
		};
	};

	public void setItem(int slot, ItemStack is) {
		throw new RuntimeException("This Inventory is not static!");
	}

	@Override
	public void addItem(ItemStack is) {
		items.add(is);
		if(autoUpdate)
			update();
	}

	public void removeItem(ItemStack is) {
		items.remove(is);
		if(autoUpdate)
			update();
	}

	public void setNextItem(Item i) {
		nextitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
				nextSide();
			}
		};
	}

	public void setBackItem(Item i) {
		backitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
				backSide();
			}
		};
	}

	public void setLineItem(Item i) {
		lineitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
			}
		};
	}

	public void setNothinkitem(Item nothinkitem) {
		this.nothinkitem = new ItemStack(nothinkitem) {
			@Override
			public void click(Click c) {}
		};
	}

	/**
	 * @param slot
	 *            <=7
	 * @param is
	 */
	public void setToolItem(int slot, ItemStack is) {
		if(slot > 7)
			throw new RuntimeException("Slot is to big!");
		toolitem[slot] = is;
		if(autoUpdate)
			update();
	}

	public void nextSide() {
		side++;
		update();
	}

	public void backSide() {
		side--;
		update();
	}

	@Override
	public void clear() {
		items.clear();
		if(autoUpdate)
			update();
	}

	private void update() {
		super.disableUpdate();
		super.clear();
		List<ItemStack> i = (side * (rows * 9) < items.size() ? items.subList(side * (rows * 9), (side + 1) * (rows * 9) < items.size() ? (side + 1) * (rows * 9) : items.size()) : new ArrayList<ItemStack>());
		for(int x = 0;x < i.size();x++)
			super.setItem(x, i.get(x));
		if(i.size() == 0)
			super.setItem(((rows / 2) * 9) + 4, nothinkitem);
		for(int x = 0;x < 9;x++){
			super.setItem((rows * 9) + x, lineitem);
		}
		nextitem.getItemMeta().setGlow((side + 1) * (rows * 9) >= items.size());
		super.setItem(getSlots() - 1, nextitem);
		backitem.getItemMeta().setGlow(side == 0);
		super.setItem(getSlots() - 9, backitem);
		for(int x = 0;x < 7;x++)
			super.setItem(getSlots() - 8 + x, toolitem[x]);
		fill(fill);
		autoUpdate = true;
		super.updateInventory();
	}

	@Override
	public void disableUpdate() {
		autoUpdate = false;
	}

	@Override
	public void enableUpdate() {
		autoUpdate = true;
		update();
	}

	@Override
	public void updateInventory() {
		this.update();
	}
	@Override
	public void fill(ItemStack is) {
		fill = is;
		if(fill != null)
		for(int i = 0;i<getSlots();i++){
			if(super.getItem(i) == null)
				super.setItem(i, is);
		}
	}
}
