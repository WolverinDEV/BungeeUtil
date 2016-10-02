package dev.wolveringer.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.BungeeUtil;
import dev.wolveringer.BungeeUtil.CostumPrintStream;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.Property;
import dev.wolveringer.BungeeUtil.gameprofile.PropertyMap;
import dev.wolveringer.BungeeUtil.gameprofile.Skin;
import dev.wolveringer.BungeeUtil.gameprofile.SkinFactory;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar.BarColor;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar.BarDivision;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardDisplayObjective.Position;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardObjective.Type;
import dev.wolveringer.NPC.InteractListener;
import dev.wolveringer.NPC.NPC;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations;
import dev.wolveringer.animations.inventory.InventoryViewChangeAnimations.AnimationType;
import dev.wolveringer.animations.inventory.LimetedScheduller;
import dev.wolveringer.api.bossbar.BossBarManager.BossBar;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;
import dev.wolveringer.api.gui.AnvilGui;
import dev.wolveringer.api.gui.AnvilGuiListener;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.ItemContainer;
import dev.wolveringer.api.particel.ParticleEffect;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.api.sound.SoundEffect;
import dev.wolveringer.chat.ChatComponentText;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.profiler.ProfileMenue;
import dev.wolveringer.profiler.Profiler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;

