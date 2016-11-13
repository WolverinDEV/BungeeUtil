package dev.wolveringer.bungeeutil.inventory.anvil;

import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.BungeeUtil.packetlib.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.packetlib.PacketHandler;
import dev.wolveringer.BungeeUtil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.InventoryType;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayInPluginMessage;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import net.md_5.bungee.protocol.DefinedPacket;

public class AnvilGui {
	
	private static class AnvilWindowSizeStringCalculator {
		private static HashMap<Character, Double> charLength = new HashMap<>(); //Length = 1 = X
		private static final int BOX_SIZE = 18;
		static {
			for(int i = 0;i<Character.MAX_VALUE;i++)
				charLength.put((char) i, 1D);
			charLength.put('I', 18D/26D); //26 in 
			charLength.put('i', 18D/52D); //Todo
			charLength.put('j', 18D/28D); 
			charLength.put('l', 18D/34D);//Todo
		}
		
		private String message;
		
		public AnvilWindowSizeStringCalculator(String message) {
			this.message = message;
		}
		
		public boolean boarderReached(){
			return reachBoarder(BOX_SIZE);
		}
		
		public boolean reachBoarder(int length){
			double out = 0;
			for(char c : message.toCharArray())
				out+=charLength.get(c);
			return out+1>length;
		}
		
		
	}
	
	private static final Item DEFAULT_CENTER_ITEM;
	private static final Item DEFAULT_OUTPUT_ITEM;
	
	static {
		DEFAULT_CENTER_ITEM = new Item(Material.BARRIER);
		DEFAULT_CENTER_ITEM.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR+"r");
		
		DEFAULT_OUTPUT_ITEM = new Item(Material.NAME_TAG);
		DEFAULT_OUTPUT_ITEM.getItemMeta().setDisplayName(ChatColorUtils.COLOR_CHAR+"aClick to finish");
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
	
	private String curruntDisplayString = "";
	
	private ArrayList<AnvilGuiListener> listener = new ArrayList<>();
	
	private PacketHandler<Packet> packet = new PacketHandler<Packet>() {
		int backgroundCount = 0;
		
		@Override
		public void handle(PacketHandleEvent<Packet> e) {
			if (e.getPacket() instanceof PacketPlayInPluginMessage) { //Message changed
				PacketPlayInPluginMessage packet = (PacketPlayInPluginMessage) e.getPacket();
				if (packet.getChannel().equalsIgnoreCase("MC|ItemName")) {
					if (e.getPlayer().equals(owner) && inv != null) {
						String message = DefinedPacket.readString(packet.getCopiedbyteBuff());
						if(curruntItemDisplayName.equalsIgnoreCase(message) && false)
							return;
						curruntItemDisplayName = message;
						if(colorPrefix.length() > message.length()) //Backspace (color prefix deleted!)
							message = colorPrefix;
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
						else
						{
							/*
							ItemStack item = new ItemStack(backgroundMaterial) {
								@Override
								public void click(Click click) {
									click.setCancelled(true);
								}
							};
							item.getItemMeta().setDisplayName(curruntItemDisplayName);
							inv.setItem(0, item);
							*/
						}
						
						handleMessage = handleMessage.replaceFirst(backgroundString, ""); // For safty
						handleMessage = ChatColorUtils.stripColor(handleMessage);
						if (handleMessage.startsWith(" ")) handleMessage = handleMessage.substring(1);
						curruntMessage = handleMessage;
						for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
							listener.onMessageChange(AnvilGui.this, handleMessage);
						if(!inv.equals(owner.getInventoryView())){
							PacketLib.removeHandler(this);
						}
					//	System.out.println("Boarderreach: "+new AnvilWindowSizeStringCalculator(handleMessage).reachBoarder(18)); 
					}
				}
			}
			else if(e.getPacket() instanceof PacketPlayInCloseWindow){
				PacketPlayInCloseWindow packet = (PacketPlayInCloseWindow) e.getPacket();
				if(owner.equals(e.getPlayer()) && (packet.getWindow() == Inventory.ID || inv.getViewer().isEmpty())){
					for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
						listener.onClose(AnvilGui.this);
					PacketLib.removeHandler(this);
					System.out.println("Removing this. ("+this+"/"+")");
					PacketLib.printListener();
				}
			}
		}

		
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
		String rawMeta = curruntItemDisplayName.substring(Math.min(colorPrefix.length(), curruntItemDisplayName.length())); //Backspace a color prefix code... fix
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
			if(listener != null)
			listener.onConfirmInput(AnvilGui.this, this.curruntMessage);
		if(owner.getInventoryView() != null)
			if(owner.getInventoryView().equals(this.inv))
				owner.closeInventory();
		PacketLib.removeHandler(packet);
	}
	
	public void close(){
		for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener))
			if(listener != null)
				listener.onClose(AnvilGui.this);
		
		if(owner.getInventoryView() != null)
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
