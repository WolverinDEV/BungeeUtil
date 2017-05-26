package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcherObjekt;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
public abstract class AbstractDataWatcher implements DataWatcher {
	static interface ValueRWHandler<T> {
		int getId();
		void write(PacketDataSerializer s, T obj);
		T read(PacketDataSerializer s);
	}
	
	@FunctionalInterface
	static interface ValueWHandler<T> {
		void write(PacketDataSerializer s, T obj);
	}
	
	@AllArgsConstructor
	static class ValueRWHandlerLamba<T> implements ValueRWHandler<T>{
		@Getter
		private final int id;
		private final Function<PacketDataSerializer, T> readFn;
		private final ValueWHandler<T> writeFn;
		
		@Override
		public T read(PacketDataSerializer s) {
			return readFn.apply(s);
		}
		
		@Override
		public void write(PacketDataSerializer s, T obj) {
			writeFn.write(s, obj);
		}
	}
	
	private static HashMap<Class<? extends DataWatcher>, HashMap<Class<?>, ValueRWHandler<?>>> valueWriters = new HashMap<>();
	protected static <T> void registerRWHandler(@NonNull Class<? extends DataWatcher> baseClass,@NonNull Class<T> target,@NonNull ValueRWHandler<T> handler){
		if(!valueWriters.containsKey(baseClass))
			valueWriters.put(baseClass, new HashMap<>());
		valueWriters.get(baseClass).put(target, handler);
	}
	
	private static HashMap<Class<? extends DataWatcher>, HashMap<Class<?>, Class<? extends EntityDataWatcher>>> interfaceTypeMapper = new HashMap<>();
	protected static void registerIMapper(@NonNull Class<? extends DataWatcher> baseClass,@NonNull Class<?> iclass,@NonNull Class<? extends EntityDataWatcher> implClass){
		if(!interfaceTypeMapper.containsKey(baseClass))
			interfaceTypeMapper.put(baseClass, new HashMap<>());
		interfaceTypeMapper.get(baseClass).put(iclass, implClass);
	}
	
	protected List<DataWatcherObjekt> objekts = new ArrayList<DataWatcherObjekt>() {
		@Override
		public DataWatcherObjekt get(int index) {
			return index >= this.size() ? null : super.get(index);
		};

		@Override
		public DataWatcherObjekt set(int index, DataWatcherObjekt element) {
			while (this.size() <= index) {
				this.add(null);
			}
			return super.set(index, element);
		};
	};

	private HashMap<Class<?>, EntityDataWatcher> watchers = new HashMap<Class<?>, EntityDataWatcher>() {
		@Override
		public EntityDataWatcher put(Class<?> key, EntityDataWatcher value) {
			Class<?> _super = key.getSuperclass();
			while (EntityDataWatcher.class.isAssignableFrom(_super) && super.get(_super) == null) {
				super.put(_super, value);
			}
			return super.put(key, value);
		};
	};
	
	public AbstractDataWatcher(){}
	
	public AbstractDataWatcher(AbstractDataWatcher ref){
		for(DataWatcherObjekt obj : ref.objekts)
			if(obj != null)
				objekts.set(obj.getPosition(), new DataWatcherObjekt(obj.getType(), obj.getPosition(), obj.getValue()));
	}
	
	@Override
	public Object get(int i) {
		Validate.isTrue(i >= 0 && i <= 255, "Invalid item range!");
		return objekts.get(i);
	}
	
	@Override
	public <T> T get(Class<T> clazz, int i) {
		Object ret = get(i);
		if(ret == null) return null;
		Validate.isTrue(ret.getClass() == clazz, "Try to get an invalid type (Expected: "+clazz.getName()+", Given: "+ret.getClass()+")");
		return null;
	}
	
	@Override
	public byte getByte(int i) {
		return get(Byte.class, i).byteValue();
	}

	@Override
	public float getFloat(int i) {
		return get(Float.class, i).floatValue();
	}

	@Override
	public int getInt(int i) {
		return get(Integer.class, i).intValue();
	}

	@Override
	public short getShort(int i) {
		return get(Short.class, i).shortValue();
	}

	@Override
	public String getString(int i) {
		return get(String.class, i);
	}
	
