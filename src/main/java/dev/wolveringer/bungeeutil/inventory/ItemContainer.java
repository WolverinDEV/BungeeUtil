package dev.wolveringer.bungeeutil.inventory;

import java.util.Arrays;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;

public class ItemContainer {
	private static ItemStack[] convert(Item... items) {
		ItemStack[] out = new ItemStack[items.length];
		for(int i = 0;i < items.length;i++){
			out[i] = convert(items[i]);
		}
		return out;
	}

	private static ItemStack convert(Item is) {
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

	private static Item[] convert(ItemStack... items) {
		Item[] out = new Item[items.length];
		for(int i = 0;i < items.length;i++){
			out[i] = items[i];
		}
		return out;
	}

	private Item[] items;

	public ItemContainer(int size) {
		this.items = new Item[size];
	}

	public ItemContainer(Item[] items) {
		this.items = items;
	}
	
	public ItemContainer(ItemStack[] items) {
		this.items = convert(items);
	}

	public void addItem(Item is) {
		for(int i = 0;i < this.items.length;i++) {
			if(this.items[i] == null){
				this.setItem(i, is);
				break;
			}else if(this.items[i].isSimilar(is)){
				if(this.items[i].getAmount() + is.getAmount() > 64){
					is.setAmount(64 - this.items[i].getAmount());
					this.items[i].setAmount(64);
					this.setItem(i, this.items[i]);
					this.addItem(is);
				}else{
					this.items[i].setAmount(this.items[i].getAmount() + is.getAmount());
					this.setItem(i, this.items[i]);
					break;
				}
			}
		}
	}

	public void clear() {
		this.items = new Item[this.items.length];
	}

	public void fill(Item is) {
		for(int i = 0;i < this.getSize();i++){
			if(this.getItem(i) == null) {
				this.setItem(i, is);
			}
		}
	}

	public Item[] getContains() {
		return this.items;
	}

	public ItemStack[] getContainsAsItemStack() {
		return convert(this.items);
	}

	public Item getItem(int slot) {
		return this.items[slot];
	}

	public int getSize() {
		return this.items.length;
	}

	public int getSlot(Item is) {
		for(int i = 0;i < this.items.length;i++){
			if(this.items[i] == null && is == null) {
				return i;
			} else if(this.items[i] != null && this.items[i].equals(is)) {
				return i;
			}
		}
		return -1;
	}

	public boolean hasItem(Item i) {
		for(Item is : this.items) {
			if(is != null) {
				if(is.equals(i)) {
					return true;
				}
			}
		}
		return false;
	}

	public void replace(Item item, Item replace) {
		for(int i = 0;i < this.getSize();i++){
			if(this.getItem(i).equals(item)) {
				this.setItem(i, replace);
			}
		}
	}

	public void resize(int size) {
		this.items = Arrays.copyOf(this.items, size);
	}

	public void setContains(Item[] contains) {
		this.items = contains;
	}
	public void setContains(ItemStack[] contains) {
		this.items = contains;
	}

	public void setItem(int slot, Item is) {
		if(is == null || is.getTypeId() == 0) {
			this.items[slot] = null;
		} else {
			this.items[slot] = is;
		}
	}
}
