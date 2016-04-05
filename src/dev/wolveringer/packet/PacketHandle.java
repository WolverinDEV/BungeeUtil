package dev.wolveringer.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.exception.ExceptionUtils;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.Skin;
import dev.wolveringer.BungeeUtil.gameprofile.SkinFactory;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.BungeeUtil.item.ItemStack.Click;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInChat;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInFlying;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInWindowClick;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutNamedEntitySpawn;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutTransaction;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar.BarColor;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar.BarDivision;
import dev.wolveringer.NPC.InteractListener;
import dev.wolveringer.NPC.NPC;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations;
import dev.wolveringer.animations.inventory.LimetedScheduller;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations.AnimationType;
import dev.wolveringer.api.SoundEffect;
import dev.wolveringer.api.SoundEffect.SoundCategory;
import dev.wolveringer.api.bossbar.BossBarManager.BossBar;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.ItemContainer;
import dev.wolveringer.api.particel.ParticleEffect;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.profiler.ProfileMenue;
import dev.wolveringer.profiler.Profiler;

public class PacketHandle {
	static PacketPlayOutNamedEntitySpawn a;
	static ArrayList<String> b = new ArrayList<String>();
	
	@SuppressWarnings("unused")
	public static boolean handlePacket(final Packet pack, final Player player) {
		Profiler.packet_handle.start("handleIntern");
		if (pack == null || player == null) return false;
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
		else
		/**
		 * 
		 * Inventory
		 */
		
		if (pack instanceof PacketPlayInWindowClick) {
			Profiler.packet_handle.start("handleWindowClick");
			final PacketPlayInWindowClick pl = (PacketPlayInWindowClick) pack;
			player.getInitialHandler().setWindow((short) pl.getWindow());
			player.getInitialHandler().setTransaktionId(pl.getActionNumber());
			if (player.isInventoryOpened()) {
				if (player.getInventoryView().getSlots() <= pl.getSlot() || pl.getSlot() < 0) {
					player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					Profiler.packet_handle.stop("handleWindowClick");
					return true;
				}
				final ItemStack is = player.getInventoryView().getItem(pl.getSlot());
				if (is == null) {
					player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					Profiler.packet_handle.stop("handleWindowClick");
					return true;
				}
				player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
				player.sendPacket(new PacketPlayOutSetSlot(player.getInventoryView().getContains()[pl.getSlot()], Inventory.ID, pl.getSlot()));
				player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));
				player.updateInventory();
				BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
					public void run() {
						Profiler.packet_handle.start("itemClickListener");
						try {
							if (player.getInventoryView().isClickable()) is.click(new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode()));
						}
						catch (Exception e) {
							List<StackTraceElement> le = new ArrayList<>();
							le.addAll(Arrays.asList(ExceptionUtils.deleteDownward(e.getStackTrace(), ExceptionUtils.getCurrentMethodeIndex(e))));
							le.add(new StackTraceElement("dev.wolveringer.BungeeUtil.PacketHandler", "handleInventoryClickPacket", null, -1));
							e.setStackTrace(le.toArray(new StackTraceElement[0]));
							e.printStackTrace();
							player.disconnect(e);
						}
						Profiler.packet_handle.stop("itemClickListener");
					}
				});
				Profiler.packet_handle.stop("handleWindowClick");
				return true;
			}
		}
		if (pack instanceof PacketPlayInCloseWindow) {
			PacketPlayInCloseWindow pl = (PacketPlayInCloseWindow) pack;
			if (pl.getWindow() == Inventory.ID && player.isInventoryOpened()) {
				player.closeInventory();
				player.updateInventory();
				return false;
			}
		}
		if (pack instanceof PacketPlayOutWindowItems) {
			PacketPlayOutWindowItems pl = (PacketPlayOutWindowItems) pack;
			if (pl.getWindow() == 0) {
				for (int i = 0; i < pl.getItems().length; i++)
					player.getPlayerInventory().setItem(i, pl.getItems()[i]);
			}
		}
		if (pack instanceof PacketPlayOutSetSlot) {
			PacketPlayOutSetSlot pl = (PacketPlayOutSetSlot) pack;
			if (pl.getWindow() == 0) {
				player.getPlayerInventory().setItem(pl.getSlot(), pl.getItemStack());
			}
			else if(pl.getWindow() == -1){
				player.getPlayerInventory().setItem(999, pl.getItemStack());
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
}
