package dev.wolveringer.bungeeutil.packets;

import dev.wolveringer.bungeeutil.packetlib.reader.PacketDataSerializer;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.NoArgsConstructor;
import org.jsoup.helper.Validate;

@NoArgsConstructor
public class PacketPlayInPluginMessage extends Packet implements PacketPlayIn{
	private String channel;
	private ByteBuf data;
	private ByteBufOutputStream os;
	private ByteBufInputStream is;

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
		Validate.isTrue(s.readableBytes() == s.writerIndex() - s.readerIndex(), "bytebuf has drunk: " + s.readableBytes() + " " + (s.writerIndex() - s.readerIndex()));
		data = s.readBytes(s.readableBytes());
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
	
	public ByteBuf getCopiedbyteBuff(){
		return data.copy();
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
