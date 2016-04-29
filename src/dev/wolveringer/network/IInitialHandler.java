package dev.wolveringer.network;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import dev.wolveringer.BungeeUtil.AsyncCatcher;
import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.configuration.Configuration;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityEffect;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutGameStateChange;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutRemoveEntityEffect;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutUpdateHealth;
import dev.wolveringer.chat.ChatSerializer;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.network.channel.ChannelWrapper;

public abstract class IInitialHandler extends InitialHandler {
	public final static Field CHANNEL_FIELD;

	static{
		Field f;
		try{
			f = InitialHandler.class.getDeclaredField("ch");
			f.setAccessible(true);
		}catch (NoSuchFieldException | SecurityException e){
			f = null;
			e.printStackTrace();
		}
		CHANNEL_FIELD = f;
	}

	public boolean isConnected;
	private Encoder b;
	private Decoder a;
	private short transaktionId;
	private short window;
	private IChatBaseComponent[] tab = new IChatBaseComponent[2];

	public IInitialHandler(ProxyServer instance, ListenerInfo listenerInfo, Decoder a, Encoder b) {
		super((BungeeCord) instance, listenerInfo);
		this.a = a;
		this.b = b;
		if(a != null)
			a.setHandler(this);
		if(b != null)
			b.setHandler(this);
	}

	public Encoder getEncoder() {
		return b;
	}

	public Decoder getDecoder() {
		return a;
	}

	@Override
	public void connected(final net.md_5.bungee.netty.ChannelWrapper channel) throws Exception {
		super.connected(new ChannelWrapper(channel, this));
	}

	@Override
	public void handle(LoginSuccess loginSuccess) throws Exception {
		super.handle(loginSuccess);
	}

	public abstract Player getPlayer();

	@Override
	public void disconnect(final BaseComponent... reason) {
		super.disconnect(reason);
	}

	public void disconnect(Exception e) {
		disconnect(e,10);
	}
	
	public void disconnect(Exception e,int stackDeep) {
		String message = ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"4Error Message: "+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"b" + e.getLocalizedMessage() + "\n";
		for(int i = 0;i < (e.getStackTrace().length > stackDeep ? stackDeep : e.getStackTrace().length);i++){
			StackTraceElement ex = e.getStackTrace()[i];
			message += format(ex) + "\n";
		}
		if(getChannel().isClosed())

		{
			return;
		}
		final IChatBaseComponent comp = ChatSerializer.fromMessage(message);
		unsafe().sendPacket(new Kick(ChatSerializer.toJSONString(comp)));
		closeChannel();
	}

	public void closeChannel() {
		if(isConnected){
			if(getPlayer().getInventoryView() != null)
				getPlayer().getInventoryView().getViewer().remove(this);
			getPlayer().getPlayerInventory().reset();
			isConnected = false;
			b = null;
			a = null;
			tab = null;
		}
		if(!getChannel().isClosed())
			getChannel().close();
	}

	public void sendPacket(Packet p) {
		AsyncCatcher.catchOp("Packet cant be sending async!");
		ByteBuf b = p.getByteBuf(ClientVersion.fromProtocoll(getVersion()));
		getChannel().getHandle().writeAndFlush(b);
		p = null;
	}

	public void sendPacketToServer(Packet p) {
		AsyncCatcher.catchOp("Packet cant be sending async!");
		ByteBuf b = p.getByteBuf(p.getVersion());
		((ServerConnection) getPlayer().getServer()).getCh().write(b);
	}

	public void setProtocol(Protocol p) {
		a.setProtocol(p);
		b.setProtocol(p);
	}

	private String format(StackTraceElement e) {
		return ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"eat "+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"5" + e.getClassName() + "."+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"b" + e.getMethodName() + (e.getFileName() != null ? "("+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a" + e.getFileName() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"b)" : (e.getFileName() != null) && (e.getLineNumber() >= 0) ? "("+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"a" + e.getFileName() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"b:"+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"c" + e.getLineNumber() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"b)" : e.isNativeMethod() ? ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"1(Native Method)" : ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"c(Unknown Source)");
	}

	@Deprecated
	public void resetClient() {
		for(int i = 1;i < 24;i++)
			sendPacket(new PacketPlayOutRemoveEntityEffect(getEntityId(), i));
		sendPacket(new PacketPlayOutPosition(getPlayer().getLocation().add(0, 10000, 0), true));
		sendPacket(new PacketPlayOutEntityEffect(getEntityId(), 15, 1, 100000, true));
		sendPacket(new PacketPlayOutGameStateChange(3, 0));
		sendPacket(new PacketPlayOutUpdateHealth(20F, 20, 0F));
		resetInventory();
	}

	@Deprecated
	public void resetInventory() {
		for(int i = 0;i < getPlayer().getPlayerInventory().getContains().length;i++)
			getPlayer().getPlayerInventory().setItem(i, null);
		getPlayer().updateInventory();
	}

	public int getEntityId() {
		try{
			Field f = UserConnection.class.getDeclaredField("clientEntityId");
			f.setAccessible(true);
			if(BungeeCord.getInstance().getPlayer(getPlayer().getName()) == null)
				return -1;
			return f.getInt(BungeeCord.getInstance().getPlayer(getPlayer().getName()));
		}catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public ChannelWrapper getChannel() {
		try{
			return (ChannelWrapper) CHANNEL_FIELD.get(this);
		}catch (IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}
	public short getTransaktionId() {
		return transaktionId;
	}

	public void setTransaktionId(short transaktionId) {
		this.transaktionId = transaktionId;
	}

	public void setWindow(short window) {
		this.window = window;
	}

	public short getWindow() {
		return window;
	}

	public IChatBaseComponent[] getTabHeader() {
		return tab;
	}

	public void setTabHeader(IChatBaseComponent header, IChatBaseComponent footer) {
		this.tab = new IChatBaseComponent[] { header, footer };
		sendPacket(new PacketPlayOutPlayerListHeaderFooter(header, footer));
	}

	public void setTabHeaderFromPacket(IChatBaseComponent header, IChatBaseComponent footer) {
		this.tab = new IChatBaseComponent[] { header, footer };
	}

	@Override
	public String toString() {
		if(Configuration.isTerminalColored())
			return "[" + (getHandshake().getRequestedProtocol() == 2 ? ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"aGAME" : ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"ePING") + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"7][" + (getHandshake().getRequestedProtocol() == 0 ? ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"6" + getName() : ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"c" + getAddress().getHostString()) + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"7]";
		else
			return super.toString();
	}
}
