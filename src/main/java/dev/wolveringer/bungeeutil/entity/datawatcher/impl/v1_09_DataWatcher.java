package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import java.util.Optional;

import dev.wolveringer.bungeeutil.entity.datawatcher.BlockData;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcherObjekt;
import dev.wolveringer.bungeeutil.entity.datawatcher.Direction;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanEntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.OptionalBlockPosition;
import dev.wolveringer.bungeeutil.entity.datawatcher.OptionalUUID;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;
import net.md_5.bungee.api.chat.BaseComponent;

public class v1_09_DataWatcher extends AbstractDataWatcher {
	protected static void register(Class<? extends AbstractDataWatcher> clazz){
		registerRWHandler(clazz, Byte.class, new ValueRWHandlerLamba<>(0, PacketDataSerializer::readByte, (s, b) -> s.writeByte(b)));
		registerRWHandler(clazz, Integer.class, new ValueRWHandlerLamba<>(1, s -> s.readVarInt(), (s, b) -> s.writeVarInt(b)));
		registerRWHandler(clazz, Float.class, new ValueRWHandlerLamba<>(2, s -> s.readFloat(), (s, b) -> s.writeFloat(b)));
		registerRWHandler(clazz, String.class, new ValueRWHandlerLamba<>(3, s -> s.readString(-1), (s, b) -> s.writeString(b)));
		registerRWHandler(clazz, BaseComponent.class, new ValueRWHandlerLamba<>(4, s -> s.readRawString(), (s, b) -> s.writeRawString(b)));
		registerRWHandler(clazz, Item.class, new ValueRWHandlerLamba<>(5, s -> s.readItem(), (s, b) -> s.writeItem(b)));
		registerRWHandler(clazz, Boolean.class, new ValueRWHandlerLamba<>(6, s -> s.readBoolean(), (s, b) -> s.writeBoolean(b)));
		registerRWHandler(clazz, Vector3f.class, new ValueRWHandlerLamba<>(7, s -> new Vector3f(s.readFloat(), s.readFloat(), s.readFloat()), (s, b) -> {
			s.writeFloat(b.getX());
			s.writeFloat(b.getY());
			s.writeFloat(b.getZ());
		}));
		registerRWHandler(clazz, BlockPosition.class, new ValueRWHandlerLamba<>(8, s -> s.readBlockPosition(), (s, b) -> s.writeBlockPosition(b)));
		registerRWHandler(clazz, OptionalBlockPosition.class, new ValueRWHandlerLamba<>(9, s -> new OptionalBlockPosition(s.readBoolean() ? s.readBlockPosition() : null), (s, b) -> {
			s.writeBoolean(b.getPosition() != null);
			if(b.getPosition() != null) s.writeBlockPosition(b.getPosition());
		}));
		registerRWHandler(clazz, Direction.class, new ValueRWHandlerLamba<>(10, s -> new Direction(s.readVarInt()), (s, b) -> s.writeVarInt(b.getDirection())));
		registerRWHandler(clazz, OptionalUUID.class, new ValueRWHandlerLamba<>(11, s -> new OptionalUUID(s.readBoolean() ? s.readUUID() : null), (s, b) -> {
			s.writeBoolean(b.getUuid() != null);
			if(b.getUuid() != null) s.writeUUID(b.getUuid());
		}));
		registerRWHandler(clazz, BlockData.class, new ValueRWHandlerLamba<>(12, s -> new BlockData(s.readVarInt()), (s, b) -> s.writeInt(b.getData())));
	
		//Lagacy support (1.8 shorts)
		registerRWHandler(clazz, Short.class, new ValueRWHandlerLamba<>(-1, s -> new Short((short) s.readVarInt()), (s, b) -> s.writeVarInt(b.intValue())));
	
		
		registerIMapper(clazz, EntityDataWatcher.class, v1_09_EntityDataWatcher.class);
		registerIMapper(clazz, LivingEntityDataWatcher.class, v1_09_LivingEntityDataWatcher.class);
		registerIMapper(clazz, HumanEntityDataWatcher.class, v1_09_HumanEntityDataWatcher.class);
	}
	static {
		register(v1_09_DataWatcher.class);
	}
	
	public v1_09_DataWatcher() {
		super();
	}

	public v1_09_DataWatcher(AbstractDataWatcher ref) {
		super(ref);
	}

	@Override
	public DataWatcher copy() {
		return new v1_09_DataWatcher(this);
	}

	@Override
	protected void writeObjectData(PacketDataSerializer s, DataWatcherObjekt obj) {
		if(obj == null)
			s.writeByte(0xFF);
		else {
			s.writeByte(obj.getPosition());
			s.writeByte(getIdFromClass(obj.getType()));
		}
	}

	@Override
	protected DataWatcherObjekt readObjectData(PacketDataSerializer s) {
		int position = s.readUnsignedByte();
		if(position == 0xFF) return null;
		return createObject(position, s.readUnsignedByte());
	}
}
