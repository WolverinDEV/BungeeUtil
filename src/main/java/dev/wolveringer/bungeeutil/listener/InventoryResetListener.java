package dev.wolveringer.bungeeutil.listener;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.player.Player;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class InventoryResetListener implements Listener{
	public InventoryResetListener() {
	}

	@EventHandler
	public void a(ServerSwitchEvent e){
		BungeeUtil.debug("Player "+e.getPlayer().getName()+" [Displayname: "+e.getPlayer().getDisplayName()+"] switched the server.");
		((Player)e.getPlayer()).getPlayerInventory().clear(); //Clear inventory after server switch
	}
}
