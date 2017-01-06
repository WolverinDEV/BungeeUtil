package dev.wolveringer.bungeeutil.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.meta.CraftItemMeta;
import dev.wolveringer.bungeeutil.item.meta.MetaListener;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.Player;
import net.md_5.bungee.api.ChatColor;

/**
 * @author WolveinGER
 *
 */
public class Inventory {
	public static interface Unsave {
		public ItemContainer getItemContainer();
		public MetaListener getItemMetaListener();
		public List<Player> getModificableViewerList();
	}

	public static final int ID = 99;

	protected ItemContainer container;
	protected String name;
	protected List<Player> viewer = Collections.synchronizedList(new ArrayList<Player>());
	protected List<InventoryListener> listeners = Collections.synchronizedList(new ArrayList<InventoryListener>());
	protected InventoryType type;
	protected boolean autoUpdate = true;
	protected boolean clickable = true;
	protected boolean resend_inventory = false;
	private MetaListener imcil;

	private Unsave unsave = new Unsave() {
		@Override
		public ItemContainer getItemContainer() {
			return Inventory.this.container;
		}

		@Override
		public MetaListener getItemMetaListener() {
			return Inventory.this.imcil;
		}

		@Override
		public List<Player> getModificableViewerList() {
			return Inventory.this.viewer;
		}
	};
	public Inventory(int size, String name) {
		this(size, name, true);
	}

	public Inventory(int size, String name, boolean checkSize) {
		if(size <= 0) {
			throw new RuntimeException(size + " must cant be smaler or equal to zero");
		}
		if(size % 9 != 0 && checkSize) {
			throw new RuntimeException(size + " % 9 != 0");
		}
		this.type = InventoryType.Chest;
		this.name = name;
		this.container = new ItemContainer(size);
		this.imcil = is -> {
			if(Inventory.this.autoUpdate) {
				Inventory.this.updateInventory();
			}
		};
	}

	public Inventory(InventoryType type, String name) {
		this.name = name;
		this.type = type;
		this.container = new ItemContainer(type.getSlots());
		this.imcil = is -> {
			if(Inventory.this.autoUpdate) {
				Inventory.this.updateInventory();
			}
		};
	}

	private Inventory(ItemStack[] items, String name, ArrayList<Player> viewer, InventoryType type) {
		this.container = new ItemContainer(items);
		this.name = name;
		this.viewer = viewer;
		this.type = type;
		this.imcil = is -> {
			if(Inventory.this.autoUpdate){
				int slot = Inventory.this.getSlot(is);
				if(slot != -1) {
					Inventory.this.broadcast(new PacketPlayOutSetSlot(is, ID, slot));
				}
			}
		};
	}

	public void addInventoryListener(InventoryListener listener){
		this.listeners.add(listener);
	}

	public void addItem(ItemStack is) {
		Item[] items = this.container.getContains();
		for(int i = 0;i < items.length;i++) {
			if(items[i] == null){
				this.setItem(i, is);
				break;
			}else if(items[i].isSimilar(is)){
				if(items[i].getAmount() + is.getAmount() > 64){
					is.setAmount(64 - items[i].getAmount());
					items[i].setAmount(64);
					this.setItem(i, this.getItemStack(items[i]));
					this.addItem(is);
				}else{
					items[i].setAmount(items[i].getAmount() + is.getAmount());
					this.setItem(i, this.getItemStack(items[i]));
					break;
				}
			}
		}
	}

	private void broadcast(Packet a) {
		for(Player p : this.viewer) {
			p.sendPacket((PacketPlayOut) a);
		}
	}

	public void clear() {
		for(Item i : this.container.getContains()) {
			if(i != null) {
				((CraftItemMeta)i.getItemMeta()).removeMetaListener(this.imcil);
			}
		}
		this.container.clear();
	}

	@Override
	public Inventory clone() {
		return new Inventory(this.container.getContainsAsItemStack(), this.name, new ArrayList<Player>(), this.type);
	}

	@Deprecated
	public void disableUpdate() {
		this.autoUpdate = false;
	}

	@Deprecated
	public void enableUpdate() {
		this.autoUpdate = true;
		this.updateInventory();
	}

	public void fill(ItemStack is) {
		for(int i = 0;i < this.getSlots();i++){
			if(this.getItem(i) == null) {
				this.setItem(i, is);
			}
		}
	}

