package dev.wolveringer.api.datawatcher.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.api.datawatcher.BlockData;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.Direction;
import dev.wolveringer.api.datawatcher.EntityDataWatcher;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;
import dev.wolveringer.api.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.api.datawatcher.OptionalBlockPosition;
import dev.wolveringer.api.datawatcher.OptionalUUID;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.api.position.Vector3f;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class v1_10_DataWatcher extends DataWatcher{
	private static final TObjectIntMap v1_10_classToId = new TObjectIntHashMap(10, 0.5F, -1);
	
	static {
		v1_10_classToId.put(Byte.class, 0);
		v1_10_classToId.put(Integer.class, 1);
		v1_10_classToId.put(Float.class, 2);
		v1_10_classToId.put(String.class, 3);
		v1_10_classToId.put(IChatBaseComponent.class, 4);
		v1_10_classToId.put(Item.class, 5);
		v1_10_classToId.put(Boolean.class, 6);
		v1_10_classToId.put(Vector3f.class, 7);
		v1_10_classToId.put(BlockPosition.class, 8);
		v1_10_classToId.put(OptionalBlockPosition.class, 9);
		v1_10_classToId.put(Direction.class, 10);
		v1_10_classToId.put(OptionalUUID.class, 11);
		v1_10_classToId.put(BlockData.class, 12);
		
		v1_10_classToId.put(Short.class, 13); //old
	}
	
	@SuppressWarnings("serial")
	private List<DataWatcherObjekt> objekts = new ArrayList<DataWatcherObjekt>() {
		public DataWatcherObjekt get(int index) {
			return index >= size() ? null : super.get(index);
		};
		
		public DataWatcherObjekt set(int index, DataWatcherObjekt element) {
			while (size() <= index) {
				add(null);
			}
			return super.set(index, element);
		};
	};
	
	@SuppressWarnings("serial")
	private HashMap<Class, EntityDataWatcher> watchers = new HashMap<Class, EntityDataWatcher>() {
		public EntityDataWatcher put(Class key, EntityDataWatcher value) {
			Class _super = key.getSuperclass();
			while (EntityDataWatcher.class.isAssignableFrom(_super) && super.get(_super) == null) {
				super.put(_super, value);
			}
			return super.put(key, value);
		};
	};
	
	public v1_10_DataWatcher(PacketDataSerializer paramPacketDataSerializer) {
		this();
		if(paramPacketDataSerializer != null)
		this.objekts = read(paramPacketDataSerializer);
	}
	
	public v1_10_DataWatcher() {
	}
	
	public void write(PacketDataSerializer packetdataserializer) {
		Iterator iterator = objekts.iterator();
		while (iterator.hasNext()) {
			DataWatcherObjekt watchableobject = (DataWatcherObjekt) iterator.next();
			if (watchableobject == null) continue;
			write(packetdataserializer, watchableobject);
		}
		packetdataserializer.writeByte(255); // end
	}
	
	private void write(PacketDataSerializer s, DataWatcherObjekt o) {
			s.writeByte(o.getPostition());
			int typeId = v1_10_classToId.get(o.getType());
			if (typeId == 13) s.writeByte(v1_10_classToId.get(Integer.class));
			else s.writeByte(typeId);
			
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
					s.writeRawString((IChatBaseComponent) o.getValue());
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
					if (p != null) s.writeBlockPosition(p.getPosition());
					break;
				case 10:
					s.writeVarInt(((Direction) o.getValue()).getDirection()); // Direction
					break;
				case 11:
					OptionalUUID uuid = (OptionalUUID) o.getValue(); // Optional
					s.writeBoolean(uuid.getUuid() != null);
					if (uuid != null) s.writeUUID(uuid.getUuid());
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
						if (packetdataserializer.readBoolean()) value = new OptionalBlockPosition(packetdataserializer.readBlockPosition());
						else value = new OptionalBlockPosition(null);
						break;
					case 10:
						value = new Direction(packetdataserializer.readVarInt());
						break;
					case 11:
						if (packetdataserializer.readBoolean()) value = new OptionalUUID(packetdataserializer.readUUID());
						else value = new OptionalUUID(null);
						break;
					case 12:
						value = new BlockData(packetdataserializer.readVarInt());
						break;
				}
				arraylist.add(new DataWatcherObjekt(getTypeId(type), pos, value));
		}
		return arraylist;
	}
	
	public void setValue(int pos, Object object) {
		if (pos > 254) { throw new IllegalArgumentException("Data value id is too big with " + pos + "! (Max is " + 254 + ")"); }
		if (objekts.get(pos) == null) objekts.set(pos, new DataWatcherObjekt(object.getClass(), pos, null));
		objekts.get(pos).setValue(object);
	}
	
	private static Class<?> getTypeId(int type) {
			for (Object o : v1_10_classToId.keys())
				if (v1_10_classToId.get(o) == type) return (Class<?>) o;
		return null;
	}
	
	@Override
	public String toString() {
		return "DataWatcher [v1_10] [objekts=" + objekts + "]";
	}
	
	public DataWatcher copy() {
		v1_10_DataWatcher watcher = new v1_10_DataWatcher();
		watcher.objekts = new ArrayList<DataWatcherObjekt>(this.objekts);
		return watcher;
	}
	
	public EntityDataWatcher getEntityDataWatcher() {
		return getSpecialDataWatcher(EntityDataWatcher.class);
	}
	
	public <T extends EntityDataWatcher> T getSpecialDataWatcher(Class<T> clazz) {
		if(clazz.isAssignableFrom(EntityDataWatcher.class))
			clazz = (Class<T>) v1_10_EntityDataWatcher.class;
		else if(clazz.isAssignableFrom(LivingEntityDataWatcher.class))
			clazz = (Class<T>) v1_10_LivingEntityDataWatcher.class;
		else if(clazz.isAssignableFrom(HumanDataWatcher.class))
			clazz = (Class<T>) v1_10_HumanEntityDataWatcher.class;
		if (watchers.get(clazz) == null) {
			try {
				watchers.put(clazz, clazz.getConstructor(DataWatcher.class).newInstance(this));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return (T) watchers.get(clazz);
	}
	
	public Object get(int i) {
		if (objekts.get(i) == null) return null;
		return objekts.get(i).getValue();
	}
	
	public byte getByte(int i) {
		return (byte) get(i);
	}
	
	public short getShort(int i) {
		return (short) get(i);
	}
	
	public int getInt(int i) {
		return (int) get(i);
	}
	
	public String getString(int i) {
		return (String) get(i);
	}
	
	public float getFloat(int i) {
		return (float) get(i);
	}
}