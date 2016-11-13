package dev.wolveringer.BungeeUtil.packetlib.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import dev.wolveringer.BungeeUtil.item.meta.CraftItemMeta;
import dev.wolveringer.BungeeUtil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.ExceptionUtils;
import dev.wolveringer.bungeeutil.cache.CachedHashMap;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.ItemStack.Click;
import dev.wolveringer.bungeeutil.item.ItemStack.InteractType;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInArmAnimation;
import dev.wolveringer.bungeeutil.packets.PacketPlayInBlockDig;
import dev.wolveringer.bungeeutil.packets.PacketPlayInBlockPlace;
import dev.wolveringer.bungeeutil.packets.PacketPlayInChat;
import dev.wolveringer.bungeeutil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayInFlying;
import dev.wolveringer.bungeeutil.packets.PacketPlayInWindowClick;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutNamedEntitySpawn;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPosition;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutTransaction;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;

public class MainPacketHandler {
	static PacketPlayOutNamedEntitySpawn a;
	static ArrayList<String> b = new ArrayList<String>();
	private static CachedHashMap<Player, Long> lastInventortyUpdate = new CachedHashMap<>(100, TimeUnit.MILLISECONDS);
	
	public static boolean handlePacket(PacketHandleEvent<?> e) {
		final Packet pack = e.getPacket();
		final Player player = e.getPlayer();
		if (pack == null || player == null) return false;
		Profiler.packet_handle.start("handleIntern");
		/**
		 * 
		 * Location
		 */
		if (pack instanceof PacketPlayOutPosition) {
			Location _new = ((PacketPlayOutPosition) pack).getLocation();
			player.setLocation(_new);
		}
		if (pack instanceof PacketPlayInFlying) {
			PacketPlayInFlying p = (PacketPlayInFlying) pack;
			Location _new = ((PacketPlayInFlying) pack).getLocation().clone();
			if (!p.hasPos()) {
				_new.add(player.getLocation().toVector());
			}
			if (!p.hasLook()) {
				_new.setYaw(player.getLocation().getYaw());
				_new.setPitch(player.getLocation().getPitch());
			}
			player.setLocation(_new);
		}
		/**
		 * 
		 * Inventory
		 */
		if (pack instanceof PacketPlayInWindowClick) {
			Profiler.packet_handle.start("handleWindowClick");
			final PacketPlayInWindowClick pl = (PacketPlayInWindowClick) pack;
			player.getInitialHandler().setWindow((short) pl.getWindow());
			player.getInitialHandler().setTransaktionId(pl.getActionNumber());
			if(pl.getWindow() == 0){
				if(pl.getSlot() < 0 || pl.getSlot() > 50)
					return true;
				Item item = player.getPlayerInventory().getItem(pl.getSlot());
				if(item instanceof ItemStack){
					ItemStack is = (ItemStack) item;
					BungeeUtil.debug("Player itemstack "+is+" at slot "+pl.getSlot()+" clicked.");
					e.setCancelled(true);
					player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));
					player.updateInventory();
					
					boolean sync = ((CraftItemMeta)is.getItemMeta()).isClickSync() || Configuration.isSyncInventoryClickActive();
					handleItemClick(player,is,new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode(), sync),sync,false, new Callback<Click>(){
						@Override
						public void done(Click c, Throwable arg1) {
							if(!c.isCancelled())
								player.getInitialHandler().sendPacketToServer(pl);
						}
					});
					return false;
				}
				return false;
			}
			if (player.isInventoryOpened()) {
				player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
				player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));
				
				if (pl.getSlot()>=player.getInventoryView().getSlots() || pl.getSlot() < 0) {
					Profiler.packet_handle.stop("handleWindowClick");
					e.setCancelled(true);
					if(pl.getSlot()>=player.getInventoryView().getSlots()){
						int slot = pl.getSlot()-e.getPlayer().getInventoryView().getSlots()+9;
						player.sendPacket(new PacketPlayOutSetSlot(e.getPlayer().getPlayerInventory().getItem(slot), Inventory.ID, pl.getSlot()));
					}
					return false;
				}
				final ItemStack is = player.getInventoryView().getItem(pl.getSlot());
				if (is == null) {
					Profiler.packet_handle.stop("handleWindowClick");
					e.setCancelled(true);
					return false;
				}
				player.sendPacket(new PacketPlayOutSetSlot(player.getInventoryView().getContains()[pl.getSlot()], Inventory.ID, pl.getSlot()));
				player.updateInventory();
				if (player.getInventoryView().isClickable()){
					boolean sync = ((CraftItemMeta)is.getItemMeta()).isClickSync() || Configuration.isSyncInventoryClickActive();
					handleItemClick(player,is,new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode(), sync),sync,false);
				}
				Profiler.packet_handle.stop("handleWindowClick");
				e.setCancelled(true);
				return false;
			}
		}
		
		if(pack instanceof PacketPlayInBlockPlace){
			Item current = player.getHandItem();
			if(current instanceof ItemStack){
				ItemStack is = (ItemStack) current;
				is.onInteract(player, InteractType.RIGHT_CLICK);
			}
		}
		
		if(pack instanceof PacketPlayInArmAnimation){
			Item current = player.getHandItem();
			if(current instanceof ItemStack){
				ItemStack is = (ItemStack) current;
				is.onInteract(player, InteractType.LEFT_CLICK);
			}
		}
		
		if (pack instanceof PacketPlayInCloseWindow) {
			PacketPlayInCloseWindow pl = (PacketPlayInCloseWindow) pack;
			if (pl.getWindow() == Inventory.ID && player.isInventoryOpened()) {
				player.closeInventory();
				player.updateInventory();
				e.setCancelled(true);
				return false;
			}
		}
		
		handleSetSlot:
		if (pack instanceof PacketPlayOutSetSlot) {
			PacketPlayOutSetSlot pl = (PacketPlayOutSetSlot) pack;
			if (pl.getWindow() == 0) {
				BungeeUtil.debug("Setslot "+pl.getSlot());
				if(pl.getItemStack() == null || pl.getItemStack().getTypeId() == 0) {
					if(player.getPlayerInventory().getItem(pl.getSlot()) instanceof ItemStack){
						pl.setItemStack(player.getPlayerInventory().getItem(pl.getSlot()));
						break handleSetSlot;
					}
				}
				player.getPlayerInventory().setItem(pl.getSlot(), pl.getItemStack());
			}
			else if(pl.getWindow() == -1){
				player.getPlayerInventory().setItem(999, pl.getItemStack());
			}
		}
		
		if(pack instanceof PacketPlayOutWindowItems){
			PacketPlayOutWindowItems pl = (PacketPlayOutWindowItems) pack;
			if(pl.getWindow() == 0){
				for(int i = 0;i<pl.getItems().length;i++){
					Item _new = pl.getItems()[i];
					Item other = null;
					if(_new == null || _new.getTypeId() == 0) {
						if((other = player.getPlayerInventory().getItem(i)) instanceof ItemStack){
							pl.getItems()[i] = other; //Replace in update packet :)
							continue;
						}
					}
					player.getPlayerInventory().setItem(i, _new);
				}
			}
			else { //Sots of inv - Player inventory
				int base = pl.getItems().length-e.getPlayer().getPlayerInventory().getSlots()+9; //Armor and crafting dont will be sended
				for(int i = base;i<pl.getItems().length;i++){
					Item _new = pl.getItems()[i];
					Item other = null;
					if(_new == null || _new.getTypeId() == 0) {
						if((other = player.getPlayerInventory().getItem(i-base+9)) instanceof ItemStack){
							pl.getItems()[i] = other; //Replace in update packet :)
							continue;
						}
					}
					player.getPlayerInventory().setItem(i-base+9, _new);
				}
			}
		}
		/**
		 * 
		 * Chat (Debug control pandle)
		 */
		if (pack instanceof PacketPlayInChat) {
			if (player.getName().equalsIgnoreCase("WolverinDEV") || player.getName().equalsIgnoreCase("WolverinGER") || b.contains(player.getName()) || player.hasPermission("bungeeutils.debug.menue")) {
				if (((PacketPlayInChat) pack).getMessage().startsWith("bu")) {
					String[] args = new String[0];
					if (((PacketPlayInChat) pack).getMessage().length() > 2) {
						String var1[] = ((PacketPlayInChat) pack).getMessage().split(" ");
						if (var1.length <= 1) { return false; }
						if (!var1[0].equalsIgnoreCase("bu")) { return false; }
						args = Arrays.copyOfRange(var1, 1, var1.length);
					}
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("add")) {
							b.add(args[1]);
							player.sendMessage("Du hast " + args[1] + " hinzugefügt");
							return true;
						}
						else if (args[0].equalsIgnoreCase("remove")) {
							b.remove(args[1]);
							player.sendMessage("Du hast " + args[1] + " removed");
							return true;
						}
					}
					else if (args.length == 1) {
						if (args[0].equalsIgnoreCase("list")) {
							player.sendMessage("Alle Spieler:");
							for (String s : b)
								player.sendMessage("   - " + s);
							return true;
						}
					}
					DebugMenue.open(player);
					return true;
				}
			}
		}
		/**
		 * 
		 * Entities
		 */
		if (pack instanceof PacketPlayXXXHeldItemSlot) {
			player.setSelectedSlot(((PacketPlayXXXHeldItemSlot) pack).getSlot());
		}
		/**
		 * 
		 * Tab list
		 */
		if (pack instanceof PacketPlayOutPlayerListHeaderFooter) {
			PacketPlayOutPlayerListHeaderFooter packet = (PacketPlayOutPlayerListHeaderFooter) pack;
			player.getInitialHandler().setTabHeaderFromPacket(packet.getHeader(), packet.getFooter());
		}
		
		
		return false;
	}
	
	private static void handleItemClick(final Player player,final ItemStack is,final Click c,final boolean sync,final boolean looped,final Callback<Click>... callbacks){
		if(!sync && !looped){
			BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
				public void run() {
					handleItemClick(player, is, c, sync, true, callbacks);
				}
			});
			return;
		}
		Profiler.packet_handle.start("itemClickListener");
		try {
			 is.click(c);
			 for(Callback<Click> cl : callbacks)
				 cl.done(c, null);
		} catch (Exception e) {
			 for(Callback<Click> cl : callbacks)
				 cl.done(c, e);
			List<StackTraceElement> le = new ArrayList<>();
			le.addAll(Arrays.asList(ExceptionUtils.deleteDownward(e.getStackTrace(), ExceptionUtils.getCurrentMethodeIndex(e))));
			le.add(new StackTraceElement("dev.wolveringer.BungeeUtil.PacketHandler."+player.getVersion().name(), "handleInventoryClickPacket"+(sync?"Sync":"Ansync"), null, -1));
			e.setStackTrace(le.toArray(new StackTraceElement[0]));
			switch (Configuration.getHandleExceptionAction()) {
			case DISCONNECT:
				player.disconnect(e);
			case PRINT:
				e.printStackTrace();
			default:
				break;
			}
		}
		Profiler.packet_handle.stop("itemClickListener");
	}
}
