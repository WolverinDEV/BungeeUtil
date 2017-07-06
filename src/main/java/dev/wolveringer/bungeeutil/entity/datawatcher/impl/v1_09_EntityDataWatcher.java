package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;

public class v1_09_EntityDataWatcher implements EntityDataWatcher{

	protected DataWatcher watcher;

	protected v1_09_EntityDataWatcher(DataWatcher dataWatcher) {
		this.watcher = dataWatcher;
	}

	@Override
	public int getAir() {
		return this.watcher.getInt(1);
	}

	@Override
	public String getCostumName() {
		return this.watcher.getString(2);
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
			this.watcher.setValue(1, 300);
		}
		if (this.watcher.get(2) == null) {
			this.watcher.setValue(2, "");
		}
		if (this.watcher.get(3) == null) {
			this.watcher.setValue(3,false);
		}
		if (this.watcher.get(4) == null) {
			this.watcher.setValue(4,false);
		}
		return this;
	}

	@Override
	public boolean isBlocking() {
		return this.getPropety(3);
	}

	@Override
	public boolean isCostumNameVisiable() {
		return (boolean) this.watcher.get(3);
	}

	@Override
	public boolean isElytra() {
		return this.getPropety(7);
	}

	@Override
	public boolean isGlowing() {
		return this.getPropety(6);
	}

	@Override
	public boolean isInvisible() {
		return this.getPropety(4);
	}

	@Override
	public boolean isOnFire() {
		return this.getPropety(0);
	}

	@Override
	public boolean isSlient() {
		return (boolean) this.watcher.get(4);
	}

	@Override
	public boolean isSneaking() {
		return this.getPropety(1);
	}

	@Override
	public boolean isSprinting() {
		return this.getPropety(2);
	}

	@Override
	public void setAir(int air) {
		this.watcher.setValue(1, air);
	}

	@Override
	public void setApplayGravity(boolean flag) {}

	@Override
	public void setBlocking(boolean block) {
		this.setPropety(3, block);
	}

	@Override
	public void setCostumName(String name) {
		this.watcher.setValue(2, name);
	}

	@Override
	public void setCostumNameVisiable(boolean flag) {
		this.watcher.setValue(3, flag);
	}

	@Override
	public void setElytra(boolean elytra) {
		this.setPropety(7, elytra);
	}

	@Override
	public void setGlowing(boolean glow) {
		this.setPropety(6, glow);
	}

	@Override
	public void setInvisible(boolean flag) {
		this.setPropety(4, flag);
	}

	@Override
	public void setOnFire(boolean fire) {
		this.setPropety(0, fire);
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
		this.watcher.setValue(4, flag);
	}

	@Override
	public void setSneaking(boolean flag) {
		this.setPropety(1, flag);
	}
	@Override
	public void setSprinting(boolean flag) {
		this.setPropety(2, flag);
	}
}
