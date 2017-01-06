package dev.wolveringer.bungeeutil.statistics.profiler;

import java.util.HashMap;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.ScrolingInventory;

public class MethodProfiler {
	private String name;
	private Profiler profile;
	private ScrolingInventory inv;
	HashMap<String, Timings> timings = new HashMap<String, Timings>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Timings get(Object key) {
			if(super.get(key) == null) {
				super.put((String) key, new Timings((String) key,MethodProfiler.this));
			}
			return super.get(key);
		};
	};

	public MethodProfiler(Profiler profile,String key) {
		this.name = key;
		this.profile = profile;
		String name = ChatColorUtils.COLOR_CHAR+"aTimings "+ChatColorUtils.COLOR_CHAR+"7("+ChatColorUtils.COLOR_CHAR+"5"+ChatColorUtils.COLOR_CHAR+"l"+profile.getName()+" "+ChatColorUtils.COLOR_CHAR+"c"+ChatColorUtils.COLOR_CHAR+"l>> "+ChatColorUtils.COLOR_CHAR+"b"+ChatColorUtils.COLOR_CHAR+"l"+this.getName()+ChatColorUtils.COLOR_CHAR+"7)";
		this.inv = new ScrolingInventory(4, name);
		this.updateInventory();
	}
	public Inventory getInventory() {
		return this.inv;
	}
	public Long getLastTiming(String name) {
		return this.timings.get(name).getLastTiming();
	}
	public String getName() {
		return this.name;
	}
	public Profiler getProfile() {
		return this.profile;
	}
	public HashMap<String, Timings> getTimings() {
		return this.timings;
	}
	public void resetTimings() {
		for(Timings t : this.timings.values()) {
			t.resetTimings();
		}
		this.updateInventory();
	}
	public void start(String name){
		this.timings.get(name).start();
	}
	public void stop(String name){
		this.timings.get(name).stop();
	}
	protected void updateInventory() {
		this.inv.disableUpdate();
		this.inv.clear();
		for(Timings t : this.timings.values()){
			t.rebuild();
			this.inv.addItem(t.getItemStack());
		}
		this.inv.enableUpdate();
	}
}
