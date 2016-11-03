package dev.wolveringer.network.channel;

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

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.compress.PacketCompressor;
import net.md_5.bungee.compress.PacketDecompressor;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Protocol;

import com.google.common.base.Preconditions;

import dev.wolveringer.Reflect.Until;
import dev.wolveringer.network.Decoder;
import dev.wolveringer.network.Encoder;
import dev.wolveringer.network.IInitialHandler;

public class ChannelWrapper extends net.md_5.bungee.netty.ChannelWrapper {

	private Channel ch;
	private IInitialHandler handler;
	@Getter
	private volatile boolean closed;
	@Getter
	private volatile boolean closing;

	public ChannelWrapper(net.md_5.bungee.netty.ChannelWrapper ctx, IInitialHandler h) {
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
		if (ch.pipeline().get(MinecraftEncoder.class) != null)
			ch.pipeline().get(MinecraftEncoder.class).setProtocol(protocol);
		if (ch.pipeline().get(MinecraftDecoder.class) != null)
			ch.pipeline().get(MinecraftDecoder.class).setProtocol(protocol);
		if (ch.pipeline().get(Encoder.class) != null)
			ch.pipeline().get(Encoder.class).setProtocol(protocol);
		if (ch.pipeline().get(Decoder.class) != null)
			ch.pipeline().get(Decoder.class).setProtocol(protocol);
	}

	public void setVersion(int protocol) {
		if (ch.pipeline().get(MinecraftEncoder.class) != null)
			ch.pipeline().get(MinecraftEncoder.class).setProtocolVersion(protocol);
		if (ch.pipeline().get(MinecraftDecoder.class) != null)
			ch.pipeline().get(MinecraftDecoder.class).setProtocolVersion(protocol);
		if (ch.pipeline().get(Encoder.class) != null)
			ch.pipeline().get(Encoder.class).setProtocolVersion(protocol);
		if (ch.pipeline().get(Decoder.class) != null)
			ch.pipeline().get(Decoder.class).setProtocolVersion(protocol);
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
			return Until.getProtocol(handler.getEncoder());
		} else if (ch.pipeline().get(Decoder.class) != null) {
			System.out.print(ch.pipeline().get(Decoder.class).getProtocol());
			return ch.pipeline().get(Decoder.class).getProtocol();
		} else if (ch.pipeline().get(Encoder.class) != null) {
			return Until.getProtocol(ch.pipeline().get(Encoder.class));
		} else if (ch.pipeline().get(MinecraftDecoder.class) != null) {
			return Until.getProtocol(ch.pipeline().get(MinecraftDecoder.class));
		} else if (ch.pipeline().get(MinecraftEncoder.class) != null) {
			return Until.getProtocol(ch.pipeline().get(MinecraftEncoder.class));
		}
		return Protocol.GAME;
	}

	@Override
	public void delayedClose(final Runnable runnable) {
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
						ChannelWrapper.this.close();
					}
				}
			}, 500, TimeUnit.MILLISECONDS);
		}
	}

	public void close() {
		if (!closed) {
			closed = true;
			ch.flush();
			ch.close();
		}
	}

	public void addBefore(String baseName, String name, ChannelHandler handler) {
		try {
			if (!ch.eventLoop().inEventLoop())
				this.handler.disconnect("Error");
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
		if (ch.pipeline().get(PacketCompressor.class) == null && compressionThreshold != -1) {
			addBefore(PipelineUtils.PACKET_ENCODER, "compress", new PacketCompressor());
		}

		if (ch.pipeline().get(PacketDecompressor.class) == null && compressionThreshold != -1) {
			addBefore(PipelineUtils.PACKET_DECODER, "decompress", new PacketDecompressor());
		}
		if (compressionThreshold != -1) {
			ch.pipeline().get(PacketCompressor.class).setThreshold(compressionThreshold);
		} else {
			ch.pipeline().remove("compress");
		}
		if (compressionThreshold == -1) {
			ch.pipeline().remove("decompress");
		}
	}

}

class EmptyChannelWrapper implements ChannelHandlerContext {
	@Override
	public <T> Attribute<T> attr(AttributeKey<T> paramAttributeKey) {
		return null;
	}

	@Override
	public Channel channel() {
		return null;
	}

	@Override
	public EventExecutor executor() {
		return null;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public ChannelHandler handler() {
		return null;
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	@Override
	public ChannelHandlerContext fireChannelRegistered() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelUnregistered() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelActive() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelInactive() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireExceptionCaught(Throwable paramThrowable) {
		return null;
	}

	@Override
	public ChannelHandlerContext fireUserEventTriggered(Object paramObject) {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelRead(Object paramObject) {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelReadComplete() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelWritabilityChanged() {
		return null;
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress) {
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress) {
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2) {
		return null;
	}

	@Override
	public ChannelFuture disconnect() {
		return null;
	}

	@Override
	public ChannelFuture close() {
		return null;
	}

	@Override
	public ChannelFuture deregister() {
		return null;
	}

	@Override
	public ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture close(ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture deregister(ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelHandlerContext read() {
		return null;
	}

	@Override
	public ChannelFuture write(Object paramObject) {
		return null;
	}

	@Override
	public ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelHandlerContext flush() {
		return null;
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise) {
		return null;
	}

	@Override
	public ChannelFuture writeAndFlush(Object paramObject) {
		return null;
	}

	@Override
	public ChannelPipeline pipeline() {
		return null;
	}

	@Override
	public ByteBufAllocator alloc() {
		return null;
	}

	@Override
	public ChannelPromise newPromise() {
		return null;
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		return null;
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		return null;
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable paramThrowable) {
		return null;
	}

	@Override
	public ChannelPromise voidPromise() {
		return null;
	}

	public <T> boolean hasAttr(AttributeKey<T> arg0) {
		return false;
	}
}