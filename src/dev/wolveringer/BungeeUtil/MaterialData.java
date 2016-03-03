package dev.wolveringer.BungeeUtil;

import dev.wolveringer.BungeeUtil.item.Item;

/**
 * Handles specific metadata for certain items or blocks
 */
public class MaterialData implements Cloneable {
	private byte data = 0;
	private final int type;

	public MaterialData(final int type) {
		this(type, (byte) 0);
	}

	public MaterialData(final int type, final byte data) {
		this.type = type;
		this.data = data;
	}

	public MaterialData(final Material type) {
		this(type, (byte) 0);
	}

	@SuppressWarnings("deprecation")
	public MaterialData(final Material type, final byte data) {
		this(type.getId(), data);
	}

	@Override
	public MaterialData clone() {
		try{
			return (MaterialData) super.clone();
		}catch (CloneNotSupportedException e){
			throw new Error(e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof MaterialData){
			MaterialData md = (MaterialData) obj;

			return md.getItemTypeId() == getItemTypeId() && md.getData() == getData();
		}else{
			return false;
		}
	}

	/**
	 * Gets the raw data in this material
	 *
	 * @return Raw data
	 */
	public byte getData() {
		return data;
	}

	/**
	 * Gets the Material that this MaterialData represents
	 *
	 * @return Material represented by this MaterialData
	 */
	@SuppressWarnings("deprecation")
	public Material getItemType() {
		return Material.getMaterial(type);
	}

	/**
	 * Gets the Material Id that this MaterialData represents
	 *
	 * @return Material Id represented by this MaterialData
	 * @deprecated Magic value
	 */
	public int getItemTypeId() {
		return type;
	}

	public int hashCode() {
		return getItemTypeId() << 8 ^ getData();
	}

	/**
	 * Sets the raw data of this material
	 *
	 * @param data
	 *            New raw data
	 * @deprecated Magic value
	 */
	public void setData(byte data) {
		this.data = data;
	}

	/**
	 * Creates a new ItemStack based on this MaterialData
	 *
	 * @return New ItemStack containing a copy of this MaterialData
	 */
	@SuppressWarnings("deprecation")
	public Item toItemStack() {
		return new Item(type, 1, data);
	}

	/**
	 * Creates a new ItemStack based on this MaterialData
	 *
	 * @param amount
	 *            The stack size of the new stack
	 * @return New ItemStack containing a copy of this MaterialData
	 */
	@SuppressWarnings("deprecation")
	public Item toItemStack(int amount) {
		return new Item(type, amount, data);
	}

	@Override
	public String toString() {
		return getItemType() + "(" + getData() + ")";
	}
}