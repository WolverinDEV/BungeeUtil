package dev.wolveringer.BungeeUtil.packets;

import java.util.UUID;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayOutBossBar extends Packet implements PacketPlayOut{
	public static enum Action {
		CREATE,
		DELETE,
		UPDATE_HEALTH,
		UPDATE_TITLE,
		UPDATE_STYLE,
		UPDATE_FLAGS;
		
		private Action() {}
	}
	public static enum BarColor {
		PING,
		BLUE,
		RED,
		GREEN,
		YELLOW,
		PURPLE,
		WHITE;
		
		private BarColor() {}
	}
	public static enum BarDivision {
		NO_DIVISION,
		SIX_DIVISIONS,
		TEN_DIVISIONS,
		TWELF_DIVISIONS,
		TWENTY_DIVISIONS;
	}
	private UUID barId;
	private Action action;
	
	private IChatBaseComponent title;
	private float health;
	private BarColor color;
	private BarDivision division;
	private short flags;
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void read(PacketDataSerializer s) {
		if(getBigVersion() != BigClientVersion.v1_9)
			return;
		barId = s.readUUID();
		int action = s.readVarInt();
		if(action >= Action.values().length){
			System.out.println("Boss bar wrong..... BarId: "+barId);
			System.out.println("Avariable data: "+s.readableBytes());
			s.skipBytes(s.readableBytes());
		}
		this.action = Action.values()[action];
		switch (this.action) {
			case CREATE:
				title = s.readRawString();
				health = s.readFloat();
				color = BarColor.values()[s.readVarInt()];
				division = BarDivision.values()[s.readVarInt()];
				flags = s.readUnsignedByte();
				break;
			case UPDATE_HEALTH:
				health = s.readFloat();
				break;
			case UPDATE_TITLE:
				title = s.readRawString();
				break;
			case UPDATE_STYLE:
				color = BarColor.values()[s.readVarInt()];
				division = BarDivision.values()[s.readVarInt()];
				break;
			case UPDATE_FLAGS:
				flags = s.readUnsignedByte();
				break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeUUID(barId);
		s.writeVarInt(action.ordinal());
		switch (action) {
			case CREATE:
				s.writeRawString(title);
				s.writeFloat(health);
				s.writeVarInt(color.ordinal());
				s.writeVarInt(division.ordinal());
				s.writeByte(flags);
				break;
			case UPDATE_HEALTH:
				s.writeFloat(health);
				break;
			case UPDATE_TITLE:
				s.writeRawString(title);
				break;
			case UPDATE_STYLE:
				s.writeVarInt(color.ordinal());
				s.writeVarInt(division.ordinal());
				break;
			case UPDATE_FLAGS:
				s.writeByte(flags);
				break;
		}
	}

	public UUID getBarId() {
		return barId;
	}

	public PacketPlayOutBossBar setBarId(UUID barId) {
		this.barId = barId;
		return this;
	}

	public Action getAction() {
		return action;
	}

	public PacketPlayOutBossBar setAction(Action action) {
		this.action = action;
		return this;
	}

	public IChatBaseComponent getTitle() {
		return title;
	}

	public PacketPlayOutBossBar setTitle(IChatBaseComponent title) {
		this.title = title;
		return this;
	}

	public float getHealth() {
		return health;
	}

	public PacketPlayOutBossBar setHealth(float health) {
		this.health = health;
		return this;
	}

	public BarColor getColor() {
		return color;
	}

	public PacketPlayOutBossBar setColor(BarColor color) {
		this.color = color;
		return this;
	}

	public BarDivision getDivision() {
		return division;
	}

	public PacketPlayOutBossBar setDivision(BarDivision division) {
		this.division = division;
		return this;
	}

	public short getFlags() {
		return flags;
	}

	public PacketPlayOutBossBar setFlags(short flags) {
		this.flags = flags;
		return this;
	}

	@Override
	public String toString() {
		return "PacketPlayOutBossBar [barId=" + barId + ", action=" + action + ", title=" + title + ", health=" + health + ", color=" + color + ", division=" + division + ", flags=" + flags + "]";
	}
}
