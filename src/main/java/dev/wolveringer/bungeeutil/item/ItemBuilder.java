package dev.wolveringer.bungeeutil.item;

import java.util.ArrayList;

public class ItemBuilder {
	public static ItemBuilder create() {
		return new ItemBuilder();
	}

	public static ItemBuilder create(int id) {
		return new ItemBuilder(id);
	}

	public static ItemBuilder create(Item handle) {
		return new ItemBuilder(handle);
	}

	public static ItemBuilder create(Material m) {
		return create(m.getId());
	}

	private int id;
	private int sid = -1;
	private int amouth = -1;
	private String name;
	private ArrayList<String> lore = new ArrayList<>();
	private boolean glow;
	private ClickListener listener;
	private Item handle;

	public ItemBuilder() {
	}

	public ItemBuilder(int id) {
		this.id = id;
	}

	public ItemBuilder(Item handle) {
		this.handle = new Item(handle); //Copy
	}

	public ItemBuilder amount(int n) {
		this.amouth = n;
		return this;
	}

	public Item build() {
		Item i;
		if (this.handle != null) {
			if (this.id != 0) {
				this.handle.setTypeId(this.id);
			}
			if (this.sid != -1) {
				this.handle.setDurability((short) this.sid);
			}
			if(this.amouth != -1) {
				this.handle.setAmount(this.amouth);
			}
			i = this.handle;
		} else {
			i = new Item(this.id, this.amouth == -1 ? 1 : this.amouth, (short) (this.sid == -1 ? 0 : this.sid));
		}
		if (this.listener != null) {
			i = new ItemStack(i) {
				@Override
				public void click(Click c) {
					ItemBuilder.this.listener.click(c);
				}
			};
		}
		if (this.name != null) {
			i.getItemMeta().setDisplayName(this.name);
		}
		if (!this.lore.isEmpty()) {
			i.getItemMeta().setLore(this.lore);
		}
		if (this.glow) {
			i.getItemMeta().setGlow(true);
		}
		return i;
	}

	public ItemBuilder clearLore() {
		this.lore.clear();
		return this;
	}

	public ItemBuilder durbility(int short_) {
		this.sid = short_;
		return this;
	}

	public ItemBuilder glow() {
		this.glow = true;
		return this;
	}

	public ItemBuilder glow(boolean flag) {
		this.glow = flag;
		return this;
	}

	public ItemBuilder id(int id) {
		this.id = id;
		return this;
	}

	public ItemBuilder listener(ClickListener run) {
		this.listener = run;
		return this;
	}

	public ItemBuilder listener(final Runnable run) {
		this.listener = click -> run.run();
		return this;
	}

	public ItemBuilder lore(String lore) {
		this.lore.add(lore);
		return this;
	}

	public ItemBuilder material(Material m) {
		this.id = m.getId();
		return this;
	}

	public ItemBuilder name(String name) {
		this.name = name;
		return this;
	}
}
