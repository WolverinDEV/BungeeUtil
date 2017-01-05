package dev.wolveringer.bungeeutil.inventory;

import java.util.ArrayList;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import lombok.Getter;
import lombok.Setter;

public final class PlayerInventory {
	private ArrayList<Item> items = new ArrayList<Item>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Item get(int index) {
			while (super.size() <= index){
				super.add(null);
			}
			return super.get(index);
		};

		@Override
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
	@Getter
	@Setter
	private int dragMode = -1;
	@Getter
	private List<Integer> dragSlots = new ArrayList<>();
	public PlayerInventory(Player player) {
		this(player,0,"");
	}

	public PlayerInventory(Player player,int ID, String name) {
		this.name = name;
		this.ID = ID;
		this.player = player;
		this.getItem(45);
	}

	private void broadcast(Packet a) {
		this.player.sendPacket((PacketPlayOut)a);
	}

	public void clear() {
		int s = this.getSlots();
		this.items.clear();
		this.items.get(s);
	}

	@Override
	protected void finalize() throws Throwable {
		this.reset();
	}

	public Item[] getContains() {
		return this.items.subList(0, Math.min(this.items.size(), this.getSlots())).toArray(new Item[0]); //46/45=Max slots
	}

	public int getFirstSlot(ItemStack is) {
		for(int i = 0;i < this.items.size();i++) {
			if(this.items.get(i) == null){
				return i;
			}else if(this.items.get(i).isSimilar(is)){
				if(is.getAmount() != 64) {
					return i;
				}
			}
		}
		return -1;
	}

	public Item getItem(int slot) {
		return this.items.get(slot);
	}

	public String getName() {
		return this.name;
	}

	public int getSlots() {
		return this.player.getVersion().getBigVersion() != BigClientVersion.v1_8 ? 46 : 45;
	}

	public int getType() {
		return 0;
	}

	public ArrayList<Player> getViewer() {
		return this.viewer;
	}

	public void reset() {
		this.items.clear();
	}

	public void setItem(int slot, Item is) {
		this.setItemNonUpdating(slot, is);
		/*
		if(player.isInventoryOpened()){
			if(slot > 8){
				broadcast(new PacketPlayOutSetSlot(is, Inventory.ID, slot-9+player.getInventoryView().getSlots()));
			}
		}
		else
		*/
		this.broadcast(new PacketPlayOutSetSlot(is, this.ID, slot));
	}

	public void setItemNonUpdating(int slot, Item is) {
		BungeeUtil.debug("Changing item in slot "+slot+" from "+this.getItem(slot)+" to "+is);
		this.items.set(slot, is);
	}
}
