package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.LivingEntityDataWatcher;

public class v1_8_LivingEntityDataWatcher extends v1_8_EntityDataWatcher implements LivingEntityDataWatcher{
	
	public v1_8_LivingEntityDataWatcher(DataWatcher dataWatcher) {
		super(dataWatcher);

	}

	public void setCustomName(String s) {
		this.watcher.setValue(2, s);
	}

	public String getCustomName() {
		return this.watcher.getString(2);
	}

	public boolean hasCustomName() {
		return this.watcher.getString(2).length() > 0;
	}

	public void setCustomNameVisible(boolean flag) {
		this.watcher.setValue(3, Byte.valueOf((byte) (flag ? 1 : 0)));
	}

	public boolean getCustomNameVisible() {
		return this.watcher.getByte(3) == 1;
	}

	public void setHealth(float h) {
		this.watcher.setValue(6, h);
	}

	public float getHealth() {
		return this.watcher.getFloat(6);
	}

	public void setArrows(int amauth) {
		this.watcher.setValue(9, (byte) amauth);
	}

	public int getArrows() {
		return this.watcher.getByte(9);
	}

	public void setParicelColor(int color){
		this.watcher.setValue(7, color);
	}
	public int getParicelColor(){
		return this.watcher.getInt(7);
	}
	
	public void setParticelVisiable(boolean flag) {
		this.watcher.setValue(8, (byte) (flag == true ? 1 : 0));
	}
	public boolean isParticelVisiable(){
		return this.watcher.getByte(8) == 1;
	}

	public void setAI(boolean flag) {
		this.watcher.setValue(15, Byte.valueOf((byte) (flag == true ? 0 : 1)));
	}

	public boolean hasAI() {
		return this.watcher.getByte(15) == 0;
	}

	@Override
	public v1_8_LivingEntityDataWatcher injektDefault() {
		super.injektDefault();
		if(this.watcher.get(2) == null)
			this.watcher.setValue(2, "");
		if(this.watcher.get(3) == null)
			this.watcher.setValue(3, Byte.valueOf((byte) 0));
		if(this.watcher.get(4) == null)
			this.watcher.setValue(4, Byte.valueOf((byte) 0));

		if(this.watcher.get(6) == null)
			this.watcher.setValue(6, Float.valueOf(10.0F));
		if(this.watcher.get(7) == null)
			this.watcher.setValue(7, Integer.valueOf(0));
		if(this.watcher.get(8) == null)
			this.watcher.setValue(8, Byte.valueOf((byte) 0));
		if(this.watcher.get(9) == null)
			this.watcher.setValue(9, Byte.valueOf((byte) 0));

		if(this.watcher.get(15) == null)
			this.watcher.setValue(15, Byte.valueOf((byte) 0));
		return this;
	}

}
