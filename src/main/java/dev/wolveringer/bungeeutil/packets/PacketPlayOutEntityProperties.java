package dev.wolveringer.bungeeutil.packets;

import java.util.ArrayList;
import java.util.UUID;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityProperties.EntityProperty.EntityPropertyModifier;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutEntityProperties extends Packet implements PacketPlayOut {
	@Getter
	@Setter
	public static class EntityProperty {
		@Getter
		@Setter
		@AllArgsConstructor
		public static class EntityPropertyModifier {
			private UUID uuid;
			private double amount;
			private byte operation;

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (this.getClass() != obj.getClass()) {
					return false;
				}
				EntityPropertyModifier other = (EntityPropertyModifier) obj;
				if (Double.doubleToLongBits(this.amount) != Double.doubleToLongBits(other.amount)) {
					return false;
				}
				if (this.operation != other.operation) {
					return false;
				}
				if (this.uuid == null) {
					if (other.uuid != null) {
						return false;
					}
				} else if (!this.uuid.equals(other.uuid)) {
					return false;
				}
				return true;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				long temp;
				temp = Double.doubleToLongBits(this.amount);
				result = prime * result + (int) (temp ^ temp >>> 32);
				result = prime * result + this.operation;
				result = prime * result + (this.uuid == null ? 0 : this.uuid.hashCode());
				return result;
			}
		}

		private String name;
		private double value;
		private ArrayList<EntityPropertyModifier> modifiers = new ArrayList<>();

		public EntityProperty(String name, double value) {
			this.name = name;
			this.value = value;
		}

		public void addModifier(EntityPropertyModifier mod) {
			this.modifiers.add(mod);
		}

		public void removeModifier(EntityPropertyModifier mod) {
			this.modifiers.add(mod);
		}
	}

	private int entityId;
	private ArrayList<EntityProperty> properties = new ArrayList<>();

	public PacketPlayOutEntityProperties addProperty(EntityProperty prop) {
		this.properties.add(prop);
		return this;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.entityId = s.readVarInt();
		int pSize = s.readInt();
		for (int i = 0; i < pSize; i++) {
			EntityProperty prop = new EntityProperty(s.readString(-1), s.readDouble());
			int mSize = s.readVarInt();
			if (mSize > 0) {
				for (int j = 0; j < mSize; j++) {
					prop.addModifier(new EntityPropertyModifier(s.readUUID(), s.readDouble(), s.readByte()));
				}
			}
			this.properties.add(prop);
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeVarInt(this.entityId);
		s.writeInt(this.properties.size());
		for (EntityProperty prop : this.properties) {
			s.writeString(prop.getName());
			s.writeDouble(prop.getValue());
			s.writeVarInt(prop.getModifiers().size());
			for (EntityPropertyModifier mod : prop.getModifiers()) {
				s.writeUUID(mod.getUuid());
				s.writeDouble(mod.getAmount());
				s.writeByte(mod.getOperation());
			}
		}
	}

}
