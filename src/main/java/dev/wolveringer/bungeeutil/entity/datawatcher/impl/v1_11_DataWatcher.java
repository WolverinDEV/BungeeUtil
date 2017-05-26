package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import java.util.ArrayList;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;

public class v1_11_DataWatcher extends v1_10_DataWatcher{
	public v1_11_DataWatcher() {
	}

	public v1_11_DataWatcher(PacketDataSerializer paramPacketDataSerializer) {
		super(paramPacketDataSerializer);
	}
	
	@Override
	public DataWatcher copy() {
		v1_11_DataWatcher watcher = new v1_11_DataWatcher();
		watcher.objekts = new ArrayList<DataWatcherObjekt>(this.objekts);
		return watcher;
	}
}