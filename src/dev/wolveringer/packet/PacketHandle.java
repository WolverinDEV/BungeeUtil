package dev.wolveringer.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.Skin;
import dev.wolveringer.BungeeUtil.gameprofile.SkinCash;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.DebugProperties;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.PlayerInfoData;
import dev.wolveringer.BungeeUtil.exception.ExceptionUtils;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.BungeeUtil.item.ItemStack.Click;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInArmAnimation;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInBlockDig;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInBlockPlace;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInChat;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPositionLook;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInUpdateSign;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInWindowClick;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutChat;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityDestroy;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityEffect;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityMetadata;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutNamedEntitySpawn;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerInfo;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.Action;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutStatistic;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutTransaction;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutUpdateSign;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOutEntityAbstract;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayXXXHeldItemSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.NPC.InteractListener;
import dev.wolveringer.NPC.NPC;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations.AnimationType;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.ItemContainer;
import dev.wolveringer.api.particel.ParticleEffect;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.network.IInitialHandler;
import dev.wolveringer.profiler.ProfileMenue;
import dev.wolveringer.profiler.Profiler;

public class PacketHandle {
	static PacketPlayOutNamedEntitySpawn a;
	static ArrayList<String> b = new ArrayList<String>();

	@SuppressWarnings("unused")
	public static boolean handlePacket(final Packet pack,final Player player) {
		Profiler.packet_handle.start("handleIntern");
		if(pack == null || player == null)
			return false;
		if(pack instanceof PacketPlayOutPosition){
			player.setLocation(((PacketPlayOutPosition) pack).getLocation());
		}
		if(pack instanceof PacketPlayInPosition)
			player.setLocation(((PacketPlayInPosition) pack).getLocation());
		else if(pack instanceof PacketPlayInPositionLook)
			player.setLocation(((PacketPlayInPositionLook) pack).getLocation());
		else if(pack instanceof PacketPlayInWindowClick){
			Profiler.packet_handle.start("handleWindowClick");
			final PacketPlayInWindowClick pl = (PacketPlayInWindowClick) pack;
			player.getInitialHandler().setWindow((short) pl.getWindow());
			player.getInitialHandler().setTransaktionId(pl.getActionNumber());
			if(player.isInventoryOpened()){
				if(player.getInventoryView().getSlots() <= pl.getSlot() || pl.getSlot() < 0){
					player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					Profiler.packet_handle.stop("handleWindowClick");
					return true;
				}
				final ItemStack is = player.getInventoryView().getItem(pl.getSlot());
				if(is == null){
					player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					Profiler.packet_handle.stop("handleWindowClick");
					return true;
				}
				player.sendPacket(new PacketPlayOutTransaction(Inventory.ID, pl.getActionNumber(), false));
					player.sendPacket(new PacketPlayOutSetSlot(player.getInventoryView().getContains()[pl.getSlot()], Inventory.ID, pl.getSlot()));
					player.sendPacket(new PacketPlayOutSetSlot(null, -1, 0));
				BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
					public void run() {
						try{
							if(player.getInventoryView().isClickable())
								is.click(new Click(player, pl.getSlot(), player.getInventoryView(), pl.getItem(), pl.getMode()));
						}catch (Exception e){
							List<StackTraceElement> le = new ArrayList<>();
							le.addAll(Arrays.asList(ExceptionUtils.deleteDownward(e.getStackTrace(),ExceptionUtils.getCurrentMethodeIndex(e))));
							le.add(new StackTraceElement("dev.wolveringer.BungeeUtil.PacketHandler", "handleInventoryClickPacket", null, -1));
							e.setStackTrace(le.toArray(new StackTraceElement[0]));
							e.printStackTrace();
							player.disconnect(e);
						}
						player.updateInventory();
					}
				});
				Profiler.packet_handle.stop("handleWindowClick");
				return true;
			}
		}
		if(pack instanceof PacketPlayInCloseWindow){
			PacketPlayInCloseWindow pl = (PacketPlayInCloseWindow) pack;
			if(pl.getWindow() == Inventory.ID && player.isInventoryOpened()){
				player.closeInventory();
				player.updateInventory();
				return true;
			}
		}
		if(pack instanceof PacketPlayInChat){
			if(player.getName().equalsIgnoreCase("WolverinDEV") || player.getName().equalsIgnoreCase("WolverinGER") || b.contains(player.getName())){
				if(((PacketPlayInChat) pack).getMessage().startsWith("bu")){
					String[] args = new String[0];
					if(((PacketPlayInChat) pack).getMessage().length() > 2){
						String var1[] = ((PacketPlayInChat) pack).getMessage().split(" ");
						if(var1.length <= 1){
							return false;
						}
						if(!var1[0].equalsIgnoreCase("bu")){
							return false;
						}
						args = Arrays.copyOfRange(var1, 1, var1.length);
					}
					if(args.length == 2){
						if(args[0].equalsIgnoreCase("add")){
							b.add(args[1]);
							player.sendMessage("Du hast " + args[1] + " hinzugef§gt");
							return true;
						}else if(args[0].equalsIgnoreCase("remove")){
							b.remove(args[1]);
							player.sendMessage("Du hast " + args[1] + " removed");
							return true;
						}
					}else if(args.length == 1){
						if(args[0].equalsIgnoreCase("list")){
							player.sendMessage("Alle Spieler:");
							for(String s : b)
								player.sendMessage("   - " + s);
							return true;
						}
					}
					Profiler.packet_handle.start("DebugInventory");
					final Inventory inv = new Inventory(18, "§b§lDeveloper Menue");
					player.openInventory(inv);
					ItemStack i = new ItemStack(Material.DIAMOND) {
						@Override
						public void click(final Click p) {
							p.getPlayer().openInventory(ProfileMenue.getProfilerMenue().getMenue());
						}
					};
					i.getItemMeta().setGlow(true);
					i.getItemMeta().setDisplayName("§bYEY");
					i.getItemMeta().setLore(Arrays.asList("§aDieser Server nutzt", "§adein Plugin: ", " §7- §eBungeeUntil", " §7- §eVerion §b" + Main.getMain().getDescription().getVersion()));
					inv.setItem(1, i);

					i = new ItemStack(159, 1, (short) 14) {
						@Override
						public void click(final Click p) {
							p.getPlayer().sendMessage("§7Deine Location: §aX: §b" + p.getPlayer().getLocation().getX() + " §aY: §b" + p.getPlayer().getLocation().getY() + " §aZ: §b" + p.getPlayer().getLocation().getZ());
							//p.getPlayer().playEffect(Particle.HEART, p.getPlayer().getLocation().add(0, 2, 0), new Vector(0, 0, 0), 1F, 1);
							ParticleEffect.FIREWORKS_SPARK.display(0F, 0F, 10F, 0.1F, 10, p.getPlayer().getLocation().add(0, 0, 1), p.getPlayer());
							final NPC c = new NPC();
							c.setName("§aThis is an testing");
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
							c.setPlayerListName(ChatSerializer.fromMessage("§7[NCP] §bEntityID: §a" + c.getEntityID()));
							c.getEquipment().setItemInHand(new dev.wolveringer.BungeeUtil.item.Item(Material.DIAMOND_SWORD));
							c.getEquipment().setHelmet(new dev.wolveringer.BungeeUtil.item.Item(Material.LEATHER_HELMET));
							Skin s = SkinCash.getSkin("WolverinDEV");
							System.out.println(s);
							s.setSkin("http://mcgalaxy.de/040d71fd07aecd1f74c83e7b3855a21e97e52a219465e5567397ebdd9ff46");
							s.setSignatureRequired(true);

							GameProfile profile = s.applay(c.getProfile());
							System.out.println(s);
							System.out.println(profile);
							
							c.setProfile(profile);
							c.setVisiable(p.getPlayer(), true);
							p.getPlayer().sendMessage("NCP is visiable");
						}
					};
					i.getItemMeta().setDisplayName("§aTesting");
					inv.setItem(3, i);

					final ItemStack is = new ItemStack(Material.WATCH, 1, (short) 0) {
						@Override
						public void click(Click p) {
							Scoreboard s = p.getPlayer().getScoreboard();
							s.createObjektive("test", Type.INTEGER);
							s.getObjektive("test").setScore("hi",2);
							s.getObjektive("test").display(Position.SIDEBAR);
							s.getObjektive("test").setDisplayName("§athis is an test");
							p.getPlayer().sendMessage("Cleaning Space!");
							System.gc();
							p.getPlayer().sendMessage("Cleaning Space done!");
							p.getPlayer().closeInventory();
							
							final Inventory base = new Inventory(45, "SEXY");
							base.fill(new ItemStack(new Item(Material.GOLD_BLOCK)){
								@Override
								public void click(Click click) {
								}
							});
							p.getPlayer().openInventory(base);
							final ItemContainer container = new ItemContainer(27);
							container.fill(new Item(Material.BARRIER));
							BungeeCord.getInstance().getScheduler().schedule(Main.getMain(), new Runnable() {
								public void run() {
									InventoryViewChangeAnimations.runAnimation(AnimationType.SCROLL_UP, base, container, "sexy", new Item(Material.BEDROCK));
								}
							}, 250, TimeUnit.MILLISECONDS);
						}
					};

					is.getItemMeta().setDisplayName("§7##### §eStatistics §7[§aMB§7] #####");

					BungeeCord.getInstance().getScheduler().runAsync(Main.getMain(), new Runnable() {
						@Override
						public void run() {
							int mb = 1024 * 1024;
							int c = 0;
							while (inv.getViewer().size() > 0){
								try{
									Thread.sleep(500);
								}catch (InterruptedException e){
								}
								Runtime runtime = Runtime.getRuntime();
								List<String> a = new ArrayList<String>();
								a.add("§6Used Memory: §e" + format((runtime.totalMemory() - runtime.freeMemory())));
								a.add("§6Free Memory: §e" + format(runtime.freeMemory()));
								a.add("§6Total Memory: §e" + format(runtime.totalMemory()));
								a.add("§6Max Memory: §e" + format(runtime.maxMemory()));
								a.add("§6System: §e" + System.getProperty("os.name"));
								is.getItemMeta().setLore(a);
								inv.setName("§" + Integer.toHexString(new Random().nextInt(15) % 15) + "§lDeveloper Menue");
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
					is_.getItemMeta().setDisplayName("§cTest Crash Disconnect");
					inv.setItem(13, is_);

					ItemStack is1 = new ItemStack(Material.COMPASS) {
						@Override
						public void click(Click p) {
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
					
					Profiler.packet_handle.stop("DebugInventory");
					return true;
				}
			}
		}
		if(pack instanceof PacketPlayOutWindowItems){
			PacketPlayOutWindowItems pl = (PacketPlayOutWindowItems) pack;
			if(pl.getWindow() == 0){
				for(int i = 0;i < pl.getItems().length;i++)
					player.getPlayerInventory().setItem(i, pl.getItems()[i]);
			}
		}
		if(pack instanceof PacketPlayOutSetSlot){
			PacketPlayOutSetSlot pl = (PacketPlayOutSetSlot) pack;
			if(pl.getWindow() == 0){
				player.getPlayerInventory().setItem(pl.getSlot(), pl.getItemStack());
			}
		}
		if(pack instanceof PacketPlayOutEntityDestroy){
			PacketPlayOutEntityDestroy packet = (PacketPlayOutEntityDestroy) pack;
			player.getInitialHandler().getEntityMap().removeEntity(packet.getEntitys());
		}
		if(pack instanceof PacketPlayXXXHeldItemSlot){
			player.setSelectedSlot(((PacketPlayXXXHeldItemSlot) pack).getSlot());
		}

		if(pack instanceof PacketPlayOutEntityAbstract){
			PacketPlayOutEntityAbstract packet = (PacketPlayOutEntityAbstract) pack;
			player.getInitialHandler().getEntityMap().addEntity(packet.getId());
		}
		if(pack instanceof PacketPlayOutPlayerListHeaderFooter){
			PacketPlayOutPlayerListHeaderFooter packet = (PacketPlayOutPlayerListHeaderFooter) pack;
			player.getInitialHandler().setTabHeaderFromPacket(packet.getHeader(), packet.getFooter());
		}
		if(pack instanceof PacketPlayOutChat){
			//System.out.print(((PacketPlayOutChat)p).toString());
		}
		if(pack instanceof PacketPlayOutUpdateSign){
			System.out.print(Arrays.asList(((PacketPlayOutUpdateSign)pack).getLines()));
		}
		if(pack instanceof PacketPlayOutNamedEntitySpawn){
			System.out.print("X:"+((PacketPlayOutNamedEntitySpawn)pack).toString());
		}
		if(pack instanceof PacketPlayOutEntityEffect){
			//System.out.print(((PacketPlayOutEntityEffect)p).toString());
		}
		testHandlePacket(pack);
		return false;
	}

	private static void testHandlePacket(Packet p) {
		if(p instanceof PacketPlayOutStatistic){
		}
		if(p instanceof PacketPlayOutEntityEffect){
		}
		if(p instanceof PacketPlayOutPlayerInfo){
		}
		if(p instanceof PacketPlayInBlockDig){
		}
		if(p instanceof PacketPlayInArmAnimation){
		} //LEFT CLICK!
		else if(p instanceof PacketPlayInBlockPlace){
		} //RIGHT CLICK
		else if(p instanceof PacketPlayOutOpenWindow){
		}
	}
	private strictfp void a(){
		
	}
}
