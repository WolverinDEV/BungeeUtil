package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.api.position.BlockPosition;
import dev.wolveringer.api.position.Vector3f;
import dev.wolveringer.packet.PacketDataSerializer;

public class PacketPlayInBlockPlace extends Packet implements PacketPlayIn {
	BlockPosition loc;
	int face;
	Item item;
	Vector3f cursorPosition;

	public PacketPlayInBlockPlace() {
		super(0x08);
	}

	@Override
	public void read(PacketDataSerializer s) {
		loc = s.readBlockPosition();
		face = s.readUnsignedByte();
		if(face == 255)
			loc.setY(255);
		item = s.readItem();
		cursorPosition = new Vector3f((float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F, (float) s.readUnsignedByte() / 16.0F);
	}

	@Override
	public void write(PacketDataSerializer s) {
		s.writeBlockPosition(loc);
		s.writeByte(face);
		s.writeItem(item);
		s.writeByte((int) (cursorPosition.getX() * 16.0F));
		s.writeByte((int) (cursorPosition.getY() * 16.0F));
		s.writeByte((int) (cursorPosition.getZ() * 16.0F));
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
