package dev.wolveringer.bungeeutil.packetlib.reader;

import java.util.UUID;

import com.google.common.base.Charsets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTReadLimiter;
import dev.wolveringer.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class PacketDataSerializer_v1_7 extends PacketDataSerializer {

	public PacketDataSerializer_v1_7(byte pid) {
		this(pid,ByteBuffCreator.createByteBuff());
	}

	public PacketDataSerializer_v1_7(byte b, ByteBuf buf) {
		super(buf);
		this.writeByte(b);
	}

	public PacketDataSerializer_v1_7(ByteBuf bytebuf) {
		super(bytebuf);
	}

	public void ab(String s) {
		byte[] abyte = s.getBytes();

		if(abyte.length > 32767){
			throw new RuntimeException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		}else{
			this.writeInteger(abyte.length);
			this.writeBytes(abyte);
		}
	}
	@Override
	public BlockPosition readBlockPosition() {
		return new BlockPosition(this.readInt(), this.readUnsignedByte(), this.readInt());
	}

	@SuppressWarnings("deprecation")
	@Override
	public Item readItem() {
		Item itemstack = null;
		short id = this.readShort();

		if(id >= 0){
			byte amauth = this.readByte();
			short durbility = this.readShort();
			itemstack = new Item(id, amauth, durbility);
			try{
				itemstack.setTag(this.readNBT());
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		return itemstack;
	}

	@Override
	public NBTTagCompound readNBT() {
		try{
			short length = this.readShort();
			if(length < 0){
				return null;
			}else{
				byte[] abyte = new byte[length];

				this.readBytes(abyte);
				try{
					return NBTCompressedStreamTools.read(abyte, new NBTReadLimiter(2097152L));
				}catch (java.io.IOException e){
					e.printStackTrace();
					return null;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return new NBTTagCompound();
	}

	@Override
	public BaseComponent readRawString() {
		return ComponentSerializer.parse(this.readString(-1))[0];
	}

	@Override
	public String readString(int i) {
		int j = this.readVariableInteger();
		if(i == -1) {
			i = j * 4;
		}
		if(j > i * 4){
			throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
		}else if(j < 0){
			throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
		}else{
			String s = new String(this.readBytes(j).array(), Charsets.UTF_8);

			if(s.length() > i){
				throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
			}else{
				return s;
			}
		}
	}

	@Override
	public UUID readUUID() {
		return new UUID(this.readLong(), this.readLong());
	}

	public int readVariableInteger() {
		int returns = 0;
		int byte_pos = 0;
		byte readbyte;
		do{
			readbyte = this.readByte();
			returns |= (readbyte & 127) << byte_pos++ * 7;
			if(byte_pos > 5){
				throw new RuntimeException("VarInt too big");
			}
		}while ((readbyte & 128) == 128);

		return returns;
	}

	@Override
	public void writeBlockPosition(BlockPosition loc) {
		this.writeInt(loc.getX());
		this.writeShort(loc.getY());
		this.writeInt(loc.getZ());
	}

	public void writeInteger(int i) {
		while ((i & -128) != 0){
			this.writeByte(i & 127 | 128);
			i >>>= 7;
		}
		this.writeByte(i);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void writeItem(Item itemstack) {
		if(itemstack == null){
			this.writeShort(-1);
		}else{
			this.writeShort(itemstack.getTypeId());
			this.writeByte(itemstack.getAmount());
			this.writeShort(itemstack.getDurability());
			this.writeNBT(itemstack.getTag());
		}
	}

	@Override
	public void writeNBT(NBTTagCompound nbttagcompound) {
		if(nbttagcompound == null){
			this.writeShort(-1);
		}else{
			byte[] abyte = null;
			try{
				abyte = NBTCompressedStreamTools.toByte(nbttagcompound);
			}catch (Exception e){
				e.printStackTrace();
			}

			this.writeShort((short) abyte.length);
			this.writeBytes(abyte);
		}
	}

	@Override
	public void writeRawString(BaseComponent s) {
		this.writeString(ComponentSerializer.toString(s));
	}
	@Override
	public void writeString(String s) {
		byte[] abyte = s.getBytes(Charsets.UTF_8);

		if(abyte.length > 32767){
			throw new RuntimeException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		}else{
			this.writeInteger(abyte.length);
			this.writeBytes(abyte);
		}
	}
	@Override
	public void writeUUID(UUID uuid) {
		this.writeLong(uuid.getMostSignificantBits());
		this.writeLong(uuid.getLeastSignificantBits());
	}
}