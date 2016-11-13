package dev.wolveringer.BungeeUtil.packetlib.reader;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.nbt.NBTTagCompound;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.util.ByteString;

public abstract class PacketDataSerializer extends ByteBuf {
	public static long clazz = 0;
	@SuppressWarnings("rawtypes")
	private static ArrayList<ObjectSereizer> serelizer = new ArrayList<ObjectSereizer>();
	
	static {
		serelizer.add(new ObjectSereizer<Item>() {
			@Override
			public void write(Item obj, PacketDataSerializer serelizer) {
				serelizer.writeItem(obj);
			}
		});
		serelizer.add(new ObjectSereizer<IChatBaseComponent>() {
			@Override
			public void write(IChatBaseComponent obj, PacketDataSerializer serelizer) {
				serelizer.writeRawString(obj);;
			}
		});
		serelizer.add(new ObjectSereizer<UUID>() {
			@Override
			public void write(UUID obj, PacketDataSerializer serelizer) {
			serelizer.writeUUID(obj);
			}
		});
		serelizer.add(new ObjectSereizer<NBTTagCompound>() {
			@Override
			public void write(NBTTagCompound obj, PacketDataSerializer serelizer) {
				serelizer.writeNBT(obj);
			}
		});
		serelizer.add(new ObjectSereizer<ByteBuf>() {
			@Override
			public void write(ByteBuf obj, PacketDataSerializer serelizer) {
				serelizer.writeBytes(obj.array());
				obj.release();
			}
		});
		serelizer.add(new ObjectSereizer<BlockPosition>() {
			@Override
			public void write(BlockPosition obj, PacketDataSerializer serelizer) {
				serelizer.writeBlockPosition(obj);
			}
		});
		serelizer.add(new ObjectSereizer<ByteString>() {
			@Override
			public void write(ByteString obj, PacketDataSerializer serelizer) {
				serelizer.writeVarInt(obj.getBytes().length);
				serelizer.writeBytes(obj.getBytes(), 0, obj.getBytes().length);
			}
		});
	}
	
