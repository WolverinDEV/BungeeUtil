package dev.wolveringer.api.datawatcher;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.api.position.Vector3f;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataWatcher {
	private static final TObjectIntMap v1_8_classToId = new TObjectIntHashMap(10, 0.5F, -1);
	private static final TObjectIntMap v1_9_classToId = new TObjectIntHashMap(10, 0.5F, -1);
	
	static {
		v1_8_classToId.put(Byte.class, 0);
		v1_8_classToId.put(Short.class, 1);
		v1_8_classToId.put(Integer.class, 2);
		v1_8_classToId.put(Float.class, 3);
		v1_8_classToId.put(String.class, 4);
		v1_8_classToId.put(Item.class, 5);
		v1_8_classToId.put(BlockPosition.class, 6);
		v1_8_classToId.put(Vector3f.class, 7);
		
		v1_9_classToId.put(Byte.class, 0);
		v1_9_classToId.put(Integer.class, 1);
		v1_9_classToId.put(Float.class, 2);
		v1_9_classToId.put(String.class, 3);
		v1_9_classToId.put(IChatBaseComponent.class, 4);
		v1_9_classToId.put(Item.class, 5);
		v1_9_classToId.put(Boolean.class, 6);
		v1_9_classToId.put(Vector3f.class, 7);
		v1_9_classToId.put(BlockPosition.class, 8);
		v1_9_classToId.put(OptionalBlockPosition.class, 9);
		v1_9_classToId.put(Direction.class, 10);
		v1_9_classToId.put(OptionalUUID.class, 11);
		v1_9_classToId.put(BlockData.class, 12);
		
		v1_9_classToId.put(Short.class, 13); // Removed short... set to int
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
	
	public DataWatcher(BigClientVersion version, PacketDataSerializer paramPacketDataSerializer) {
		this();
		this.objekts = read(version, paramPacketDataSerializer);
	}
	
	public DataWatcher() {
	}
	
	public void write(BigClientVersion version, PacketDataSerializer packetdataserializer) {
		Iterator iterator = objekts.iterator();
		while (iterator.hasNext()) {
			DataWatcherObjekt watchableobject = (DataWatcherObjekt) iterator.next();
			if (watchableobject == null) continue;
			write(version, packetdataserializer, watchableobject);
		}
		if (version == BigClientVersion.v1_9) packetdataserializer.writeByte(255);
		else packetdataserializer.writeByte(127); // end
	}
	
	public void write(BigClientVersion version, PacketDataSerializer s, DataWatcherObjekt o) {
		if (version == BigClientVersion.v1_9) {
			s.writeByte(o.getPostition());
			int typeId = v1_9_classToId.get(o.getType());
			System.out.println("Type: " + o.getType() + " Id: " + typeId);
			if (typeId == 13) s.writeVarInt(v1_9_classToId.get(Integer.class));
			else s.writeVarInt(typeId);
			
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
					break;
			}
		}
		else {
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
			}
		}
	}
	
	public static List read(BigClientVersion version, PacketDataSerializer packetdataserializer) {
		ArrayList arraylist = new ArrayList();
		if (version == BigClientVersion.v1_9) {
			for (int data = packetdataserializer.readUnsignedByte(); data != 255; data = packetdataserializer.readUnsignedByte()) {
				byte pos = packetdataserializer.readByte();
				int type = packetdataserializer.readInt();
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
				arraylist.add(new DataWatcherObjekt(getTypeId(version, type), pos, value));
			}
		}
		else {
			for (byte data = packetdataserializer.readByte(); data != 127; data = packetdataserializer.readByte()) {
				int type = (data & 0xE0) >> 5;
				int position = data & 0x1F;
				DataWatcherObjekt objekt = null;
				switch (type) {
					case 0:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, Byte.valueOf(packetdataserializer.readByte()));
						break;
						
					case 1:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, Short.valueOf(packetdataserializer.readShort()));
						break;
						
					case 2:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, Integer.valueOf(packetdataserializer.readInt()));
						break;
						
					case 3:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, Float.valueOf(packetdataserializer.readFloat()));
						break;
						
					case 4:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, packetdataserializer.readString(32767));
						break;
						
					case 5:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, packetdataserializer.readItem());
						break;
						
					case 6:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, new BlockPosition(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt()));
						break;
						
					case 7:
						objekt = new DataWatcherObjekt(getTypeId(version, type), position, new Vector3f(packetdataserializer.readFloat(), packetdataserializer.readFloat(), packetdataserializer.readFloat()));
						break;
				}
				
				arraylist.add(objekt);
			}
		}
		return arraylist;
	}
	
	public void setValue(int pos, Object object) {
		if (pos > 31) { throw new IllegalArgumentException("Data value id is too big with " + pos + "! (Max is " + 31 + ")"); }
		if (objekts.get(pos) == null) objekts.set(pos, new DataWatcherObjekt(object.getClass(), pos, null));
		objekts.get(pos).setValue(object);
	}
	
	private static Class<?> getTypeId(BigClientVersion v, int type) {
		if (v == BigClientVersion.v1_9) {
			for (Object o : v1_9_classToId.keys())
				if (v1_9_classToId.get(o) == type) return (Class<?>) o;
		}
		else for (Object o : v1_8_classToId.keys())
			if (v1_8_classToId.get(o) == type) return (Class<?>) o;
		return null;
	}
	
	@Override
	public String toString() {
		return "DataWatcher [objekts=" + objekts + "]";
	}
	
	public DataWatcher copy() {
		DataWatcher watcher = new DataWatcher();
		watcher.objekts = new ArrayList<DataWatcherObjekt>(this.objekts);
		return watcher;
	}
	
	public EntityDataWatcher getEntityDataWatcher() {
		return getSpecialDataWatcher(EntityDataWatcher.class);
	}
	
	public <T extends EntityDataWatcher> T getSpecialDataWatcher(Class<T> clazz) {
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
