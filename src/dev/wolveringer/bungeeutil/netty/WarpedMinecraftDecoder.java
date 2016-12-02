package dev.wolveringer.bungeeutil.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
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
	private static final Field field_protocol = getField(MinecraftDecoder.class, "protocol");
	private static final Field field_protocolVersion = getField(MinecraftDecoder.class, "protocolVersion");
	private static final Field field_server = getField(MinecraftDecoder.class, "server");

	private static Field getField(Class<?> s, String field) {
		try{
			for(Field f : s.getDeclaredFields())
				if(f.getName().equals(field)){
					f.setAccessible(true);
					return f;
				}
			for(Field f : s.getFields())
				if(f.getName().equals(field)){
					f.setAccessible(true);
					return f;
				}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Getter
	@Setter
	private IInitialHandler initHandler;
	private Protocol prot;
	private int version = -1;
	private ClientVersion clientVersion = ClientVersion.UnderknownVersion;
	private Direction direction;

	public WarpedMinecraftDecoder(Protocol protocol, boolean server, int protocolVersion, IInitialHandler i, Direction dir) {
		super(protocol, server, protocolVersion);
		this.direction = dir;
		this.initHandler = i;
		this.setProtocolVersion(protocolVersion);
	}

	public IInitialHandler getHandler() {
		return initHandler;
	}

	public Protocol getProtocol() {
		try{
			return (Protocol) field_protocol.get(this);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public int getProtocolVersion() {
		try{
			return (int) field_protocolVersion.get(this);
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public boolean isServer() {
		try{
			return (boolean) field_server.get(this);
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void setProtocol(Protocol protocol) {
		super.setProtocol(protocol);
		this.prot = protocol;
	}

	@Override
	public void setProtocolVersion(int protocolVersion) {
		super.setProtocolVersion(protocolVersion);
		this.version = protocolVersion;
		this.clientVersion = ClientVersion.fromProtocoll(protocolVersion);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(initHandler == null){
			super.decode(ctx, in, out);
			System.out.println("Missing inithandler in WarpedMinecraftDecoder instance ("+super.toString()+"; initHandler="+initHandler+")");
			return;
		}
		Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
		try{
			Packet packet = null;
			try{
				Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.create.packet")); //$NON-NLS-1$
				if(clientVersion == null){
					System.out.println("Could not find the ClientVersion for the protocollversion "+version);
					BungeeUtil.debug("Could not resolve ClientVersion for "+version+". Disconnecting client");
					initHandler.disconnect("§cYour client versions isnt supported!");
					return;
				}
				packet = Packet.getPacket(clientVersion.getProtocollVersion() ,getProtocol(), direction, in, initHandler.getPlayer());
				Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.create.packet")); //$NON-NLS-1$
				if(packet == null){
					Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
				}else{
					Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle"));
					Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
					PacketHandleEvent<? extends Packet> e = new PacketHandleEvent(packet, initHandler.getPlayer());
					if(!MainPacketHandler.handlePacket(e)){
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
						Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle.extern")); //$NON-NLS-1$
						PacketLib.handlePacket(e);
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.handle.extern")); //$NON-NLS-1$
						if(!e.isCancelled()){
							packet = e.getPacket();
						}else{
							Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
							Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
							return;
						}
					}else{
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
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
			ByteBuf copy = packet == null ? in.copy() : packet.writeToByteBuff(ByteBuffCreator.createByteBuff(),ClientVersion.fromProtocoll(initHandler.getVersion()));
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
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
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
		Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
	}
}
