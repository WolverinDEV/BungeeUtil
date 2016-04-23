package dev.wolveringer.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.util.List;

import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.Propeties;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.packet.ByteBuffCreator;
import dev.wolveringer.packet.PacketHandle;
import dev.wolveringer.profiler.Profiler;
import dev.wolveringer.strings.Messages;

public class Decoder extends MinecraftDecoder {
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

	private IInitialHandler connection;
	private Protocol prot;
	private int version;
	private ClientVersion clientVersion = ClientVersion.UnderknownVersion;
	private Direction dir;

	public Decoder(Protocol protocol, boolean server, int protocolVersion, IInitialHandler i, Direction dir) {
		super(protocol, server, protocolVersion);
		this.dir = dir;
		this.connection = i;
		this.setProtocolVersion(protocolVersion);
	}

	public IInitialHandler getHandler() {
		return connection;
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

	public void setHandler(IInitialHandler i) {
		this.connection = i;
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
		Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
		try{
			Packet packet = null;
			try{
				Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.create.packet")); //$NON-NLS-1$
				if(clientVersion == null){
					System.out.println("Client version = null");
				}
				if(connection == null)
					System.out.println("Connection == null");
				packet = Packet.getPacket(clientVersion.getBigVersion() ,getProtocol(), dir, in, connection.getPlayer());
				Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.create.packet")); //$NON-NLS-1$
				if(packet == null){
					Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
				}else{
					Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle"));
					Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
					PacketHandleEvent<? extends Packet> e = new PacketHandleEvent(packet, connection.getPlayer());
					if(Propeties.HANDLE_INTERN_PACKET || !PacketHandle.handlePacket(e)){
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.handle.intern")); //$NON-NLS-1$
						Profiler.decoder_timings.start(Messages.getString("network.timings.decoder.handle.extern")); //$NON-NLS-1$
						PacketLib.handle(e);
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
			}catch (NoClassDefFoundError e){
				System.out.print("Error: 102");
				return;
			}catch (Exception e){
				e.printStackTrace();
				connection.disconnect(e);
				return;
			}
	
			Protocol.DirectionData prot = isServer() ? this.getProtocol().TO_SERVER : this.getProtocol().TO_CLIENT;
			ByteBuf copy = packet == null ? in.copy() : packet.writeToByteBuff(ByteBuffCreator.createByteBuff(),ClientVersion.fromProtocoll(connection.getVersion()));
			try{
				int packetId = DefinedPacket.readVarInt(in);
				DefinedPacket bungeePacket = null;
				if((bungeePacket = prot.createPacket(packetId, getProtocolVersion())) != null){
					bungeePacket.read(in, prot.getDirection(), getProtocolVersion());
					if(in.readableBytes() != 0){
						Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
						throw new BadPacketException("Did not read all bytes from packet " + bungeePacket.getClass() + " " + packetId + " Protocol " + this.getProtocolVersion() + " Direction " + prot+"! Left bytes: "+in.readableBytes());
					}
					//Main.sendMessage("Decode: " + bungeePacket);
				}else{
					in.skipBytes(in.readableBytes());
				}
	
				out.add(new PacketWrapper(bungeePacket, copy));
				copy = null;
			}finally{
				if(copy != null){
					copy.release();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Profiler.decoder_timings.stop(Messages.getString("network.timings.decoder.read")); //$NON-NLS-1$
	}
}
