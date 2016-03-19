package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;

public class v1_9_HumanEntityDataWatcher extends v1_9_LivingEntityDataWatcher implements HumanDataWatcher {
	
	public v1_9_HumanEntityDataWatcher(DataWatcher watcher) {
		super(watcher);
	}
	
	public void setSkinFlags(byte flag) {
		watcher.setValue(12, flag);
	}
	
	public byte getSkinFlag() {
		return watcher.getByte(12);
	}
	
	public void setCapeActive(boolean b) {
		// TODO exception
	}
	
	public boolean isCapeActive() {
		// TODO exception
		return false;
	}
	
	public void setAbsorptionHearts(float f) {
		watcher.setValue(10, f);
	}
	
	public float getAbsorptionHearts() {
		return watcher.getFloat(10);
	}
	
	public void setScore(int score) {
		watcher.setValue(11, score);
	}
	
	public int getScore() {
		return watcher.getInt(11);
	}
	
	public v1_9_HumanEntityDataWatcher injektDefault() {
		super.injektDefault();
		if (watcher.get(10) == null)
			watcher.setValue(10, (float) 0F);
		if (watcher.get(11) == null)
			watcher.setValue(11, (int) 0);
		if (watcher.get(12) == null)
			watcher.setValue(12, (byte) 0);
		if (watcher.get(13) == null)
			watcher.setValue(13, (byte) 0);
		return this;
	}
}
