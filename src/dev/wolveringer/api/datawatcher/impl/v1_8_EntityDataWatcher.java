package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.EntityDataWatcher;

public class v1_8_EntityDataWatcher implements EntityDataWatcher{
	
	protected DataWatcher watcher;
	
	protected v1_8_EntityDataWatcher(DataWatcher dataWatcher) {
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
		return getPropety(3);
	}
	
	public void setSprinting(boolean flag) {
		setPropety(3, flag);
	}
	
	public boolean isInvisible() {
		return getPropety(5);
	}
	
	public void setInvisible(boolean flag) {
		setPropety(5, flag);
	}
	
	public EntityDataWatcher injektDefault() {
		if (this.watcher.get(0) == null)
			this.watcher.setValue(0, Byte.valueOf((byte) 0));
		if (this.watcher.get(1) == null)
			this.watcher.setValue(1, Short.valueOf((short) 300));
		return this;
	}
	
	public DataWatcher getWatcher() {
		return watcher;
	}

	@Override
	public boolean isOnFire() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setOnFire(boolean fire) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isBlocking() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setBlocking(boolean block) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isGlowing() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setGlowing(boolean glow) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isElytra() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setElytra(boolean elytra) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public int getAir() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setAir(int air) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setCostumName(String name) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public String getCostumName() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isCostumNameVisiable() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setCostumNameVisiable(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isSlient() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setSlient(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}
}
