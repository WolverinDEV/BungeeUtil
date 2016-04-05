package dev.wolveringer.api.guy;

import java.util.ArrayList;

import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPluginMessage;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.InventoryType;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import net.md_5.bungee.protocol.DefinedPacket;

public class AnvilGui {
	private static final Item DEFAULT_CENTER_ITEM;
	private static final Item DEFAULT_OUTPUT_ITEM;
	
	static {
		DEFAULT_CENTER_ITEM = new Item(Material.BARRIER);
		DEFAULT_CENTER_ITEM.getItemMeta().setDisplayName("§r");
		
		DEFAULT_OUTPUT_ITEM = new Item(Material.NAME_TAG);
		DEFAULT_OUTPUT_ITEM.getItemMeta().setDisplayName("§aClick to finish");
		//private static final Item DEFAULT_CENTER_ITEM;
	}
	
	private Player owner;
	private Inventory inv;
	private String curruntMessage = "";
	private String backgroundString = "Message here: ";
	private Material backgroundMaterial = Material.STONE;
	private String colorPrefix = "§a";
	private Item centerItem = DEFAULT_CENTER_ITEM;
	private Item outputItem = DEFAULT_OUTPUT_ITEM;
	
	private String curruntItemDisplayName = "";
	
	private boolean noBackground = false;
	
	private ArrayList<AnvilGuiListener> listener = new ArrayList<>();
	
	private PacketHandler<Packet> packet = new PacketHandler<Packet>() {
		int backgroundCount = 0;
		
		@Override
		public void handle(PacketHandleEvent<Packet> e) {
			if (e.getPacket() instanceof PacketPlayInPluginMessage) { //Message changed
				PacketPlayInPluginMessage packet = (PacketPlayInPluginMessage) e.getPacket();
				if (packet.getChannel().equalsIgnoreCase("MC|ItemName")) {
					if (e.getPlayer().equals(owner) && inv != null) {
						String message = curruntItemDisplayName = DefinedPacket.readString(packet.getCopiedbyteBuff());
						message = message.substring(colorPrefix.length(), message.length()); // replace
	                                                                                         // color
	                                                                                         // prefix
						
						String handleMessage = message;
						
						if (message.length() == 0 && noBackground) {
							ItemStack item = new ItemStack(backgroundMaterial) {
								@Override
								public void click(Click click) {
									click.setCancelled(true);
								}
							};
							item.getItemMeta().setDisplayName(curruntItemDisplayName = colorPrefix + backgroundString);
							inv.setItem(0, item);
							noBackground = false;
							handleMessage = "";
							return;
						}
						if (buildOutString(message, backgroundString).length() <= 1 && noBackground == false) {// Checking
	                                                                                                           // for
	                                                                                                           // background
							if ((backgroundCount++ > 0)) {
								handleMessage = "";
								noBackground = true;
								backgroundCount = 0;
								String newMessage = buildOutString(backgroundString, backgroundString.substring(0, Math.min(backgroundString.length(), message.length())));
								if (newMessage.length() == 0) {// No extra chars
	                                                           // found!
									if (message.length() < backgroundString.length()) {// Char
	                                                                                   // removed!
										newMessage = "";
									}
									else { // Char added at the end
										newMessage = message.substring(backgroundString.length(), message.length());
									}
								}
								ItemStack item = new ItemStack(backgroundMaterial) {
									@Override
									public void click(Click click) {
										click.setCancelled(true);
									}
								};
								item.getItemMeta().setDisplayName(curruntItemDisplayName = colorPrefix + newMessage);
								inv.setItem(0, item);
							}
						}
						
						handleMessage = handleMessage.replaceFirst(backgroundString, ""); // For safty
						handleMessage = ChatColorUtils.stripColor(handleMessage);
						if (handleMessage.startsWith(" ")) handleMessage = handleMessage.substring(1);
						curruntMessage = handleMessage;
						for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
							listener.onMessageChange(AnvilGui.this, handleMessage);
					}
				}
			}
			else if(e.getPacket() instanceof PacketPlayInCloseWindow){
				PacketPlayInCloseWindow packet = (PacketPlayInCloseWindow) e.getPacket();
				if(owner.equals(e.getPlayer()) && (packet.getWindow() == Inventory.ID || inv.getViewer().isEmpty())){
					for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
						listener.onClose(AnvilGui.this);
					PacketLib.removeHandler(this);
				}
			}
		}
		/**
		 * 
		 * @param in
		 *            XXXXX
		 * @param in2
		 *            YYXYY
		 * @return
		 */
		
		private String buildOutString(String in, String in2) {
			char[] ca = in.toCharArray();
			int cpa = 0;
			char[] cb = in2.toCharArray();
			int cpb = 0;
			String out = "";
			for (; Math.max(cpa, cpb) < Math.min(ca.length, cb.length);) {
				if (ca[cpa] == cb[cpb]) {
					cpa++;
					cpb++;
				}
				else if (cpb + 1 < cb.length && ca[cpa] == cb[cpb + 1]) {
					out = out += new String(new char[] { cb[cpb] });
					cpb++;
				}
				else if (cpa + 1 < ca.length && ca[cpa + 1] == cb[cpb]) {
					cpa++;
				}
				else {
					// System.out.print("Non one char added!");
					break;
				}
			}
			return out;
		}
	};
	
