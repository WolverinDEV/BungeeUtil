package dev.wolveringer.api.datawatcher;

public class EntityDataWatcher{
	
	protected DataWatcher watcher;

	protected EntityDataWatcher(DataWatcher dataWatcher) {
		this.watcher = dataWatcher;
	}
	
	private boolean getPropety(int pos) {
		return (this.watcher.getByte(0) & 1 << pos) != 0;
	}

	private void setPropety(int pos, boolean flag) {
		if(this.watcher.get(0) == null)
			this.watcher.setValue(0, (byte) 0);
		byte b0 = this.watcher.getByte(0);

		if(flag){
			this.watcher.setValue(0, Byte.valueOf((byte) (b0 | 1 << pos)));
		}else{
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
		if(this.watcher.get(0) == null)
			this.watcher.setValue(0, Byte.valueOf((byte)0));
		if(this.watcher.get(1) == null)
			this.watcher.setValue(1, Short.valueOf((short)300));
		return this;
	}
	
	public DataWatcher getWatcher() {
		return watcher;
	}
}
