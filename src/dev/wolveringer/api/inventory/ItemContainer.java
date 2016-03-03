package dev.wolveringer.api.inventory;

import java.util.Arrays;

import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.item.ItemStack;

public class ItemContainer {
	private static Item[] convert(ItemStack... items) {
		Item[] out = new Item[items.length];
		for(int i = 0;i < items.length;i++){
			out[i] = items[i];
		}
		return out;
	}

	private static ItemStack[] convert(Item... items) {
		ItemStack[] out = new ItemStack[items.length];
		for(int i = 0;i < items.length;i++){
			out[i] = convert(items[i]);
		}
		return out;
	}

	private static ItemStack convert(Item is) {
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

	private Item[] items;

	public ItemContainer(ItemStack[] items) {
		this.items = convert(items);
	}

	public ItemContainer(int size) {
		this.items = new Item[size];
	}

	public void addItem(Item is) {
		for(int i = 0;i < items.length;i++)
			if(items[i] == null){
				setItem(i, is);
				break;
			}else if(items[i].isSimilar(is)){
				if(items[i].getAmount() + is.getAmount() > 64){
					is.setAmount(64 - items[i].getAmount());
					items[i].setAmount(64);
					setItem(i, items[i]);
					addItem(is);
				}else{
					items[i].setAmount(items[i].getAmount() + is.getAmount());
					setItem(i, items[i]);
					break;
				}
			}
	}

	public Item[] getContains() {
		return items;
	}

	public ItemStack[] getContainsAsItemStack() {
		return convert(items);
	}
	
	public Item getItem(int slot) {
		return items[slot];
	}

	public boolean hasItem(Item i) {
		for(Item is : items)
			if(is != null)
				if(is.equals(i))
					return true;
		return false;
	}

	public int getSize() {
		return items.length;
	}

	public void setItem(int slot, Item is) {
		items[slot] = is;
	}

	public void clear() {
		items = new Item[items.length];
	}

	public void resize(int size) {
		items = Arrays.copyOf(items, size);
	}

	public void fill(Item is) {
		for(int i = 0;i < getSize();i++){
			if(getItem(i) == null)
				setItem(i, is);
		}
	}

	public void replace(Item item, Item replace) {
		for(int i = 0;i < getSize();i++){
			if(getItem(i).equals(item))
				setItem(i, replace);
		}
	}

	public void setContains(Item[] contains) {
		this.items = contains;
	}
	public void setContains(ItemStack[] contains) {
		this.items = contains;
	}

	public int getSlot(Item is) {
		for(int i = 0;i < items.length;i++){
			if(items[i] == null && is == null)
				return i;
			else if(items[i] != null && items[i].equals(is))
				return i;
		}
		return -1;
	}
}
