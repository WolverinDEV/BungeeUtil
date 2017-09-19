package dev.wolveringer.BungeeUtil.packets;

import io.netty.buffer.ByteBuf;

import javax.naming.OperationNotSupportedException;

import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.DebugProperties;

public abstract class BetaPacket extends Packet{
	
	public BetaPacket() {}
	
	public BetaPacket(byte id) {
		super(id);
		
	}
	public BetaPacket(int i) {
		super(i);
		
	}
	@Override
	protected Packet load(ByteBuf b,ClientVersion version) {
		if(!DebugProperties.PACKET_DEVELOPMENT)
			throw new RuntimeException(new OperationNotSupportedException("Packet is stille under development!"));
		return super.load(b,version);
	}
	@Override
	public ByteBuf getByteBuf(ClientVersion version) {
		if(!DebugProperties.PACKET_DEVELOPMENT)
			throw new RuntimeException(new OperationNotSupportedException("Packet is stille under development!"));
		return super.getByteBuf(version);
	}
}
