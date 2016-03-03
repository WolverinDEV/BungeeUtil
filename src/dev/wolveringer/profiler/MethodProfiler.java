package dev.wolveringer.profiler;

import java.util.HashMap;

import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.ScrolingInventory;

public class MethodProfiler {
	private String name;
	private Profiler profile;
	private ScrolingInventory inv;
	@SuppressWarnings("serial")
	HashMap<String, Timings> timings = new HashMap<String, Timings>(){
		public Timings get(Object key) {
			if(super.get(key) == null)
				super.put((String) key, new Timings((String) key,MethodProfiler.this));
			return super.get(key);
		};
	};
	
	public MethodProfiler(Profiler profile,String key) {
		this.name = key;
		this.profile = profile;
		String name = "§aTimings §7(§5§l"+profile.getName()+" §c§l>> §b§l"+getName()+"§7)";
		inv = new ScrolingInventory(4, name);
		updateInventory();
	}
	public void start(String name){
		timings.get(name).start();
	}
	public void stop(String name){
		timings.get(name).stop();
	}
	public Long getLastTiming(String name) {
		return timings.get(name).getLastTiming();
	}
	public String getName() {
		return name;
	}
	public Profiler getProfile() {
		return profile;
	}
	public Inventory getInventory() {
		return inv;
	}
	protected void updateInventory() {
		inv.disableUpdate();
		inv.clear();
		for(Timings t : timings.values()){
			t.rebuild();
			inv.addItem(t.getItemStack());
		}
		inv.enableUpdate();
	}
	public void resetTimings() {
		for(Timings t : timings.values())
			t.resetTimings();
		updateInventory();
	}
	public HashMap<String, Timings> getTimings() {
		return this.timings;
	}
}
