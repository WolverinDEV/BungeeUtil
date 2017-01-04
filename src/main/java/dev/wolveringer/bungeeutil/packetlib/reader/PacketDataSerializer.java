package dev.wolveringer.bungeeutil.packetlib.reader;

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

import dev.wolveringer.bungeeutil.ByteString;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.position.BlockPosition;
import dev.wolveringer.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import net.md_5.bungee.api.chat.BaseComponent;

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
		serelizer.add(new ObjectSereizer<BaseComponent>() {
			@Override
			public void write(BaseComponent obj, PacketDataSerializer serelizer) {
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

	public static PacketDataSerializer create(byte id, ClientVersion version, ByteBuf buf) {
		return create((int)id,version,buf);
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

	public static int readVarInt(ByteBuf b) {
		int out = 0;
		int bytes = 0;
		byte in;
		while (true){
			in = b.readByte();
			out |= (in & 0x7F) << bytes++ * 7;

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

	protected final ByteBuf base;

	public PacketDataSerializer(ByteBuf a) {
		clazz++;
		this.base = a;
	}

	@Override
	public ByteBufAllocator alloc() {
		return this.base.alloc();
	}

	@Override
	public byte[] array() {
		return this.base.array();
	}

	@Override
	public int arrayOffset() {
		return this.base.arrayOffset();
	}

	@Override
	public int bytesBefore(byte b0) {
		return this.base.bytesBefore(b0);
	}

	@Override
	public int bytesBefore(int i, byte b0) {
		return this.base.bytesBefore(i, b0);
	}

	@Override
	public int bytesBefore(int i, int j, byte b0) {
		return this.base.bytesBefore(i, j, b0);
	}

	@Override
	public int capacity() {
		return this.base.capacity();
	}

	@Override
	public ByteBuf capacity(int i) {
		return this.base.capacity(i);
	}

	@Override
	public ByteBuf clear() {
		return this.base.clear();
	}

	@Override
	public int compareTo(ByteBuf bytebuf) {
		return this.base.compareTo(bytebuf);
	}

	@Override
	public ByteBuf copy() {
		return this.base.copy();
	}

	@Override
	public ByteBuf copy(int i, int j) {
		return this.base.copy(i, j);
	}
	@Override
	public ByteBuf discardReadBytes() {
		return this.base.discardReadBytes();
	}

	@Override
	public ByteBuf discardSomeReadBytes() {
		return this.base.discardSomeReadBytes();
	}

	@Override
	public ByteBuf duplicate() {
		return this.base.duplicate();
	}

	@Override
	public ByteBuf ensureWritable(int i) {
		return this.base.ensureWritable(i);
	}
	@Override
	public int ensureWritable(int i, boolean flag) {
		return this.base.ensureWritable(i, flag);
	}

	@Override
	public boolean equals(Object object) {
		return this.base.equals(object);
	}

	@Override
	protected void finalize() throws Throwable {
		clazz--;
		super.finalize();
	}

	@Override
	public int forEachByte(ByteBufProcessor arg0) {
		return this.base.forEachByte(arg0);
	}

	@Override
	public int forEachByte(int arg0, int arg1, ByteBufProcessor arg2) {
		return this.base.forEachByte(arg0, arg1, arg2);
	}

	@Override
	public int forEachByteDesc(ByteBufProcessor arg0) {
		return this.base.forEachByteDesc(arg0);
	}

	@Override
	public int forEachByteDesc(int arg0, int arg1, ByteBufProcessor arg2) {
		return this.base.forEachByteDesc(arg0, arg1, arg2);
	}

	@Override
	public boolean getBoolean(int i) {
		return this.base.getBoolean(i);
	}

	@Override
	public byte getByte(int i) {
		return this.base.getByte(i);
	}

	@Override
	public ByteBuf getBytes(int i, byte[] abyte) {
		return this.base.getBytes(i, abyte);
	}

	@Override
	public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
		return this.base.getBytes(i, abyte, j, k);
	}

	@Override
	public ByteBuf getBytes(int i, ByteBuf bytebuf) {
		return this.base.getBytes(i, bytebuf);
	}

	@Override
	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
		return this.base.getBytes(i, bytebuf, j);
	}

	@Override
	public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
		return this.base.getBytes(i, bytebuf, j, k);
	}

	@Override
	public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
		return this.base.getBytes(i, bytebuffer);
	}

	@Override
	public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws RuntimeException {
		try{
			return this.base.getBytes(i, gatheringbytechannel, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	@Override
	public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws RuntimeException {
		try{
			return this.base.getBytes(i, outputstream, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return this.base;
	}

	@Override
	public char getChar(int i) {
		return this.base.getChar(i);
	}

	@Override
	public double getDouble(int i) {
		return this.base.getDouble(i);
	}

	@Override
	public float getFloat(int i) {
		return this.base.getFloat(i);
	}

	@Override
	public int getInt(int i) {
		return this.base.getInt(i);
	}

	@Override
	public long getLong(int i) {
		return this.base.getLong(i);
	}

	@Override
	public int getMedium(int i) {
		return this.base.getMedium(i);
	}

	@Override
	public short getShort(int i) {
		return this.base.getShort(i);
	}

	@Override
	public short getUnsignedByte(int i) {
		return this.base.getUnsignedByte(i);
	}

	@Override
	public long getUnsignedInt(int i) {
		return this.base.getUnsignedInt(i);
	}

	@Override
	public int getUnsignedMedium(int i) {
		return this.base.getUnsignedMedium(i);
	}

	@Override
	public int getUnsignedShort(int i) {
		return this.base.getUnsignedShort(i);
	}

	@Override
	public boolean hasArray() {
		return this.base.hasArray();
	}

	@Override
	public int hashCode() {
		return this.base.hashCode();
	}

	@Override
	public boolean hasMemoryAddress() {
		return this.base.hasMemoryAddress();
	}

	@Override
	public int indexOf(int i, int j, byte b0) {
		return this.base.indexOf(i, j, b0);
	}

	@Override
	public ByteBuffer internalNioBuffer(int i, int j) {
		return this.base.internalNioBuffer(i, j);
	}

	@Override
	public boolean isDirect() {
		return this.base.isDirect();
	}

	@Override
	public boolean isReadable() {
		return this.base.isReadable();
	}

	@Override
	public boolean isReadable(int i) {
		return this.base.isReadable(i);
	}

	@Override
	public boolean isWritable() {
		return this.base.isWritable();
	}

	@Override
	public boolean isWritable(int i) {
		return this.base.isWritable(i);
	}

	@Override
	public ByteBuf markReaderIndex() {
		return this.base.markReaderIndex();
	}

	@Override
	public ByteBuf markWriterIndex() {
		return this.base.markWriterIndex();
	}

	@Override
	public int maxCapacity() {
		return this.base.maxCapacity();
	}

	@Override
	public int maxWritableBytes() {
		return this.base.maxWritableBytes();
	}

	@Override
	public long memoryAddress() {
		return this.base.memoryAddress();
	}

	@Override
	public ByteBuffer nioBuffer() {
		return this.base.nioBuffer();
	}

	@Override
	public ByteBuffer nioBuffer(int i, int j) {
		return this.base.nioBuffer(i, j);
	}

	@Override
	public int nioBufferCount() {
		return this.base.nioBufferCount();
	}

	@Override
	public ByteBuffer[] nioBuffers() {
		return this.base.nioBuffers();
	}

	@Override
	public ByteBuffer[] nioBuffers(int i, int j) {
		return this.base.nioBuffers(i, j);
	}

	@Override
	public ByteOrder order() {
		return this.base.order();
	}

	@Override
	public ByteBuf order(ByteOrder byteorder) {
		return this.base.order(byteorder);
	}

	@Override
	public int readableBytes() {
		return this.base.readableBytes();
	}

	public abstract BlockPosition readBlockPosition();

	@Override
	public boolean readBoolean() {
		return this.base.readBoolean();
	}

	@Override
	public byte readByte() {
		return this.base.readByte();
	}

	public byte[] readByteArray() {
		byte[] abyte = new byte[this.readVarInt()];
		this.readBytes(abyte);
		return abyte;
	}

	@Override
	public ByteBuf readBytes(byte[] abyte) {
		return this.base.readBytes(abyte);
	}

	@Override
	public ByteBuf readBytes(byte[] abyte, int i, int j) {
		return this.base.readBytes(abyte, i, j);
	}

	@Override
	public ByteBuf readBytes(ByteBuf bytebuf) {
		return this.base.readBytes(bytebuf);
	}

	@Override
	public ByteBuf readBytes(ByteBuf bytebuf, int i) {
		return this.base.readBytes(bytebuf, i);
	}

	@Override
	public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
		return this.base.readBytes(bytebuf, i, j);
	}

	@Override
	public ByteBuf readBytes(ByteBuffer bytebuffer) {
		return this.base.readBytes(bytebuffer);
	}

	@Override
	public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws RuntimeException {
		try{
			return this.base.readBytes(gatheringbytechannel, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public ByteBuf readBytes(int i) {
		return this.base.readBytes(i);
	}

	@Override
	public ByteBuf readBytes(OutputStream outputstream, int i) throws RuntimeException {
		try{
			return this.base.readBytes(outputstream, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return this.base;
	}

	@Override
	public char readChar() {
		return this.base.readChar();
	}

	@Override
	public double readDouble() {
		return this.base.readDouble();
	}

	@Override
	public int readerIndex() {
		return this.base.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(int i) {
		return this.base.readerIndex(i);
	}

	@Override
	public float readFloat() {
		return this.base.readFloat();
	}

	@Override
	public int readInt() {
		return this.base.readInt();
	}

	public abstract Item readItem();

	@Override
	public long readLong() {
		return this.base.readLong();
	}

	@Override
	public int readMedium() {
		return this.base.readMedium();
	}

	public abstract NBTTagCompound readNBT();

	public int readPositionX(long val) {
		return (int) (val >> 38);
	}

	public int readPositionY(long val) {
		return (int) (val << 26 >> 52);
	}

	public int readPositionZ(long val) {
		return (int) (val << 38 >> 38);
	}

	public abstract BaseComponent readRawString();

	@Override
	public short readShort() {
		return this.base.readShort();
	}

	@Override
	public ByteBuf readSlice(int i) {
		return this.base.readSlice(i);
	}

	public abstract String readString(int max);

	public ByteString readStringBytes(){
		byte[] data = new byte[this.readVarInt()];
		this.readBytes(data, 0, data.length);
		return new ByteString(data);
	}

	@Override
	public short readUnsignedByte() {
		return this.base.readUnsignedByte();
	}

	@Override
	public long readUnsignedInt() {
		return this.base.readUnsignedInt();
	}

	@Override
	public int readUnsignedMedium() {
		return this.base.readUnsignedMedium();
	}

	@Override
	public int readUnsignedShort() {
		return this.base.readUnsignedShort();
	}

	public abstract UUID readUUID();

	public int readVarInt() {
		return PacketDataSerializer.readVarInt(this);
	}

	@Override
	public int refCnt() {
		return this.base.refCnt();
	}

	@Override
	public boolean release() {
		return this.base.release();
	}

	@Override
	public boolean release(int i) {
		return this.base.release(i);
	}

	@Override
	public ByteBuf resetReaderIndex() {
		return this.base.resetReaderIndex();
	}

	@Override
	public ByteBuf resetWriterIndex() {
		return this.base.resetWriterIndex();
	}

	@Override
	public ByteBuf retain() {
		return this.base.retain();
	}

	@Override
	public ByteBuf retain(int i) {
		return this.base.retain(i);
	}

	@Override
	public ByteBuf setBoolean(int i, boolean flag) {
		return this.base.setBoolean(i, flag);
	}

	@Override
	public ByteBuf setByte(int i, int j) {
		return this.base.setByte(i, j);
	}

	@Override
	public ByteBuf setBytes(int i, byte[] abyte) {
		return this.base.setBytes(i, abyte);
	}

	@Override
	public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
		return this.base.setBytes(i, abyte, j, k);
	}

	@Override
	public ByteBuf setBytes(int i, ByteBuf bytebuf) {
		return this.base.setBytes(i, bytebuf);
	}

	@Override
	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
		return this.base.setBytes(i, bytebuf, j);
	}

	@Override
	public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
		return this.base.setBytes(i, bytebuf, j, k);
	}

	@Override
	public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
		return this.base.setBytes(i, bytebuffer);
	}

	@Override
	public int setBytes(int i, InputStream inputstream, int j) throws RuntimeException {
		try{
			return this.base.setBytes(i, inputstream, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	@Override
	public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws RuntimeException {
		try{
			return this.base.setBytes(i, scatteringbytechannel, j);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return j;
	}

	@Override
	public ByteBuf setChar(int i, int j) {
		return this.base.setChar(i, j);
	}

	@Override
	public ByteBuf setDouble(int i, double d0) {
		return this.base.setDouble(i, d0);
	}

	@Override
	public ByteBuf setFloat(int i, float f) {
		return this.base.setFloat(i, f);
	}

	@Override
	public ByteBuf setIndex(int i, int j) {
		return this.base.setIndex(i, j);
	}

	@Override
	public ByteBuf setInt(int i, int j) {
		return this.base.setInt(i, j);
	}

	@Override
	public ByteBuf setLong(int i, long j) {
		return this.base.setLong(i, j);
	}

	@Override
	public ByteBuf setMedium(int i, int j) {
		return this.base.setMedium(i, j);
	}

	@Override
	public ByteBuf setShort(int i, int j) {
		return this.base.setShort(i, j);
	}

	@Override
	public ByteBuf setZero(int i, int j) {
		return this.base.setZero(i, j);
	}

	@Override
	public ByteBuf skipBytes(int i) {
		return this.base.skipBytes(i);
	}

	@Override
	public ByteBuf slice() {
		return this.base.slice();
	}

	@Override
	public ByteBuf slice(int i, int j) {
		return this.base.slice(i, j);
	}

	@Override
	public String toString() {
		return this.base.toString();
	}

	@Override
	public String toString(Charset charset) {
		return this.base.toString(charset);
	}

	@Override
	public String toString(int i, int j, Charset charset) {
		return this.base.toString(i, j, charset);
	}

	@Override
	public ByteBuf unwrap() {
		return this.base.unwrap();
	}

	@Override
	public int writableBytes() {
		return this.base.writableBytes();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write(Object o) {
		if(o == null) {
			return;
		}
		for(ObjectSereizer s : serelizer) {
			if(s.getType().isAssignableFrom(o.getClass())){
				s.write(o, this);
				return;
			}
		}
		if(o instanceof Integer) {
			this.writeInt((Integer) o);
		} else if(o instanceof String) {
			this.writeString((String) o);
		} else if(o instanceof Boolean) {
			this.writeBoolean((Boolean) o);
		} else if(o instanceof Double) {
			this.writeDouble((Double) o);
		} else if(o instanceof Float) {
			this.writeFloat((Float) o);
		} else if(o instanceof Long) {
			this.writeLong((Long) o);
		} else if(o.getClass().isArray()){
			int length = Array.getLength(o);
			for(int i = 0;i < length;i++){
				this.write(Array.get(o, i));
			}
		} else {
			System.err.print("Object \"" + o + "\" don t find");
		}
	}

	public abstract void writeBlockPosition(BlockPosition loc);

	@Override
	public ByteBuf writeBoolean(boolean flag) {
		return this.base.writeBoolean(flag);
	}

	@Override
	public ByteBuf writeByte(int i) {
		return this.base.writeByte(i);
	}

	public void writeByteArray(byte[] abyte) {
		this.writeVarInt(abyte.length);
		this.writeBytes(abyte);
	}

	@Override
	public ByteBuf writeBytes(byte[] abyte) {
		return this.base.writeBytes(abyte);
	}

	@Override
	public ByteBuf writeBytes(byte[] abyte, int i, int j) {
		return this.base.writeBytes(abyte, i, j);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf bytebuf) {
		return this.base.writeBytes(bytebuf);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
		return this.base.writeBytes(bytebuf, i);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
		return this.base.writeBytes(bytebuf, i, j);
	}

	@Override
	public ByteBuf writeBytes(ByteBuffer bytebuffer) {
		return this.base.writeBytes(bytebuffer);
	}

	@Override
	public int writeBytes(InputStream inputstream, int i) throws RuntimeException {
		try{
			return this.base.writeBytes(inputstream, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws RuntimeException {
		try{
			return this.base.writeBytes(scatteringbytechannel, i);
		}catch (java.io.IOException e){
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public ByteBuf writeChar(int i) {
		return this.base.writeChar(i);
	}

	@Override
	public ByteBuf writeDouble(double d0) {
		return this.base.writeDouble(d0);
	}

	@Override
	public ByteBuf writeFloat(float f) {
		return this.base.writeFloat(f);
	}

	@Override
	public ByteBuf writeInt(int i) {
		return this.base.writeInt(i);
	}

	public abstract void writeItem(Item i);

	@Override
	public ByteBuf writeLong(long i) {
		return this.base.writeLong(i);
	}

	@Override
	public ByteBuf writeMedium(int i) {
		return this.base.writeMedium(i);
	}

	public abstract void writeNBT(NBTTagCompound c);

	public abstract void writeRawString(BaseComponent s);

	@Override
	public int writerIndex() {
		return this.base.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(int i) {
		return this.base.writerIndex(i);
	}

	@Override
	public ByteBuf writeShort(int i) {
		return this.base.writeShort(i);
	}



	public abstract void writeString(String s);

	public void writeStringBytes(ByteString string){
		this.writeVarInt(string.getBytes().length);
		this.writeBytes(string.getBytes(), 0, string.getBytes().length);
	}

	public abstract void writeUUID(UUID i);

	public void writeVarInt(int i) {
		PacketDataSerializer.writeVarInt(i, this);
	}

	@Override
	public ByteBuf writeZero(int i) {
		return this.base.writeZero(i);
	}
}
