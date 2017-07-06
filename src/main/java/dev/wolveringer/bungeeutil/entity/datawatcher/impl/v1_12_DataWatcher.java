package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.nbt.NBTTagCompound;

public class v1_12_DataWatcher extends v1_09_DataWatcher {
	static {
		v1_09_DataWatcher.register(v1_12_DataWatcher.class);
		registerRWHandler(v1_12_DataWatcher.class, NBTTagCompound.class, new ValueRWHandlerLamba<>(13, PacketDataSerializer::readNBT, (s, b) -> s.writeNBT(b)));
	}
	
	public v1_12_DataWatcher() {
		super();
	}

	public v1_12_DataWatcher(AbstractDataWatcher ref) {
		super(ref);
	}

	@Override
	public DataWatcher copy() {
		return new v1_12_DataWatcher(this);
	}
}
