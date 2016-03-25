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
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityDestroy;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutNamedEntitySpawn;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutTransaction;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOutEntityAbstract;
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
		if (pack == null || player == null)
			return false;
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
			if(!p.hasPos()){
				_new.add(player.getLocation().toVector());
			}
			if(!p.hasLook()){
				_new.setYaw(player.getLocation().getYaw());
				_new.setPitch(player.getLocation().getPitch());
			}
			player.setLocation(_new);
		} else 
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
							if (player.getInventoryView().isClickable())
								is.click(new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode()));
						} catch (Exception e) {
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
				return true;
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
						if (var1.length <= 1) {
							return false;
						}
						if (!var1[0].equalsIgnoreCase("bu")) {
							return false;
						}
						args = Arrays.copyOfRange(var1, 1, var1.length);
					}
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("add")) {
							b.add(args[1]);
							player.sendMessage("Du hast " + args[1] + " hinzugefï¿½gt");
							return true;
						} else if (args[0].equalsIgnoreCase("remove")) {
							b.remove(args[1]);
							player.sendMessage("Du hast " + args[1] + " removed");
							return true;
						}
					} else if (args.length == 1) {
						if (args[0].equalsIgnoreCase("list")) {
							player.sendMessage("Alle Spieler:");
							for (String s : b)
								player.sendMessage("   - " + s);
							return true;
						}
					}
					Profiler.packet_handle.start("buildDebugInventory");
					final Inventory inv = new Inventory(18, ChatColorUtils.COLOR_CHAR + "b" + ChatColorUtils.COLOR_CHAR + "lDeveloper Menue");
					player.openInventory(inv);
					ItemStack i = new ItemStack(Material.DIAMOND) {
						@Override
						public void click(final Click p) {
							p.getPlayer().openInventory(ProfileMenue.getProfilerMenue().getMenue());
						}
					};
					i.getItemMeta().setGlow(true);
					i.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "bYEY");
					i.getItemMeta().setLore(Arrays.asList(ChatColorUtils.COLOR_CHAR + "aDieser Server nutzt", ChatColorUtils.COLOR_CHAR + "adein Plugin: ", " " + ChatColorUtils.COLOR_CHAR + "7- " + ChatColorUtils.COLOR_CHAR + "eBungeeUntil", " " + ChatColorUtils.COLOR_CHAR + "7- " + ChatColorUtils.COLOR_CHAR + "eVerion " + ChatColorUtils.COLOR_CHAR + "b" + Main.getMain().getDescription().getVersion()));
					inv.setItem(1, i);
					
					i = new ItemStack(159, 1, (short) 14) {
						@Override
						public void click(final Click p) {
							p.getPlayer().closeInventory();
							final Location target = p.getPlayer().getLocation().clone();
							new LimetedScheduller(5, TimeUnit.SECONDS, 75, TimeUnit.MILLISECONDS) {
								@Override
								public void run(int count) {
									double steps = 0.125;
									double max = 16.5;
									for (double d = 0; d < max; d += steps) {
										ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor((int) (0xFF * (((d+count*2*steps)%max) / max)), 0x00, (int) (0xFF - 0xFF * (((d+count*2*steps)%max) / max))), target.clone().add(target.getDirection().multiply(d)).add(0D, 2 + 1.6D, 0D), p.getPlayer());
									}
								}
							}.start();
							p.getPlayer().sendMessage(ChatColorUtils.COLOR_CHAR + "7Deine Location: " + ChatColorUtils.COLOR_CHAR + "aX: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getX() + " " + ChatColorUtils.COLOR_CHAR + "aY: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getY() + " " + ChatColorUtils.COLOR_CHAR + "aZ: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getZ() + " "+ChatColorUtils.COLOR_CHAR+"7["+ChatColorUtils.COLOR_CHAR+"aYaw: "+ChatColorUtils.COLOR_CHAR+"b" + p.getPlayer().getLocation().getYaw() + ChatColorUtils.COLOR_CHAR+"7, "+ChatColorUtils.COLOR_CHAR+"aPitch: "+ChatColorUtils.COLOR_CHAR+"b" + p.getPlayer().getLocation().getPitch() + ChatColorUtils.COLOR_CHAR+"7]");
							ParticleEffect.FIREWORKS_SPARK.display(0F, 0F, 10F, 0.1F, 10, p.getPlayer().getLocation().add(0, 0, 1), p.getPlayer());
							final NPC c = new NPC();
							c.setName(ChatColorUtils.COLOR_CHAR + "aThis is an testing");
							c.setLocation(p.getPlayer().getLocation().add(0, 2, 0));
							c.addListener(new InteractListener() {
								@Override
								public void rightClick(Player p) {
									p.sendMessage("rightClick");
								}
								
								@Override
								public void leftClick(Player p) {
									p.sendMessage("leftClick");
								}
							});
							c.setPing(2000);
							c.setTabListed(true);
							c.setPlayerListName(ChatSerializer.fromMessage(ChatColorUtils.COLOR_CHAR + "7[NCP] " + ChatColorUtils.COLOR_CHAR + "bEntityID: " + ChatColorUtils.COLOR_CHAR + "a" + c.getEntityID()));
							c.getEquipment().setItemInHand(p.getPlayer().getHandItem());
							if (p.getPlayer().getVersion().getBigVersion() == BigClientVersion.v1_9) {
								Item i = p.getPlayer().getOffHandItem();
								if (i != null)
									c.getEquipment().setItemInOffHand(i);
							}
							c.getEquipment().setHelmet(new dev.wolveringer.BungeeUtil.item.Item(Material.LEATHER_HELMET));
							Skin s = SkinFactory.getSkin("WolverinDEV");
							s.setPublic(true);
							Main.sendMessage(s + "");
							
							GameProfile profile = s.applay(c.getProfile());

							Main.sendMessage(s + "");
							Main.sendMessage(profile + "");
							
							c.setProfile(profile);
							c.setVisiable(p.getPlayer(), true);
							ParticleEffect.HEART.display(0F, 0F, 1F, 0F, 1, c.getLocation(), p.getPlayer());
							p.getPlayer().sendMessage("NCP is visiable");
						}
					};
					i.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "aTesting");
					inv.setItem(3, i);
					
					final ItemStack is = new ItemStack(Material.WATCH, 1, (short) 0) {
						@Override
						public void click(final Click p) {
							final Scoreboard s = p.getPlayer().getScoreboard();
							if(s.getObjektive("test") == null){
								s.createObjektive("test", Type.INTEGER);
								s.getObjektive("test").setScore("§a-----------", -1);
								s.getObjektive("test").setScore("§aHello world", -2);
								s.getObjektive("test").display(Position.SIDEBAR);
								s.getObjektive("test").setDisplayName(ChatColorUtils.COLOR_CHAR + "athis is an test");
							}
							else
							{
								s.removeObjektive("test");
							}
							if(p.getPlayer().getVersion().getBigVersion() == BigClientVersion.v1_9){
								BossBar var0 = null;
						
								var0 = p.getPlayer().getBossBarManager().createNewBossBar();
								var0.setColor(BarColor.GREEN);
								var0.setDivision(BarDivision.NO_DIVISION);
								var0.setHealth(0F);
								var0.setMessage(ChatSerializer.fromMessage("§cHello world"));
								var0.display();
								
								p.getPlayer().sendMessage("Your boss bars:");
								for(BossBar bar : p.getPlayer().getBossBarManager().getActiveBossBars())
									p.getPlayer().sendMessage("  §7- "+ChatSerializer.toMessage(bar.getMessage()));
								final BossBar bar = var0;
								new LimetedScheduller(32,250,TimeUnit.MILLISECONDS) {
									int currunt = 0;
									@Override
									public void run(int count) {
										if(s.getObjektive("test") != null){
											s.getObjektive("test").removeScore(ChatColorUtils.COLOR_CHAR+Integer.toHexString((currunt)%16)+"Testing score");
											currunt+=1;
											s.getObjektive("test").setScore(ChatColorUtils.COLOR_CHAR+Integer.toHexString(currunt%16)+"Testing score", currunt%16);
										}
										if(bar != null){
											bar.setMessage(ChatSerializer.fromMessage(ChatColorUtils.COLOR_CHAR+Integer.toHexString((currunt)%16)+"Hello world"));
											bar.dynamicChangeHealth((float)((float)count/(float)limit), 250, TimeUnit.MILLISECONDS);
										}
									}
									@Override
									public void done() {
										s.removeObjektive("test");
										if(bar != null){
											bar.setColor(BarColor.RED);
											BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
												@Override
												public void run() {
													try {
														Thread.sleep(500);
													}
													catch (InterruptedException e) {
													}
													p.getPlayer().getBossBarManager().deleteBossBar(bar);
												}
											});
										}
									}
								}.start();
							}
							p.getPlayer().sendMessage("Cleaning Space!");
							System.gc();
							p.getPlayer().sendMessage("Cleaning Space done!");
							p.getPlayer().closeInventory();
							
							final Inventory base = new Inventory(45, "SEXY");
							base.fill(new ItemStack(new Item(Material.GOLD_BLOCK)) {
								@Override
								public void click(Click click) {
								}
							});
							p.getPlayer().openInventory(base);
							final ItemContainer container = new ItemContainer(27);
							container.fill(new Item(Material.BARRIER));
							BungeeCord.getInstance().getScheduler().schedule(Main.getMain(), new Runnable() {
								public void run() {
									InventoryViewChangeAnimations.runAnimation(AnimationType.SCROLL_LEFT, base, container, "sexy", new Item(Material.BEDROCK), 500);
								}
							}, 500, TimeUnit.MILLISECONDS);
						}
					};
					
					is.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "7##### " + ChatColorUtils.COLOR_CHAR + "eStatistics " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "aMB" + ChatColorUtils.COLOR_CHAR + "7] #####");
					
					BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
						@Override
						public void run() {
							int mb = 1024 * 1024;
							int c = 0;
							while (inv.getViewer().size() > 0) {
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
								}
								Runtime runtime = Runtime.getRuntime();
								List<String> a = new ArrayList<String>();
								a.add(ChatColorUtils.COLOR_CHAR + "6Used Memory: " + ChatColorUtils.COLOR_CHAR + "e" + format((runtime.totalMemory() - runtime.freeMemory())));
								a.add(ChatColorUtils.COLOR_CHAR + "6Free Memory: " + ChatColorUtils.COLOR_CHAR + "e" + format(runtime.freeMemory()));
								a.add(ChatColorUtils.COLOR_CHAR + "6Total Memory: " + ChatColorUtils.COLOR_CHAR + "e" + format(runtime.totalMemory()));
								a.add(ChatColorUtils.COLOR_CHAR + "6Max Memory: " + ChatColorUtils.COLOR_CHAR + "e" + format(runtime.maxMemory()));
								a.add(ChatColorUtils.COLOR_CHAR + "6System: " + ChatColorUtils.COLOR_CHAR + "e" + System.getProperty("os.name"));
								is.getItemMeta().setLore(a);
								inv.setName(ChatColorUtils.COLOR_CHAR + "" + Integer.toHexString(new Random().nextInt(15) % 15) + ChatColorUtils.COLOR_CHAR + "lDeveloper Menue");
								c++;
							}
						}
						
						private String format(long l) {
							return (l / (1014 * 1024)) + "MB " + ((l / 1024) % 1024) + "KB " + (l % 1024) + "B";
						}
					});
					inv.setItem(7, is);
					
					ItemStack is_ = new ItemStack(player.getVersion().getBigVersion() == BigClientVersion.v1_7 ? Material.FIRE : Material.BARRIER, 1) {
						public void click(Click p) {
							throw new RuntimeException("Demo Crash");
						};
					};
					is_.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "cTest Crash Disconnect");
					inv.setItem(13, is_);
					
					ItemStack is1 = new ItemStack(Material.COMPASS) {
						@Override
						public void click(Click p) {
							p.getPlayer().sendMessage("Sound sended");
							p.getPlayer().playSound(SoundEffect.getEffect("block.anvil.land"),SoundCategory.MASTER, p.getPlayer().getLocation(), 1F, 0);
						}
					};
					final ArrayList<String> out = new ArrayList<String>();
					Packet.listPackets(new CostumPrintStream() {
						@Override
						public void println(String s) {
							out.add(s);
						}
						
						@Override
						public void print(String s) {
							out.add(s);
						}
					});
					is1.getItemMeta().setDisplayName(out.get(0));
					is1.getItemMeta().setLore(out.subList(1, out.size()));
					inv.setItem(5, is1);
					
					Profiler.packet_handle.stop("buildDebugInventory");
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
