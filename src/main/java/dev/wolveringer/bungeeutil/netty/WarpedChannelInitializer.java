package dev.wolveringer.bungeeutil.netty;

import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class WarpedChannelInitializer extends ChannelInitializer<Channel> {
	private UserConnection conn;
	private ServerInfo target;

	public void initChannel(Channel ch) throws Exception {
		PipelineUtils.BASE.initChannel(ch);
		ch.pipeline().addAfter("frame-decoder", "packet-decoder", new WarpedMinecraftDecoder(Protocol.HANDSHAKE, false, conn.getPendingConnection().getVersion(),(IInitialHandler) conn.getPendingConnection(),Direction.TO_CLIENT));
		ch.pipeline().addAfter("frame-prepender", "packet-encoder", new MinecraftEncoder(Protocol.HANDSHAKE, false, conn.getPendingConnection().getVersion()));
		((HandlerBoss) ch.pipeline().get(HandlerBoss.class)).setHandler(new ServerConnector(BungeeCord.getInstance(), conn, (BungeeServerInfo) target));
	}
}
