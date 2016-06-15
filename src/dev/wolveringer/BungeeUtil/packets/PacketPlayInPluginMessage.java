package dev.wolveringer.BungeeUtil.packets;

import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.packet.PacketDataSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketPlayInPluginMessage extends Packet implements PacketPlayIn{
	private String channel;
	private ByteBuf data;
	private ByteBufOutputStream os;
	private ByteBufInputStream is;
	
	@Override
	public void read(PacketDataSerializer s) {
		channel = s.readString(-1);
		data = s.copy(s.readerIndex(), s.readableBytes());
		s.skipBytes(s.readableBytes());
	}

	@Override
	public void write(PacketDataSerializer s) {
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
