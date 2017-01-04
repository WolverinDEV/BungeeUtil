package dev.wolveringer.bungeeutil.item;

import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.player.Player;

public abstract class ItemStack extends Item {

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

		public Inventory getInventory() {
			return this.inventory;
		}

		public Item getItem() {
			return this.item;
		}

		public int getMode() {
			return this.mode;
		}

		public Player getPlayer() {
			return this.player;
		}

		public int getSlot() {
			return this.slot;
		}

		@Deprecated
		public boolean isCancelled() {
			return this.cancel;
		}

		public boolean isSyncHandle() {
			return this.sync;
		}

		@Deprecated
		public void setCancelled(boolean b) {
			this.cancel = b;
		}
	}

	public static enum InteractType {
		RIGHT_CLICK,
		LEFT_CLICK;
	}

	@SuppressWarnings("deprecation")
	public ItemStack(int type, int amount, short damage) {
		super(type, amount, damage);
	}

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
	public abstract void click(Click click);

	public void onInteract(Player player,InteractType type){}
}