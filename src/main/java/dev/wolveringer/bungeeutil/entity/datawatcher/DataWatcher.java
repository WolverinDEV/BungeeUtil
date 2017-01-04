package dev.wolveringer.bungeeutil.entity.datawatcher;

import dev.wolveringer.bungeeutil.entity.datawatcher.impl.v1_10_DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.impl.v1_11_DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.impl.v1_8_DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.impl.v1_9_DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

public abstract class DataWatcher {
	
	public static DataWatcher createDataWatcher(BigClientVersion version){
		return createDataWatcher(version, null);
	}
	
	public static DataWatcher createDataWatcher(BigClientVersion version,PacketDataSerializer watcher){
		switch (version) {
			case v1_8:
				return new v1_8_DataWatcher(watcher);
			case v1_9:
				return new v1_9_DataWatcher(watcher);
			case v1_10:
				return new v1_10_DataWatcher(watcher);
			case v1_11:
				return new v1_11_DataWatcher(watcher);
			default:
				throw new RuntimeException("Cant find datawatcher for "+version);
		}
	}
	
	public abstract void write(PacketDataSerializer packetdataserializer);
	
	public abstract void setValue(int pos, Object object);
	
	@Override
	public abstract String toString();
	
	public abstract DataWatcher copy();
	
	public abstract EntityDataWatcher getEntityDataWatcher();
	
	public abstract <T extends EntityDataWatcher> T getSpecialDataWatcher(Class<T> clazz);
	
	public abstract Object get(int i);
	
	public abstract byte getByte(int i);
	
	public abstract short getShort(int i);
	
	public abstract int getInt(int i);
	
	public abstract String getString(int i);
	
	public abstract float getFloat(int i);
}
