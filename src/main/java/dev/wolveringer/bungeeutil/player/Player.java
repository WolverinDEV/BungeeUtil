package dev.wolveringer.bungeeutil.player;

import dev.wolveringer.bungeeutil.bossbar.BossBarManager;
import dev.wolveringer.bungeeutil.inventory.CloseReason;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.PlayerInventory;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.profile.Skin;
import dev.wolveringer.bungeeutil.scoreboard.Scoreboard;
import dev.wolveringer.bungeeutil.sound.SoundCategory;
import dev.wolveringer.bungeeutil.sound.SoundEffect;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface Player extends ProxiedPlayer {
	public void closeInventory();

	public void closeInventory(CloseReason reason);

	public void disconnect(Exception e);

	public BossBarManager getBossBarManager();



	public Item getCursorItem();

	public Item getHandItem();

	public IInitialHandler getInitialHandler();

	public Inventory getInventoryView();

	public Location getLastLocation();

	public Location getLocation();

	public Item getOffHandItem();
	public PlayerInventory getPlayerInventory();

	public Scoreboard getScoreboard();

	public int getSelectedSlot();

	public BaseComponent[] getTabHeader();

	public ClientVersion getVersion();

	public boolean isInventoryOpened();

	public void openInventory(Inventory inv);
	public void openInventory(Inventory inv, boolean resetCoursor);
	
	public void performCommand(String command);

	/**
	 * @param effect
	 * 
	 * Make sure to check if the client can actually play the sound with 
	 * SoundEffect.<Sound>.isAvariable(<player>.getVersion().getBigVersion())
	 */
	public void playSound(SoundEffect effect);

	/**
	 * @param effect
	 * @param volume
	 * 
	 * Make sure to check if the client can actually play the sound with 
	 * SoundEffect.<Sound>.isAvariable(<player>.getVersion().getBigVersion())
	 */
	public void playSound(SoundEffect effect, float volume);
	
	/**
	 * @param effect
	 * @param volume
	 * @param pitch (0-360)
	 * 
	 * Make sure to check if the client can actually play the sound with 
	 * SoundEffect.<Sound>.isAvariable(<player>.getVersion().getBigVersion())
	 */
	public void playSound(SoundEffect effect, float volume, float pitch);

	/**
	 * @param effect
	 * @param location
	 * @param volume
	 * @param pitch (0-360)
	 * 
	 * Make sure to check if the client can actually play the sound with 
	 * SoundEffect.<Sound>.isAvariable(<player>.getVersion().getBigVersion())
	 */
	public void playSound(SoundEffect effect, Location location, float volume, float pitch);
	
	/**
	 * @param effect
	 * @param soundcategory
	 * @param location
	 * @param volume
	 * @param pitch (0-360)
	 * 
	 * Make sure to check if the client can actually play the sound with 
	 * SoundEffect.<Sound>.isAvariable(<player>.getVersion().getBigVersion())
	 */
	public void playSound(SoundEffect effect, SoundCategory soundcategory, Location location, float volume, float pitch);
	
	/**
	 * @param skin
	 * 
	 * Note: The player need to switch servers to make the skin visible.
	 * Skin will reset when the player disconnect form bungee.
	 * If you use this method on {@see PostLoginEvent} the player don't need to reconnect.
	 * You can provide the original skin for the player to reset it.
	 * If you provide null it will throw a NullPointerException
	 */
	public void setSkin(Skin skin);

	public void sendPacket(PacketPlayOut packet);

	@Deprecated
	public void sendPacketToServer(PacketPlayIn p);

	public void setCursorItem(Item is);

	public void setLocation(Location loc);

	public void setSelectedSlot(int slot);

	@Override
	public void setTabHeader(BaseComponent header,BaseComponent footer);


	public void updateInventory();
}
