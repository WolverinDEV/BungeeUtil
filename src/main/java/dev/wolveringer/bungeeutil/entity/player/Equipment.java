package dev.wolveringer.bungeeutil.entity.player;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityEquipment;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityEquipment.Slot;
import dev.wolveringer.bungeeutil.player.Player;

public final class Equipment {
	//0: Hand 1: Off hand 2: boots, 3: leggings, 4: chestplate, 5: helmet
	private PlayerNPC handle;
	private Item[] contains = new Item[6];

	public Equipment(PlayerNPC handle) {
		this.handle = handle;
	}

	public Item getBoots() {
		return this.contains[2];
	}
	public Item getChestplate() {
		return this.contains[4];
	}
	public Item getHelmet() {
		return this.contains[5];
	}
	public Item getItemInHand(){
		return this.contains[0];
	}
	public Item getItemInOffHand(){
		return this.contains[1];
	}
	public Item getLeggins() {
		return this.contains[3];
	}

	protected void sendItems(Player p){
		for(int i = 0;i < this.contains.length;i++){
			if(this.contains[i] != null) {
				p.sendPacket(new PacketPlayOutEntityEquipment(this.handle.getEntityID(), this.contains[i], Slot.values()[i]));
			}
		}
	}
	private void sendUpdate(int slot,Item i){
		for(Player p : this.handle.getViewer()) {
			p.sendPacket(new PacketPlayOutEntityEquipment(this.handle.getEntityID(), i, Slot.values()[slot]));
		}
	}
	public void setBoots(Item boots) {
		this.contains[2] = boots;
		this.sendUpdate(2,boots);
	}
	public void setChestplate(Item chestplate) {
		this.contains[4] = chestplate;
		this.sendUpdate(4,chestplate);
	}
	public void setHelmet(Item head) {
		this.contains[5] = head;
		this.sendUpdate(5, head);
	}
	public void setItemInHand(Item item){
		this.contains[0] = item;
		this.sendUpdate(0, item);
	}

	public void setItemInOffHand(Item item){
		this.contains[1] = item;
		this.sendUpdate(1, item);
	}

	public void setLeggins(Item leggins) {
		this.contains[3] = leggins;
		this.sendUpdate(3, leggins);
	}
}
