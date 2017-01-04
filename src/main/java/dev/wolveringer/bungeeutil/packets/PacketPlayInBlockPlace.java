package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.HandType;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PacketPlayInBlockPlace extends Packet implements PacketPlayIn {
	private BlockPosition location;
	private int face;
	private int hand = 0;
	private Item item;
	private Vector3f cursorPosition;

	public HandType getHand() {
		return HandType.values()[this.hand];
	}

	@Override
	public void read(PacketDataSerializer s) {
		this.location = s.readBlockPosition();

		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			this.face = s.readVarInt();
			this.hand = s.readVarInt();
			break;
		case v1_8:
			this.face = s.readUnsignedByte();
			this.item = s.readItem();
			break;
		default:
			break;
		}

		if(this.face == 255) {
			this.location.setY(255);
		}
		switch (this.getBigVersion()) {
		case v1_11:
			this.cursorPosition = new Vector3f(s.readFloat(), s.readFloat(), s.readFloat());
			break;
		case v1_10:
		case v1_9:
		case v1_8:
			this.cursorPosition = new Vector3f(s.readUnsignedByte() / 16.0F, s.readUnsignedByte() / 16.0F, s.readUnsignedByte() / 16.0F);
			break;
		default:
			break;
		}
	}

	public void setHand(HandType hand) {
		this.hand = hand.ordinal();
	}

	@Override
	public String toString() {
		return "PacketPlayInBlockPlace [loc=" + this.location + ", face=" + this.face + ", item=" + this.item + ", cursorPosition=" + this.cursorPosition + "]";
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(this.location);

		switch (this.getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			s.writeVarInt(this.face);
			s.writeVarInt(this.hand);
			break;
		case v1_8:
			s.writeByte(this.face);
			s.writeItem(this.item);
			break;
		default:
			break;
		}

		switch (this.getBigVersion()) {
		case v1_11:
			s.writeFloat(this.cursorPosition.getX());
			s.writeFloat(this.cursorPosition.getY());
			s.writeFloat(this.cursorPosition.getZ());
			break;
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeByte((int) (this.cursorPosition.getX() * 16.0F));
			s.writeByte((int) (this.cursorPosition.getY() * 16.0F));
			s.writeByte((int) (this.cursorPosition.getZ() * 16.0F));
			break;
		default:
			break;
		}
	}
}
