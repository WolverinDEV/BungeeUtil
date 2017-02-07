package dev.wolveringer.bungeeutil.inventory.anvil;

import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.InventoryType;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemBuilder;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketHandler;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayInPluginMessage;
import dev.wolveringer.bungeeutil.player.Player;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.protocol.DefinedPacket;

public class AnvilGui {

	@SuppressWarnings("unused")
	private static class AnvilWindowSizeStringCalculator {
		private static HashMap<Character, Double> charLength = new HashMap<>(); //Length = 1 = X
		private static final int BOX_SIZE = 18;
		static {
			for(int i = 0;i<Character.MAX_VALUE;i++) {
				charLength.put((char) i, 1D);
			}
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
			return this.reachBoarder(BOX_SIZE);
		}

		public boolean reachBoarder(int length){
			double out = 0;
			for(char c : this.message.toCharArray()) {
				out+=charLength.get(c);
			}
			return out+1>length;
		}


	}

	private static final Item DEFAULT_CENTER_ITEM;
	private static final Item DEFAULT_OUTPUT_ITEM;

	static {
		DEFAULT_CENTER_ITEM = new Item(Material.BARRIER);
		DEFAULT_CENTER_ITEM.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"r");

		DEFAULT_OUTPUT_ITEM = new Item(Material.NAME_TAG);
		DEFAULT_OUTPUT_ITEM.getItemMeta().setDisplayName(ChatColor.COLOR_CHAR+"aClick to finish");
	}

	private Player owner;
	private Inventory inv;
	
	private String curruntMessage = "";
	private String backgroundString = "Message here: ";
	
	//private Material backgroundMaterial = Material.STONE;
	private ItemBuilder backgroundBuilder = ItemBuilder.create().material(Material.ENCHANTED_BOOK);
	private String colorPrefix = ChatColor.GREEN.toString();
	private Item centerItem = DEFAULT_CENTER_ITEM;
	private Item outputItem = DEFAULT_OUTPUT_ITEM;

	private String curruntItemDisplayName = "";

	private boolean backgroundHidden = false;

	@SuppressWarnings("unused")
	private String curruntDisplayString = "";

	private ArrayList<AnvilGuiListener> listener = new ArrayList<>();

	private PacketHandler<Packet> packet = new PacketHandler<Packet>() {
		int backgroundCount = 0;

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


		@Override
		public void handle(PacketHandleEvent<Packet> e) {
			if (e.getPacket() instanceof PacketPlayInPluginMessage) { //Message changed
				PacketPlayInPluginMessage packet = (PacketPlayInPluginMessage) e.getPacket();
				if (packet.getChannel().equalsIgnoreCase("MC|ItemName")) {
					if (e.getPlayer().equals(AnvilGui.this.owner) && AnvilGui.this.inv != null) {
						String message = DefinedPacket.readString(packet.getCopiedbyteBuff());
						//if(AnvilGui.this.curruntItemDisplayName.equalsIgnoreCase(message)) {
						//	return;
						//}
						AnvilGui.this.curruntItemDisplayName = message;
						if(AnvilGui.this.colorPrefix.length() > message.length()) {
							message = AnvilGui.this.colorPrefix;
						}
						message = message.substring(AnvilGui.this.colorPrefix.length(), message.length()); // replace
	                                                                                         // color
	                                                                                         // prefix
						String handleMessage = message;
						if (message.length() == 0 && AnvilGui.this.backgroundHidden) {
							AnvilGui.this.inv.setItem(0, backgroundBuilder.clone().name(AnvilGui.this.curruntItemDisplayName = AnvilGui.this.colorPrefix + AnvilGui.this.backgroundString).build());
							AnvilGui.this.backgroundHidden = false;
							handleMessage = "";
							return;
						}

						if (this.buildOutString(message, AnvilGui.this.backgroundString).length() <= 1 && AnvilGui.this.backgroundHidden == false) {// Checking
	                                                                                                           // for
	                                                                                                           // background
							if (this.backgroundCount++ > 0) {
								handleMessage = "";
								AnvilGui.this.backgroundHidden = true;
								this.backgroundCount = 0;
								String newMessage = this.buildOutString(AnvilGui.this.backgroundString, AnvilGui.this.backgroundString.substring(0, Math.min(AnvilGui.this.backgroundString.length(), message.length())));
								if (newMessage.length() == 0) {// No extra chars
	                                                           // found!
									if (message.length() < AnvilGui.this.backgroundString.length()) {// Char
	                                                                                   // removed!
										newMessage = "";
									}
									else { // Char added at the end
										newMessage = message.substring(AnvilGui.this.backgroundString.length(), message.length());
									}
								}
								AnvilGui.this.inv.setItem(0, backgroundBuilder.clone().name(AnvilGui.this.curruntItemDisplayName = AnvilGui.this.colorPrefix + AnvilGui.this.backgroundString).build());
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

						handleMessage = handleMessage.replaceFirst(AnvilGui.this.backgroundString, ""); // For safty
						handleMessage = ChatColorUtils.stripColor(handleMessage);
						if (handleMessage.startsWith(" ")) {
							handleMessage = handleMessage.substring(1);
						}
						AnvilGui.this.curruntMessage = handleMessage;
						AnvilGui.this.curruntItemDisplayName = AnvilGui.this.colorPrefix + (handleMessage.isEmpty() ? AnvilGui.this.backgroundString : AnvilGui.this.curruntMessage);
						AnvilGui.this.updateBackgroundItem();
						for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener)) {
							listener.onMessageChange(AnvilGui.this, handleMessage);
						}
						if(!AnvilGui.this.inv.equals(AnvilGui.this.owner.getInventoryView())){
							PacketLib.removeHandler(this);
						}
					//	System.out.println("Boarderreach: "+new AnvilWindowSizeStringCalculator(handleMessage).reachBoarder(18));
					}
				}
			}
			else if(e.getPacket() instanceof PacketPlayInCloseWindow){
				PacketPlayInCloseWindow packet = (PacketPlayInCloseWindow) e.getPacket();
				if(AnvilGui.this.owner.equals(e.getPlayer()) && (packet.getWindow() == Inventory.ID || AnvilGui.this.inv.getViewer().isEmpty())){
					for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener)) {
						listener.onClose(AnvilGui.this);
					}
					PacketLib.removeHandler(this);
				}
			}
		}
	};

	public AnvilGui(Player owner) {
		this.owner = owner;
		PacketLib.addHandler(this.packet);
	}

	public void addListener(AnvilGuiListener listener){
		this.listener.add(listener);
	}
	public void close(){
		for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener)) {
			if(listener != null) {
				listener.onClose(AnvilGui.this);
			}
		}

		if(this.owner.getInventoryView() != null) {
			if(this.owner.getInventoryView().equals(this.inv)) {
				this.owner.closeInventory();
			}
		}
		PacketLib.removeHandler(this.packet);
	}

	public String getBackgroundMessage() {
		return this.backgroundString;
	}

	public Item getCenterItem() {
		return this.centerItem;
	}

	public String getCurruntInput() {
		return this.curruntMessage;
	}

	public Item getOutputItem() {
		return this.outputItem;
	}

	protected void handleSuccessClick(){
		for(AnvilGuiListener listener : new ArrayList<>(AnvilGui.this.listener)) {
			if(listener != null) {
				listener.onConfirmInput(AnvilGui.this, this.curruntMessage);
			}
		}
		if(this.owner.getInventoryView() != null) {
			if(this.owner.getInventoryView().equals(this.inv)) {
				this.owner.closeInventory();
			}
		}
		PacketLib.removeHandler(this.packet);
	}

	public void open() {
		this.inv = new Inventory(InventoryType.Anvil, "This is an AnvilGui by WolverinDEV");

		updateBackgroundItem();
		this.inv.setItem(1, new ItemStack(this.centerItem) {
			@SuppressWarnings("deprecation")
			@Override
			public void click(Click click) {
				click.setCancelled(true);
			}
		});

		this.inv.setItem(2, new ItemStack(this.outputItem){
			@SuppressWarnings("deprecation")
			@Override
			public void click(Click click) {
				click.setCancelled(true);
				AnvilGui.this.handleSuccessClick();
			}
		});

		this.owner.openInventory(this.inv);
	}

	public void removeListener(AnvilGuiListener listener){
		this.listener.remove(listener);
	}

	public void setBackgroundItem(ItemBuilder builder) {
		this.backgroundBuilder = builder;
		updateBackgroundItem();
	}

	public void setBackgroundMessage(String backgroundString) {
		this.backgroundString = backgroundString;
		if(!this.backgroundHidden){
			updateBackgroundItem();
		}
	}

	public void setCenterItem(Item centerItem) {
		this.centerItem = centerItem;
		if(this.inv != null)
			this.inv.setItem(1, centerItem);
	}

	public void setColorPrefix(String prefix) {
		if (this.colorPrefix.equalsIgnoreCase(prefix)) {
			return;
		}
		String rawMeta = this.curruntItemDisplayName.substring(Math.min(this.colorPrefix.length(), this.curruntItemDisplayName.length())); //Backspace a color prefix code... fix
		this.curruntItemDisplayName = rawMeta;
		this.colorPrefix = prefix;
		updateBackgroundItem();
	}

	public void setCurruntInput(String curruntName) {
		this.curruntMessage = curruntName;
		if(backgroundHidden)
			AnvilGui.this.curruntItemDisplayName = this.colorPrefix + this.curruntMessage;
		else
			AnvilGui.this.curruntItemDisplayName = this.colorPrefix + this.backgroundString;
		updateBackgroundItem();
	}
	public void setOutputItem(Item item){
		this.outputItem = item;
		if(this.inv != null)
			this.inv.setItem(2, new ItemStack(this.outputItem){
				@SuppressWarnings("deprecation")
				@Override
				public void click(Click click) {
					click.setCancelled(true);
					AnvilGui.this.handleSuccessClick();
				}
			});
	}
	
	private void updateBackgroundItem(){
		if(this.inv != null)
			this.inv.setItem(0, backgroundBuilder.clone().name(AnvilGui.this.curruntItemDisplayName).listener((click)->{
				click.setCancelled(true);
			}).build());
	}
}
