package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.player.HandType;
import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.bungeeutil.position.Vector3f;

public class PacketPlayInBlockPlace extends Packet implements PacketPlayIn {
	private BlockPosition loc;
	private int face;
	private int hand = 0;
	private Item item;
	private Vector3f cursorPosition;

	public PacketPlayInBlockPlace() {}

	@Override
	public void read(PacketDataSerializer s) {
		loc = s.readBlockPosition();
		
		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			face = s.readVarInt();
			hand = s.readVarInt();
			break;
		case v1_8:
			face = s.readUnsignedByte();
			item = s.readItem();
			break;
		default:
			break;
		}
		
		if(face == 255)
			loc.setY(255);
		switch (getBigVersion()) {
		case v1_11:
			cursorPosition = new Vector3f(s.readFloat(), s.readFloat(), s.readFloat());
			break;
		case v1_10:
		case v1_9:
		case v1_8:
			cursorPosition = new Vector3f((float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F);
			break;
		default:
			break;
		}
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);

		switch (getBigVersion()) {
		case v1_11:
		case v1_10:
		case v1_9:
			s.writeVarInt(face);
			s.writeVarInt(hand);
			break;
		case v1_8:
			s.writeByte(face);
			s.writeItem(item);
			break;
		default:
			break;
		}
		
		switch (getBigVersion()) {
		case v1_11:
			s.writeFloat(cursorPosition.getX());
			s.writeFloat(cursorPosition.getY());
			s.writeFloat(cursorPosition.getZ());
			break;
		case v1_10:
		case v1_9:
		case v1_8:
			s.writeByte((int) (cursorPosition.getX() * 16.0F));
			s.writeByte((int) (cursorPosition.getY() * 16.0F));
			s.writeByte((int) (cursorPosition.getZ() * 16.0F));
			break;
		default:
			break;
		}
	}

	public HandType getHand() {
		return HandType.values()[hand];
	}
	
	public void setHand(HandType hand) {
		this.hand = hand.ordinal();
	}
	
	@Override
	public String toString() {
		return "PacketPlayInBlockPlace [loc=" + loc + ", face=" + face + ", item=" + item + ", cursorPosition=" + cursorPosition + "]";
	}

	public BlockPosition getLoc() {
		return loc;
	}

	public void setLoc(BlockPosition loc) {
		this.loc = loc;
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Vector3f getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(Vector3f cursorPosition) {
		this.cursorPosition = cursorPosition;
	}
}