	public AnvilGui(Player owner) {
		this.owner = owner;
		PacketLib.addHandler(packet);
		backgroundMaterial = Material.ENCHANTED_BOOK;
	}
	
	public void addListener(AnvilGuiListener listener){
		this.listener.add(listener);
	}
	public void removeListener(AnvilGuiListener listener){
		this.listener.remove(listener);
	}
	
	public void open() {
		inv = new Inventory(InventoryType.Anvil, "This is an AnvilGuy by WolverinDEV");
		
		ItemStack item = new ItemStack(backgroundMaterial) {
			@Override
			public void click(Click click) {
				click.setCancelled(true);
				handleSuccessClick();
			}
		};
		item.getItemMeta().setDisplayName(curruntItemDisplayName = colorPrefix + backgroundString);
		inv.setItem(0, item);
		
		inv.setItem(1, new ItemStack(centerItem) {
			@Override
			public void click(Click click) {
				click.setCancelled(true);
			}
		});
		
		inv.setItem(2, new ItemStack(outputItem){
			@Override
			public void click(Click click) {
				click.setCancelled(true);
				handleSuccessClick();
			}
		});
		
		owner.openInventory(inv);
	}
	
	public void setBackgroundMaterial(Material backgroundMaterial) {
		this.backgroundMaterial = backgroundMaterial;
		ItemStack item = new ItemStack(backgroundMaterial) {
			@Override
			public void click(Click click) {
				click.setCancelled(true);
			}
		};
		item.getItemMeta().setDisplayName(colorPrefix+curruntMessage);
		inv.setItem(0, item);
	}
	
	public void setBackgroundMessage(String backgroundString) {
		this.backgroundString = backgroundString;
		if(!noBackground){
			ItemStack item = new ItemStack(backgroundMaterial) {
				@Override
				public void click(Click click) {
					click.setCancelled(true);
				}
			};
			item.getItemMeta().setDisplayName(colorPrefix+backgroundString);
			inv.setItem(0, item);
		}
	}
	
	public void setCenterItem(Item centerItem) {
		this.centerItem = centerItem;
		this.inv.setItem(1, centerItem);
	}
	
	public Item getCenterItem() {
		return centerItem;
	}
	
	public String getBackgroundMessage() {
		return backgroundString;
	}
	
	public String getCurruntInput() {
		return curruntMessage;
	}
	
	public void setCurruntInput(String curruntName) {
		this.curruntMessage = curruntName;
		ItemStack item = new ItemStack(backgroundMaterial) {
			@Override
			public void click(Click click) {
				click.setCancelled(true);
			}
		};
		item.getItemMeta().setDisplayName(colorPrefix+curruntName);
		inv.setItem(0, item);
	}
	
	public void setColorPrefix(String prefix) {
		if (colorPrefix.equalsIgnoreCase(prefix)) return;
		String rawMeta = curruntItemDisplayName.substring(colorPrefix.length());
		colorPrefix = prefix;
		ItemStack item = new ItemStack(backgroundMaterial) {
			@Override
			public void click(Click click) {
				click.setCancelled(true);
			}
		};
		item.getItemMeta().setDisplayName(curruntItemDisplayName = colorPrefix + rawMeta);
		inv.setItem(0, item);
	}
	
	protected void handleSuccessClick(){
		for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
			listener.onConfirmInput(AnvilGui.this, this.curruntMessage);
		
		if(owner.getInventoryView().equals(this.inv))
			owner.closeInventory();
		PacketLib.removeHandler(packet);
	}
	
	public void setOutputItem(Item item){
		inv.setItem(2, new ItemStack(outputItem = item){
			@Override
			public void click(Click click) {
				click.setCancelled(true);
				handleSuccessClick();
			}
		});
	}
	public Item getOutputItem() {
		return outputItem;
	}
}
