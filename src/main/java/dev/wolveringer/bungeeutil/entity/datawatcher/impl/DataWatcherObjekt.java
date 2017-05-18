package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import lombok.NonNull;

public class DataWatcherObjekt {
	private final Class<?> clazz;
	private final int pos;
	private Object value;
	private boolean update;

	public DataWatcherObjekt(@NonNull Class<?> clazz, int pos, Object object) {
		this.pos = pos;
		this.value = object;
		this.clazz = clazz;
	}

	public int getPostition() {
		return this.pos;
	}

	public Class<?> getType() {
		return this.clazz;
	}

	public Object getValue() {
		return this.value;
	}

	public boolean hasUpdate() {
		return this.update;
	}

	public void setValue(Object object) {
		this.value = object;
		this.update = true;
	}

	@Override
	public String toString() {
		return "[" + this.pos + "=" + this.value + " (" + this.clazz.getClass().getName().split("\\.")[this.clazz.getClass().getName().split("\\.").length - 1] + ")]";
	}
}
