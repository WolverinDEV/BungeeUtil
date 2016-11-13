package dev.wolveringer.BungeeUtil.packetlib.reader;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.io.DataOutput;
import java.util.UUID;

import net.md_5.bungee.protocol.DefinedPacket;

import com.google.common.base.Charsets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTReadLimiter;
import dev.wolveringer.nbt.NBTTagCompound;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;

public class PacketDataSerializer_v1_8 extends PacketDataSerializer {
	public PacketDataSerializer_v1_8(byte pid) {
		this(pid, ByteBuffCreator.createByteBuff());
	}

	public PacketDataSerializer_v1_8(ByteBuf bytebuf) {
		super(bytebuf);
	}

	public PacketDataSerializer_v1_8(byte b, ByteBuf buf) {
		super(buf);
		DefinedPacket.writeVarInt(b, this);
	}

	public static int a(int i) {
		for(int j = 1;j < 5;j++){
			if((i & -1 << j * 7) == 0){
				return j;
			}
		}
		return 5;
	}

	public void a(byte[] abyte) {
		writeVarInt(abyte.length);
		writeBytes(abyte);
	}

	public byte[] a() {
		byte[] abyte = new byte[e()];

		readBytes(abyte);
		return abyte;
	}

	@SuppressWarnings("rawtypes")
	public Enum a(Class oclass) {
		return ((Enum[]) oclass.getEnumConstants())[e()];
	}

	@SuppressWarnings("rawtypes")
	public void a(Enum oenum) {
		writeVarInt(oenum.ordinal());
	}

	public int e() {
		int i = 0;
		int j = 0;
		byte b0;
		do{
			b0 = readByte();
			i |= (b0 & 0x7F) << j++ * 7;
			if(j > 5){
				throw new RuntimeException("VarInt too big");
			}
		}while ((b0 & 0x80) == 128);
		return i;
	}

	public long f() {
		long i = 0L;
		int j = 0;
		byte b0;
		do{
			b0 = readByte();
			i |= (b0 & 0x7F) << j++ * 7;
			if(j > 10){
				throw new RuntimeException("VarLong too big");
			}
		}while ((b0 & 0x80) == 128);
		return i;
	}

	public void a(UUID uuid) {
		writeLong(uuid.getMostSignificantBits());
		writeLong(uuid.getLeastSignificantBits());
	}

	public UUID g() {
		return new UUID(readLong(), readLong());
	}

	public void writeVarInt(int i) {
		while ((i & 0xFFFFFF80) != 0){
			writeByte(i & 0x7F | 0x80);
			i >>>= 7;
		}
		writeByte(i);
	}

	public void b(long i) {
		while ((i & 0xFFFFFF80) != 0L){
			writeByte((int) (i & 0x7F) | 0x80);
			i >>>= 7;
		}
		writeByte((int) i);
	}

	public void a(NBTTagCompound nbttagcompound) {
		if(nbttagcompound == null){
			writeByte(0);
		}else{
			try{
				NBTCompressedStreamTools.write(nbttagcompound, (DataOutput) new ByteBufOutputStream(this));
			}catch (Exception ioexception){
				throw new EncoderException(ioexception);
			}
		}
	}

	public NBTTagCompound h() {
		int i = readerIndex();
		byte b0 = readByte();
		if(b0 == 0){
			return null;
		}
		readerIndex(i);
		try{
			return NBTCompressedStreamTools.read(new ByteBufInputStream(this), new NBTReadLimiter(2097152L));
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void a(Item itemstack) {
		if(itemstack == null){
			writeShort(-1);
		}else{
			writeShort(itemstack.getTypeId());
			writeByte(itemstack.getAmount());
			writeShort(itemstack.getDurability());
			a(itemstack.getTag());
		}
	}

	@SuppressWarnings("deprecation")
	public Item i() {
		Item itemstack = null;
		short id = readShort();
		if(id >= 0){
			byte amauth = readByte();
			short durbility = readShort();
			itemstack = new Item(id, amauth, durbility);
			itemstack.setTag(h());
		}
		return itemstack;
	}

	public String c(int i) {
		int j = readVarInt();
		if(i == -1) //Overflow fix
			i = Short.MAX_VALUE;
		if(j > i * 4){
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
		}
		if(j < 0){
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		}
		byte[] x = new byte[j];
		readBytes(x);
		String s = new String(x, Charsets.UTF_8);
		if(s.length() > i && i != -1){
			throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
		}
		return s;
	}

	public PacketDataSerializer a(String s) {
		byte[] abyte = s.getBytes(Charsets.UTF_8);
		if(abyte.length > 32767){
			throw new EncoderException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		}
		writeVarInt(abyte.length);
		writeBytes(abyte);
		return this;
	}

	@Override
	public BlockPosition readBlockPosition() {
		long val = readLong();
		return new BlockPosition((int) (val >> 38), (int) (val << 26 >> 52), (int) (val << 38 >> 38));
	}

	@Override
	public void writeBlockPosition(BlockPosition loc) {
		writeLong(loc.toLong());
	}

	@Override
	public void writeItem(Item i) {
		a(i);
	}

	@Override
	public Item readItem() {
		return i();
	}

	@Override
	public void writeString(String s) {
		a(s);
	}

	@Override
	public String readString(int max) {
		return c(max);
	}

	@Override
	public void writeRawString(IChatBaseComponent s) {
		writeString(ChatSerializer.toJSONString(s));
	}

	@Override
	public IChatBaseComponent readRawString() {
		return ChatSerializer.fromJSON(readString(32767));
	}

	@Override
	public void writeUUID(UUID i) {
		a(i);
	}

	@Override
	public UUID readUUID() {
		return g();
	}

	@Override
	public void writeNBT(NBTTagCompound c) {
		a(c);
	}

	@Override
	public NBTTagCompound readNBT() {
		return h();
	}
}
