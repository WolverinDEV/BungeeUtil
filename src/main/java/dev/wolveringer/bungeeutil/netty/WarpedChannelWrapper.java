package dev.wolveringer.bungeeutil.netty;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import dev.wolveringer.bungeeutil.system.ProxyType;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import lombok.Getter;
import net.md_5.bungee.compress.PacketCompressor;
import net.md_5.bungee.compress.PacketDecompressor;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;

public class WarpedChannelWrapper extends ChannelWrapper {

	private Channel ch;
	private IInitialHandler handler;
	@Getter
	private volatile boolean closed;
	@Getter
	private volatile boolean closing;

	public WarpedChannelWrapper(net.md_5.bungee.netty.ChannelWrapper ctx, IInitialHandler h) {
		super(new EmptyChannelWrapper());
		this.handler = h;
		try {
			Field f = net.md_5.bungee.netty.ChannelWrapper.class.getDeclaredField("ch");
			f.setAccessible(true);
			this.ch = (Channel) f.get(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setProtocol(Protocol protocol) {
		if (ch.pipeline().get(WarpedMinecraftEncoder.class) != null)
			ch.pipeline().get(WarpedMinecraftEncoder.class).setProtocol(protocol);
		else if (ch.pipeline().get(MinecraftEncoder.class) != null)
			ch.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
		
		if (ch.pipeline().get(WarpedMinecraftDecoder.class) != null)
			ch.pipeline().get(WarpedMinecraftDecoder.class).setProtocol(protocol);
		else if (ch.pipeline().get(MinecraftDecoder.class) != null)
			ch.pipeline().get(MinecraftDecoder.class).setProtocol(protocol);
	}

	public void setVersion(int protocol) {
		if (ch.pipeline().get(WarpedMinecraftEncoder.class) != null)
			ch.pipeline().get(WarpedMinecraftEncoder.class).setProtocolVersion(protocol);
		else if (ch.pipeline().get(MinecraftEncoder.class) != null)
			ch.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
		
		if (ch.pipeline().get(WarpedMinecraftDecoder.class) != null)
			ch.pipeline().get(WarpedMinecraftDecoder.class).setProtocolVersion(protocol);
		else if (ch.pipeline().get(MinecraftDecoder.class) != null)
			ch.pipeline().get(MinecraftDecoder.class).setProtocolVersion(protocol);
	}

	public void write(Object packet) {
		if (!closed && (handler.isConnected || (getProtocol() != Protocol.GAME))) {
			if (packet instanceof PacketWrapper) {
				((PacketWrapper) packet).setReleased(true);
				ch.write(((PacketWrapper) packet).buf, ch.voidPromise());
			} else {
				ch.write(packet, ch.voidPromise());
			}
			ch.flush();
		}
	}

	public Protocol getProtocol() {
		if (handler.getEncoder() != null) {
			return NettyUtil.getProtocol(handler.getEncoder());
		} else if (ch.pipeline().get(WarpedMinecraftDecoder.class) != null) {
			return ch.pipeline().get(WarpedMinecraftDecoder.class).getProtocol();
		} else if (ch.pipeline().get(WarpedMinecraftEncoder.class) != null) {
			return NettyUtil.getProtocol(ch.pipeline().get(WarpedMinecraftEncoder.class));
		} else if (ch.pipeline().get(MinecraftDecoder.class) != null) {
			return NettyUtil.getProtocol(ch.pipeline().get(MinecraftDecoder.class));
		} else if (ch.pipeline().get(MinecraftEncoder.class) != null) {
			return NettyUtil.getProtocol(ch.pipeline().get(MinecraftEncoder.class));
		}
		return Protocol.GAME;
	}

	@Override
	public synchronized void delayedClose(final Runnable runnable) {
		Preconditions.checkArgument(runnable != null, "runnable");

		if (!closing) {
			closing = true;
			if(ch == null){
				System.err.println("One Channel of "+handler.getName()+" is null.");
				return;
			}
			if(ch.eventLoop() == null){
				System.err.println("One ChannelEventLoop of "+handler.getName()+" is null. ChanneL "+ch);
				return;
			}
			// Minecraft client can take some time to switch protocols.
			// Sending the wrong disconnect packet whilst a protocol switch is in progress will crash it.
			// Delay 500ms to ensure that the protocol switch (if any) has definitely taken place.
			ch.eventLoop().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						runnable.run();
					} finally {
						WarpedChannelWrapper.this.close();
					}
				}
			}, 500, TimeUnit.MILLISECONDS);
		}
	}

	public synchronized void close() {
		if (!closed) {
			closed = true;
			ch.flush();
			ch.close();
		}
	}

	public void addBefore(String baseName, String name, ChannelHandler handler) {
		try {
			Preconditions.checkState(ch.eventLoop().inEventLoop(), "cannot add handler outside of event loop");
			ch.pipeline().flush();
			ch.pipeline().addBefore(baseName, name, handler);
		} catch (Exception e) {
			this.handler.disconnect(e);
		}
	}

	public Channel getHandle() {
		return ch;
	}

	public void setCompressionThreshold(int compressionThreshold) {
		if(compressionThreshold != -1){
			if (ch.pipeline().get(PacketCompressor.class) == null) 
				addBefore(PipelineUtils.PACKET_ENCODER, "compress", new PacketCompressor());
			if (ch.pipeline().get(PacketDecompressor.class) == null) 
				switch (ProxyType.getType()) {
				case BUNGEECORD:
					addBefore(PipelineUtils.PACKET_DECODER, "decompress", new PacketDecompressor());
					break;
				case WATERFALL:
					addBefore(PipelineUtils.PACKET_DECODER, "decompress", new PacketDecompressor(compressionThreshold));
					break;
				default:
					break;
				}
		}

		if (compressionThreshold != -1) {
			ch.pipeline().get(PacketCompressor.class).setThreshold(compressionThreshold);
		} else {
			ch.pipeline().remove("compress");
			ch.pipeline().remove("decompress");
		}
	}

}

