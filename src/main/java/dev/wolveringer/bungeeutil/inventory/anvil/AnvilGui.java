package dev.wolveringer.bungeeutil.inventory.anvil;

import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.bungeeutil.BungeeUtil;
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
import lombok.NonNull;
import net.md_5.bungee.api.Callback;
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
		DEFAULT_CENTER_ITEM.getItemMeta().setDisplayName(ChatColor.RESET.toString());

		DEFAULT_OUTPUT_ITEM = new Item(Material.NAME_TAG);
		DEFAULT_OUTPUT_ITEM.getItemMeta().setDisplayName(ChatColor.GREEN+"Click to finish");
	}

	private Player owner;
	private Inventory anvilHandle;
	
	private String curruntMessage = "";
	private String backgroundString = "Message here: ";
	
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
		private String getInsertedString(String org, String message){
			for(int i = 0;i<org.length();i++){
				if(org.charAt(i) != message.charAt(i)){
					for(int j = i; j < message.length(); j++)
						if(message.charAt(j) == org.charAt(i)){
							if(message.replace(message.substring(i, j), "").equals(org))
								return message.substring(i, j);
						}
				}
			}
			return message.substring(org.length());
		}
		
		long last = System.currentTimeMillis();
		@SuppressWarnings("deprecation")
		@Override
		public synchronized void handle(PacketHandleEvent<Packet> e) {
			if (e.getPacket() instanceof PacketPlayInPluginMessage) { //Message changed
				PacketPlayInPluginMessage packet = (PacketPlayInPluginMessage) e.getPacket();
				if (packet.getChannel().equalsIgnoreCase("MC|ItemName")) {
					if (e.getPlayer().equals(AnvilGui.this.owner) && AnvilGui.this.anvilHandle != null) {
						String message = DefinedPacket.readString(packet.getCopiedbyteBuff());
						
						if(AnvilGui.this.curruntItemDisplayName.equals(message)) return;
						if(System.currentTimeMillis() - last < Integer.getInteger("bungeeutil.inventory.anvil.writeDelay", 50)) return;
						last = System.currentTimeMillis();
						
						if(AnvilGui.this.colorPrefix.length() > message.length()) message = AnvilGui.this.colorPrefix;
						
						String handleMessage = message.substring(AnvilGui.this.colorPrefix.length(), message.length()); // replace color prefix
						
						if (handleMessage.length() == 0 && AnvilGui.this.backgroundHidden) {
							AnvilGui.this.curruntItemDisplayName = AnvilGui.this.colorPrefix + AnvilGui.this.backgroundString;
							updateBackgroundItem();
							AnvilGui.this.backgroundHidden = false;
							curruntMessage = "";
							applayAction((l, ex)-> l.onMessageChange(AnvilGui.this, ""));
							return;
						}
						if(!AnvilGui.this.backgroundHidden && !message.equalsIgnoreCase(colorPrefix + backgroundString)){
							//Input typed!
							String original = colorPrefix + backgroundString;
							if(message.length() > original.length()){ //Char[s] added
								handleMessage = getInsertedString(original, message);
							} else { //Char[s] removed
								handleMessage = "";
							}
							AnvilGui.this.backgroundHidden = true;
						}
						BungeeUtil.debug("[AV-GUI] Having message: "+handleMessage);
						curruntMessage = handleMessage;
						curruntItemDisplayName = colorPrefix + handleMessage;
						
						final String newMessage = handleMessage;
						applayAction((l, ex) -> l.onMessageChange(AnvilGui.this, newMessage));
						
						anvilHandle.disableUpdate();
						updateBackgroundItem();
						updateOutputItem();
						anvilHandle.enableUpdate();
					}
				}
			} else if(e.getPacket() instanceof PacketPlayInCloseWindow){
				PacketPlayInCloseWindow packet = (PacketPlayInCloseWindow) e.getPacket();
				if(AnvilGui.this.owner.equals(e.getPlayer()) && (packet.getWindow() == Inventory.ID || AnvilGui.this.anvilHandle.getViewer().isEmpty())){
					unload((l, ex)->l.onClose(AnvilGui.this));
				}
			}
		}
	};

	public AnvilGui(Player owner) {
		this.owner = owner;
	}

	public void open() {
		unloaded = false;
		//Validate.isTrue(this.anvilHandle == null, "Anvil inventory alredy opened!");
		this.anvilHandle = new Inventory(InventoryType.Anvil, "This is an AnvilGui by WolverinDEV");
		this.anvilHandle.addInventoryListener((inv, player, reason) ->  unload((e, ex)->e.onClose(AnvilGui.this)));
		
		this.curruntItemDisplayName = colorPrefix + backgroundString;
		this.backgroundHidden = false;
		
		updateBackgroundItem();
		setCenterItem(this.centerItem);
		updateOutputItem();
		
		PacketLib.addHandler(this.packet);
		this.owner.openInventory(this.anvilHandle);
	}

	public void close(){
		unload((e, ex)-> e.onClose(this));
	}

	protected void handleSuccessClick(){
		unload((e, ex)-> e.onConfirmInput(this, this.curruntMessage));
	}
	
	private boolean unloaded = false;
	protected synchronized void unload(Callback<AnvilGuiListener> evHandler){
		if(unloaded) return;
		unloaded = true;
		applayAction(evHandler);
		if(this.owner.getInventoryView() != null) {
			if(this.owner.getInventoryView().equals(this.anvilHandle)) {
				this.owner.closeInventory();
			}
		}
		
		PacketLib.removeHandler(this.packet);
	}
	
	private void applayAction(Callback<AnvilGuiListener> evHandler){
		listener.forEach(e -> evHandler.done(e, null));
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

	public void removeListener(@NonNull AnvilGuiListener listener){
		this.listener.remove(listener);
	}

	public void addListener(@NonNull AnvilGuiListener listener){
		this.listener.add(listener);
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
		if(this.anvilHandle != null)
			this.anvilHandle.setItem(1, ItemBuilder.create(centerItem).listener((c)->updateOutputItem()).build());
	}

	public void setColorPrefix(String prefix) {
		if (this.colorPrefix.equalsIgnoreCase(prefix)) {
			return;
		}
		this.curruntItemDisplayName = prefix + (backgroundHidden ? curruntMessage : backgroundString);
		this.colorPrefix = prefix;
		updateBackgroundItem();
	}

	public void setCurruntInput(String curruntName) {
		if(curruntName != null){
			this.curruntMessage = curruntName;
			this.curruntItemDisplayName = this.colorPrefix + this.curruntMessage;
			this.backgroundHidden = true;
		} else {
			this.curruntMessage = "";
			this.curruntItemDisplayName = this.colorPrefix + this.backgroundString;
			this.backgroundHidden = false;
		}
		updateBackgroundItem();
	}
	public void setOutputItem(Item item){
		this.outputItem = item;
		if(this.anvilHandle != null)
			this.anvilHandle.setItem(2, new ItemStack(this.outputItem){
				@SuppressWarnings("deprecation")
				@Override
				public void click(Click click) {
					click.setCancelled(true);
					AnvilGui.this.handleSuccessClick();
				}
			});
	}
	
	private void updateBackgroundItem(){
		if(this.anvilHandle != null)
			this.anvilHandle.setItem(0, backgroundBuilder.clone().name(AnvilGui.this.curruntItemDisplayName).listener((click)->updateOutputItem()).build());
	}
	
	private void updateOutputItem(){
		if(this.anvilHandle != null) this.anvilHandle.setItem(2, ItemBuilder.create(this.outputItem).listener(()->AnvilGui.this.handleSuccessClick()).build());
	}
}
