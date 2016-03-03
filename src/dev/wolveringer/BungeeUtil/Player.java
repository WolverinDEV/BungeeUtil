package dev.wolveringer.BungeeUtil;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.SoundEffect;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.PlayerInventory;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.network.IInitialHandler;

public interface Player extends ProxiedPlayer {
	public IInitialHandler getInitialHandler();

	public void playEffect(SoundEffect effect, Location loc, float volume);

	public void playEffect(SoundEffect effect, Location loc, float volume, float pitch);

	
	public Location getLocation();
	
	public void setLocation(Location loc);

	public Location getLastLocation();

	
	
	public void performCommand(String command);
	
	public boolean isInventoryOpened();

	public void openInventory(Inventory inv);

	public void updateInventory();

	public Inventory getInventoryView();
	
	public PlayerInventory getPlayerInventory();
	
	public void closeInventory();
	
	
	public void setCursorItem(Item is);

	public Item getCursorItem();

	

	public ClientVersion getVersion();

	public void sendPacket(PacketPlayOut packet);
	
	@Deprecated
	public void sendPacketToServer(PacketPlayIn p);

	
	public void setSelectedSlot(int slot);

	public int getSelectedSlot();

	
	public void setTabHeader(IChatBaseComponent header,IChatBaseComponent footer);
	
	public IChatBaseComponent[] getTabHeader();
	
	
	public Scoreboard getScoreboard();

	public void disconnect(Exception e);
}
