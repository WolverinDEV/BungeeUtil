package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;

public class v1_08_EntityDataWatcher implements EntityDataWatcher{

	protected DataWatcher watcher;

	protected v1_08_EntityDataWatcher(DataWatcher dataWatcher) {
		this.watcher = dataWatcher;
	}

	@Override
	public int getAir() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public String getCostumName() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	private boolean getPropety(int pos) {
		return (this.watcher.getByte(0) & 1 << pos) != 0;
	}

	@Override
	public DataWatcher getWatcher() {
		return this.watcher;
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public EntityDataWatcher injektDefault() {
		if (this.watcher.get(0) == null) {
			this.watcher.setValue(0, Byte.valueOf((byte) 0));
		}
		if (this.watcher.get(1) == null) {
			this.watcher.setValue(1, Short.valueOf((short) 300));
		}
		return this;
	}

	@Override
	public boolean isBlocking() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isCostumNameVisiable() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isElytra() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isGlowing() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isInvisible() {
		return this.getPropety(5);
	}

	@Override
	public boolean isOnFire() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isSlient() {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public boolean isSneaking() {
		return this.getPropety(1);
	}

	@Override
	public boolean isSprinting() {
		return this.getPropety(3);
	}

	@Override
	public void setAir(int air) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setApplayGravity(boolean flag) {}

	@Override
	public void setBlocking(boolean block) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setCostumName(String name) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setCostumNameVisiable(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setElytra(boolean elytra) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setGlowing(boolean glow) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setInvisible(boolean flag) {
		this.setPropety(5, flag);
	}

	@Override
	public void setOnFire(boolean fire) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	private void setPropety(int pos, boolean flag) {
		if (this.watcher.get(0) == null) {
			this.watcher.setValue(0, (byte) 0);
		}
		byte b0 = this.watcher.getByte(0);

		if (flag) {
			this.watcher.setValue(0, Byte.valueOf((byte) (b0 | 1 << pos)));
		} else {
			this.watcher.setValue(0, Byte.valueOf((byte) (b0 & (1 << pos ^ 0xFFFFFFFF))));
		}
	}

	@Override
	public void setSlient(boolean flag) {
		throw new RuntimeException("Methode not implimented in 1.8");
	}

	@Override
	public void setSneaking(boolean flag) {
		this.setPropety(1, flag);
	}
	@Override
	public void setSprinting(boolean flag) {
		this.setPropety(3, flag);
	}
}