	@Override
	public void setValue(int pos, Object object) {
		DataWatcherObjekt watcher = objekts.get(pos);
		if(watcher != null && object != null){
			Validate.isAssignableFrom(watcher.getType(), object.getClass(), "Try to get an invalid type (Expected: "+watcher.getType().getName()+", Given: "+object.getClass()+")");
		}
		if(watcher == null && object != null){
			objekts.set(pos, watcher = new DataWatcherObjekt(object.getClass(), pos, object));
		} else if(watcher != null){
			watcher.setValue(object);
		}
	}
	
	@Override
	public void read(PacketDataSerializer packetdataserializer) {
		objekts.clear();
		
		DataWatcherObjekt current = null;
		while((current = readObjectData(packetdataserializer)) != null){
			current.setValue(readObject(current.getType(), packetdataserializer));
			objekts.add(current);
		}
	}
	
	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		for(DataWatcherObjekt obj : objekts){
			if(obj == null) continue;
			
			writeObjectData(packetdataserializer, obj);
			writeObject(obj, packetdataserializer);
		}
		writeObjectData(packetdataserializer, null);
	}
	
	protected abstract void writeObjectData(PacketDataSerializer s, DataWatcherObjekt obj);
	protected abstract DataWatcherObjekt readObjectData(PacketDataSerializer s);
	
	protected DataWatcherObjekt createObject(int position, int type){
		Class<?> objClass = getClassFromId(type);
		Validate.notNull(objClass, "Cant find type "+type+" for class "+getClass());
		return new DataWatcherObjekt(objClass, position, null);
	}
	
	protected <T> Class<T> getClassFromId(int id){
		HashMap<Class<?>, ValueRWHandler<?>> writers = valueWriters.get(getClass());
		Validate.notNull(writers, "Cant find RWHandlerList for class "+getClass());
		for(Entry<Class<?>, ValueRWHandler<?>> entry : writers.entrySet())
			if(entry.getValue().getId() == id)
				return (Class<T>) entry.getKey();
		return null;
	}
	
	protected int getIdFromClass(Class<?> clazz){
		HashMap<Class<?>, ValueRWHandler<?>> writers = valueWriters.get(getClass());
		Validate.notNull(writers, "Cant find RWHandlerList for class "+getClass());
		for(Entry<Class<?>, ValueRWHandler<?>> entry : writers.entrySet())
			if(entry.getKey() == clazz)
				return entry.getValue().getId();
		return -1;
	}
	
	protected <T> T readObject(Class<T> type, PacketDataSerializer s){
		HashMap<Class<?>, ValueRWHandler<?>> writers = valueWriters.get(getClass());
		Validate.notNull(writers, "Cant find RWHandlerList for class "+getClass());
		ValueRWHandler<T> rw = (ValueRWHandler<T>) writers.get(type);
		Validate.notNull(rw, "Cant find RWHandler for type "+type+" of class "+getClass());
		return rw.read(s);
	}
	
	protected <T> void writeObject(DataWatcherObjekt obj, PacketDataSerializer s){
		HashMap<Class<?>, ValueRWHandler<?>> writers = valueWriters.get(getClass());
		Validate.notNull(writers, "Cant find RWHandlerList for class "+getClass());
		ValueRWHandler<T> rw = (ValueRWHandler<T>) writers.get(obj.getType());
		Validate.notNull(rw, "Cant find RWHandler for type "+obj.getType()+" of class "+getClass());
		rw.write(s, (T) obj.getValue());
	}
	
	
	@Override
	public <T extends EntityDataWatcher> T getSpecialDataWatcher(Class<T> clazz) {
		HashMap<Class<?>, Class<? extends EntityDataWatcher>> iMapper = interfaceTypeMapper.get(getClass());
		Validate.notNull(iMapper, "Cant find InterfaceMapper for class "+getClass());
		Class<? extends EntityDataWatcher> targetClass = iMapper.get(clazz);
		Validate.notNull(targetClass, "Cant find interface implementation for "+clazz+" of class "+getClass());
		
		if (this.watchers.get(targetClass) == null) {
			try {
				this.watchers.put(targetClass, targetClass.getConstructor(DataWatcher.class).newInstance(this));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return (T) this.watchers.get(targetClass);
	}
	
	@Override
	public EntityDataWatcher getEntityDataWatcher() {
		return getSpecialDataWatcher(EntityDataWatcher.class);
	}
}
