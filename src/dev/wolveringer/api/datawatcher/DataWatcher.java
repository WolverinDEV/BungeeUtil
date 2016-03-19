package dev.wolveringer.api.datawatcher;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.api.datawatcher.impl.v1_8_DataWatcher;
import dev.wolveringer.api.datawatcher.impl.v1_9_DataWatcher;
import dev.wolveringer.packet.PacketDataSerializer;

public abstract class DataWatcher {
	
	public static DataWatcher createDataWatcher(BigClientVersion version){
		if(version == BigClientVersion.v1_9)
			return new v1_9_DataWatcher();
		else
			return new v1_8_DataWatcher();
	}
	
	public static DataWatcher createDataWatcher(BigClientVersion version,PacketDataSerializer watcher){
		if(version == BigClientVersion.v1_9)
			return new v1_9_DataWatcher(watcher);
		else
			return new v1_8_DataWatcher(watcher);
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
