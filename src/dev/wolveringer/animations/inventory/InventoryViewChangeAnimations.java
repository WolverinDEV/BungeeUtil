package dev.wolveringer.animations.inventory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.Material;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.ItemContainer;

public class InventoryViewChangeAnimations {
	public static enum AnimationType {
		SCROLL_DOWN,
		SCROLL_UP,
		SCROLL_LEFT;
	}

	public static void runAnimation(AnimationType type, Inventory base, ItemContainer new_contains) {
		runAnimation(type, base, new_contains, base.getName());
	}

	public static void runAnimation(AnimationType type, Inventory base, ItemContainer new_contains, String new_name) {
		runAnimation(type, base, new_contains, new_name, new Item(Material.WOOL));
	}

	public static void runAnimation(AnimationType type, Inventory base, ItemContainer new_contains, String new_name, Item spacer) {
		runAnimation(type, base, new_contains, new_name, spacer, 200);
	}

	public static void runAnimation(AnimationType type, Inventory base, ItemContainer new_contains, String new_name, Item spacer,int delay) {
		if(type == AnimationType.SCROLL_DOWN)
			runScroolUpAnimation(base, new_contains, new_name, spacer,delay);
		else if(type == AnimationType.SCROLL_UP)
			runScroolDownAnimation(base, new_contains, new_name, spacer, delay);
	}
	
	private static void runScroolLeftAnimation(final Inventory base, final ItemContainer new_contains, final String new_name, final Item space,int delay) {
		
	}

	private static void runScroolUpAnimation(final Inventory base, final ItemContainer new_contains, final String new_name, final Item space,int delay) {
		final Item[][] old_rows = buildCollums(base);
		final Item[][] new_rows = buildCollums(new_contains);

		final Item[][] spacer = new Item[1][9];
		Arrays.fill(spacer[0], space);

		/**
		 * [ROW_NUMBER][ROW]
		 */
		final Item[][] rows = new Item[old_rows.length + new_rows.length + 1][9];

		put(rows, old_rows, 0);
		put(rows, spacer, old_rows.length);
		put(rows, new_rows, old_rows.length + 1);

		final int old_row_count = base.getSlots() / 9;
		final int row_count = new_contains.getSize() / 9;
		final int row_diff = row_count - old_row_count;

		LimetedScheduller scheduller = new LimetedScheduller(old_rows.length + new_rows.length + 1 - row_count, delay, TimeUnit.MILLISECONDS) {
			@Override
			public void run(int count) {
				base.disableUpdate();
				if(limit - count + 1 <= row_diff){
					base.resize(base.getSlots() + 9);
					for(int i = count;i < count + row_count;i++)
						if((i - count) * 9 < base.getSlots())
							if(i < rows.length)
								setRowContains(base, i - count, rows[i]);
				}else{
					boolean x = false;
					for(int i = count;i < count + old_row_count;i++){
						if(i < rows.length)
							setRowContains(base, i - count, rows[i]);
						else
							x = true;
					}
					if(x)
						base.resize(base.getSlots() - 9);
				}
				if(this.count ==(int) (this.limit/2) && !base.getName().equalsIgnoreCase(new_name))
					base.setName(new_name);
				base.enableUpdate();
			}
		};
		scheduller.start();
	}

	private static void runScroolDownAnimation(final Inventory base, final ItemContainer new_contains, final String new_name, final Item space,int delay) {
		final Item[][] old_rows = buildCollums(base);
		final Item[][] new_rows = buildCollums(new_contains);

		final Item[][] spacer = new Item[1][9];
		Arrays.fill(spacer[0], space);

		/**
		 * [ROW_NUMBER][ROW]
		 */
		final Item[][] rows = new Item[old_rows.length + new_rows.length + 1][9];

		put(rows, new_rows, 0);
		put(rows, spacer, new_rows.length);
		put(rows, old_rows, new_rows.length + 1);

		final int new_row_count = new_contains.getSize() / 9;

		LimetedScheduller scheduller = new LimetedScheduller(old_rows.length + new_rows.length + 1  - new_row_count, delay, TimeUnit.MILLISECONDS) {
			boolean rezised_done = false;
			@Override
			public void run(int c) {
				base.disableUpdate();
				int count = limit-c;
				boolean resized = false;
				if(count < 0){
					base.enableUpdate();
					return;
				}
				if(base.getSlots() / 9 < new_row_count){
					base.resize(base.getSlots() + 9);
					this.count-=1;
					resized = true;
					if((int)(base.getSlots() / 9) == new_row_count){
						resized = false;
						rezised_done = true;
					}
				}
				if(base.getSlots() / 9 > new_row_count){
					base.resize(base.getSlots()-9);
					base.enableUpdate();
					return;
				}
				base.clear();
				for(int i = count;i < count+new_row_count;i++){
					if(rows.length>i+(resized?1:0))
						setRowContains(base, i-count, rows[i+(resized?1:0)]);
					else
						setRowContains(base, i-count, rows[i]);
				}
				if(rezised_done){
					this.count++;
					rezised_done = false;
				}
				if(this.count ==(int) (this.limit/2) && !base.getName().equalsIgnoreCase(new_name))
					base.setName(new_name);
				base.enableUpdate();
			}
		};
		scheduller.start();
	}

	private static void put(Item[][] base, Item[][] data, int start) {
		for(int i = start;i - start < data.length;i++){
			base[i] = data[i - start];
		}
	}

	private static void setRowContains(Inventory inv, int row, Item[] rowItems) {
		for(int i = row * 9;i < row * 9 + 9;i++)
			if(i < inv.getSlots())
				inv.setItem(i, rowItems[i - row * 9]);
	}

	/**
	 * @param inv
	 * @return Item[ROW_NUMBER][ROW]
	 */
	private static Item[][] buildCollums(Inventory inv) {
		Item[][] rows = new Item[inv.getSlots() / 9][9];
		for(int x = 0;x < rows.length;x++)
			for(int y = 0;y < rows[0].length;y++)
				rows[x][y] = inv.getItem(x * 9 + y);
		return rows;
	}

	/**
	 * @param container
	 * @return Item[ROW_NUMBER][ROW]
	 */
	private static Item[][] buildCollums(ItemContainer container) {
		Item[][] rows = new Item[container.getSize() / 9][9];
		for(int x = 0;x < rows.length;x++)
			for(int y = 0;y < rows[0].length;y++)
				rows[x][y] = container.getItem(x * 9 + y);
		return rows;
	}
}