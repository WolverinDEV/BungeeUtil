package dev.wolveringer.bungeeutil.packetlib.reader;

import java.io.DataOutput;
import java.util.UUID;

import com.google.common.base.Charsets;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTReadLimiter;
import dev.wolveringer.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.DefinedPacket;

public class PacketDataSerializer_v1_8 extends PacketDataSerializer {
	public static int a(int i) {
		for(int j = 1;j < 5;j++){
			if((i & -1 << j * 7) == 0){
				return j;
			}
		}
		return 5;
	}

	public PacketDataSerializer_v1_8(byte pid) {
		this(pid, ByteBuffCreator.createByteBuff());
	}

	public PacketDataSerializer_v1_8(byte b, ByteBuf buf) {
		super(buf);
		DefinedPacket.writeVarInt(b, this);
	}

	public PacketDataSerializer_v1_8(ByteBuf bytebuf) {
		super(bytebuf);
	}

	public byte[] a() {
		byte[] abyte = new byte[this.e()];

		this.readBytes(abyte);
		return abyte;
	}

	public void a(byte[] abyte) {
		this.writeVarInt(abyte.length);
		this.writeBytes(abyte);
	}

	@SuppressWarnings("rawtypes")
	public Enum a(Class oclass) {
		return ((Enum[]) oclass.getEnumConstants())[this.e()];
	}

	@SuppressWarnings("rawtypes")
	public void a(Enum oenum) {
		this.writeVarInt(oenum.ordinal());
	}

	@SuppressWarnings("deprecation")
	public void a(Item itemstack) {
		if(itemstack == null){
			this.writeShort(-1);
		}else{
			this.writeShort(itemstack.getTypeId());
			this.writeByte(itemstack.getAmount());
			this.writeShort(itemstack.getDurability());
			this.a(itemstack.getTag());
		}
	}

	public void a(NBTTagCompound nbttagcompound) {
		if(nbttagcompound == null){
			this.writeByte(0);
		}else{
			try{
				NBTCompressedStreamTools.write(nbttagcompound, (DataOutput) new ByteBufOutputStream(this));
			}catch (Exception ioexception){
				throw new EncoderException(ioexception);
			}
		}
	}

	public PacketDataSerializer a(String s) {
		byte[] abyte = s.getBytes(Charsets.UTF_8);
		if(abyte.length > 32767){
			throw new EncoderException("String too big (was " + s.length() + " bytes encoded, max " + 32767 + ")");
		}
		this.writeVarInt(abyte.length);
		this.writeBytes(abyte);
		return this;
	}

	public void a(UUID uuid) {
		this.writeLong(uuid.getMostSignificantBits());
		this.writeLong(uuid.getLeastSignificantBits());
	}

	public void b(long i) {
		while ((i & 0xFFFFFF80) != 0L){
			this.writeByte((int) (i & 0x7F) | 0x80);
			i >>>= 7;
		}
		this.writeByte((int) i);
	}

	public String c(int i) {
		int j = this.readVarInt();
		if(i == -1) {
			i = Short.MAX_VALUE;
		}
		if(j > i * 4){
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
		}
		if(j < 0){
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		}
		byte[] x = new byte[j];
		this.readBytes(x);
		String s = new String(x, Charsets.UTF_8);
		if(s.length() > i && i != -1){
			throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
		}
		return s;
	}

	public int e() {
		int i = 0;
		int j = 0;
		byte b0;
		do{
			b0 = this.readByte();
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
			b0 = this.readByte();
			i |= (b0 & 0x7F) << j++ * 7;
			if(j > 10){
				throw new RuntimeException("VarLong too big");
			}
		}while ((b0 & 0x80) == 128);
		return i;
	}

	public UUID g() {
		return new UUID(this.readLong(), this.readLong());
	}

	public NBTTagCompound h() {
		int i = this.readerIndex();
		byte b0 = this.readByte();
		if(b0 == 0){
			return null;
		}
		this.readerIndex(i);
		try{
			return NBTCompressedStreamTools.read(new ByteBufInputStream(this), new NBTReadLimiter(2097152L));
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public Item i() {
		Item itemstack = null;
		short id = this.readShort();
		if(id >= 0){
			byte amauth = this.readByte();
			short durbility = this.readShort();
			itemstack = new Item(id, amauth, durbility);
			itemstack.setTag(this.h());
		}
		return itemstack;
	}

	@Override
	public BlockPosition readBlockPosition() {
		long val = this.readLong();
		return new BlockPosition((int) (val >> 38), (int) (val << 26 >> 52), (int) (val << 38 >> 38));
	}

	@Override
	public Item readItem() {
		return this.i();
	}

	@Override
	public NBTTagCompound readNBT() {
		return this.h();
	}

	@Override
	public BaseComponent readRawString() {
		return ComponentSerializer.parse(this.readString(-1))[0];
	}

	@Override
	public String readString(int max) {
		return this.c(max);
	}

	@Override
	public UUID readUUID() {
		return this.g();
	}

	@Override
	public void writeBlockPosition(BlockPosition loc) {
		this.writeLong(loc.toLong());
	}

	@Override
	public void writeItem(Item i) {
		this.a(i);
	}

	@Override
	public void writeNBT(NBTTagCompound c) {
		this.a(c);
	}

	@Override
	public void writeRawString(BaseComponent s) {
		this.writeString(ComponentSerializer.toString(s));
	}

	@Override
	public void writeString(String s) {
		this.a(s);
	}

	@Override
	public void writeUUID(UUID i) {
		this.a(i);
	}

	@Override
	public void writeVarInt(int i) {
		while ((i & 0xFFFFFF80) != 0){
			this.writeByte(i & 0x7F | 0x80);
			i >>>= 7;
		}
		this.writeByte(i);
	}
}
