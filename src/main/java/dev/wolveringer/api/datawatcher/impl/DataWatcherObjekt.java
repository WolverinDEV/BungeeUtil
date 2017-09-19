package dev.wolveringer.api.datawatcher.impl;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;

public class DataWatcherObjekt {
	private final Class<?> clazz;
	private final int pos;
	private Object value;
	private boolean update;
	
	public DataWatcherObjekt(Class<?> clazz, int pos, Object object) {
		this.pos = pos;
		this.value = object;
		this.clazz = clazz;
	}

	public int getPostition() {
		return this.pos;
	}

	public void setValue(Object object) {
		this.value = object;
		update = true;
	}

	public Object getValue() {
		return this.value;
	}

	public Class<?> getType() {
		return this.clazz;
	}
	
	public boolean hasUpdate() {
		return update;
	}
	
	@Override
	public String toString() {
		return "[" + pos + "=" + value + " (" + value.getClass().getName().split("\\.")[value.getClass().getName().split("\\.").length - 1] + ")],";
	}

	public int getTypeId(BigClientVersion version) {
		return 0;
	}
}
