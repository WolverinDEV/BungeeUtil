package dev.wolveringer.bungeeutil.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packetlib.handler.MainPacketHandler;
import dev.wolveringer.bungeeutil.packetlib.reader.ByteBuffCreator;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import dev.wolveringer.bungeeutil.statistics.profiler.Profiler;
import dev.wolveringer.bungeeutil.system.ProxyType;
import dev.wolveringer.bungeeutil.translation.Messages;

public class WarpedMinecraftDecoder extends MinecraftDecoder {
	private final static String DECODING;
	private final static String BUNGEE_WRITE;
	private final static String PACKET_CREATION;
	private final static String HANDLE_GENERAL;
	private final static String HANDLE_INTERN;
	private final static String HANDLE_EXTERN;
	private final static String WRITE_BUFF;
	
	static {
		BungeeUtil.debug("Loading WarpedMinecraftDecoder timings translations");
		DECODING = Messages.getString("network.timings.decoder.decoding");
		BUNGEE_WRITE = Messages.getString("network.timings.decoder.create.bungeepacket");
		PACKET_CREATION = Messages.getString("network.timings.decoder.create.packet");
		HANDLE_GENERAL = Messages.getString("network.timings.decoder.handle");
		HANDLE_INTERN = Messages.getString("network.timings.decoder.handle.intern");
		HANDLE_EXTERN = Messages.getString("network.timings.decoder.handle.extern");
		WRITE_BUFF = Messages.getString("network.timings.decoder.write.writeNewByteBuff");
	}
	
	@Getter
	@Setter
	private IInitialHandler initHandler;
	private Protocol protocol;
	private int version = -1;
	private boolean server;
	private ClientVersion clientVersion = ClientVersion.UnderknownVersion;
	private Direction direction;
	
	public WarpedMinecraftDecoder(Protocol protocol, boolean server, int protocolVersion, IInitialHandler i, Direction dir) {
		super(protocol, server, protocolVersion);
		this.protocol = protocol;
		this.direction = dir;
		this.initHandler = i;
		this.server = server;
		this.setProtocolVersion(protocolVersion);
	}

	public IInitialHandler getHandler() {
		return initHandler;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public int getProtocolVersion() {
		return version;
	}

	public boolean isServer() {
		return server;
	}

	@Override
	public void setProtocol(Protocol protocol) {
		super.setProtocol(protocol);
		this.protocol = protocol;
	}

	@Override
	public void setProtocolVersion(int protocolVersion) {
		super.setProtocolVersion(protocolVersion);
		this.version = protocolVersion;
		this.clientVersion = ClientVersion.fromProtocoll(protocolVersion);
		if(clientVersion != null && !this.clientVersion.getProtocollVersion().isSupported())
			this.clientVersion = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(initHandler == null){
			System.out.println("Missing inithandler in WarpedMinecraftDecoder instance ("+super.toString()+"; initHandler="+initHandler+")");
			super.decode(ctx, in, out);
			return;
		}
		if(clientVersion == null){ //In the theorie impossible :)
			System.err.println("Could not find the ClientVersion for the ProtocolVersion "+version+". Disconnecting the client.");
			initHandler.disconnect("§cYour client version isnt supported!");
			return;
		}
		
		Profiler.decoder_timings.start(DECODING);
		try{
			Packet packet = null;
			try{
				Profiler.decoder_timings.start(PACKET_CREATION);
				packet = Packet.getPacket(clientVersion.getProtocollVersion(), getProtocol(), direction, in, initHandler.getPlayer());
				Profiler.decoder_timings.stop(PACKET_CREATION);
				if(packet == null){
					Profiler.decoder_timings.stop(PACKET_CREATION);
				}else{
					Profiler.decoder_timings.start(HANDLE_GENERAL);
					Profiler.decoder_timings.start(HANDLE_INTERN);
					PacketHandleEvent<? extends Packet> e = new PacketHandleEvent(packet, initHandler.getPlayer());
					if(!MainPacketHandler.handlePacket(e)){
						Profiler.decoder_timings.stop(HANDLE_INTERN);
						Profiler.decoder_timings.start(HANDLE_EXTERN);
						PacketLib.handlePacket(e);
						Profiler.decoder_timings.stop(HANDLE_EXTERN);
						if(!e.isCancelled()){
							packet = e.getPacket();
						}else{
							Profiler.decoder_timings.stop(HANDLE_INTERN);
							Profiler.decoder_timings.stop(HANDLE_GENERAL);
							return;
						}
					}else{
						Profiler.decoder_timings.stop(HANDLE_INTERN);
						Profiler.decoder_timings.stop(HANDLE_GENERAL);
						return;
					}
				}
			}catch (Exception e){
				if(e instanceof ClassNotFoundException){
					e.printStackTrace();
					System.err.println("Could not find class '"+e.getMessage()+"' in decode methode.");
					if(e.getMessage().startsWith("dev.wolveringer.BungeeUtil") || e.getMessage().startsWith("dev.wolveringer.packet") || e.getMessage().startsWith("dev.wolveringer.network")){
						System.err.println("");
						System.err.println("");
						System.err.println("-----------------------------------------------------------------------------------------------");
						System.err.println("                      Missing inital class! Shuting down BungeeUtils!");
						if(ChannelInizializer.getChannelInitializer() instanceof BungeeUtilChannelInizializer){
							BungeeUtilChannelInizializer channelInit = (BungeeUtilChannelInizializer) ChannelInizializer.getChannelInitializer();
							channelInit.throwClassNotFoundError((ClassNotFoundException) e);
						}
						System.err.println("-----------------------------------------------------------------------------------------------");
					}
				}
				BungeeUtil.debug(e);
				if(!initHandler.isConnected)
					return;
				switch (Configuration.getHandleExceptionAction()) {
				case DISCONNECT:
					initHandler.disconnect(e);
				case PRINT:
					e.printStackTrace();
				default:
					break;
				}
				return;
			}
	
			Protocol.DirectionData prot = isServer() ? this.getProtocol().TO_SERVER : this.getProtocol().TO_CLIENT;
			ByteBuf copy = packet == null ? in.copy() : packet.writeToByteBuff(ByteBuffCreator.createByteBuff(),clientVersion);
			try{
				int packetId = DefinedPacket.readVarInt(in);
				DefinedPacket bungeePacket = null;
				switch (ProxyType.getType()) {
				case BUNGEECORD:
					bungeePacket = prot.createPacket(packetId, getProtocolVersion());
					break;
				case WATERFALL:
					bungeePacket = prot.createPacket(packetId, getProtocolVersion(), false); //Dont support forge
					break;
				default:
					break;
				}
				if(bungeePacket != null){
					bungeePacket.read(in, prot.getDirection(), getProtocolVersion());
					if(in.readableBytes() != 0){
						Profiler.decoder_timings.stop(DECODING);
						throw new BadPacketException("Did not read all bytes from packet " + bungeePacket.getClass() + " " + packetId + " Protocol " + this.getProtocolVersion() + " Direction " + prot+"! Left bytes: "+in.readableBytes());
					}
				}else{
					in.skipBytes(in.readableBytes());
				}
	
				out.add(new PacketWrapper(bungeePacket, copy));
				copy = null;
			}finally{
				if(copy != null){
					copy.release();
				}
				copy = null;
			}
			packet = null;
		}catch(Exception e){
			if(initHandler.isConnected)
				e.printStackTrace();
		}
		Profiler.decoder_timings.stop(DECODING);
	}
}