public class DebugMenue {
	public static void open(Player player){
		Profiler.packet_handle.start("buildDebugInventory");
		final Inventory inv = new Inventory(27, ChatColorUtils.COLOR_CHAR + "b" + ChatColorUtils.COLOR_CHAR + "lDeveloper Menue");
		
		player.openInventory(inv);
		ItemStack i = new ItemStack(Material.DIAMOND) {
			@Override
			public void click(final Click p) {
				p.getPlayer().openInventory(ProfileMenue.getProfilerMenue().getMenue());
			}
		};
		i.getItemMeta().setGlow(true);
		i.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "bYEY");
		i.getItemMeta().setLore(Arrays.asList(ChatColorUtils.COLOR_CHAR + "aDieser Server nutzt", ChatColorUtils.COLOR_CHAR + "adein Plugin: ", " " + ChatColorUtils.COLOR_CHAR + "7- " + ChatColorUtils.COLOR_CHAR + "eBungeeUntil", " " + ChatColorUtils.COLOR_CHAR + "7- " + ChatColorUtils.COLOR_CHAR + "eVerion " + ChatColorUtils.COLOR_CHAR + "b" + BungeeUtil.getPluginInstance().getDescription().getVersion()));
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
							ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor((int) (0xFF * (((d + count * 2 * steps) % max) / max)), 0x00, (int) (0xFF - 0xFF * (((d + count * 2 * steps) % max) / max))), target.clone().add(target.getDirection().multiply(d)).add(0D, 2 + 1.6D, 0D), p.getPlayer());
						}
					}
				}.start();
				p.getPlayer().sendMessage(ChatColorUtils.COLOR_CHAR + "7Deine Location: " + ChatColorUtils.COLOR_CHAR + "aX: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getX() + " " + ChatColorUtils.COLOR_CHAR + "aY: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getY() + " " + ChatColorUtils.COLOR_CHAR + "aZ: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getZ() + " " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "aYaw: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getYaw() + ChatColorUtils.COLOR_CHAR + "7, " + ChatColorUtils.COLOR_CHAR + "aPitch: " + ChatColorUtils.COLOR_CHAR + "b" + p.getPlayer().getLocation().getPitch() + ChatColorUtils.COLOR_CHAR + "7]");
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
					if (i != null) c.getEquipment().setItemInOffHand(i);
				}
				c.getEquipment().setHelmet(new dev.wolveringer.BungeeUtil.item.Item(Material.LEATHER_HELMET));
				Skin s = SkinFactory.getSkin("WolverinGER");
				BungeeUtil.getInstance().sendMessage(s + "");
				
				GameProfile profile = s.applay(c.getProfile());
				
				BungeeUtil.getInstance().sendMessage(s + "");
				BungeeUtil.getInstance().sendMessage(profile + "");
				
				c.setProfile(profile);
				c.setVisiable(p.getPlayer(), true);
				ParticleEffect.HEART.display(0F, 0F, 1F, 0F, 1, c.getLocation(), p.getPlayer());
				p.getPlayer().sendMessage("NCP is visiable");
				
				NPC npc = new NPC();
				npc.setPing(1);
		        npc.setPlayerListName(new ChatComponentText("§a-----NPC----"));
		        npc.setTabListed(true);
		        Item item = new Item(Material.WATCH);
		        item.getItemMeta().setGlow(true);
		        npc.getEquipment().setItemInHand(item);
		        PropertyMap pm = new PropertyMap();
		        pm.put("§c神奇的NPC", new Property("textures",
		                "eyJ0aW1lc3RhbXAiOjE0NTg0NTMxNjM3MDksInByb2ZpbGVJZCI6ImNhZWZmYmFhMzdhZDRhYjI5Mjg5NGQxZDEzOTk4YTg5IiwicHJvZmlsZU5hbWUiOiJ3YXlfX3plciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQzYzdmN2E3ZjIyZTU5NDIxYzRkZDI3NjY2ZDVlZTlmYzc4MTEwODE3MTU3MmQxYzRlMzZkMzhmMjZjOWQzIn19fQ==",
		                "ofNqZSqPhaWL4b8mHRUpv+vUVw3bqXH1mhvoZzyaxXOTE4TqXfapJRTmct4KV5r25ezW9in9zryaadGX4yueERYZzUkVj5MJqduI44z/rqc1oy4NSTqRAHzaka6PWD/DZyG8kySKhDXBhwEKKeGTHUAeohbLtyfvs3FRLxMf6GkyEzibM32hj65eln9itaqKH6uU9l7bzjPfNx3RlLOg2LbmdBIajXffSDQBgklSOg/v/7OiD5fXYREWzYPNex5Iiw8id0NFTTpQE4Dvrv31ijXcnWsS4Mp62+zPYponsloPynZ9AqQaXVeuLJQhSwJktkavThqz9dfaS9+IGn1Ko7AUhFyLxWjqbet6M7sKd29sqlWbdxB8LMh6I+RMNb9Tx4yKa0EeedFHXfYMaURs2TdPMO2QtiM3nG+IieaXaZwh64wyf/u3iuu3sNd4/s1JmOM9nVYvVaIQVsT80HW/NQwz/N+ufJOh4L7unTt/Jlwm4DkldaTKzUiuVLB4ypYON2/Pa1dXBgRZbU7dCwjXuAL/Cox1UKqYcq3uEuCKhBWYFdEe2p7NDNTuFhjX38MP+h94SlFjbfaSpzDegjr92qO8r41JqpcyewQYI2fkwk8Ju9AJDMTpeoA6ofHEzAMhajXkViKvUPoPbl4HUDvtRBZu5k8dggWKQ83v1UXsDxQ="));
		        npc.getProfile().setProperties(pm);
		        HumanDataWatcher dw = npc.getDatawatcher();
		        dw.setParicelColor(1224755642);
		        dw.setSkinFlags((byte) 127);
		        npc.setName("§c神奇的NPC");
		        npc.addListener(new InteractListener() {

		            @Override
		            public void rightClick(Player p) {
		                    p.sendMessage(new TextComponent("§c§l[NPC] §cHI! §c§l" + p.getDisplayName() + "  §c§l请先登录,再尝试打开"));
		            }

		            @Override
		            public void leftClick(Player p) {
		                p.sendMessage(new TextComponent("§c§l[NPC] §cHI! §c§l" + p.getDisplayName() + "  §a欢迎来到MFT服务器"));
		            }
		        });

		        npc.setLocation(p.getPlayer().getLocation().add(0, 5, 0));
		        npc.setVisiable(p.getPlayer(), true);
			}
		};
		i.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "aTesting");
		inv.setItem(3, i);
		
		final ItemStack is = new ItemStack(Material.WATCH, 1, (short) 0) {
			@Override
			public void click(final Click p) {
				final Scoreboard s = p.getPlayer().getScoreboard();
				if (s.getObjektive("test") == null) {
					s.createObjektive("test", Type.INTEGER);
					s.getObjektive("test").setScore("§a-----------", -1);
					s.getObjektive("test").setScore("§aHello world", -2);
					s.getObjektive("test").display(Position.SIDEBAR);
					s.getObjektive("test").setDisplayName(ChatColorUtils.COLOR_CHAR + "athis is an test");
				}
				else {
					s.removeObjektive("test");
				}
				if (p.getPlayer().getVersion().getBigVersion() != BigClientVersion.v1_8) {
					BossBar var0 = null;
					
					var0 = p.getPlayer().getBossBarManager().createNewBossBar();
					var0.setColor(BarColor.GREEN);
					var0.setDivision(BarDivision.NO_DIVISION);
					var0.setHealth(0F);
					var0.setMessage(ChatSerializer.fromMessage("§cHello world"));
					var0.display();
					
					p.getPlayer().sendMessage("Your boss bars:");
					for (BossBar bar : p.getPlayer().getBossBarManager().getActiveBossBars())
						p.getPlayer().sendMessage("  §7- " + ChatSerializer.toMessage(bar.getMessage()));
					final BossBar bar = var0;
					new LimetedScheduller(32, 250, TimeUnit.MILLISECONDS) {
						int currunt = 0;
						
						@Override
						public void run(int count) {
							if (s.getObjektive("test") != null) {
								s.getObjektive("test").removeScore(ChatColorUtils.COLOR_CHAR + Integer.toHexString((currunt) % 16) + "Testing score");
								currunt += 1;
								s.getObjektive("test").setScore(ChatColorUtils.COLOR_CHAR + Integer.toHexString(currunt % 16) + "Testing score", currunt % 16);
							}
							if (bar != null) {
								bar.setMessage(ChatSerializer.fromMessage(ChatColorUtils.COLOR_CHAR + Integer.toHexString((currunt) % 16) + "Hello world"));
								bar.dynamicChangeHealth((float) ((float) count / (float) limit), 250, TimeUnit.MILLISECONDS);
							}
						}
						
						@Override
						public void done() {
							s.removeObjektive("test");
							if (bar != null) {
								bar.setColor(BarColor.RED);
								BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
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
				base.fill(new ItemStack(new Item(Material.NAME_TAG)) {
					@Override
					public void click(Click click) {
					}
				});
				p.getPlayer().openInventory(base);
				final ItemContainer container = new ItemContainer(27);
				container.fill(new Item(Material.BARRIER));
				BungeeCord.getInstance().getScheduler().schedule(BungeeUtil.getPluginInstance(), new Runnable() {
					public void run() {
						InventoryViewChangeAnimations.runAnimation(AnimationType.SCROLL_LEFT, base, container, "sexy", new Item(Material.BEDROCK), 500);
					}
				}, 500, TimeUnit.MILLISECONDS);
			}
		};
		
		is.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR + "7##### " + ChatColorUtils.COLOR_CHAR + "eStatistics " + ChatColorUtils.COLOR_CHAR + "7[" + ChatColorUtils.COLOR_CHAR + "aMB" + ChatColorUtils.COLOR_CHAR + "7] #####");
		
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				int mb = 1024 * 1024;
				int c = 0;
				while (inv.getViewer().size() > 0) {
					try {
						Thread.sleep(500);
					}
					catch (InterruptedException e) {
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
		inv.setItem(22, is_);
		
		ItemStack is1 = new ItemStack(Material.COMPASS) {
			@Override
			public void click(Click p) {
				p.getPlayer().sendMessage("Sound sended");
				if(SoundEffect.BLOCK_ANVIL_FALL.isAvariable(p.getPlayer().getVersion().getBigVersion()))
					p.getPlayer().playSound(SoundEffect.BLOCK_ANVIL_LAND, p.getPlayer().getLocation(), 1F, 0);
				else
					p.getPlayer().sendMessage("Sound not avariable for "+p.getPlayer().getVersion().toString());
				//p.getPlayer().playSound(SoundEffect.getEffect("block.anvil.land"), SoundCategory.MASTER, p.getPlayer().getLocation(), 1F, 0);
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
		
		i = new ItemStack(Material.EMERALD){
			@Override
			public void click(final Click click) {
				click.getPlayer().sendMessage("Open anvil menue");
				AnvilGui guy = new AnvilGui(click.getPlayer());
				guy.addListener(new AnvilGuiListener() {
					@Override
					public void onMessageChange(AnvilGui guy, String newMessage) {
				    	//Changing text color
				    	if ("HelloWorld #Yolo".startsWith(newMessage)){
				    		guy.setColorPrefix("§6");
				    		guy.setCenterItem(new Item(Material.getMaterial(351),1,(byte)10));
						}
						else if(newMessage.startsWith("HelloWorld #Yolo")){
							guy.setColorPrefix("§a");
							guy.setCurruntInput("You did it!");
						}
						else{
							guy.setColorPrefix("§c");
							guy.setCenterItem(new Item(Material.getMaterial(351),1,(byte)1));
						}
				    	
				    	//Update output item ;)
						Item item = new Item(Material.ENCHANTED_BOOK);
				    	item.getItemMeta().setDisplayName("§aYour message: §e" + (newMessage.length() == 0 ? "§cNo message" : newMessage));
				    	guy.setOutputItem(item);
					}
					
					@Override
					public void onConfirmInput(AnvilGui guy, String message) {
						click.getPlayer().sendMessage("You confirmed you input. Your input: "+message);
						click.getPlayer().closeInventory();
					}
					
					@Override
					public void onClose(AnvilGui guy) {
						click.getPlayer().sendMessage("Your last input wars: "+guy.getCurruntInput());
					}
				});
				guy.open();
			}
		};
		i.getItemMeta().setDisplayName("§aTesting anvil guy");
		inv.setItem(10, i);
		
		Profiler.packet_handle.stop("buildDebugInventory");
	}
}