	public ItemStack[] getContains() {
		return this.container.getContainsAsItemStack();
	}

	public List<InventoryListener> getInventoryListener(){
		return Collections.unmodifiableList(this.listeners);
	}

	public ItemStack getItem(int slot) {
		return this.getItemStack(this.container.getItem(slot));
	}

	private ItemStack getItemStack(Item is) {
		if(is == null) {
			return null;
		} else if(is instanceof ItemStack) {
			return (ItemStack) is;
		} else {
			return new ItemStack(is) {
				@Override
				public void click(Click click) {
				};
			};
		}
	}

	public String getName() {
		return this.name;
	}

	public int getSlot(Item is){
		return this.container.getSlot(is);
	}

	public int getSlots() {
		return this.container.getSize();
	}

	public InventoryType getType() {
		return this.type;
	}

	public List<Player> getViewer() {
		return Collections.unmodifiableList(this.viewer);
	}

	public boolean hasItem(Item i) {
		return this.container.hasItem(i);
	}

	public boolean isClickable() {
		return this.clickable;
	}

	public boolean removeInventoryListener(InventoryListener listener){
		return this.listeners.remove(listener);
	}

	public void replace(Item item, ItemStack replace) {
		for(int i = 0;i < this.getSlots();i++){
			if(this.getItem(i).equals(item)) {
				this.setItem(i, replace);
			}
		}
	}

	public void resize(int size) {
		if(this.type != InventoryType.Chest) {
			throw new IllegalStateException("Inventorytype isnt a Chest!");
		}
		this.container.resize(size);
		if(this.autoUpdate){
			Item[] items = this.container.getContains();
			for(Player p : this.viewer) {
				p.sendPacket(new PacketPlayOutOpenWindow(ID, this.type.getType(p.getVersion()), this.name, items.length, false));
			}
			this.updateInventory();
		}else{
			this.resend_inventory = true;
		}
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public void setContains(ItemStack[] contains) {
		for(Item i : this.container.getContains()) {
			((CraftItemMeta)i.getItemMeta()).removeMetaListener(this.imcil);
		}
		this.container.setContains(contains);
		for(ItemStack is : contains) {
			((CraftItemMeta)is.getItemMeta()).addMetaListener(this.imcil);
		}
		if(this.autoUpdate) {
			this.updateInventory();
		}
	}

	public void setItem(int i, Item item) {
		this.setItem(i, this.getItemStack(item));
	}

	public synchronized void setItem(int slot, ItemStack is) {
		if(this.getItem(slot) != null) {
			if((CraftItemMeta) this.getItem(slot).getItemMeta() != null) {
				((CraftItemMeta) this.getItem(slot).getItemMeta()).removeMetaListener(this.imcil);
			}
		}
		this.container.setItem(slot, is);
		if(is != null) {
			((CraftItemMeta) is.getItemMeta()).addMetaListener(this.imcil);
		}
		if(this.autoUpdate) {
			this.broadcast(new PacketPlayOutSetSlot(is, ID, slot));
		}
	}

	public void setName(String name) {
		this.name = name;
		if(this.autoUpdate){
			for(Player p : this.viewer){
				PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, this.getType().getType(p.getVersion()), this.getName(), this.getType() == InventoryType.Chest ? this.getSlots() : this.getType().getDefaultSlots(), false);
				e.UTF_8 = true;
				p.sendPacket(e);
			}
			this.updateInventory();
		}else{
			this.resend_inventory = true;
		}
	}

	@Override
	public String toString() {
		return "Inventory{name=\""+ChatColor.RESET + this.getName() + ChatColor.RESET +"\" viewer=" + this.getViewer() + " ObjektID=\"" + super.toString().split("@")[1] + "\"}";
	}
	public Unsave unsave() {
		return this.unsave;
	}
	public void updateInventory() {
		this.autoUpdate = true;
		if(this.resend_inventory){
			for(Player p : this.viewer){
				PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, this.getType().getType(p.getVersion()), this.getName(), this.getType() == InventoryType.Chest ? this.getSlots() : this.getType().getDefaultSlots(), false);
				e.UTF_8 = true;
				p.sendPacket(e);
			}
			this.broadcast(new PacketPlayOutWindowItems(ID, this.container.getContains()));
		}else{
			this.broadcast(new PacketPlayOutWindowItems(ID, this.container.getContains()));
		}
	}
}
