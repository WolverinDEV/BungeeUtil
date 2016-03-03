package dev.wolveringer.api.inventory;

import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;

public enum InventoryType {
	Chest("0","minecraft:chest",36,36),
	Workbench("1","minecraft:crafting_table",0,9),
	Furnace("2","minecraft:furnace",3,3),
	Dispenser("3","minecraft:dispenser",9,9),
	EnchantmentTable("4","minecraft:enchanting_table",0,2),
	BrewingStand("5","minecraft:brewing_stand",4,4),
	Villager("6","minecraft:villager",3,3),
	Beacon("7","minecraft:beacon",1,1),
	Anvil("8","minecraft:anvil",0,3),
	Hopper("9","minecraft:hopper",5,5),
	Dropper("10","minecraft:dropper",9,9);
	
	private String type_v1_7;
	private String type_v1_8;
	private int aslots;
	private int islots;
	private InventoryType(String a,String b,int s,int i) {
		this.type_v1_7=a;
		this.type_v1_8=b;
		this.aslots=s;
		this.islots=i;
	}
	
	public String getType(ClientVersion v){
		return v.getBigVersion() == BigClientVersion.v1_7?type_v1_7:type_v1_8;
	}
	public int getDefaultSlots(){
		return aslots;
	}
	public int getSlots(){
		return islots;
	}
}
