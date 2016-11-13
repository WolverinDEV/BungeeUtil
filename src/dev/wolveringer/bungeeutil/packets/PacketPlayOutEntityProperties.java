package dev.wolveringer.bungeeutil.packets;

import java.util.ArrayList;
import java.util.UUID;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityProperties.EntityProperty.EntityPropertyModifier;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;

public class PacketPlayOutEntityProperties extends Packet implements PacketPlayOut{
	public static class EntityProperty {
		public static class EntityPropertyModifier{
			private UUID uuid;
			private double amount;
			private byte operation;
			
			public EntityPropertyModifier(UUID uuid, double amount, byte operation) {
				this.uuid = uuid;
				this.amount = amount;
				this.operation = operation;
			}
			
			public double getAmount() {
				return amount;
			}
			public byte getOperation() {
				return operation;
			}
			public UUID getUUID() {
				return uuid;
			}

			public void setAmount(double amount) {
				this.amount = amount;
			}
			public void setOperation(byte operation) {
				this.operation = operation;
			}
			public void setUUID(UUID uuid) {
				this.uuid = uuid;
			}
			
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				long temp;
				temp = Double.doubleToLongBits(amount);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				result = prime * result + operation;
				result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) return true;
				if (obj == null) return false;
				if (getClass() != obj.getClass()) return false;
				EntityPropertyModifier other = (EntityPropertyModifier) obj;
				if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount)) return false;
				if (operation != other.operation) return false;
				if (uuid == null) {
					if (other.uuid != null) return false;
				}
				else if (!uuid.equals(other.uuid)) return false;
				return true;
			}
		}
		private String name;
		private double value;
		private ArrayList<EntityPropertyModifier> modifiers = new ArrayList<>();
		
		public EntityProperty(String name,double value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		public ArrayList<EntityPropertyModifier> getModifiers() {
			return modifiers;
		}
		public void addModifier(EntityPropertyModifier mod){
			modifiers.add(mod);
		}
		public void removeModifier(EntityPropertyModifier mod){
			modifiers.add(mod);
		}
	}
	int entity;
	ArrayList<EntityProperty> properties = new ArrayList<>();
	
	public PacketPlayOutEntityProperties() {
		super(0x20);
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(entity);
		s.writeInt(properties.size());
		for(EntityProperty prop : properties){
			s.writeString(prop.getName());
			s.writeDouble(prop.getValue());
			s.writeVarInt(prop.getModifiers().size());
			for(EntityPropertyModifier mod : prop.getModifiers()){
				s.writeUUID(mod.getUUID());
				s.writeDouble(mod.getAmount());
				s.writeByte(mod.getOperation());
			}
		}
	}
	@Override
	public void read(PacketDataSerializer s) {
		entity = s.readVarInt();
		int pSize = s.readInt();
		for(int i = 0;i<pSize;i++){
			EntityProperty prop = new EntityProperty(s.readString(-1), s.readDouble());
			int mSize = s.readVarInt();
			if(mSize > 0)
				for(int j = 0;j<mSize;j++)
					prop.addModifier(new EntityPropertyModifier(s.readUUID(), s.readDouble(), s.readByte()));
			properties.add(prop);
		}
	}
	public ArrayList<EntityProperty> getProperties() {
		return properties;
	}
	public PacketPlayOutEntityProperties setProperties(ArrayList<EntityProperty> properties) {
		this.properties = properties;
		return this;
	}
	public int getEntityID() {
		return entity;
	}
	public PacketPlayOutEntityProperties setEntityID(int entity) {
		this.entity = entity;
		return this;
	}
	public PacketPlayOutEntityProperties addProperty(EntityProperty prop){
		properties.add(prop);
		return this;
	}
	
}
