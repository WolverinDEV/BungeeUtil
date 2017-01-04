package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;

public class v1_10_LivingEntityDataWatcher extends v1_10_EntityDataWatcher implements LivingEntityDataWatcher{

	public v1_10_LivingEntityDataWatcher(DataWatcher dataWatcher) {
		super(dataWatcher);
	}

	@Override
	public int getArrows() {
		return this.watcher.getByte(10);
	}

	@Override
	public float getHealth() {
		return this.watcher.getFloat(7);
	}

	@Override
	public int getParicelColor(){
		return this.watcher.getInt(8);
	}

	@Override
	public boolean hasAI() {
		throw new RuntimeException("Methode not implimented in 1.10");
	}

	@Override
	public v1_10_LivingEntityDataWatcher injektDefault() {
		super.injektDefault();
		if(this.watcher.get(6) == null) {
			this.watcher.setValue(6, Byte.valueOf((byte)0));
		}
		if(this.watcher.get(7) == null) {
			this.watcher.setValue(7, Float.valueOf(20.0F));
		}
		if(this.watcher.get(8) == null) {
			this.watcher.setValue(8, Integer.valueOf(0));
		}
		if(this.watcher.get(9) == null) {
			this.watcher.setValue(9, false);
		}
		if(this.watcher.get(10) == null) {
			this.watcher.setValue(10, 0);
		}
		return this;
	}
	@Override
	public boolean isParticelVisiable(){
		return this.watcher.getByte(9) == 1;
	}

	@Override
	public void setAI(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.10");
	}
	@Override
	public void setArrows(int amauth) {
		this.watcher.setValue(10, (byte) amauth);
	}

	@Override
	public void setHealth(float h) {
		this.watcher.setValue(7, h);
	}

	@Override
	public void setParicelColor(int color){
		this.watcher.setValue(8, color);
	}

	@Override
	public void setParticelVisiable(boolean flag) {
		this.watcher.setValue(9, (byte) (flag == true ? 1 : 0));
	}

}