	public static void addObjectSerelizer(ObjectSereizer<?> serelizer){
		PacketDataSerializer.serelizer.add(serelizer);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write(Object o) {
		if(o == null)
			return;
		for(ObjectSereizer s : serelizer)
			if(s.getType().isAssignableFrom(o.getClass())){
				s.write(o, this);
				return;
			}
		if(o instanceof Integer)
			writeInt((Integer) o);
		else if(o instanceof String)
			writeString((String) o);
		else if(o instanceof Boolean)
			writeBoolean((Boolean) o);
		else if(o instanceof Double)
			writeDouble((Double) o);
		else if(o instanceof Float)
			writeFloat((Float) o);
		else if(o instanceof Long)
			writeLong((Long) o);
		else if(o.getClass().isArray()){
			int length = Array.getLength(o);
			for(int i = 0;i < length;i++){
				write(Array.get(o, i));
			}
		}else
			System.err.print("Object \"" + o + "\" don t find");
	}
	
	public static PacketDataSerializer create(ByteBuf b, ClientVersion v) {
		switch (v.getBigVersion()) {
			case v1_7:
				return new PacketDataSerializer_v1_7(b);
			case v1_8:
				return new PacketDataSerializer_v1_8(b);
			case v1_9:
				return new PacketDataSerializer_v1_8(b);
			case v1_10:
				return new PacketDataSerializer_v1_8(b);
			default:
				return new PacketDataSerializer_vX_X(b);
		}
	}

	public static PacketDataSerializer create(int id, ClientVersion v) {
		return create(id, v, ByteBuffCreator.createByteBuff());
	}

	public static PacketDataSerializer create(int b, ClientVersion v,ByteBuf buf) {
		switch (v.getBigVersion()) {
			case v1_7:
				return new PacketDataSerializer_v1_7((byte) b,buf);
			case v1_8:
				return new PacketDataSerializer_v1_8((byte) b,buf);
			case v1_9:
				return new PacketDataSerializer_v1_8((byte) b,buf);
			case v1_10:
				return new PacketDataSerializer_v1_8((byte) b,buf);
			default:
				return new PacketDataSerializer_vX_X((byte) b,buf);
		}
	}
	
	public static PacketDataSerializer create(byte id, ClientVersion version, ByteBuf buf) {
		return create((int)id,version,buf);
	}
	
	public static int readVarInt(ByteBuf b) {
		int out = 0;
		int bytes = 0;
		byte in;
		while (true){
			in = b.readByte();
			out |= (in & 0x7F) << (bytes++ * 7);

			if(bytes > 5){
				throw new RuntimeException("VarInt too big");
			}

			if((in & 0x80) != 0x80){
				break;
			}
		}

		return out;
	}

	public static void writeVarInt(int value, ByteBuf output) {
		int part;
		while (true){
			part = value & 0x7F;

			value >>>= 7;
			if(value != 0){
				part |= 0x80;
			}

			output.writeByte(part);

			if(value == 0){
				break;
			}
		}
	}

	public abstract void writeItem(Item i);

	public abstract Item readItem();

	public abstract void writeString(String s);

	public abstract String readString(int max);

	public abstract void writeRawString(IChatBaseComponent s);

	public abstract IChatBaseComponent readRawString();

	public abstract void writeUUID(UUID i);

	public abstract UUID readUUID();

	public abstract void writeNBT(NBTTagCompound c);

	public abstract NBTTagCompound readNBT();

	public abstract void writeBlockPosition(BlockPosition loc);

	public abstract BlockPosition readBlockPosition();

	public ByteString readStringBytes(){
		byte[] data = new byte[this.readVarInt()];
		this.readBytes(data, 0, data.length);
		return new ByteString(data);
	}
	public void writeStringBytes(ByteString string){
		this.writeVarInt(string.getBytes().length);
		this.writeBytes(string.getBytes(), 0, string.getBytes().length);
	}
	
	public void writeVarInt(int i) {
		PacketDataSerializer.writeVarInt(i, this);
	}

	public int readVarInt() {
		return PacketDataSerializer.readVarInt(this);
	}

	protected final ByteBuf base;
	public PacketDataSerializer(ByteBuf a) {
		clazz++;
		this.base = a;
	}

	public int readPositionX(long val) {
		return (int) (val >> 38);
	}

	public int readPositionY(long val) {
		return (int) (val << 26 >> 52);
	}

	public int readPositionZ(long val) {
		return (int) (val << 38 >> 38);
	}

	public int capacity() {
		return this.base.capacity();
	}

	public ByteBuf capacity(int i) {
		return this.base.capacity(i);
	}

	public int maxCapacity() {
		return this.base.maxCapacity();
	}

	public ByteBufAllocator alloc() {
		return this.base.alloc();
	}

	public ByteOrder order() {
		return this.base.order();
	}

	public ByteBuf order(ByteOrder byteorder) {
		return this.base.order(byteorder);
	}

	public ByteBuf unwrap() {
		return this.base.unwrap();
	}

	public boolean isDirect() {
		return this.base.isDirect();
	}

	public int readerIndex() {
		return this.base.readerIndex();
	}

	public ByteBuf readerIndex(int i) {
		return this.base.readerIndex(i);
	}

	public int writerIndex() {
		return this.base.writerIndex();
	}

	public ByteBuf writerIndex(int i) {
		return this.base.writerIndex(i);
	}

	public ByteBuf setIndex(int i, int j) {
		return this.base.setIndex(i, j);
	}

	public int readableBytes() {
		return this.base.readableBytes();
	}

	public int writableBytes() {
		return this.base.writableBytes();
	}

	public int maxWritableBytes() {
		return this.base.maxWritableBytes();
	}

	public boolean isReadable() {
		return this.base.isReadable();
	}

	public boolean isReadable(int i) {
		return this.base.isReadable(i);
	}

	public boolean isWritable() {
		return this.base.isWritable();
	}

	public boolean isWritable(int i) {
		return this.base.isWritable(i);
	}

	public ByteBuf clear() {
		return this.base.clear();
	}

	public ByteBuf markReaderIndex() {
		return this.base.markReaderIndex();
	}

	public ByteBuf resetReaderIndex() {
		return this.base.resetReaderIndex();
	}

	public ByteBuf markWriterIndex() {
		return this.base.markWriterIndex();
	}

	public ByteBuf resetWriterIndex() {
		return this.base.resetWriterIndex();
	}

	public ByteBuf discardReadBytes() {
		return this.base.discardReadBytes();
	}

	public ByteBuf discardSomeReadBytes() {
		return this.base.discardSomeReadBytes();
	}

	public ByteBuf ensureWritable(int i) {
		return this.base.ensureWritable(i);
	}

	public int ensureWritable(int i, boolean flag) {
		return this.base.ensureWritable(i, flag);
	}

	public boolean getBoolean(int i) {
		return this.base.getBoolean(i);
	}

	public byte getByte(int i) {
		return this.base.getByte(i);
	}

	public short getUnsignedByte(int i) {
		return this.base.getUnsignedByte(i);
	}

	public short getShort(int i) {
		return this.base.getShort(i);
	}

	public int getUnsignedShort(int i) {
		return this.base.getUnsignedShort(i);
	}

	public int getMedium(int i) {
		return this.base.getMedium(i);
	}

	public int getUnsignedMedium(int i) {
		return this.base.getUnsignedMedium(i);
	}

	public int getInt(int i) {
		return this.base.getInt(i);
	}

	public long getUnsignedInt(int i) {
		return this.base.getUnsignedInt(i);
	}

	public long getLong(int i) {
		return this.base.getLong(i);
	}

	public char getChar(int i) {
		return this.base.getChar(i);
	}

	public float getFloat(int i) {
		return this.base.getFloat(i);
	}

	public double getDouble(int i) {
		return this.base.getDouble(i);
	}

	public ByteBuf getBytes(int i, ByteBuf bytebuf) {
		return this.base.getBytes(i, bytebuf);
	}

	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
		return this.base.getBytes(i, bytebuf, j);
	}

	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
		return this.base.getBytes(i, bytebuf, j, k);
	}

	public ByteBuf getBytes(int i, byte[] abyte) {
		return this.base.getBytes(i, abyte);
	}

	public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
		return this.base.getBytes(i, abyte, j, k);
	}

	public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
		return this.base.getBytes(i, bytebuffer);
	}

	public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws RuntimeException {
		try{
			return this.base.getBytes(i, outputstream, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return base;
	}

	public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws RuntimeException {
		try{
			return this.base.getBytes(i, gatheringbytechannel, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	public ByteBuf setBoolean(int i, boolean flag) {
		return this.base.setBoolean(i, flag);
	}

	public ByteBuf setByte(int i, int j) {
		return this.base.setByte(i, j);
	}

	public ByteBuf setShort(int i, int j) {
		return this.base.setShort(i, j);
	}

	public ByteBuf setMedium(int i, int j) {
		return this.base.setMedium(i, j);
	}

	public ByteBuf setInt(int i, int j) {
		return this.base.setInt(i, j);
	}

	public ByteBuf setLong(int i, long j) {
		return this.base.setLong(i, j);
	}

	public ByteBuf setChar(int i, int j) {
		return this.base.setChar(i, j);
	}

	public ByteBuf setFloat(int i, float f) {
		return this.base.setFloat(i, f);
	}

	public ByteBuf setDouble(int i, double d0) {
		return this.base.setDouble(i, d0);
	}

	public ByteBuf setBytes(int i, ByteBuf bytebuf) {
		return this.base.setBytes(i, bytebuf);
	}

	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
		return this.base.setBytes(i, bytebuf, j);
	}

	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
		return this.base.setBytes(i, bytebuf, j, k);
	}

	public ByteBuf setBytes(int i, byte[] abyte) {
		return this.base.setBytes(i, abyte);
	}

	public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
		return this.base.setBytes(i, abyte, j, k);
	}

	public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
		return this.base.setBytes(i, bytebuffer);
	}

	public int setBytes(int i, InputStream inputstream, int j) throws RuntimeException {
		try{
			return this.base.setBytes(i, inputstream, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws RuntimeException {
		try{
			return this.base.setBytes(i, scatteringbytechannel, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	public ByteBuf setZero(int i, int j) {
		return this.base.setZero(i, j);
	}

	public boolean readBoolean() {
		return this.base.readBoolean();
	}

	public byte readByte() {
		return this.base.readByte();
	}

	public short readUnsignedByte() {
		return this.base.readUnsignedByte();
	}

	public short readShort() {
		return this.base.readShort();
	}

	public int readUnsignedShort() {
		return this.base.readUnsignedShort();
	}

	public int readMedium() {
		return this.base.readMedium();
	}

	public int readUnsignedMedium() {
		return this.base.readUnsignedMedium();
	}

	public int readInt() {
		return this.base.readInt();
	}

	public long readUnsignedInt() {
		return this.base.readUnsignedInt();
	}

	public long readLong() {
		return this.base.readLong();
	}

	public char readChar() {
		return this.base.readChar();
	}

	public float readFloat() {
		return this.base.readFloat();
	}

	public double readDouble() {
		return this.base.readDouble();
	}

	public ByteBuf readBytes(int i) {
		return this.base.readBytes(i);
	}

	public ByteBuf readSlice(int i) {
		return this.base.readSlice(i);
	}

	public ByteBuf readBytes(ByteBuf bytebuf) {
		return this.base.readBytes(bytebuf);
	}

	public ByteBuf readBytes(ByteBuf bytebuf, int i) {
		return this.base.readBytes(bytebuf, i);
	}

	public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
		return this.base.readBytes(bytebuf, i, j);
	}

	public ByteBuf readBytes(byte[] abyte) {
		return this.base.readBytes(abyte);
	}

	public ByteBuf readBytes(byte[] abyte, int i, int j) {
		return this.base.readBytes(abyte, i, j);
	}

	public ByteBuf readBytes(ByteBuffer bytebuffer) {
		return this.base.readBytes(bytebuffer);
	}

	public ByteBuf readBytes(OutputStream outputstream, int i) throws RuntimeException {
		try{
			return this.base.readBytes(outputstream, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return base;
	}

	public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws RuntimeException {
		try{
			return this.base.readBytes(gatheringbytechannel, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	public ByteBuf skipBytes(int i) {
		return this.base.skipBytes(i);
	}

	public ByteBuf writeBoolean(boolean flag) {
		return this.base.writeBoolean(flag);
	}

	public ByteBuf writeByte(int i) {
		return this.base.writeByte(i);
	}

	public ByteBuf writeShort(int i) {
		return this.base.writeShort(i);
	}

	public ByteBuf writeMedium(int i) {
		return this.base.writeMedium(i);
	}

	public ByteBuf writeInt(int i) {
		return this.base.writeInt(i);
	}

	public ByteBuf writeLong(long i) {
		return this.base.writeLong(i);
	}

	public ByteBuf writeChar(int i) {
		return this.base.writeChar(i);
	}

	public ByteBuf writeFloat(float f) {
		return this.base.writeFloat(f);
	}

	public ByteBuf writeDouble(double d0) {
		return this.base.writeDouble(d0);
	}

	public ByteBuf writeBytes(ByteBuf bytebuf) {
		return this.base.writeBytes(bytebuf);
	}

	public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
		return this.base.writeBytes(bytebuf, i);
	}

	public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
		return this.base.writeBytes(bytebuf, i, j);
	}

	public ByteBuf writeBytes(byte[] abyte) {
		return this.base.writeBytes(abyte);
	}

	public ByteBuf writeBytes(byte[] abyte, int i, int j) {
		return this.base.writeBytes(abyte, i, j);
	}

	public ByteBuf writeBytes(ByteBuffer bytebuffer) {
		return this.base.writeBytes(bytebuffer);
	}

	public int writeBytes(InputStream inputstream, int i) throws RuntimeException {
		try{
			return this.base.writeBytes(inputstream, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws RuntimeException {
		try{
			return this.base.writeBytes(scatteringbytechannel, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	public ByteBuf writeZero(int i) {
		return this.base.writeZero(i);
	}

	public int indexOf(int i, int j, byte b0) {
		return this.base.indexOf(i, j, b0);
	}

	public int bytesBefore(byte b0) {
		return this.base.bytesBefore(b0);
	}

	public int bytesBefore(int i, byte b0) {
		return this.base.bytesBefore(i, b0);
	}

	public int bytesBefore(int i, int j, byte b0) {
		return this.base.bytesBefore(i, j, b0);
	}

	public ByteBuf copy() {
		return this.base.copy();
	}

	public ByteBuf copy(int i, int j) {
		return this.base.copy(i, j);
	}

	public ByteBuf slice() {
		return this.base.slice();
	}

	public ByteBuf slice(int i, int j) {
		return this.base.slice(i, j);
	}

	public ByteBuf duplicate() {
		return this.base.duplicate();
	}

	public int nioBufferCount() {
		return this.base.nioBufferCount();
	}

	public ByteBuffer nioBuffer() {
		return this.base.nioBuffer();
	}

	public ByteBuffer nioBuffer(int i, int j) {
		return this.base.nioBuffer(i, j);
	}

	public ByteBuffer internalNioBuffer(int i, int j) {
		return this.base.internalNioBuffer(i, j);
	}

	public ByteBuffer[] nioBuffers() {
		return this.base.nioBuffers();
	}

	public ByteBuffer[] nioBuffers(int i, int j) {
		return this.base.nioBuffers(i, j);
	}

	public boolean hasArray() {
		return this.base.hasArray();
	}

	public byte[] array() {
		return this.base.array();
	}

	public int arrayOffset() {
		return this.base.arrayOffset();
	}

	public boolean hasMemoryAddress() {
		return this.base.hasMemoryAddress();
	}

	public long memoryAddress() {
		return this.base.memoryAddress();
	}

	public String toString(Charset charset) {
		return this.base.toString(charset);
	}

	public String toString(int i, int j, Charset charset) {
		return this.base.toString(i, j, charset);
	}

	public int hashCode() {
		return this.base.hashCode();
	}

	public boolean equals(Object object) {
		return this.base.equals(object);
	}

	public int compareTo(ByteBuf bytebuf) {
		return this.base.compareTo(bytebuf);
	}

	public String toString() {
		return this.base.toString();
	}

	public ByteBuf retain(int i) {
		return this.base.retain(i);
	}

	public ByteBuf retain() {
		return this.base.retain();
	}

	public int refCnt() {
		return this.base.refCnt();
	}

	public boolean release() {
		return this.base.release();
	}

	public boolean release(int i) {
		return this.base.release(i);
	}

	public byte[] readByteArray() {
		byte[] abyte = new byte[readVarInt()];
		readBytes(abyte);
		return abyte;
	}

	public void writeByteArray(byte[] abyte) {
		writeVarInt(abyte.length);
		writeBytes(abyte);
	}

	
	
	public int forEachByte(ByteBufProcessor arg0) {
		return base.forEachByte(arg0);
	}

	public int forEachByte(int arg0, int arg1, ByteBufProcessor arg2) {
		return base.forEachByte(arg0, arg1, arg2);
	}

	public int forEachByteDesc(ByteBufProcessor arg0) {
		return base.forEachByteDesc(arg0);
	}

	public int forEachByteDesc(int arg0, int arg1, ByteBufProcessor arg2) {
		return base.forEachByteDesc(arg0, arg1, arg2);
	}
	
	@Override
	protected void finalize() throws Throwable {
		clazz--;
		super.finalize();
	}
}
