package dev.wolveringer.bungeeutil.inventory;

import dev.wolveringer.bungeeutil.player.Player;

public interface InventoryListener {
	public void onClose(Inventory inv, Player player, CloseReason reason);
}
