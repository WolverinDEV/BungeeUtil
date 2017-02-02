package dev.wolveringer.bungeeutil.item.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.UtilReflection;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.ItemStack.Click;
import dev.wolveringer.bungeeutil.item.SyncHandle;
import dev.wolveringer.nbt.NBTTagCompound;
import dev.wolveringer.nbt.NBTTagList;
import dev.wolveringer.nbt.NBTTagString;

public class CraftItemMeta implements ItemMeta {
	protected Item item;
	protected ArrayList<MetaListener> listener = new ArrayList<MetaListener>();
	private int sync = -1;

	public CraftItemMeta(Item i) {
		this.item = i;
	}

	public void addMetaListener(MetaListener listener) {
		this.listener.add(listener);
	}

	protected void build() {}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		CraftItemMeta other = (CraftItemMeta) obj;
		if (this.item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!this.item.getTag().equals(other.item.getTag())) {
			return false;
		}
		return true;
	}

	protected void fireUpdate() {
		for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
			if (e.getClassName().contains(Inventory.class.getCanonicalName() + "$2")
					&& e.getMethodName().equalsIgnoreCase("onUpdate")) {
				// in
																		// update–
				return;
			}
		}
		if (this.listener.size() != 0){
			try{
			for (MetaListener l : new ArrayList<>(this.listener)) {
				if (l != null) {
					l.onUpdate(this.item);
				}
			}
			}catch(NegativeArraySizeException e){
				System.out.println("NegativeArraySizeException from ArrayList?! I'm crazy! size()->"+this.listener.size());
			}
		}
	}

	@Override
	public String getDisplayName() {
		if (!this.getTag().hasKey("display")) {
			this.getTag().set("display", new NBTTagCompound());
		}
		return this.hasDisplayName() ? this.getTag().getCompound("display").getString("Name") : "";
	}

	@Override
	public List<String> getLore() {
		if (!this.hasLore()) {
			return new ArrayList<String>();
		}
		List<String> l = new ArrayList<>();
		NBTTagList x = this.getTag().getCompound("display").getList("Lore");
		for (int y = 0; y < x.size(); y++) {
			l.add(x.getString(y));
		}
		return l;
	}

	@Override
	public NBTTagCompound getTag() {
		if (this.item.getTag() == null) {
			this.item.setTag(new NBTTagCompound());
		}
		return this.item.getTag();
	}

	@Override
	public boolean hasDisplayName() {
		return this.getTag() != null && this.getTag().hasKey("display") && this.getTag().getCompound("display").hasKey("Name");
	}

	@Override
	public boolean hasGlow() {
		return this.getTag() != null && this.getTag().hasKey("ench");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.item == null ? 0 : this.item.hashCode());
		return result;
	}

	@Override
	public boolean hasLore() {
		return this.getTag() != null && this.getTag().hasKey("display") && this.getTag().getCompound("display").hasKey("Lore");
	}

	@Override
	public boolean hasTag() {
		return this.getTag() != null && !this.getTag().isEmpty();
	}

	public boolean isClickSync(){
		if(this.sync == -1){
			if(!(this.item instanceof ItemStack)) {
				this.sync = 0;
				return false;
			}
			ItemStack is = (ItemStack) this.item;
			try{
				Method m = UtilReflection.getMethod(is.getClass(), "click", Click.class);
				this.sync = m.isAnnotationPresent(SyncHandle.class) ? 1 : 0;
				BungeeUtil.getInstance();
				BungeeUtil.debug((this.sync == 1 ? "Sync-":"Ansync-")+"Click handeling for "+m.getDeclaringClass().getName()+"#"+m.getName());
			}catch(Exception e){
				BungeeUtil.getInstance();
				BungeeUtil.debug(e, "Exception while try to detect sync handeling....");
			}
		}
		return this.sync == 1;
	}

	public void removeMetaListener(MetaListener listener) {
		this.listener.remove(listener);
	}

	@Override
	public void setDisplayName(String name) {
		if (!this.getTag().hasKey("display")) {
			this.getTag().set("display", new NBTTagCompound());
		}
		this.getTag().getCompound("display").set("Name", new NBTTagString(name));
		this.fireUpdate();
	}

	@Override
	public void setGlow(boolean b) {
		if (b) {
			if (!this.getTag().hasKey("ench")) {
				this.getTag().set("ench", new NBTTagList());
			}
		} else if (!b) {
			if (this.getTag().hasKey("ench")) {
				this.getTag().remove("ench");
			}
		}
		this.fireUpdate();
	}

	@Override
	public void setLore(List<String> lore) {
		if (!this.getTag().hasKey("display")) {
			this.getTag().set("display", new NBTTagCompound());
		}
		NBTTagList l = new NBTTagList();
		for (String s : lore) {
			l.add(new NBTTagString(s));
		}
		this.getTag().getCompound("display").set("Lore", l);
		this.fireUpdate();
	}

	@Override
	public String toString() {
		return "CraftItemMeta@" + System.identityHashCode(this) + "[listener=" + this.listener + "]";
	}
}
