package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.BungeeUtil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import lombok.NoArgsConstructor;
import org.jsoup.helper.Validate;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

@SuppressWarnings("Duplicates")
@NoArgsConstructor
public class PacketPlayOutPluginMessage extends Packet implements PacketPlayIn{
	private String channel;
	private ByteBuf data;
	private ByteBufOutputStream os;
	private ByteBufInputStream is;
//	private int length = -1;
	
	@Override
	public void read(PacketDataSerializer s) {
		int readerIndex = s.readerIndex();
		try{
			channel = s.readString(-1);
			readerIndex = s.readerIndex();
			if(s.readableBytes() + s.readerIndex() != s.writerIndex()){
				System.out.println("Incorrect length: "+(s.readableBytes() + s.readerIndex()+" - "+s.writerIndex()));
			}
		}catch(Exception e){
			channel = null;
			e.printStackTrace();
			s.readerIndex(readerIndex);
		}
//		length = s.readableBytes();
		data = s.readBytes(s.readableBytes());
//		data = Unpooled.buffer(length);
//		s.readBytes(data, length);
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(channel != null)
			s.writeString(channel);
		try{
			data.readerIndex(0);
			int length = data.readableBytes();
			s.ensureWritable(length, true);
			s.writeBytes(data, length);
			data.release();
		}catch(Exception e){
			System.out.println("out - Buffer: "+data+" - ");
			throw e;
		}
	}
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public ByteBufInputStream getCopiedByteBufInputStream(){
		return new ByteBufInputStream(data.copy());
	}
	public ByteBufInputStream getByteBufInputStream(){
		if(is == null)
			is = new ByteBufInputStream(data);
		return is;
	}
	public ByteBufOutputStream getByteBufOutputStream(){
		if(os == null)
			os = new ByteBufOutputStream(data);
		return os;
	}
	public void setData(ByteBuf data) {
		this.data = data;
		this.os = null;
		this.is = null;
	}
}
