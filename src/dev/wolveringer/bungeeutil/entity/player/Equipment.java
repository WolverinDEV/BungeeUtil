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
	
	public Item getHelmet() {
		return contains[5];
	}
	public Item getChestplate() {
		return contains[4];
	}
	public Item getLeggins() {
		return contains[3];
	}
	public Item getBoots() {
		return contains[2];
	}
	public Item getItemInHand(){
		return contains[0];
	}
	public Item getItemInOffHand(){
		return contains[1];
	}
	
	public void setHelmet(Item head) {
		this.contains[5] = head;
		sendUpdate(5, head);
	}
	public void setLeggins(Item leggins) {
		this.contains[3] = leggins;
		sendUpdate(3, leggins);
	}
	public void setBoots(Item boots) {
		this.contains[2] = boots;
		sendUpdate(2,boots);	
	}
	public void setChestplate(Item chestplate) {
		this.contains[4] = chestplate;
		sendUpdate(4,chestplate);
	}
	public void setItemInHand(Item item){
		this.contains[0] = item;
		sendUpdate(0, item);
	}
	public void setItemInOffHand(Item item){
		this.contains[1] = item;
		sendUpdate(1, item);
	}
	
	private void sendUpdate(int slot,Item i){
		for(Player p : handle.getViewer())
			p.sendPacket(new PacketPlayOutEntityEquipment(handle.getEntityID(), i, Slot.values()[slot]));
	}
	
	protected void sendItems(Player p){
		for(int i = 0;i < contains.length;i++){
			if(contains[i] != null)
				p.sendPacket(new PacketPlayOutEntityEquipment(handle.getEntityID(), contains[i], Slot.values()[i]));
		}
	}
}