class EmptyChannelWrapper implements ChannelHandlerContext {
	private RuntimeException createException(){
		return new UnsupportedOperationException("Methode should be overwritten");
	}
	
	@Override
	public <T> Attribute<T> attr(AttributeKey<T> paramAttributeKey) {
		throw createException();
	}

	@Override
	public Channel channel() {
		return null;
	}

	@Override
	public EventExecutor executor() {
		throw createException();
	}

	@Override
	public String name() {
		throw createException();
	}

	@Override
	public ChannelHandler handler() {
		throw createException();
	}

	@Override
	public boolean isRemoved() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelRegistered() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelUnregistered() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelActive() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelInactive() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireExceptionCaught(Throwable paramThrowable) {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireUserEventTriggered(Object paramObject) {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelRead(Object paramObject) {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelReadComplete() {
		throw createException();
	}

	@Override
	public ChannelHandlerContext fireChannelWritabilityChanged() {
		throw createException();
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress) {
		throw createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress) {
		throw createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) {
		throw createException();
	}

	@Override
	public ChannelFuture disconnect() {
		throw createException();
	}

	@Override
	public ChannelFuture close() {
		throw createException();
	}

	@Override
	public ChannelFuture deregister() {
		throw createException();
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture close(ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture deregister(ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelHandlerContext read() {
		throw createException();
	}

	@Override
	public ChannelFuture write(Object paramObject) {
		throw createException();
	}

	@Override
	public ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelHandlerContext flush() {
		throw createException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise) {
		throw createException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject) {
		throw createException();
	}

	@Override
	public ChannelPipeline pipeline() {
		throw createException();
	}

	@Override
	public ByteBufAllocator alloc() {
		throw createException();
	}

	@Override
	public ChannelPromise newPromise() {
		throw createException();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		throw createException();
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		throw createException();
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable paramThrowable) {
		throw createException();
	}

	@Override
	public ChannelPromise voidPromise() {
		throw createException();
	}

	public <T> boolean hasAttr(AttributeKey<T> arg0) {
		throw createException();
	}
}