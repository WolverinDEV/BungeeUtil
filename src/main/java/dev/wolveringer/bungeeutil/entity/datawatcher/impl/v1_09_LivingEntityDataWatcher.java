package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;

public class v1_09_LivingEntityDataWatcher extends v1_09_EntityDataWatcher implements LivingEntityDataWatcher{

	public v1_09_LivingEntityDataWatcher(DataWatcher dataWatcher) {
		super(dataWatcher);

	}

	@Override
	public int getArrows() {
		return this.watcher.getByte(9);
	}

	@Override
	public float getHealth() {
		return this.watcher.getFloat(6);
	}

	@Override
	public int getParicelColor(){
		return this.watcher.getInt(7);
	}

	@Override
	public boolean hasAI() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public v1_09_LivingEntityDataWatcher injektDefault() {
		super.injektDefault();
		if(this.watcher.get(6) == null) {
			this.watcher.setValue(6, Float.valueOf(20.0F));
		}
		if(this.watcher.get(7) == null) {
			this.watcher.setValue(7, Integer.valueOf(0));
		}
		if(this.watcher.get(8) == null) {
			this.watcher.setValue(8, false);
		}
		if(this.watcher.get(9) == null) {
			this.watcher.setValue(9, 0);
		}
		return this;
	}
	@Override
	public boolean isParticelVisiable(){
		return this.watcher.getByte(8) == 1;
	}

	@Override
	public void setAI(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.9");
	}
	@Override
	public void setArrows(int amauth) {
		this.watcher.setValue(9, (byte) amauth);
	}

	@Override
	public void setHealth(float h) {
		this.watcher.setValue(6, h);
	}

	@Override
	public void setParicelColor(int color){
		this.watcher.setValue(7, color);
	}

	@Override
	public void setParticelVisiable(boolean flag) {
		this.watcher.setValue(8, (byte) (flag == true ? 1 : 0));
	}

}
