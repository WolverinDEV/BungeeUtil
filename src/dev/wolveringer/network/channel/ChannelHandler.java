package dev.wolveringer.network.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

import java.io.IOException;
import java.util.logging.Level;

import com.google.common.base.Preconditions;

import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.strings.Messages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.connection.CancelSendSignal;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.PingHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PacketHandler;
import net.md_5.bungee.protocol.BadPacketException;
import net.md_5.bungee.protocol.PacketWrapper;

public class ChannelHandler extends HandlerBoss {
	private ChannelWrapper channel;
	private PacketHandler handler;

	public ChannelHandler() {
	}
	
	private String formatColor(String in){
		if(Configuration.isTerminalColored())
			return in;
		else
			return ChatColorUtils.stripColor(in);
	}

	@Override
	public void setHandler(PacketHandler handler) {
		Preconditions.checkArgument(handler != null, "handler"); //$NON-NLS-1$
		this.handler = handler;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if(this.handler != null){
			this.channel = new ChannelWrapper(ctx);
			this.handler.connected(this.channel);
			if((!(this.handler instanceof InitialHandler)) && (!(this.handler instanceof PingHandler))){
				ProxyServer.getInstance().getLogger().log(Level.INFO, formatColor(Messages.getString("ChannelHandler.connection.connect")), this.handler); //$NON-NLS-1$
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(this.handler != null){
			this.handler.disconnected(this.channel);
			if((!(this.handler instanceof InitialHandler)) && (!(this.handler instanceof PingHandler))){
				ProxyServer.getInstance().getLogger().log(Level.INFO, formatColor(Messages.getString("ChannelHandler.connection.disconnect")), this.handler); //$NON-NLS-1$
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(this.handler != null){
			try{
				PacketWrapper packet = (PacketWrapper) msg;
				boolean sendPacket = true;
				try{
					if(packet.packet != null){
						try{
							packet.packet.handle(this.handler);
						}catch (CancelSendSignal ex){
							sendPacket = false;
						}
					}
					if(sendPacket){
						this.handler.handle(packet);
					}
				}finally{
					packet.trySingleRelease();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(ctx.channel().isActive()){
			if((cause instanceof ReadTimeoutException)){
				ProxyServer.getInstance().getLogger().log(Level.WARNING, formatColor(Messages.getString("ChannelHandler.connection.timeout")), this.handler); //$NON-NLS-1$
			}else if((cause instanceof BadPacketException)){
				ProxyServer.getInstance().getLogger().log(Level.WARNING, formatColor(Messages.getString("ChannelHandler.connection.bad-packet")), this.handler); //$NON-NLS-1$
			}else if((cause instanceof IOException)){
				ProxyServer.getInstance().getLogger().log(Level.WARNING, formatColor(Messages.getString("ChannelHandler.connection.IOException")), new Object[] { this.handler, cause.getMessage() }); //$NON-NLS-1$
			}else{
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, this.handler + formatColor(Messages.getString("ChannelHandler.connection.encounteredException")), cause); //$NON-NLS-1$
			}

			if(this.handler != null){
				try{
					this.handler.exception(cause);
				}catch (Exception ex){
					ProxyServer.getInstance().getLogger().log(Level.SEVERE, this.handler + formatColor(Messages.getString("ChannelHandler.connection.progressingException")), ex); //$NON-NLS-1$
				}
			}
			ctx.close();
		}
	}
}
