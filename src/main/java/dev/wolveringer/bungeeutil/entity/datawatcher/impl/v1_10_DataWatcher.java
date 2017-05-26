package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;

public class v1_10_DataWatcher extends v1_09_DataWatcher {
	static {
		v1_09_DataWatcher.register(v1_10_DataWatcher.class);
	}
	
	public v1_10_DataWatcher() {
		super();
	}

	public v1_10_DataWatcher(AbstractDataWatcher ref) {
		super(ref);
	}

	@Override
	public DataWatcher copy() {
		return new v1_10_DataWatcher(this);
	}
}
