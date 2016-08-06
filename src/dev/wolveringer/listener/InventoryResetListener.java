package dev.wolveringer.listener;

import dev.wolveringer.BungeeUtil.Player;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class InventoryResetListener implements Listener{
	@EventHandler
	public void a(ServerSwitchEvent e){
		((Player)e.getPlayer()).getPlayerInventory().clear(); //Clear inventory after server switch
	}
}
