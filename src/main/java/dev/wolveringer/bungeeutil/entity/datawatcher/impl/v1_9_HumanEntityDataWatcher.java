package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanDataWatcher;

public class v1_9_HumanEntityDataWatcher extends v1_9_LivingEntityDataWatcher implements HumanDataWatcher {

	public v1_9_HumanEntityDataWatcher(DataWatcher watcher) {
		super(watcher);
	}

	@Override
	public float getAbsorptionHearts() {
		return this.watcher.getFloat(10);
	}

	@Override
	public int getScore() {
		return this.watcher.getInt(11);
	}

	@Override
	public byte getSkinFlag() {
		return this.watcher.getByte(12);
	}

	@Override
	public v1_9_HumanEntityDataWatcher injektDefault() {
		super.injektDefault();
		if (this.watcher.get(10) == null) {
			this.watcher.setValue(10, 0F);
		}
		if (this.watcher.get(11) == null) {
			this.watcher.setValue(11, 0);
		}
		if (this.watcher.get(12) == null) {
			this.watcher.setValue(12, (byte) 0);
		}
		if (this.watcher.get(13) == null) {
			this.watcher.setValue(13, (byte) 0);
		}
		return this;
	}

	@Override
	public boolean isCapeActive() {
		return false;
	}

	@Override
	public void setAbsorptionHearts(float f) {
		this.watcher.setValue(10, f);
	}

	@Override
	public void setCapeActive(boolean b) {
	}

	@Override
	public void setScore(int score) {
		this.watcher.setValue(11, score);
	}

	@Override
	public void setSkinFlags(byte flag) {
		this.watcher.setValue(12, flag);
	}
}
