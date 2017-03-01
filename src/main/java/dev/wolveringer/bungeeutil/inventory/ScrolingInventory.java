package dev.wolveringer.bungeeutil.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import net.md_5.bungee.api.ChatColor;

public class ScrolingInventory extends Inventory {

	private List<ItemStack> items =Collections.synchronizedList(new ArrayList<ItemStack>());

	private ItemStack[] toolitem = new ItemStack[7];
	private int side = 0;
	private int rows = -1;
	private boolean autoUpdate = true;
	private ItemStack fill;
	private ItemStack nextitem = new ItemStack(Material.STICK) {
		@Override
		public void click(Click p) {
			if(!this.getItemMeta().hasGlow()) {
				ScrolingInventory.this.nextSide();
			}
		}
	};
	private ItemStack backitem = new ItemStack(Material.STICK) {
		@Override
		public void click(Click p) {
			if(!this.getItemMeta().hasGlow()) {
				ScrolingInventory.this.backSide();
			}
		}
	};
	@SuppressWarnings("deprecation")
	private ItemStack lineitem = new ItemStack(Material.getMaterial(160), 1, (short) 15) {
		@Override
		public void click(Click p) {
		}
	};
	private ItemStack nothinkitem = new ItemStack(Material.FENCE) {
		@Override
		public void click(Click p) {
		};
	};
	public ScrolingInventory(int rows, String name) {
		super(rows * 9 + 18, name);
		this.rows = rows;
		this.nextitem.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"7next Side "+ChatColor.COLOR_CHAR+"b-->");
		this.backitem.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"b<-- "+ChatColor.COLOR_CHAR+"7previous Side");
		this.lineitem.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"a");
		this.lineitem.getItemMeta().setGlow(true);
		this.nothinkitem.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"cKein Item verf"+ChatColor.COLOR_CHAR+"gbar");
		this.update();
	}

	@Override
	public void addItem(ItemStack is) {
		this.items.add(is);
		if(this.autoUpdate) {
			this.update();
		}
	}

	public void backSide() {
		this.side--;
		this.update();
	}

	@Override
	public void clear() {
		this.items.clear();
		if(this.autoUpdate) {
			this.update();
		}
	}

	@Override
	public void disableUpdate() {
		this.autoUpdate = false;
	}

	@Override
	public void enableUpdate() {
		this.autoUpdate = true;
		this.update();
	}

	@Override
	public void fill(ItemStack is) {
		this.fill = is;
		if(this.fill != null) {
			for(int i = 0;i<this.getSlots();i++){
				if(super.getItem(i) == null) {
					super.setItem(i, is);
				}
			}
		}
	}

	public void nextSide() {
		this.side++;
		this.update();
	}

	public void removeItem(ItemStack is) {
		this.items.remove(is);
		if(this.autoUpdate) {
			this.update();
		}
	}

	public void setBackItem(Item i) {
		this.backitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
				ScrolingInventory.this.backSide();
			}
		};
		this.update();
	}

	@Override
	public void setItem(int slot, ItemStack is) {
		throw new RuntimeException("This Inventory is not static!");
	}

	public void setLineItem(Item i) {
		this.lineitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
			}
		};
		this.update();
	}

	public void setNextItem(Item i) {
		this.nextitem = new ItemStack(i) {
			@Override
			public void click(Click p) {
				ScrolingInventory.this.nextSide();
			}
		};
	}

	public void setNothinkitem(Item nothinkitem) {
		this.nothinkitem = new ItemStack(nothinkitem) {
			@Override
			public void click(Click c) {}
		};
		this.update();
	}

	/**
	 * @param slot
	 *            <=7
	 * @param is
	 */
	public void setToolItem(int slot, ItemStack is) {
		if(slot > 7) {
			throw new RuntimeException("Slot is to big!");
		}
		this.toolitem[slot] = is;
		if(this.autoUpdate) {
			this.update();
		}
	}

	@SuppressWarnings("deprecation")
	private void update() {
		super.disableUpdate();
		super.clear();
		List<ItemStack> i = this.side * this.rows * 9 < this.items.size() ? this.items.subList(this.side * this.rows * 9, (this.side + 1) * this.rows * 9 < this.items.size() ? (this.side + 1) * this.rows * 9 : this.items.size()) : new ArrayList<ItemStack>();
		for(int x = 0;x < i.size();x++) {
			super.setItem(x, i.get(x));
		}
		if(i.size() == 0) {
			super.setItem(this.rows / 2 * 9 + 4, this.nothinkitem);
		}
		for(int x = 0;x < 9;x++){
			super.setItem(this.rows * 9 + x, this.lineitem);
		}
		this.nextitem.getItemMeta().setGlow((this.side + 1) * this.rows * 9 >= this.items.size());
		super.setItem(this.getSlots() - 1, this.nextitem);
		this.backitem.getItemMeta().setGlow(this.side == 0);
		super.setItem(this.getSlots() - 9, this.backitem);
		for(int x = 0;x < 7;x++) {
			super.setItem(this.getSlots() - 8 + x, this.toolitem[x]);
		}
		this.fill(this.fill);
		this.autoUpdate = true;
		super.updateInventory();
	}
	@Override
	public void updateInventory() {
		this.update();
	}
}
