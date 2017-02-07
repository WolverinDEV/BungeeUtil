package dev.wolveringer;

import java.util.Arrays;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.item.ItemBuilder;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.item.meta.SkullMeta;

public class ItemBuilderExample {
	@SuppressWarnings("deprecation")
	public static Item createItemWithBuilder(){
		return ItemBuilder.create()
		        .id(1) //Change the item id
		        .durbility(0) //Change the item durbility (parameter must be inside Short.MIN_VALUE and Short.MAX_VALUE)
		        .amount(12) //The amount of the item (maximal Byte.MAX_VALUE minimal Byte.MIN_VALUE)
		        .name("§aHello world") //Change the item name
		        .lore("First") //Add a line at the lore
		        .lore("Second") //Add a second line
		        .glow() //Let the item glow (the enchant glow)
		        .listener((click)->{ //Applay a click listener. The variable type of click is ItemStack.Click (Like the general click listener)
		            click.getPlayer().sendMessage("Hey, you clicked me!"); //Just sending a message to the player who cliked at the item
		        })
		        .postListener((builder, item) ->{
		        	if(item.getType() == Material.SKULL_ITEM){
		        		SkullMeta meta = (SkullMeta) item.getItemMeta();
		        		meta.setSkullOwner("WolverinDEV");
		        		meta.setSkin("WolverinDEV");
		        	}
		        	return item;
		        }) //This methode will be called after the item has been created. 
		           //You can now apply your own stuff before the create function returns this item.
		        .build(); //Lets build the item.
		                  //Return type is an Item, but if the listener has a click listener then
		                  //its it returned an instance of an ItemStack
	}
	
	public static Item createItemWithoutBuilder(){
		ItemStack item = new ItemStack(1, 12,(byte) 0) { //Do the same stuff like with the item builder but in this time with the constructor
			@SuppressWarnings("deprecation")
			@Override
			public void click(Click click) {
				click.getPlayer().sendMessage("Hey, you clicked me!");
			}
		};
		item.getItemMeta().setDisplayName("§aHello world");
		item.getItemMeta().setLore(Arrays.asList("First", "Second"));
		item.getItemMeta().setGlow(true);
		
		if(item.getType() == Material.SKULL_ITEM){
    		SkullMeta meta = (SkullMeta) item.getItemMeta();
    		meta.setSkullOwner("WolverinDEV");
    		meta.setSkin("WolverinDEV");
    	}
		return item;
	}
}
