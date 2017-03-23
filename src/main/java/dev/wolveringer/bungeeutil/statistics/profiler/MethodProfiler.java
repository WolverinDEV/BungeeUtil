package dev.wolveringer.bungeeutil.statistics.profiler;

import java.util.HashMap;

import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.inventory.ScrolingInventory;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class MethodProfiler {
	@Getter
	private String name;
	@Getter
	private Profiler profile;
	@Getter
	private ScrolingInventory inventory;
	
	@SuppressWarnings("serial")
	HashMap<String, Timings> timings = new HashMap<String, Timings>(){
		@Override
		public Timings get(Object key) {
			if(super.get(key) == null) {
				super.put((String) key, new Timings((String) key, MethodProfiler.this));
			}
			return super.get(key);
		};
	};

	public MethodProfiler(Profiler profile,String key) {
		this.name = key;
		this.profile = profile;
		String name = ChatColor.GREEN+"Timings "+ChatColor.GRAY+"("+ChatColor.DARK_PURPLE+ChatColor.BOLD+profile.getName()+" "+ChatColor.RED+ChatColor.BOLD+">> "+ChatColorUtils.COLOR_CHAR+"b"+ChatColorUtils.COLOR_CHAR+"l"+this.getName()+ChatColorUtils.COLOR_CHAR+"7)";
		this.inventory = new ScrolingInventory(4, name);
		this.updateInventory();
	}
	public Long getLastTiming(String name) {
		return this.timings.get(name).getLastTiming();
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
		this.inventory.disableUpdate();
		this.inventory.clear();
		for(Timings t : this.timings.values()){
			t.rebuild();
			this.inventory.addItem(t.getItem());
		}
		this.inventory.enableUpdate();
	}
}
