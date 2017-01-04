package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class v1_8_DataWatcher extends DataWatcher{
	private static final TObjectIntMap v1_8_classToId = new TObjectIntHashMap(10, 0.5F, -1);

	static {
		v1_8_classToId.put(Byte.class, 0);
		v1_8_classToId.put(Short.class, 1);
		v1_8_classToId.put(Integer.class, 2);
		v1_8_classToId.put(Float.class, 3);
		v1_8_classToId.put(String.class, 4);
		v1_8_classToId.put(Item.class, 5);
		v1_8_classToId.put(BlockPosition.class, 6);
		v1_8_classToId.put(Vector3f.class, 7);
	}

	private static Class<?> getTypeId(int type) {
		for (Object o : v1_8_classToId.keys()) {
			if (v1_8_classToId.get(o) == type) {
				return (Class<?>) o;
			}
		}
		return null;
	}

	@SuppressWarnings("serial")
	private List<DataWatcherObjekt> objekts = new ArrayList<DataWatcherObjekt>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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

	@SuppressWarnings("serial")
	private HashMap<Class, EntityDataWatcher> watchers = new HashMap<Class, EntityDataWatcher>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public EntityDataWatcher put(Class key, EntityDataWatcher value) {
			Class _super = key.getSuperclass();
			while (EntityDataWatcher.class.isAssignableFrom(_super) && super.get(_super) == null) {
				super.put(_super, value);
			}
			return super.put(key, value);
		};
	};

	public v1_8_DataWatcher() {}

	public v1_8_DataWatcher(PacketDataSerializer paramPacketDataSerializer) {
		this();
		if(paramPacketDataSerializer != null) {
			this.objekts = this.read(paramPacketDataSerializer);
		}
	}

	@Override
	public v1_8_DataWatcher copy() {
		v1_8_DataWatcher watcher = new v1_8_DataWatcher();
		watcher.objekts = new ArrayList<DataWatcherObjekt>(this.objekts);
		return watcher;
	}

	@Override
	public Object get(int i) {
		if (this.objekts.get(i) == null) {
			return null;
		}
		return this.objekts.get(i).getValue();
	}

	@Override
	public byte getByte(int i) {
		return (byte) this.get(i);
	}

	@Override
	public EntityDataWatcher getEntityDataWatcher() {
		return this.getSpecialDataWatcher(EntityDataWatcher.class);
	}

	@Override
	public float getFloat(int i) {
		return (float) this.get(i);
	}

	@Override
	public int getInt(int i) {
		return (int) this.get(i);
	}

	@Override
	public short getShort(int i) {
		return (short) this.get(i);
	}

	@Override
	public <T extends EntityDataWatcher> T getSpecialDataWatcher(Class<T> clazz) {
		if(clazz.isAssignableFrom(EntityDataWatcher.class)) {
			clazz = (Class<T>) v1_8_EntityDataWatcher.class;
		} else if(clazz.isAssignableFrom(LivingEntityDataWatcher.class)) {
			clazz = (Class<T>) v1_8_LivingEntityDataWatcher.class;
		} else if(clazz.isAssignableFrom(HumanDataWatcher.class)) {
			clazz = (Class<T>) v1_8_HumanEntityDataWatcher.class;
		}
		if (this.watchers.get(clazz) == null) {
			try {
				this.watchers.put(clazz, clazz.getConstructor(DataWatcher.class).newInstance(this));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return (T) this.watchers.get(clazz);
	}

	@Override
	public String getString(int i) {
		return (String) this.get(i);
	}

	private List read(PacketDataSerializer packetdataserializer) {
		ArrayList arraylist = new ArrayList();
		for (byte data = packetdataserializer.readByte(); data != 127; data = packetdataserializer.readByte()) {
			int type = (data & 0xE0) >> 5;
			int position = data & 0x1F;
			DataWatcherObjekt objekt = null;
			switch (type) {
				case 0:
					objekt = new DataWatcherObjekt(getTypeId(type), position, Byte.valueOf(packetdataserializer.readByte()));
					break;

				case 1:
					objekt = new DataWatcherObjekt(getTypeId(type), position, Short.valueOf(packetdataserializer.readShort()));
					break;

				case 2:
					objekt = new DataWatcherObjekt(getTypeId(type), position, Integer.valueOf(packetdataserializer.readInt()));
					break;

				case 3:
					objekt = new DataWatcherObjekt(getTypeId(type), position, Float.valueOf(packetdataserializer.readFloat()));
					break;

				case 4:
					objekt = new DataWatcherObjekt(getTypeId(type), position, packetdataserializer.readString(32767));
					break;

				case 5:
					objekt = new DataWatcherObjekt(getTypeId(type), position, packetdataserializer.readItem());
					break;

				case 6:
					objekt = new DataWatcherObjekt(getTypeId(type), position, new BlockPosition(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt()));
					break;

				case 7:
					objekt = new DataWatcherObjekt(getTypeId(type), position, new Vector3f(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat()));
					break;
				default:
					System.out.println("Error 01");
			}

			arraylist.add(objekt);
		}
		return arraylist;
	}

	@Override
	public void setValue(int pos, Object object) {
		if (pos > 31) {
			throw new IllegalArgumentException("Data value id is too big with " + pos + "! (Max is " + 31 + ")");
		}
		if (this.objekts.get(pos) == null) {
			this.objekts.set(pos, new DataWatcherObjekt(object.getClass(), pos, null));
		}
		this.objekts.get(pos).setValue(object);
	}

	@Override
	public String toString() {
		return "DataWatcher [v1_8] [objekts=" + this.objekts + "]";
	}

	@Override
	public void write(PacketDataSerializer packetdataserializer) {
		Iterator iterator = this.objekts.iterator();
		while (iterator.hasNext()) {
			DataWatcherObjekt watchableobject = (DataWatcherObjekt) iterator.next();
			if (watchableobject == null) {
				continue;
			}
			this.write(packetdataserializer, watchableobject);
		}
		packetdataserializer.writeByte(127);
	}

	private void write(PacketDataSerializer s, DataWatcherObjekt o) {
		int data = (v1_8_classToId.get(o.getType()) << 5 | o.getPostition() & 0x1F) & 0xFF;
		s.writeByte(data);
		switch (v1_8_classToId.get(o.getType())) {
			case 0:
				s.writeByte(((Byte) o.getValue()).byteValue());
				break;
			case 1:
				s.writeShort(((Short) o.getValue()).shortValue());
				break;
			case 2:
				s.writeInt(((Integer) o.getValue()).intValue());
				break;
			case 3:
				s.writeFloat(((Float) o.getValue()).floatValue());
				break;
			case 4:
				s.writeString((String) o.getValue());
				break;
			case 5:
				Item itemstack = (Item) o.getValue();
				s.writeItem(itemstack);
				break;
			case 6:
				BlockPosition blockposition = (BlockPosition) o.getValue();
				s.writeInt(blockposition.getX());
				s.writeInt(blockposition.getY());
				s.writeInt(blockposition.getZ());
				break;
			case 7:
				Vector3f vector3f = (Vector3f) o.getValue();
				s.writeFloat(vector3f.getX());
				s.writeFloat(vector3f.getY());
				s.writeFloat(vector3f.getZ());
				break;
			default:
				System.out.println("Error 02");
		}
	}
}