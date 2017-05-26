package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcherObjekt;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanEntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;

public class v1_08_DataWatcher extends AbstractDataWatcher {
	static {
		registerRWHandler(v1_08_DataWatcher.class, Byte.class, new ValueRWHandlerLamba<>(0, PacketDataSerializer::readByte, (s, b) -> s.writeByte(b)));
		registerRWHandler(v1_08_DataWatcher.class, Short.class, new ValueRWHandlerLamba<>(1, PacketDataSerializer::readShort, (s, b) -> s.writeShort(b)));
		registerRWHandler(v1_08_DataWatcher.class, Integer.class, new ValueRWHandlerLamba<>(2, PacketDataSerializer::readInt, (s, b) -> s.writeInt(b)));
		registerRWHandler(v1_08_DataWatcher.class, Float.class, new ValueRWHandlerLamba<>(3, PacketDataSerializer::readFloat, (s, b) -> s.writeFloat(b)));
		registerRWHandler(v1_08_DataWatcher.class, String.class, new ValueRWHandlerLamba<>(4, s -> s.readString(-1), (s, b) -> s.writeString(b)));
		registerRWHandler(v1_08_DataWatcher.class, Item.class, new ValueRWHandlerLamba<>(5, PacketDataSerializer::readItem, (s, b) -> s.writeItem(b)));
		
		registerRWHandler(v1_08_DataWatcher.class, BlockPosition.class, new ValueRWHandlerLamba<>(6, s -> new BlockPosition(s.readInt(), s.readInt(), s.readInt()), (s, b) -> {
			s.writeInt(b.getX());
			s.writeInt(b.getY());
			s.writeInt(b.getZ());
		}));
		registerRWHandler(v1_08_DataWatcher.class, Vector3f.class, new ValueRWHandlerLamba<>(7, s -> new Vector3f(s.readFloat(), s.readFloat(), s.readFloat()), (s, b) -> {
			s.writeFloat(b.getX());
			s.writeFloat(b.getY());
			s.writeFloat(b.getZ());
		}));
		
		registerIMapper(v1_08_DataWatcher.class, EntityDataWatcher.class, v1_08_EntityDataWatcher.class);
		registerIMapper(v1_08_DataWatcher.class, LivingEntityDataWatcher.class, v1_08_LivingEntityDataWatcher.class);
		registerIMapper(v1_08_DataWatcher.class, HumanEntityDataWatcher.class, v1_08_HumanEntityDataWatcher.class);
	}
	
	public v1_08_DataWatcher() {
		super();
	}

	public v1_08_DataWatcher(AbstractDataWatcher ref) {
		super(ref);
	}

	@Override
	public DataWatcher copy() {
		return new v1_08_DataWatcher(this);
	}

	@Override
	protected void writeObjectData(PacketDataSerializer s, DataWatcherObjekt obj) {
		if(obj == null){
			s.writeByte(127);
		} else {
			s.writeByte((getIdFromClass(obj.getType()) << 5 | obj.getPosition() & 0x1F) & 0xFF);
		}
	}

	@Override
	protected DataWatcherObjekt readObjectData(PacketDataSerializer s) {
		int data = s.readByte();
		if(data == 127) return null;
		int type = (data & 0xE0) >> 5;
		int position = data & 0x1F;
		return createObject(position, type);
	}

}
