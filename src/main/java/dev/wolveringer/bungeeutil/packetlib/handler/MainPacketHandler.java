package dev.wolveringer.bungeeutil.packetlib.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.ExceptionUtils;
import dev.wolveringer.bungeeutil.inventory.CloseReason;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.ItemStack.Click;
import dev.wolveringer.bungeeutil.item.ItemStack.InteractType;
import dev.wolveringer.bungeeutil.item.meta.CraftItemMeta;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInArmAnimation;
import dev.wolveringer.bungeeutil.packets.PacketPlayInBlockPlace;
import dev.wolveringer.bungeeutil.packets.PacketPlayInChat;
import dev.wolveringer.bungeeutil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayInFlying;
import dev.wolveringer.bungeeutil.packets.PacketPlayInWindowClick;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnPlayer;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPosition;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutTransaction;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;

public class MainPacketHandler {
	static PacketPlayOutSpawnPlayer a;
	static ArrayList<String> b = new ArrayList<String>();
	//private static CachedHashMap<Player, Long> lastInventortyUpdate = new CachedHashMap<>(100, TimeUnit.MILLISECONDS);

	@SuppressWarnings("unchecked")
	private static void handleItemClick(final Player player,final ItemStack is,final Click c,final boolean sync,final boolean looped,final Callback<Click>... callbacks){
		if(!sync && !looped){
			BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), () -> handleItemClick(player, is, c, sync, true, callbacks));
			return;
		}
		Profiler.PACKET_HANDLE.start("itemClickListener");
		try {
			 is.click(c);
			 for(Callback<Click> cl : callbacks) {
				cl.done(c, null);
			}
		} catch (Exception e) {
			 for(Callback<Click> cl : callbacks) {
				cl.done(c, e);
			}
			List<StackTraceElement> le = new ArrayList<>();
			le.addAll(Arrays.asList(ExceptionUtils.deleteDownward(e.getStackTrace(), ExceptionUtils.getCurrentMethodeIndex(e))));
			le.add(new StackTraceElement("dev.wolveringer.bungeeUtil.packetlib.Handler_"+player.getVersion().name(), "handleInventoryClickPacket"+(sync?"Sync":"Async"), null, -1));
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
		Profiler.PACKET_HANDLE.stop("itemClickListener");
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static boolean handlePacket(PacketHandleEvent<?> e) {
		final Packet pack = e.getPacket();
		final Player player = e.getPlayer();
		if (pack == null || player == null) {
			return false;
		}
		Profiler.PACKET_HANDLE.start("handleIntern");
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
			Profiler.PACKET_HANDLE.start("handleWindowClick");
			final PacketPlayInWindowClick pl = (PacketPlayInWindowClick) pack;

			byte mode = (byte) (pl.getMode() >> 4 & 0xF);
			byte button = (byte) (pl.getMode()  & 0xF);
			BungeeUtil.debug("Having packet click. Window: "+pl.getWindow()+" Slot: "+pl.getSlot()+" Mode: "+ mode +" Button: "+button);
			player.getInitialHandler().setWindow((short) pl.getWindow());
			player.getInitialHandler().setTransaktionId(pl.getActionNumber());
			if(pl.getWindow() == 0){
				if(pl.getSlot() >= 0 && pl.getSlot() <= 47){
					Item item = player.getPlayerInventory().getItem(pl.getSlot());
					if(item instanceof ItemStack){
						ItemStack is = (ItemStack) item;
						BungeeUtil.debug("Player itemstack "+is+" at slot "+pl.getSlot()+" clicked.");
						e.setCancelled(true);
						player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
						player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));
						player.updateInventory();

						boolean sync = ((CraftItemMeta)is.getItemMeta()).isClickSync() || Configuration.isSyncInventoryClickActive();
						handleItemClick(player,is,new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode(), sync),sync,false, (c, arg1) -> {
							if(!c.isCancelled()) {
								player.getInitialHandler().sendPacketToServer(pl);
							}
						});
						return false;
					}
				}


				if(mode == 0){
					if(pl.getSlot() == -999){
						if(player.getCursorItem() == null){
							BungeeUtil.debug("Changing inv with null cursor item");
							return false;
						}
						if(button == 0){
							player.setCursorItem(null); //Drop full stack
						} else if(button == 1){
							player.getCursorItem().setAmount(player.getCursorItem().getAmount()-1);
							if(player.getCursorItem().getAmount() <= 0) {
								player.setCursorItem(null);
							}
						}
					}
					else {
						if(button == 0){
							Item slotItem = player.getPlayerInventory().getItem(pl.getSlot());
							player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), player.getCursorItem());
							player.setCursorItem(slotItem);
						} else if(button == 1){
							if(player.getCursorItem() == null){
								Item slotItem = player.getPlayerInventory().getItem(pl.getSlot());
								if(slotItem == null) {
									return false;
								}
								int restCount = slotItem.getAmount()/2;
								int pickupCount = slotItem.getAmount()-restCount;
								if(restCount > 0) {
									slotItem.setAmount(restCount);
								} else {
									player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), null);
								}

								Item cursor = new Item(slotItem);
								cursor.setAmount(pickupCount);
								player.setCursorItem(cursor);
							} else {
								Item slotItem = player.getPlayerInventory().getItem(pl.getSlot());
								Item cursor = player.getCursorItem();
								if(slotItem == null){
									slotItem = new Item(player.getCursorItem());
								}
								if(slotItem.isSimilar(player.getCursorItem())){
									slotItem.setAmount(slotItem.getAmount()+1);
									if(slotItem.getAmount() == 1) {
										player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), slotItem);
									}
									cursor.setAmount(cursor.getAmount()-1);
									if(cursor.getAmount() <= 0) {
										player.setCursorItem(null);
									}
								}
								else {
									player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), player.getCursorItem());
									player.setCursorItem(slotItem);
								}
							}
						}
					}
				} else if(mode == 1){
					//TODO handle item move
				} else if(mode == 2){
					int cursorSlot = 36 + button;
					Item pickedSlot = player.getPlayerInventory().getItem(pl.getSlot());
					player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), player.getPlayerInventory().getItem(cursorSlot));
					player.getPlayerInventory().setItemNonUpdating(cursorSlot, pickedSlot);
				} else if(mode == 3){  //Only on creative i think :D
				} else if(mode == 4){
					if(button == 0){
						if(pl.getSlot() == -999){
							BungeeUtil.debug("Invalid window click mode!");
							return false;
						}
						Item slotItem = player.getPlayerInventory().getItem(pl.getSlot());
						if(slotItem == null) {
							return false;
						}
						slotItem.setAmount(slotItem.getAmount()-1);
						if(slotItem.getAmount() <= 0) {
							player.getPlayerInventory().setItem(pl.getSlot(), null);
						}
					} else if(button == 1){
						player.getPlayerInventory().setItem(pl.getSlot(), null);
					}
				} else if(mode == 5){
					if(button == 0){
						player.getPlayerInventory().setDragMode(0);
						player.getPlayerInventory().getDragSlots().clear();
					} else if(button == 4){
						player.getPlayerInventory().setDragMode(1);
						player.getPlayerInventory().getDragSlots().clear();
					} else if(button == 2 || button == 6){
						player.getPlayerInventory().setDragMode(-1);
						if(player.getCursorItem() == null)
							return false;
						if(button == 6){
							if(player.getCursorItem().getAmount()-player.getPlayerInventory().getDragSlots().size() == 0){ //Server will update inv if cursor item != 0
								for(Integer slot : player.getPlayerInventory().getDragSlots()){
									Item sitem = player.getPlayerInventory().getItem(slot);
									if(sitem == null){
										sitem = new Item(player.getCursorItem());
										sitem.setAmount(0);
										player.getPlayerInventory().setItemNonUpdating(slot, sitem);
									}
									sitem.setAmount(sitem.getAmount()+1);
								}
								player.setCursorItem(null);
							}
						}
						else if(button == 2){
							if(player.getCursorItem().getAmount() % player.getPlayerInventory().getDragSlots().size() == 0){  //Server will update inv if cursor item != 0
								int incCount = player.getCursorItem().getAmount() / player.getPlayerInventory().getDragSlots().size();
								for(Integer slot : player.getPlayerInventory().getDragSlots()){
									Item sitem = player.getPlayerInventory().getItem(slot);
									if(sitem == null){
										sitem = new Item(player.getCursorItem());
										sitem.setAmount(0);
										player.getPlayerInventory().setItemNonUpdating(slot, sitem);
									}
									sitem.setAmount(sitem.getAmount()+incCount);
								}
								player.setCursorItem(null);
							}
						}
						player.getPlayerInventory().getDragSlots().clear();

					} else if(button == 1 || button == 5){
						player.getPlayerInventory().getDragSlots().add(pl.getSlot());
					}
				}

				return false;
			}
			
			if (player.isInventoryOpened()) {
				player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
				player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));

				if (pl.getSlot()>=player.getInventoryView().getSlots() || pl.getSlot() < 0) {
					Profiler.PACKET_HANDLE.stop("handleWindowClick");
					e.setCancelled(true);
					if(pl.getSlot()>=player.getInventoryView().getSlots()){
						int slot = pl.getSlot()-e.getPlayer().getInventoryView().getSlots()+9;
						player.sendPacket(new PacketPlayOutSetSlot(e.getPlayer().getPlayerInventory().getItem(slot), Inventory.ID, pl.getSlot()));
					}
					if(PacketPlayInWindowClick.Mode.isShiftClick(pl.getMode()))
						player.getInventoryView().updateInventory();
					return false;
				}
				final Item is = player.getInventoryView().getItem(pl.getSlot());
				if (is == null) {
					Profiler.PACKET_HANDLE.stop("handleWindowClick");
					e.setCancelled(true);
					return false;
				}
				player.sendPacket(new PacketPlayOutSetSlot(player.getInventoryView().getContains()[pl.getSlot()], Inventory.ID, pl.getSlot()));
				player.updateInventory();
				if (player.getInventoryView().isClickable() && is instanceof ItemStack){
					boolean sync = ((CraftItemMeta)is.getItemMeta()).isClickSync() || Configuration.isSyncInventoryClickActive();
					handleItemClick(player,(ItemStack) is,new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode(), sync),sync,false);
				}
				Profiler.PACKET_HANDLE.stop("handleWindowClick");
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
				player.closeInventory(CloseReason.CLIENT_CLOSED);
				player.updateInventory();
				e.setCancelled(true);
				return false;
			}
			if(player.getCursorItem() != null)
			 {
				player.setCursorItem(null); //Cant have an cursor item with closed inv!
			}
		}

		if(pack instanceof PacketPlayOutCloseWindow){
			if(player.getInventoryView() != null) {
				player.closeInventory(CloseReason.SERVER_CLOSED);
			}
		}
		if(pack instanceof PacketPlayOutOpenWindow){
			if(player.getInventoryView() != null) {
				player.closeInventory(CloseReason.SERVER_OPEN_NEW);
			}
		}

		handleSetSlot:
		if (pack instanceof PacketPlayOutSetSlot) {
			PacketPlayOutSetSlot pl = (PacketPlayOutSetSlot) pack;
			if (pl.getWindow() == 0) {
				BungeeUtil.debug("Setslot "+pl.getSlot()+" to "+pl.getItemStack()+" in window "+pl.getWindow());
				if(pl.getItemStack() == null || pl.getItemStack().getTypeId() == 0) {
					if(player.getPlayerInventory().getItem(pl.getSlot()) instanceof ItemStack){
						pl.setItemStack(player.getPlayerInventory().getItem(pl.getSlot()));
						break handleSetSlot;
					}
				}
				player.getPlayerInventory().setItemNonUpdating(pl.getSlot(), pl.getItemStack());
			}
			else if(pl.getWindow() == -1){
				player.getPlayerInventory().setItemNonUpdating(999, pl.getItemStack());
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
					player.getPlayerInventory().setItemNonUpdating(i, _new);
				}
			}
			else { //Sots of inv - Player inventory
				int equipmentSize = -1;
				switch (e.getPlayer().getVersion().getBigVersion()) {
				case v1_11:
				case v1_10:
				case v1_9:
					equipmentSize = 10; //Offhand
					break;
				case v1_8:
					equipmentSize = 9;
					break;
				default:
					break;
				}

				int base = pl.getItems().length-e.getPlayer().getPlayerInventory().getSlots()+equipmentSize; //Armor and crafting dont will be sended
				for(int i = base;i<pl.getItems().length;i++){
					Item _new = pl.getItems()[i];
					Item other = null;
					if(_new == null || _new.getTypeId() == 0) {
						if((other = player.getPlayerInventory().getItem(i-base+9)) instanceof ItemStack){
							pl.getItems()[i] = other; //Replace in update packet :)
							continue;
						}
					}
					player.getPlayerInventory().setItemNonUpdating(i-base+9, _new);
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
							for (String s : b) {
								player.sendMessage("   - " + s);
							}
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
}
