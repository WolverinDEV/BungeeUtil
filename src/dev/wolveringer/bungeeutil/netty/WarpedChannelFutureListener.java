package dev.wolveringer.bungeeutil.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class WarpedChannelFutureListener implements ChannelFutureListener {
	Callback<Boolean> callback;
	UserConnection conn;
	BungeeServerInfo target;
	boolean retry;

	public WarpedChannelFutureListener(Callback<Boolean> callback, UserConnection conn, BungeeServerInfo target, boolean retry) {
		this.callback = callback;
		this.conn = conn;
		this.target = target;
		this.retry = retry;
	}

	public void operationComplete(ChannelFuture future) throws Exception {
		if(callback != null){
			callback.done(Boolean.valueOf(future.isSuccess()), future.cause());
		}
		if(future.isSuccess()){
			
			return;
		}
		future.channel().close();
		conn.getPendingConnects().remove(target);

		ServerInfo def = (ServerInfo) ProxyServer.getInstance().getServers().get(conn.getPendingConnection().getListener().getFallbackServer());
		if((retry) && (target != def) && (((conn.getServer() == null) || (def != conn.getServer().getInfo())))){
			conn.sendMessage(BungeeCord.getInstance().getTranslation("fallback_lobby", new Object[0]));
			conn.connect(def, null, false);

		}else if(conn.isDimensionChange()){
			conn.disconnect(BungeeCord.getInstance().getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
		}else{
			conn.sendMessage(BungeeCord.getInstance().getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
		}
	}
}
