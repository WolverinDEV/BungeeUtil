package dev.wolveringer.bungeeutil.item.meta;

import java.util.List;
import java.util.Set;

import dev.wolveringer.nbt.NBTTagCompound;

/**
 * This type represents the storage mechanism for auxiliary item data.
 * <p>
 * An implementation will handle the creation and application for ItemMeta. This
 * class should not be implemented by a plugin in a live environment.
 */
public interface ItemMeta extends Cloneable {

	/**
	 * Gets the display name that is set.
	 * <p>
	 * Plugins should check that hasDisplayName() returns <code>true</code>
	 * before calling this method.
	 *
	 * @return the display name that is set
	 */
	String getDisplayName();

	/**
	 * Gets the lore that is set.
	 * <p>
	 * Plugins should check if hasLore() returns <code>true</code> before
	 * calling this method.
	 *
	 * @return a list of lore that is set
	 */
	List<String> getLore();

	NBTTagCompound getTag();

	/**
	 * Checks for existence of a display name.
	 *
	 * @return true if this has a display name
	 */
	boolean hasDisplayName();

	boolean hasGlow();

	/**
	 * Checks for existence of lore.
	 *
	 * @return true if this has lore
	 */
	boolean hasLore();

	boolean hasTag();

	/**
	 * Sets the display name.
	 *
	 * @param name
	 *            the name to set
	 */
	void setDisplayName(String name);

	void setGlow(boolean b);

	/**
	 * Sets the lore for this item.
	 * Removes lore when given null.
	 *
	 * @param lore
	 *            the lore that will be set
	 */
	void setLore(List<String> lore);
	
	public Set<ItemFlag> getItemFlags();
	public void removeItemFlags(ItemFlag ... hideFlags);
	public void addItemFlags(ItemFlag ... hideFlags);
	public boolean hasItemFlag(ItemFlag flag);

}