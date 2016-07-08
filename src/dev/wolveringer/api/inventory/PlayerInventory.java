package dev.wolveringer.api.inventory;

import java.util.ArrayList;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;

public final class PlayerInventory {
	@SuppressWarnings("serial")
	private ArrayList<Item> items = new ArrayList<Item>() {
		public Item get(int index) {
			while (super.size() <= index){
				super.add(null);
			}
			return super.get(index);
		};

		public Item set(int index, Item element) {
			while (super.size() <= index){
				super.add(null);
			}
			return super.set(index, element);
		};
	};
	private String name;
	private ArrayList<Player> viewer = new ArrayList<Player>();
	private int ID;
	private Player player;
	public PlayerInventory(Player player,int ID, String name) {
		this.name = name;
		this.ID = ID;
		this.player = player;
		getItem(45);
	}

	public PlayerInventory(Player player) {
		this(player,0,"");
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
		player.sendPacket((PacketPlayOut)a);
	}

	public Item[] getContains() {
		return items.toArray(new Item[Math.min(items.size(), player.getVersion().getBigVersion() == BigClientVersion.v1_9 ? 46 : 45)]); //46/45=Max slots
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public String getName() {
		return name;
	}

	public int getSlots() {
		return items.size();
	}

	public int getType() {
		return 0;
	}

	public void setItem(int slot, Item is) {
		items.set(slot, is);
		if(player.isInventoryOpened()){
			if(slot > 8){
				broadcast(new PacketPlayOutSetSlot(is, Inventory.ID, slot-9+player.getInventoryView().getSlots()));
			}
		}
		else
			broadcast(new PacketPlayOutSetSlot(is, ID, slot));
	}

	public ArrayList<Player> getViewer() {
		return viewer;
	}

	public void reset() {
		items.clear();
	}
	
	public int getFirstSlot(ItemStack is) {
		for(int i = 0;i < items.size();i++)
			if(items.get(i) == null){
				return i;
			}else if(items.get(i).isSimilar(is)){
				if(is.getAmount() != 64)
					return i;
			}
		return -1;
	}
	
	@Override
	protected void finalize() throws Throwable {
		reset();
	}

	public void clear() {
		int s = getSlots();
		items.clear();
		items.get(s);
	}
}
