package dev.wolveringer.bungeeutil.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packetlib.handler.MainPacketHandler;
import dev.wolveringer.bungeeutil.packetlib.reader.ByteBuffCreator;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;
import dev.wolveringer.bungeeutil.translation.Messages;

public class WarpedMinecraftEncoder extends MinecraftEncoder {
	private final static String PACKET_CREATION;
	private final static String HANDLE_GENERAL;
	private final static String HANDLE_INTERN;
	private final static String HANDLE_EXTERN;
	private final static String WRITE_BUFF;
	static {
		BungeeUtil.debug("Loading WarpedMinecraftEncoder timings translations");
		PACKET_CREATION = Messages.getString("network.timings.encoder.create.packet");
		HANDLE_GENERAL = Messages.getString("network.timings.encoder.handle");
		HANDLE_INTERN = Messages.getString("network.timings.encoder.handle.intern");
		HANDLE_EXTERN = Messages.getString("network.timings.encoder.handle.extern");
		WRITE_BUFF = Messages.getString("network.timings.encoder.write.writeNewByteBuff");
	}
	
	@Getter
	@Setter
	private IInitialHandler initHandler;
	@Getter
	private Protocol protocoll = Protocol.HANDSHAKE;
	@Getter
	private int version;
	@Getter
	private ClientVersion clientVersion = ClientVersion.UnderknownVersion;
	@Getter
	private	boolean server;

	public WarpedMinecraftEncoder(Protocol protocol, boolean server, int protocolVersion, IInitialHandler i) {
		super(protocol, server, protocolVersion);
		this.initHandler = i;
		this.server = server;
		this.clientVersion = ClientVersion.fromProtocoll(protocolVersion);
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, DefinedPacket msg, ByteBuf out) throws Exception {
		if(initHandler == null){
			System.err.println("Try to send a DefinedPacket ("+msg.getClass().getName()+") over a WarpedMinecraftEncoder without a valid InitialHandler! Skipping packet handling.");
			super.encode(ctx, msg, out);
			return;
		}
		if(clientVersion == null){
			BungeeUtil.debug("Sending a DefinedPacket ("+msg.getClass().getName()+") to a client with an unknown version. ProtocolVersion is "+version);
			super.encode(ctx, msg, out);
			return;
		}
		
		Profiler.encoder_timings.start(HANDLE_GENERAL);
		if(msg instanceof LoginSuccess)
			initHandler.isConnected = true;
		
		
		ByteBuf encodedBuffer;
		super.encode(ctx, msg, encodedBuffer = Unpooled.buffer());

		Profiler.encoder_timings.start(PACKET_CREATION);
		Packet packet = Packet.getPacket(clientVersion.getProtocollVersion(),protocoll, Direction.TO_CLIENT, encodedBuffer, initHandler.getPlayer());
		Profiler.encoder_timings.stop(PACKET_CREATION);
		if(packet == null){
			ByteBuffCreator.copy(encodedBuffer, out);
			encodedBuffer.release();
			Profiler.encoder_timings.stop(HANDLE_GENERAL);
			return;
		}
		encodedBuffer.release();
		
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PacketHandleEvent<?> e = new PacketHandleEvent(packet, initHandler.getPlayer());
		Profiler.encoder_timings.start(HANDLE_INTERN);
		boolean cancelFromIntern = MainPacketHandler.handlePacket(e);
		Profiler.encoder_timings.stop(HANDLE_INTERN);
		if(!cancelFromIntern){
			Profiler.encoder_timings.start(HANDLE_EXTERN);
			PacketLib.handlePacket(e);
			Profiler.encoder_timings.stop(HANDLE_EXTERN);
			if(!e.isCancelled()){
				Profiler.encoder_timings.start(WRITE_BUFF);
				e.getPacket().writeToByteBuff(out, ClientVersion.fromProtocoll(initHandler.getVersion()));
				Profiler.encoder_timings.stop(WRITE_BUFF);
			}
		}
		Profiler.encoder_timings.stop(HANDLE_GENERAL);
	}
	
	public void setProtocol(Protocol protocol) {
		super.setProtocol(protocol);
		this.protocoll = protocol;
	}

	public void setProtocolVersion(int protocol) {
		super.setProtocolVersion(protocol);
		this.version = protocol;
		this.clientVersion = ClientVersion.fromProtocoll(protocol);
		if(this.clientVersion == null || !this.clientVersion.getProtocollVersion().isSupported())
			this.clientVersion = null; //Dont try to handle packies from a client with an unsupported protocol.
	}
}
