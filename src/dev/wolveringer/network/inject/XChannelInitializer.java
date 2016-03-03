package dev.wolveringer.network.inject;

import dev.wolveringer.network.Decoder;
import dev.wolveringer.network.IInitialHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.ServerConnector;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

public class XChannelInitializer extends ChannelInitializer<Channel> {
	UserConnection conn;
	ServerInfo target;

	public XChannelInitializer(UserConnection conn, ServerInfo target) {
		this.conn = conn;
		this.target = target;
	}

	public void initChannel(Channel ch) throws Exception {
		PipelineUtils.BASE.initChannel(ch);
		ch.pipeline().addAfter("frame-decoder", "packet-decoder", new Decoder(Protocol.HANDSHAKE, false, conn.getPendingConnection().getVersion(),(IInitialHandler) conn.getPendingConnection(),Direction.TO_CLIENT)); //
		ch.pipeline().addAfter("frame-prepender", "packet-encoder", new MinecraftEncoder(Protocol.HANDSHAKE, false, conn.getPendingConnection().getVersion()));
		((HandlerBoss) ch.pipeline().get(HandlerBoss.class)).setHandler(new ServerConnector(BungeeCord.getInstance(), conn, (BungeeServerInfo) target));
	}
}
