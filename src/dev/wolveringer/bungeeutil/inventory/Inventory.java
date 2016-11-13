package dev.wolveringer.bungeeutil.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.wolveringer.BungeeUtil.item.meta.CraftItemMeta;
import dev.wolveringer.BungeeUtil.item.meta.MetaListener;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.Player;

/**
 * @author WolveinGER
 *
 */
public class Inventory {
	public static interface Unsave {
		public ItemContainer getItemContainer();
		public List<Player> getModificableViewerList();
		public MetaListener getItemMetaListener();
	}
	public static final int ID = 99;

	protected ItemContainer container;
	protected String name;
	protected List<Player> viewer = (List<Player>) Collections.synchronizedList(new ArrayList<Player>());
	protected InventoryType type;
	protected boolean autoUpdate = true;
	protected boolean clickable = true;
	protected boolean resend_inventory = false;
	private MetaListener imcil;
	private Unsave unsave = new Unsave() {
		@Override
		public List<Player> getModificableViewerList() {
			return viewer;
		}
		
		@Override
		public MetaListener getItemMetaListener() {
			return imcil;
		}
		
		@Override
		public ItemContainer getItemContainer() {
			return container;
		}
	};
	public Unsave unsave() {
		return unsave;
	}
	
	public Inventory(int size, String name) {
		this(size, name, true);
	}

	private Inventory(ItemStack[] items, String name, ArrayList<Player> viewer, InventoryType type) {
		super();
		this.container = new ItemContainer(items);
		this.name = name;
		this.viewer = viewer;
		this.type = type;
		this.imcil = new MetaListener() {
			@Override
			public void onUpdate(Item is) {
				if(autoUpdate){
					int slot = getSlot(is);
					if(slot != -1)
						broadcast(new PacketPlayOutSetSlot(is, ID, slot));
				}
			}
		};
	}

	public Inventory(int size, String name, boolean check) {
		if(size % 9 != 0 && check)
			throw new RuntimeException(size + " % 9 != 0");
		this.type = InventoryType.Chest;
		this.name = name;
		this.container = new ItemContainer(size);
		this.imcil = new MetaListener() {
			@Override
			public void onUpdate(Item is) {
				if(autoUpdate)
					updateInventory();
			}
		};
	}

	public Inventory(InventoryType type, String name) {
		this.name = name;
		this.type = type;
		this.container = new ItemContainer(type.getSlots());
		this.imcil = new MetaListener() {
			@Override
			public void onUpdate(Item is) {
				if(autoUpdate)
					updateInventory();
			}
		};
	}

	public void addItem(ItemStack is) {
		Item[] items = container.getContains();
		for(int i = 0;i < items.length;i++)
			if(items[i] == null){
				setItem(i, is);
				break;
			}else if(items[i].isSimilar(is)){
				if(items[i].getAmount() + is.getAmount() > 64){
					is.setAmount(64 - items[i].getAmount());
					items[i].setAmount(64);
					setItem(i, getItemStack(items[i]));
					addItem(is);
				}else{
					items[i].setAmount(items[i].getAmount() + is.getAmount());
					setItem(i, getItemStack(items[i]));
					break;
				}
			}
	}
	
	/**
	 * Contains spelling mistake
	 * @deprecated Use {@link #broadcast(Packet a)} instead.  
	 */
	@Deprecated
	private void brotcast(Packet a) {
		broadcast(a);
	}
	
	private void broadcast(Packet a) {
		for(Player p : viewer)
			p.sendPacket((PacketPlayOut) a);
	}

	public ItemStack[] getContains() {
		return container.getContainsAsItemStack();
	}

	public ItemStack getItem(int slot) {
		return getItemStack(container.getItem(slot));
	}

	public boolean hasItem(Item i) {
		return container.hasItem(i);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if(autoUpdate){
			for(Player p : viewer){
				PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, getType().getType(p.getVersion()), getName(), getType() == InventoryType.Chest ? getSlots() : getType().getDefaultSlots(), false);
				e.UTF_8 = true;
				p.sendPacket(e);
			}
			updateInventory();
		}else{
			resend_inventory = true;
		}
	}

	public int getSlots() {
		return container.getSize();
	}
	
	public int getSlot(Item is){
		return container.getSlot(is);
	}

	public InventoryType getType() {
		return type;
	}

	public synchronized void setItem(int slot, ItemStack is) {
		if(getItem(slot) != null)
			if((CraftItemMeta) getItem(slot).getItemMeta() != null)
				((CraftItemMeta) getItem(slot).getItemMeta()).removeMetaListener(this.imcil);
		container.setItem(slot, is);
		if(is != null)
			((CraftItemMeta) is.getItemMeta()).addMetaListener(this.imcil);
		if(autoUpdate)
			broadcast(new PacketPlayOutSetSlot(is, ID, slot));
	}

	public void setItem(int i, Item item) {
		setItem(i, getItemStack(item));
	}

	public List<Player> getViewer() {
		return Collections.unmodifiableList(viewer);
	}

	public void clear() {
		for(Item i : container.getContains())
			if(i != null)
			((CraftItemMeta)i.getItemMeta()).removeMetaListener(imcil);
		container.clear();
	}

	public void resize(int size) {
		if(type != InventoryType.Chest)
			throw new IllegalStateException("Inventorytype isn�t a Chest!");
		container.resize(size);
		if(autoUpdate){
			Item[] items = container.getContains();
			for(Player p : viewer)
				p.sendPacket(new PacketPlayOutOpenWindow(ID, type.getType(p.getVersion()), name, items.length, false));
			updateInventory();
		}else{
			resend_inventory = true;
		}
	}

	public void fill(ItemStack is) {
		for(int i = 0;i < getSlots();i++){
			if(getItem(i) == null)
				setItem(i, is);
		}
	}

	public void replace(Item item, ItemStack replace) {
		for(int i = 0;i < getSlots();i++){
			if(getItem(i).equals(item))
				setItem(i, replace);
		}
	}

	public void updateInventory() {
		autoUpdate = true;
		if(resend_inventory){
			for(Player p : viewer){
				PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, getType().getType(p.getVersion()), getName(), getType() == InventoryType.Chest ? getSlots() : getType().getDefaultSlots(), false);
				e.UTF_8 = true;
				p.sendPacket(e);
			}
			broadcast(new PacketPlayOutWindowItems(ID, this.container.getContains()));
		}else{
			broadcast(new PacketPlayOutWindowItems(ID, this.container.getContains()));
		}
	}

	public Inventory clone() {
		return new Inventory(container.getContainsAsItemStack(), name, new ArrayList<Player>(), type);
	}

	@Override
	public String toString() {
		return "Inventory{name=\""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r" + getName() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r\" viewer=" + getViewer() + " ObjektID=\"" + super.toString().split("@")[1] + "\"}";
	}

	public void setContains(ItemStack[] contains) {
		for(Item i : container.getContains())
			((CraftItemMeta)i.getItemMeta()).removeMetaListener(imcil);
		container.setContains(contains);
		for(ItemStack is : contains)
			((CraftItemMeta)is.getItemMeta()).addMetaListener(imcil);
		if(autoUpdate)
			updateInventory();
	}

	@Deprecated
	public void disableUpdate() {
		autoUpdate = false;
	}

	@Deprecated
	public void enableUpdate() {
		autoUpdate = true;
		updateInventory();
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public boolean isClickable() {
		return this.clickable;
	}

	private ItemStack getItemStack(Item is) {
		if(is == null)
			return null;
		else if(is instanceof ItemStack)
			return (ItemStack) is;
		else
			return new ItemStack(is) {
				public void click(Click click) {
				};
			};
	}
}
