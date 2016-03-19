package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.EntityDataWatcher;

public class v1_9_EntityDataWatcher implements EntityDataWatcher{
	
	protected DataWatcher watcher;
	
	protected v1_9_EntityDataWatcher(DataWatcher dataWatcher) {
		this.watcher = dataWatcher;
	}
	
	private boolean getPropety(int pos) {
		return (this.watcher.getByte(0) & 1 << pos) != 0;
	}
	
	private void setPropety(int pos, boolean flag) {
		if (this.watcher.get(0) == null)
			this.watcher.setValue(0, (byte) 0);
		byte b0 = this.watcher.getByte(0);
		
		if (flag) {
			this.watcher.setValue(0, Byte.valueOf((byte) (b0 | 1 << pos)));
		} else {
			this.watcher.setValue(0, Byte.valueOf((byte) (b0 & (1 << pos ^ 0xFFFFFFFF))));
		}
	}
	
	public boolean isSneaking() {
		return getPropety(1);
	}
	
	public void setSneaking(boolean flag) {
		setPropety(1, flag);
	}
	
	public boolean isSprinting() {
		return getPropety(2);
	}
	
	public void setSprinting(boolean flag) {
		setPropety(2, flag);
	}
	
	public boolean isInvisible() {
		return getPropety(4);
	}
	
	public void setInvisible(boolean flag) {
		setPropety(4, flag);
	}
	
	public EntityDataWatcher injektDefault() {
		if (this.watcher.get(0) == null)
			this.watcher.setValue(0, Byte.valueOf((byte) 0));
		if (this.watcher.get(1) == null)
			this.watcher.setValue(1, (Integer)((int) 300));
		if (this.watcher.get(2) == null)
			this.watcher.setValue(2, "");
		if (this.watcher.get(3) == null)
			this.watcher.setValue(3,false);
		if (this.watcher.get(4) == null)
			this.watcher.setValue(4,false);
		return this;
	}
	
	public DataWatcher getWatcher() {
		return watcher;
	}

	@Override
	public boolean isOnFire() {
		return getPropety(0);
	}

	@Override
	public void setOnFire(boolean fire) {
		setPropety(0, fire);
	}

	@Override
	public boolean isBlocking() {
		return getPropety(3);
	}

	@Override
	public void setBlocking(boolean block) {
		setPropety(3, block);
	}

	@Override
	public boolean isGlowing() {
		return getPropety(6);
	}

	@Override
	public void setGlowing(boolean glow) {
		setPropety(6, glow);
	}

	@Override
	public boolean isElytra() {
		return getPropety(7);
	}

	@Override
	public void setElytra(boolean elytra) {
		setPropety(7, elytra);
	}

	@Override
	public int getAir() {
		return watcher.getInt(1);
	}

	@Override
	public void setAir(int air) {
		watcher.setValue(1, air);
	}

	@Override
	public void setCostumName(String name) {
		watcher.setValue(2, name);
	}

	@Override
	public String getCostumName() {
		return watcher.getString(2);
	}

	@Override
	public boolean isCostumNameVisiable() {
		return (boolean) watcher.get(3);
	}

	@Override
	public void setCostumNameVisiable(boolean flag) {
		watcher.setValue(3, flag);
	}

	@Override
	public boolean isSlient() {
		return (boolean) watcher.get(4);
	}

	@Override
	public void setSlient(boolean flag) {
		watcher.setValue(4, flag);
	}
}
