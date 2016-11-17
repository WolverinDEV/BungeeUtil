package dev.wolveringer.bungeeutil.netty;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.KickStringWriter;
import net.md_5.bungee.protocol.LegacyDecoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;
import net.md_5.bungee.protocol.Varint21FrameDecoder;
import net.md_5.bungee.protocol.Varint21LengthFieldPrepender;
import dev.wolveringer.bungeeutil.BungeeUtil;

public class BungeeUtilChannelInizializer <T extends InitialHandler> extends ChannelInizializer {
	protected Varint21LengthFieldPrepender framePrepender;
	
	public static final DefaultChannelInizializer dinti = new DefaultChannelInizializer();
	private Constructor<? extends InitialHandler> cons;
	
	public BungeeUtilChannelInizializer(Class<T> handler) {
		if(handler == null)
			throw new NullPointerException();
		try {
			initFramePrender();
			this.cons = handler.getConstructor(new Class[] { ProxyServer.class, ListenerInfo.class, WarpedMinecraftDecoder.class, WarpedMinecraftEncoder.class });
		}
		catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private void initFramePrender() {
		Field f = null;
		try {
			f = PipelineUtils.class.getDeclaredField("framePrepender");
		}
		catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		catch (SecurityException e1) {
			e1.printStackTrace();
		}
		f.setAccessible(true); 
		try {
			framePrepender = (Varint21LengthFieldPrepender) f.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void initialize(Channel ch) throws Exception {
		initBaseChannel(ch);
		try {
			WarpedMinecraftDecoder a = null;
			WarpedMinecraftEncoder b = null;
			ch.pipeline().addBefore(PipelineUtils.FRAME_DECODER, PipelineUtils.LEGACY_DECODER, new LegacyDecoder());
			ch.pipeline().addAfter("frame-prepender", "legacy-kick", new KickStringWriter());
			
			ch.pipeline().addAfter("frame-decoder", "packet-decoder", a = new WarpedMinecraftDecoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion(), null, Direction.TO_SERVER));
			ch.pipeline().addAfter("frame-prepender", "packet-encoder", b = new WarpedMinecraftEncoder(Protocol.HANDSHAKE, true, ProxyServer.getInstance().getProtocolVersion(), null));
			
			ch.pipeline().get(HandlerBoss.class).setHandler(createInitialHandler(ch, b, a));
		}
		catch (Throwable e) {
			if (e instanceof NoClassDefFoundError) {
				throwClassNotFoundError((ClassNotFoundException) e);
				return;
			}
			e.printStackTrace();
		}
	}
	
	public void initBaseChannel(Channel ch) {
		try {
			ch.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
		}
		catch (ChannelException ex) {
		}
		ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);
		ch.pipeline().addLast("timeout", new ReadTimeoutHandler(BungeeCord.getInstance().config.getTimeout(), java.util.concurrent.TimeUnit.MILLISECONDS));
		ch.pipeline().addLast("frame-decoder", new Varint21FrameDecoder());
		ch.pipeline().addLast("frame-prepender", framePrepender);
		ch.pipeline().addLast("inbound-boss", new WarpedChannelHandler());
	}
	
	@SuppressWarnings("deprecation")
	public void throwClassNotFoundError(ClassNotFoundException exception) {
		BungeeCord.getInstance().getConsole().sendMessage(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7] "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cBungeeUntil cant load some Classes!");
		BungeeCord.getInstance().getConsole().sendMessage(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7] "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cDisable BungeeUntil!");
		try {
			setStaticFinalValue(PipelineUtils.class.getDeclaredField("SERVER_CHILD"), dinti);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			BungeeCord.getInstance().getConsole().sendMessage(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7] "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cError while setting default ConnectionHandler.");
			BungeeCord.getInstance().getConsole().sendMessage(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7] "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cRestarting BungeeCord!");
			BungeeCord.getInstance().stop();
			return;
		}
		BungeeCord.getInstance().getPluginManager().unregisterListeners(BungeeUtil.getPluginInstance()); //TODO onley bungeeutil
		BungeeUtil.getInstance().disable();
		for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers())
			p.disconnect(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cBungeeUntil Class error");
		BungeeCord.getInstance().getConsole().sendMessage(""+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"7] "+dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"cBungeeUntil is disabled!");
	}
	
	public T createInitialHandler(Channel ch, WarpedMinecraftEncoder e, WarpedMinecraftDecoder d) throws Exception {
		return (T) this.cons.newInstance(new Object[] { ProxyServer.getInstance(), ch.attr(PipelineUtils.LISTENER).get(), d, e });
	}
	
	private void setStaticFinalValue(Field f, Object n) throws Exception {
		f.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		f.set(null, n);
	}
}
