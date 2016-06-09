package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.HandType;
import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.api.position.Vector3f;
import dev.wolveringer.packet.PacketDataSerializer;

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
		face = (getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10) ? s.readVarInt() : s.readUnsignedByte();
		if(getBigVersion() == BigClientVersion.v1_9  || getBigVersion() == BigClientVersion.v1_10)
			hand = s.readVarInt();
		if(face == 255)
			loc.setY(255);
		if(getBigVersion() == BigClientVersion.v1_8)
			item = s.readItem();
		cursorPosition = new Vector3f((float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			s.writeVarInt(face);
		else
			s.writeByte(face);
		if(getBigVersion() == BigClientVersion.v1_9 || getBigVersion() == BigClientVersion.v1_10)
			s.writeVarInt(hand);
		if(getBigVersion() == BigClientVersion.v1_8)
			s.writeItem(item);
		s.writeByte((int) (cursorPosition.getX() * 16.0F));
		s.writeByte((int) (cursorPosition.getY() * 16.0F));
		s.writeByte((int) (cursorPosition.getZ() * 16.0F));
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
