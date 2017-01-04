package dev.wolveringer.bungeeutil.packets;

import org.jsoup.helper.Validate;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PacketPlayInPluginMessage extends Packet implements PacketPlayIn{
	@Getter
	@Setter
	private String channel;
	private ByteBuf data;
	private ByteBufOutputStream os;
	private ByteBufInputStream is;

	public ByteBufInputStream getByteBufInputStream(){
		if(this.is == null) {
			this.is = new ByteBufInputStream(this.data);
		}
		return this.is;
	}

	public ByteBufOutputStream getByteBufOutputStream(){
		if(this.os == null) {
			this.os = new ByteBufOutputStream(this.data);
		}
		return this.os;
	}

	public ByteBuf getCopiedbyteBuff(){
		return this.data.copy();
	}

	public ByteBufInputStream getCopiedByteBufInputStream(){
		return new ByteBufInputStream(this.data.copy());
	}

	@Override
	public void read(PacketDataSerializer s) {
		int readerIndex = s.readerIndex();
		try{
			this.channel = s.readString(-1);
			readerIndex = s.readerIndex();
			if(s.readableBytes() + s.readerIndex() != s.writerIndex()){
				System.out.println("Incorrect length: "+s.readableBytes() + s.readerIndex()+" - "+s.writerIndex());
			}
		}catch(Exception e){
			this.channel = null;
			e.printStackTrace();
			s.readerIndex(readerIndex);
		}
		Validate.isTrue(s.readableBytes() == s.writerIndex() - s.readerIndex(), "bytebuf has drunk: " + s.readableBytes() + " " + (s.writerIndex() - s.readerIndex()));
		this.data = s.readBytes(s.readableBytes());
	}
	public void setData(ByteBuf data) {
		this.data = data;
		this.os = null;
		this.is = null;
	}
	@Override
	public void write(PacketDataSerializer s) {
		if(this.channel != null) {
			s.writeString(this.channel);
		}
		try{
			this.data.readerIndex(0);
			int length = this.data.readableBytes();
			s.ensureWritable(length, true);
			s.writeBytes(this.data, length);
			this.data.release();
		}catch(Exception e){
			throw e;
		}
	}
}
