package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;

public class v1_10_HumanEntityDataWatcher extends v1_10_LivingEntityDataWatcher implements HumanDataWatcher {
	
	public v1_10_HumanEntityDataWatcher(DataWatcher watcher) {
		super(watcher);
	}
	
	public void setSkinFlags(byte flag) {
		watcher.setValue(13, flag);
	}
	
	public byte getSkinFlag() {
		return watcher.getByte(13);
	}
	
	public void setCapeActive(boolean b) {
	}
	
	public boolean isCapeActive() {
		return false;
	}
	
	public void setAbsorptionHearts(float f) {
		watcher.setValue(11, f);
	}
	
	public float getAbsorptionHearts() {
		return watcher.getFloat(11);
	}
	
	public void setScore(int score) {
		watcher.setValue(12, score);
	}
	
	public int getScore() {
		return watcher.getInt(12);
	}
	
	public v1_10_HumanEntityDataWatcher injektDefault() {
		super.injektDefault();
		if (watcher.get(11) == null)
			watcher.setValue(11, (float) 0F);
		if (watcher.get(12) == null)
			watcher.setValue(12, (int) 0);
		if (watcher.get(13) == null)
			watcher.setValue(13, (byte) 0);
		if (watcher.get(14) == null)
			watcher.setValue(14, (byte) 0);
		return this;
	}
}
