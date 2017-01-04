package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayOutOpenWindow extends Packet implements PacketPlayOut {
	private int horesID;
	private boolean horse;
	private int id;
	private String name;
	private int slots;
	private String type;
	public boolean UTF_8 = false;

	public PacketPlayOutOpenWindow(int id, String type, String name, int slots, boolean ishorse) {
		super(0x2D);
		this.id = id;
		this.type = type + "";
		this.name = name;
		this.slots = slots;
		this.horse = ishorse;
	}

	public PacketPlayOutOpenWindow(int id, String type, String name, int slots, int horse_id) {
		this(id, type, name, slots, true);
		this.horesID = horse_id;
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.id = s.readUnsignedByte();
		this.type = s.readString(32);
		this.name = s.readString(-1);
		this.slots = s.readUnsignedByte();
		if (this.type.equalsIgnoreCase("EntityHorse")) {
			this.horesID = s.readInt();
		}
	}

	@Override
	public String toString() {
		return "PacketPlayOutOpenWindow [horesID=" + this.horesID + ", horse=" + this.horse + ", id=" + this.id + ", name=" + this.name + ", slots=" + this.slots + ", type=" + this.type + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.id);
		s.writeString(this.type);
		if (!this.name.startsWith("{")) {
			if (!this.name.startsWith("\"")) {
				this.name = "\"" + this.name + "\"";
			}
			this.name = "{\"translate\":" + this.name + "}";
		}
		s.writeString(this.name);
		s.writeByte(this.slots);
		if (this.type.equals("EntityHorse")) {
			s.writeInt(this.horesID);
		}
	}

}
