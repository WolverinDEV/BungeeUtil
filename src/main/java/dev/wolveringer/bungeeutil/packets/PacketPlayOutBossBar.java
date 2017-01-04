package dev.wolveringer.bungeeutil.packets;

import java.util.UUID;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

	private BaseComponent title;
	private float health;
	private BarColor color;
	private BarDivision division;
	private short flags;

	@Override
	public void read(PacketDataSerializer s) {
		this.barId = s.readUUID();
		int action = s.readVarInt();
		if(action >= Action.values().length){
			System.out.println("Boss bar wrong..... BarId: "+this.barId);
			System.out.println("Avariable data: "+s.readableBytes());
			s.skipBytes(s.readableBytes());
		}
		this.action = Action.values()[action];
		switch (this.action) {
			case CREATE:
				this.title = s.readRawString();
				this.health = s.readFloat();
				this.color = BarColor.values()[s.readVarInt()];
				this.division = BarDivision.values()[s.readVarInt()];
				this.flags = s.readUnsignedByte();
				break;
			case UPDATE_HEALTH:
				this.health = s.readFloat();
				break;
			case UPDATE_TITLE:
				this.title = s.readRawString();
				break;
			case UPDATE_STYLE:
				this.color = BarColor.values()[s.readVarInt()];
				this.division = BarDivision.values()[s.readVarInt()];
				break;
			case UPDATE_FLAGS:
				this.flags = s.readUnsignedByte();
				break;
			case DELETE:
				break;
		}
	}

	@Override
	public String toString() {
		return "PacketPlayOutBossBar [barId=" + this.barId + ", action=" + this.action + ", title=" + this.title + ", health=" + this.health + ", color=" + this.color + ", division=" + this.division + ", flags=" + this.flags + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeUUID(this.barId);
		s.writeVarInt(this.action.ordinal());
		switch (this.action) {
			case CREATE:
				s.writeRawString(this.title);
				s.writeFloat(this.health);
				s.writeVarInt(this.color.ordinal());
				s.writeVarInt(this.division.ordinal());
				s.writeByte(this.flags);
				break;
			case UPDATE_HEALTH:
				s.writeFloat(this.health);
				break;
			case UPDATE_TITLE:
				s.writeRawString(this.title);
				break;
			case UPDATE_STYLE:
				s.writeVarInt(this.color.ordinal());
				s.writeVarInt(this.division.ordinal());
				break;
			case UPDATE_FLAGS:
				s.writeByte(this.flags);
				break;
			case DELETE:
				break;
		}
	}
}
