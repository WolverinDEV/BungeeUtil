package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanEntityDataWatcher;

public class v1_08_HumanEntityDataWatcher extends v1_08_LivingEntityDataWatcher implements HumanEntityDataWatcher{

	public v1_08_HumanEntityDataWatcher(DataWatcher watcher) {
		super(watcher);
	}

	@Override
	public float getAbsorptionHearts() {
		return this.watcher.getFloat(17);
	}

	@Override
	public int getScore() {
		return this.watcher.getInt(18);
	}

	@Override
	public byte getSkinFlag() {
		return this.watcher.getByte(10);
	}

	@Override
	public v1_08_HumanEntityDataWatcher injektDefault() {
		super.injektDefault();
		if(this.watcher.get(10) == null) {
			this.watcher.setValue(10, (byte) 0);
		}
		if(this.watcher.get(16) == null) {
			this.watcher.setValue(16, (byte) 0);
		}
		if(this.watcher.get(17) == null) {
			this.watcher.setValue(17, 0F);
		}
		if(this.watcher.get(18) == null) {
			this.watcher.setValue(18, 0);
		}
		return this;
	}

	@Override
	public boolean isCapeActive() {
		return this.watcher.getByte(16) == 1;
	}

	@Override
	public void setAbsorptionHearts(float f) {
		this.watcher.setValue(17, f);
	}

	@Override
	public void setCapeActive(boolean b) {
		this.watcher.setValue(16, (byte) (b == true ? 1 : 0));
	}

	@Override
	public void setScore(int score) {
		this.watcher.setValue(18, score);
	}

	@Override
	public void setSkinFlags(byte flag) {
		this.watcher.setValue(10, flag);
	}

}
