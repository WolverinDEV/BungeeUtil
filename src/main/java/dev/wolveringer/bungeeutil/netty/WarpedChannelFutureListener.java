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

	@SuppressWarnings("deprecation")
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if(this.callback != null){
			this.callback.done(Boolean.valueOf(future.isSuccess()), future.cause());
		}
		if(future.isSuccess()){

			return;
		}
		future.channel().close();
		this.conn.getPendingConnects().remove(this.target);

		ServerInfo def = ProxyServer.getInstance().getServers().get(this.conn.getPendingConnection().getListener().getFallbackServer());
		if(this.retry && this.target != def && (this.conn.getServer() == null || def != this.conn.getServer().getInfo())){
			this.conn.sendMessage(BungeeCord.getInstance().getTranslation("fallback_lobby", new Object[0]));
			this.conn.connect(def, null, false);

		}else if(this.conn.isDimensionChange()){
			this.conn.disconnect(BungeeCord.getInstance().getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
		}else{
			this.conn.sendMessage(BungeeCord.getInstance().getTranslation("fallback_kick", new Object[] { future.cause().getClass().getName() }));
		}
	}
}
