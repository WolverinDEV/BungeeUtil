package dev.wolveringer.bungeeutil.entity.datawatcher.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dev.wolveringer.bungeeutil.entity.datawatcher.BlockData;
import dev.wolveringer.bungeeutil.entity.datawatcher.DataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.Direction;
import dev.wolveringer.bungeeutil.entity.datawatcher.EntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.HumanDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.bungeeutil.entity.datawatcher.OptionalBlockPosition;
import dev.wolveringer.bungeeutil.entity.datawatcher.OptionalUUID;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.md_5.bungee.api.chat.BaseComponent;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class v1_9_DataWatcher extends DataWatcher{
	private static final TObjectIntMap v1_9_classToId = new TObjectIntHashMap(10, 0.5F, -1);

	static {
		v1_9_classToId.put(Byte.class, 0);
		v1_9_classToId.put(Integer.class, 1);
		v1_9_classToId.put(Float.class, 2);
		v1_9_classToId.put(String.class, 3);
		v1_9_classToId.put(BaseComponent.class, 4);
		v1_9_classToId.put(Item.class, 5);
		v1_9_classToId.put(Boolean.class, 6);
		v1_9_classToId.put(Vector3f.class, 7);
		v1_9_classToId.put(BlockPosition.class, 8);
		v1_9_classToId.put(OptionalBlockPosition.class, 9);
		v1_9_classToId.put(Direction.class, 10);
		v1_9_classToId.put(OptionalUUID.class, 11);
		v1_9_classToId.put(BlockData.class, 12);
		v1_9_classToId.put(Short.class, 13);
	}

	private static Class<?> getTypeId(int type) {
			for (Object o : v1_9_classToId.keys()) {
				if (v1_9_classToId.get(o) == type) {
					return (Class<?>) o;
				}
			}
		return null;
	}

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

	public v1_9_DataWatcher() {
	}

	public v1_9_DataWatcher(PacketDataSerializer paramPacketDataSerializer) {
		this();
		if(paramPacketDataSerializer != null) {
			this.objekts = this.read(paramPacketDataSerializer);
		}
	}

	@Override
	public DataWatcher copy() {
		v1_9_DataWatcher watcher = new v1_9_DataWatcher();
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
			clazz = (Class<T>) v1_9_EntityDataWatcher.class;
		} else if(clazz.isAssignableFrom(LivingEntityDataWatcher.class)) {
			clazz = (Class<T>) v1_9_LivingEntityDataWatcher.class;
		} else if(clazz.isAssignableFrom(HumanDataWatcher.class)) {
			clazz = (Class<T>) v1_9_HumanEntityDataWatcher.class;
		}
		if (this.watchers.get(clazz) == null) {
			try {
				this.watchers.put(clazz, clazz.getConstructor(DataWatcher.class).newInstance(this));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
			for (int data = packetdataserializer.readUnsignedByte(); data != 255; data = packetdataserializer.readUnsignedByte()) {
				int pos = data;
				int type = packetdataserializer.readUnsignedByte();
				Object value = null;
				switch (type) {
					case 0:
						value = packetdataserializer.readByte();
						break;
					case 1:
						value = packetdataserializer.readVarInt();
						break;
					case 2:
						value = packetdataserializer.readFloat();
						break;
					case 3:
						value = packetdataserializer.readString(-1);
						break;
					case 4:
						value = packetdataserializer.readRawString();
						break;
					case 5:
						value = packetdataserializer.readItem();
						break;
					case 6:
						value = packetdataserializer.readBoolean();
						break;
					case 7:
						value = new Vector3f(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat());
						break;
					case 8:
						value = packetdataserializer.readBlockPosition();
						break;
					case 9:
						if (packetdataserializer.readBoolean()) {
							value = new OptionalBlockPosition(packetdataserializer.readBlockPosition());
						} else {
							value = new OptionalBlockPosition(null);
						}
						break;
					case 10:
						value = new Direction(packetdataserializer.readVarInt());
						break;
					case 11:
						if (packetdataserializer.readBoolean()) {
							value = new OptionalUUID(packetdataserializer.readUUID());
						} else {
							value = new OptionalUUID(null);
						}
						break;
					case 12:
						value = new BlockData(packetdataserializer.readVarInt());
						break;
				}
				arraylist.add(new DataWatcherObjekt(getTypeId(type), pos, value));
		}
		return arraylist;
	}

	@Override
	public void setValue(int pos, Object object) {
		if (pos > 254) { throw new IllegalArgumentException("Data value id is too big with " + pos + "! (Max is " + 254 + ")"); }
		if (this.objekts.get(pos) == null) {
			this.objekts.set(pos, new DataWatcherObjekt(object.getClass(), pos, null));
		}
		this.objekts.get(pos).setValue(object);
	}

	@Override
	public String toString() {
		return "DataWatcher [v1_9] [objekts=" + this.objekts + "]";
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
		packetdataserializer.writeByte(255); // end
	}

	private void write(PacketDataSerializer s, DataWatcherObjekt o) {
			s.writeByte(o.getPostition());
			int typeId = v1_9_classToId.get(o.getType());
			if (typeId == 13) {
				s.writeByte(v1_9_classToId.get(Integer.class));
			} else {
				s.writeByte(typeId);
			}

			switch (typeId) {
				case 0:
					s.writeByte((byte) o.getValue());
					break;
				case 1:
					s.writeVarInt((int) o.getValue());
					break;
				case 2:
					s.writeFloat((float) o.getValue());
					break;
				case 3:
					s.writeString((String) o.getValue());
					break;
				case 4:
					s.writeRawString((BaseComponent) o.getValue());
					break;
				case 5:
					s.writeItem((Item) o.getValue());
					break;
				case 6:
					s.writeBoolean((boolean) o.getValue());
					break;
				case 7:
					Vector3f v = (Vector3f) o.getValue();
					s.writeFloat(v.getX());
					s.writeFloat(v.getY());
					s.writeFloat(v.getZ());
					break;
				case 8:
					s.writeBlockPosition((BlockPosition) o.getValue());
					break;
				case 9:
					OptionalBlockPosition p = (OptionalBlockPosition) o.getValue(); // Optional
					s.writeBoolean(p.getPosition() != null);
					if (p != null) {
						s.writeBlockPosition(p.getPosition());
					}
					break;
				case 10:
					s.writeVarInt(((Direction) o.getValue()).getDirection()); // Direction
					break;
				case 11:
					OptionalUUID uuid = (OptionalUUID) o.getValue(); // Optional
					s.writeBoolean(uuid.getUuid() != null);
					if (uuid != null) {
						s.writeUUID(uuid.getUuid());
					}
					break;
				case 12:
					s.writeVarInt(((BlockData) o.getValue()).getData()); // Block
					break; // Data
				case 13: // Short will write as an interger
					s.writeVarInt((Short) o.getValue());
					break;
				default:
					System.out.println("Type not found ("+typeId+") ("+o.getType()+")");
					break;
			}
	}
}