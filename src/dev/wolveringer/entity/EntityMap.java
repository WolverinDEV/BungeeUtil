package dev.wolveringer.entity;

import java.util.ArrayList;
import java.util.Arrays;

import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityDestroy;

public class EntityMap {
	ArrayList<Integer> entitys = new ArrayList<Integer>();
	
	public void addEntity(int id){
		entitys.add(id);
	}
	public void removeEntity(int id){
		entitys.remove(id);
	}
	public void removeEntity(int... id){
		entitys.removeAll(Arrays.asList(id));
	}
	public PacketPlayOutEntityDestroy getDestroyAll(){
		int[] i = new int[entitys.size()];
		for(int x = 0;x<entitys.size();x++)
			i[x] = entitys.get(x);
		entitys.clear();
		return new PacketPlayOutEntityDestroy(i);
	}
	public void clear() {
		entitys.clear();
	}
}
