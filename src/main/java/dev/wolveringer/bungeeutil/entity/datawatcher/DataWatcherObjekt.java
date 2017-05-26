package dev.wolveringer.bungeeutil.entity.datawatcher;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class DataWatcherObjekt {
	private final Class<?> type;
	private final int position;
	private Object value;
	private boolean dirty;

	public DataWatcherObjekt(@NonNull Class<?> clazz, int pos, Object object) {
		this.position = pos;
		this.value = object;
		this.type = clazz;
	}

	public void setValue(Object object) {
		this.value = object;
		this.dirty = true;
	}

	@Override
	public String toString() {
		return "[" + this.position + "=" + this.value + " (" + this.value.getClass().getName().split("\\.")[this.value.getClass().getName().split("\\.").length - 1] + ")],";
	}
}
