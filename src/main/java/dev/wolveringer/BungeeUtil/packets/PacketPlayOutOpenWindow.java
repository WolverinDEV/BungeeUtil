package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutOpenWindow extends Packet implements PacketPlayOut {
	private int horesID;
	private boolean horse;
	private int id;
	private String name;
	private int slots;
	private String type;
	public boolean UTF_8 = false;
	
	public PacketPlayOutOpenWindow() {
		super(0x2D);
	}
	
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
	
	public int getHoresID() {
		return horesID;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSlots() {
		return slots;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isHorse() {
		return horse;
	}
	
	@Override
	public void read(PacketDataSerializer s) {
		this.id = s.readUnsignedByte();
		this.type = s.readString(32);
		this.name = s.readString(-1);
		this.slots = s.readUnsignedByte();
		if (type.equalsIgnoreCase("EntityHorse")) this.horesID = s.readInt();
	}
	
	public void setHoresID(int horesID) {
		this.horesID = horesID;
	}
	
	public void setHorse(boolean horse) {
		this.horse = horse;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSlots(int slots) {
		this.slots = slots;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public void write(PacketDataSerializer s) {
		s.writeByte(this.id);
		s.writeString(this.type);
		if (!name.startsWith("{")) {
			if (!name.startsWith("\"")) name = "\"" + name + "\"";
			name = "{\"translate\":" + name + "}";
		}
		s.writeString(this.name);
		s.writeByte(this.slots);
		if (this.type.equals("EntityHorse")) {
			s.writeInt(this.horesID);
		}
	}
	
	@Override
	public String toString() {
		return "PacketPlayOutOpenWindow [horesID=" + horesID + ", horse=" + horse + ", id=" + id + ", name=" + name + ", slots=" + slots + ", type=" + type + "]";
	}
	
}
