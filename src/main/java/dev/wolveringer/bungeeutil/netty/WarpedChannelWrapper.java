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
import io.netty.channel.ChannelFutureListener;
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
import net.md_5.bungee.protocol.packet.Kick;

class EmptyChannelWrapper implements ChannelHandlerContext {
	@Override
	public ByteBufAllocator alloc() {
		throw this.createException();
	}

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> paramAttributeKey) {
		throw this.createException();
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress) {
		throw this.createException();
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public Channel channel() {
		return null;
	}

	@Override
	public ChannelFuture close() {
		throw this.createException();
	}

	@Override
	public ChannelFuture close(ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress) {
		throw this.createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) {
		throw this.createException();
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2,
			ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	private RuntimeException createException() {
		return new UnsupportedOperationException("Methode should be overwritten");
	}

	@Override
	public ChannelFuture deregister() {
		throw this.createException();
	}

	@Override
	public ChannelFuture deregister(ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public ChannelFuture disconnect() {
		throw this.createException();
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public EventExecutor executor() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelActive() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelInactive() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelRead(Object paramObject) {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelReadComplete() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelRegistered() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelUnregistered() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireChannelWritabilityChanged() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireExceptionCaught(Throwable paramThrowable) {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext fireUserEventTriggered(Object paramObject) {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext flush() {
		throw this.createException();
	}

	@Override
	public ChannelHandler handler() {
		throw this.createException();
	}

	public <T> boolean hasAttr(AttributeKey<T> arg0) {
		throw this.createException();
	}

	@Override
	public boolean isRemoved() {
		throw this.createException();
	}

	@Override
	public String name() {
		throw this.createException();
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable paramThrowable) {
		throw this.createException();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		throw this.createException();
	}

	@Override
	public ChannelPromise newPromise() {
		throw this.createException();
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		throw this.createException();
	}

	@Override
	public ChannelPipeline pipeline() {
		throw this.createException();
	}

	@Override
	public ChannelHandlerContext read() {
		throw this.createException();
	}

	@Override
	public ChannelPromise voidPromise() {
		throw this.createException();
	}

	@Override
	public ChannelFuture write(Object paramObject) {
		throw this.createException();
	}

	@Override
	public ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise) {
		throw this.createException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject) {
		throw this.createException();
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise) {
		throw this.createException();
	}
}

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

	@Override
	public void addBefore(String baseName, String name, ChannelHandler handler) {
		try {
			Preconditions.checkState(this.ch.eventLoop().inEventLoop(), "cannot add handler outside of event loop");
			this.ch.pipeline().flush();
			this.ch.pipeline().addBefore(baseName, name, handler);
		} catch (Exception e) {
			this.handler.disconnect(e);
		}
	}

	@Override
	public synchronized void close() {
		if (!this.closed) {
			this.closed = true;
			this.ch.flush();
			this.ch.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void close(Object packet) {
		if (!closed) {
			closed = closing = true;

			if (packet != null && ch.isActive()) {
				ch.writeAndFlush(packet).addListeners(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE, ChannelFutureListener.CLOSE);
				ch.eventLoop().schedule(new Runnable() {
					@Override
					public void run() {
						ch.close();
					}
				}, 250, TimeUnit.MILLISECONDS);
			} else {
				ch.flush();
				ch.close();
			}
		}
	}

	@Override
	public void delayedClose(final Kick kick) {
		if (!closing) {
			closing = true;

			// Minecraft client can take some time to switch protocols.
			// Sending the wrong disconnect packet whilst a protocol switch is
			// in progress will crash it.
			// Delay 250ms to ensure that the protocol switch (if any) has
			// definitely taken place.
			ch.eventLoop().schedule(new Runnable() {
				@Override
				public void run() {
					close(kick);
				}
			}, 250, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public Channel getHandle() {
		return this.ch;
	}

	public Protocol getProtocol() {
		if (this.handler.getEncoder() != null) {
			return NettyUtil.getProtocol(this.handler.getEncoder());
		} else if (this.ch.pipeline().get(WarpedMinecraftDecoder.class) != null) {
			return this.ch.pipeline().get(WarpedMinecraftDecoder.class).getProtocol();
		} else if (this.ch.pipeline().get(WarpedMinecraftEncoder.class) != null) {
			return NettyUtil.getProtocol(this.ch.pipeline().get(WarpedMinecraftEncoder.class));
		} else if (this.ch.pipeline().get(MinecraftDecoder.class) != null) {
			return NettyUtil.getProtocol(this.ch.pipeline().get(MinecraftDecoder.class));
		} else if (this.ch.pipeline().get(MinecraftEncoder.class) != null) {
			return NettyUtil.getProtocol(this.ch.pipeline().get(MinecraftEncoder.class));
		}
		return Protocol.GAME;
	}

	@Override
	public void setCompressionThreshold(int compressionThreshold) {
		if (compressionThreshold != -1) {
			if (this.ch.pipeline().get(PacketCompressor.class) == null) {
				this.addBefore(PipelineUtils.PACKET_ENCODER, "compress", new PacketCompressor());
			}
			if (this.ch.pipeline().get(PacketDecompressor.class) == null) {
				switch (ProxyType.getType()) {
				case BUNGEECORD:
					this.addBefore(PipelineUtils.PACKET_DECODER, "decompress", new PacketDecompressor());
					break;
				case WATERFALL:
					this.addBefore(PipelineUtils.PACKET_DECODER, "decompress",
							new PacketDecompressor(compressionThreshold));
					break;
				default:
					break;
				}
			}
		}

		if (compressionThreshold != -1) {
			this.ch.pipeline().get(PacketCompressor.class).setThreshold(compressionThreshold);
		} else {
			this.ch.pipeline().remove("compress");
			this.ch.pipeline().remove("decompress");
		}
	}

	@Override
	public void setProtocol(Protocol protocol) {
		if (this.ch.pipeline().get(WarpedMinecraftEncoder.class) != null) {
			this.ch.pipeline().get(WarpedMinecraftEncoder.class).setProtocol(protocol);
		} else if (this.ch.pipeline().get(MinecraftEncoder.class) != null) {
			this.ch.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
		}

		if (this.ch.pipeline().get(WarpedMinecraftDecoder.class) != null) {
			this.ch.pipeline().get(WarpedMinecraftDecoder.class).setProtocol(protocol);
		} else if (this.ch.pipeline().get(MinecraftDecoder.class) != null) {
			this.ch.pipeline().get(MinecraftDecoder.class).setProtocol(protocol);
		}
	}

	@Override
	public void setVersion(int protocol) {
		if (this.ch.pipeline().get(WarpedMinecraftEncoder.class) != null) {
			this.ch.pipeline().get(WarpedMinecraftEncoder.class).setProtocolVersion(protocol);
		} else if (this.ch.pipeline().get(MinecraftEncoder.class) != null) {
			this.ch.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
		}

		if (this.ch.pipeline().get(WarpedMinecraftDecoder.class) != null) {
			this.ch.pipeline().get(WarpedMinecraftDecoder.class).setProtocolVersion(protocol);
		} else if (this.ch.pipeline().get(MinecraftDecoder.class) != null) {
			this.ch.pipeline().get(MinecraftDecoder.class).setProtocolVersion(protocol);
		}
	}

	@Override
	public void write(Object packet) {
		if (!this.closed && (this.handler.isConnected || this.getProtocol() != Protocol.GAME)) {
			if (packet instanceof PacketWrapper) {
				((PacketWrapper) packet).setReleased(true);
				this.ch.write(((PacketWrapper) packet).buf, this.ch.voidPromise());
			} else {
				this.ch.write(packet, this.ch.voidPromise());
			}
			this.ch.flush();
		}
	}

}