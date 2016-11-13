package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;

public class v1_8_HumanEntityDataWatcher extends v1_8_LivingEntityDataWatcher implements HumanDataWatcher{

	public v1_8_HumanEntityDataWatcher(DataWatcher watcher) {
		super(watcher);
	}

	public void setSkinFlags(byte flag) {
		watcher.setValue(10, flag);
	}

	public byte getSkinFlag() {
		return watcher.getByte(10);
	}

	public void setCapeActive(boolean b) {
		watcher.setValue(16, (byte) (b == true ? 1 : 0));
	}

	public boolean isCapeActive() {
		return watcher.getByte(16) == 1;
	}

	public void setAbsorptionHearts(float f) {
		watcher.setValue(17, f);
	}

	public float getAbsorptionHearts() {
		return watcher.getFloat(17);
	}

	public void setScore(int score) {
		watcher.setValue(18, score);
	}

	public int getScore() {
		return watcher.getInt(18);
	}

	@Override
	public v1_8_HumanEntityDataWatcher injektDefault() {
		super.injektDefault();
		if(watcher.get(10) == null)
			watcher.setValue(10, (byte) 0);
		if(watcher.get(16) == null)
			watcher.setValue(16, (byte) 0);
		if(watcher.get(17) == null)
			watcher.setValue(17, (float) 0F);
		if(watcher.get(18) == null)
			watcher.setValue(18, (int) 0);
		return this;
	}

}
