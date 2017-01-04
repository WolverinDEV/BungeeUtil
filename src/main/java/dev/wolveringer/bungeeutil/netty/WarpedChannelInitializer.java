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

	@Override
	public void initChannel(Channel ch) throws Exception {
		PipelineUtils.BASE.initChannel(ch);
		ch.pipeline().addAfter("frame-decoder", "packet-decoder", new WarpedMinecraftDecoder(Protocol.HANDSHAKE, false, this.conn.getPendingConnection().getVersion(),(IInitialHandler) this.conn.getPendingConnection(),Direction.TO_CLIENT));
		ch.pipeline().addAfter("frame-prepender", "packet-encoder", new MinecraftEncoder(Protocol.HANDSHAKE, false, this.conn.getPendingConnection().getVersion()));
		ch.pipeline().get(HandlerBoss.class).setHandler(new ServerConnector(BungeeCord.getInstance(), this.conn, (BungeeServerInfo) this.target));
	}
}
