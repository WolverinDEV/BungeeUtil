package dev.wolveringer.bungeeutil.item;

import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.player.Player;

public abstract class ItemStack extends Item {

	public ItemStack(Item stack) throws IllegalArgumentException {
		super(stack);
	}

	public ItemStack(Material type) {
		super(type);
	}

	public ItemStack(Material type, int amount) {
		super(type, amount);
	}

	public ItemStack(Material type, int amount, short damage) {
		super(type, amount, damage);
	}

	@SuppressWarnings("deprecation")
	public ItemStack(int type, int amount, short damage) {
		super(type, amount, damage);
	}

	public static enum InteractType {
		RIGHT_CLICK,
		LEFT_CLICK;
	}
	
	public abstract void click(Click click);
	public void onInteract(Player player,InteractType type){}
	
	public static class Click {
		private Player player;
		private int slot;
		private Inventory inventory;
		private int mode;
		private boolean cancel = true;
		private Item item;
		private boolean sync;
		
		public Click(Player p, int slot, Inventory inv,Item ci, int mode,boolean sync) {
			this.player = p;
			this.slot = slot;
			this.inventory = inv;
			this.mode = mode;
			this.item = ci;
			this.sync = sync;
		}

		public Player getPlayer() {
			return player;
		}

		public int getSlot() {
			return slot;
		}

		public Inventory getInventory() {
			return inventory;
		}

		public int getMode() {
			return mode;
		}
		
		public Item getItem() {
			return item;
		}
		
		@Deprecated
		public void setCancelled(boolean b) {
			this.cancel = b;
		}

		@Deprecated
		public boolean isCancelled() {
			return cancel;
		}
		
		public boolean isSyncHandle() {
			return sync;
		}
	}
}