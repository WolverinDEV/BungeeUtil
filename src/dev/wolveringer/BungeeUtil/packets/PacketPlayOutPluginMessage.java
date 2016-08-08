package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayOutPluginMessage extends Packet implements PacketPlayIn{
	private String channel;
	private ByteBuf data;
	private ByteBufOutputStream os;
	private ByteBufInputStream is;
	
	@Override
	public void read(PacketDataSerializer s) {
		s.markReaderIndex();
		try{
			channel = s.readString(-1);
			if(s.readableBytes() + s.readerIndex() != s.writerIndex()){
				System.out.println("Incorrect length: "+(s.readableBytes() + s.readerIndex()+" - "+s.writerIndex()));
			}
		}catch(Exception e){
			channel = null;
			e.printStackTrace();
			s.resetReaderIndex();
		}
		int length = Math.min(s.readableBytes(), s.readableBytes() + s.readerIndex());
		data = s.copy(s.readerIndex(), length);
		s.skipBytes(s.readableBytes());
	}

	@Override
	public void write(PacketDataSerializer s) {
		if(channel != null)
			s.writeString(channel);
		data.resetReaderIndex();
		data.readBytes(s,data.readableBytes());
		data.release();
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
