package dev.wolveringer.bungeeutil.player.connection;

import java.lang.reflect.Field;

import dev.wolveringer.bungeeutil.AsyncCatcher;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.inventory.CloseReason;
import dev.wolveringer.bungeeutil.netty.WarpedChannelWrapper;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftDecoder;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftEncoder;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutGameStateChange;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerListHeaderFooter;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutRemoveEntityEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutUpdateHealth;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.Kick;
import net.md_5.bungee.protocol.packet.LoginSuccess;
import net.md_5.bungee.protocol.packet.Respawn;

public abstract class IInitialHandler extends InitialHandler {
	public final static Field CHANNEL_FIELD;

	static {
		Field f;
		try {
			f = InitialHandler.class.getDeclaredField("ch");
			f.setAccessible(true);
		}
		catch (NoSuchFieldException | SecurityException e) {
			f = null;
			e.printStackTrace();
		}
		CHANNEL_FIELD = f;
	}

	public boolean isConnected = false;

	private boolean isDisconnecting = false;
	private WarpedMinecraftEncoder b;
	private WarpedMinecraftDecoder a;
	private short transaktionId;
	private short window;
	private BaseComponent[] tab = new BaseComponent[2];
	public IInitialHandler(ProxyServer instance, ListenerInfo listenerInfo, WarpedMinecraftDecoder a, WarpedMinecraftEncoder b) {
		super(BungeeCord.getInstance(), listenerInfo);
		this.a = a;
		this.b = b;
		if (a != null) {
			a.setInitHandler(this);
		}
		if (b != null) {
			b.setInitHandler(this);
		}
	}


	public void closeChannel() {
		if (!this.getChannel().isClosed()) {
			this.getChannel().close();
		}
		if (this.isConnected) {
			if (this.getPlayer().getInventoryView() != null) {
				this.getPlayer().getInventoryView().unsave().getModificableViewerList().remove(this);
			}
			this.getPlayer().getPlayerInventory().reset();
			this.isConnected = false;
			this.b = null;
			this.a = null;
			this.tab = null;
		}
	}

	@Override
	public void connected(final net.md_5.bungee.netty.ChannelWrapper channel) throws Exception {
		super.connected(new WarpedChannelWrapper(channel, this));
	}

	@Override
	public void disconnect(final BaseComponent... reason) {
		if (this.isDisconnecting) {
			return;
		}
		this.isDisconnecting = true;
		if (this.getHandshake() != null && this.getHandshake().getRequestedProtocol() == 2) {
			if(this.getEncoder().getProtocoll() == Protocol.HANDSHAKE) {
				this.setProtocol(Protocol.LOGIN);
			}
			this.unsafe().sendPacket(new Kick(ComponentSerializer.toString(reason)));
		}
		this.closeChannel();
	}

	public void disconnect(Exception e) {
		this.disconnect(e, 10);
	}

	public void disconnect(Exception e, int stackDeep) {
		if (this.isDisconnecting) {
			return;
		}
		this.isDisconnecting = true;
		if(this.getChannel() == null){
			BungeeUtil.debug("disconnection an null channel.");
			return;
		}
		if (this.getChannel().isClosed()) { return; }
		String message = "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "4Error Message: " + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "b" + e.getLocalizedMessage() + "\n";
		for (int i = 0; i < (e.getStackTrace().length > stackDeep ? stackDeep : e.getStackTrace().length); i++) {
			StackTraceElement ex = e.getStackTrace()[i];
			if(ex.getMethodName().equalsIgnoreCase("channelRead") && ex.getClassName().equalsIgnoreCase("io.netty.handler.codec.MessageToMessageDecoder") && ex.getLineNumber() == 89) {
				break;
			}
			message += this.format(ex) + "\n";
		}
		this.disconnect(message);
	}

	@Override
	public void disconnect(String reason) {
		this.disconnect(TextComponent.fromLegacyText(reason));
	}

	@Override
	public void disconnected(ChannelWrapper channel) throws Exception {
		if(this.getPlayer().isInventoryOpened()) {
			this.getPlayer().closeInventory(CloseReason.CLIENT_DISCONNECTED);
		}
		super.disconnected(channel);
	}

	private String format(StackTraceElement e) {
		return "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "eat " + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "5" + e.getClassName() + "." + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "b" + e.getMethodName() + (e.getFileName() != null ? "(" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "a" + e.getFileName() + ":"+e.getLineNumber() + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "b)" : e.getFileName() != null && e.getLineNumber() >= 0 ? "(" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "a" + e.getFileName() + "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "b:" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "c" + e.getLineNumber() + "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "b)" : e.isNativeMethod() ? "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "1(Native Method)" : "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "c(Unknown Source)");
	}

	public WarpedChannelWrapper getChannel() {
		try {
			return (WarpedChannelWrapper) CHANNEL_FIELD.get(this);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public WarpedMinecraftDecoder getDecoder() {
		return this.a;
	}

	public WarpedMinecraftEncoder getEncoder() {
		return this.b;
	}

	public int getEntityId() {
		try {
			Field f = UserConnection.class.getDeclaredField("clientEntityId");
			f.setAccessible(true);
			if (BungeeCord.getInstance().getPlayer(this.getPlayer().getName()) == null) {
				return -1;
			}
			return f.getInt(BungeeCord.getInstance().getPlayer(this.getPlayer().getName()));
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public abstract Player getPlayer();

	public BaseComponent[] getTabHeader() {
		return this.tab;
	}

	public short getTransaktionId() {
		return this.transaktionId;
	}

	public short getWindow() {
		return this.window;
	}

	@Override
	public void handle(LoginSuccess loginSuccess) throws Exception {
		super.handle(loginSuccess);
		this.isConnected = true;
	}

	@Override
	public void handle(Respawn respawn) throws Exception {
		super.handle(respawn);
		if(this.getPlayer().isInventoryOpened()) {
			this.getPlayer().closeInventory(CloseReason.SERVER_CLOSED);
		}
	}

	@Deprecated
	public void resetClient() {
		for (int i = 1; i < 24; i++) {
			this.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getEntityId(), i));
		}
		this.sendPacket(new PacketPlayOutEntityEffect(this.getEntityId(), 15, 1, 100000, true));
		this.sendPacket(new PacketPlayOutGameStateChange(3, 0));
		this.sendPacket(new PacketPlayOutUpdateHealth(20F, 20, 0F));
		this.resetInventory();
	}

	@Deprecated
	public void resetInventory() {
		for (int i = 0; i < this.getPlayer().getPlayerInventory().getContains().length; i++) {
			this.getPlayer().getPlayerInventory().setItem(i, null);
		}
		this.getPlayer().updateInventory();
	}

	public void sendPacket(Packet p) {
		AsyncCatcher.catchOp("Packet cant be sending async!");
		ByteBuf b = p.getByteBuf(ClientVersion.fromProtocoll(this.getVersion()));
		this.getChannel().getHandle().writeAndFlush(b);
		p = null;
	}

	public void sendPacketToServer(Packet p) {
		AsyncCatcher.catchOp("Packet cant be sending async!");
		ByteBuf b = p.getByteBuf(ClientVersion.fromProtocoll(this.getVersion()));
		((ServerConnection) this.getPlayer().getServer()).getCh().write(b);
	}

	public void setProtocol(Protocol p) {
		this.a.setProtocol(p);
		this.b.setProtocol(p);
	}

	public void setTabHeader(BaseComponent header, BaseComponent footer) {
		this.tab = new BaseComponent[] { header, footer };
		this.sendPacket(new PacketPlayOutPlayerListHeaderFooter(header, footer));
	}

	public void setTabHeaderFromPacket(BaseComponent header, BaseComponent footer) {
		this.tab = new BaseComponent[] { header, footer };
	}

	public void setTransaktionId(short transaktionId) {
		this.transaktionId = transaktionId;
	}

	public void setWindow(short window) {
		this.window = window;
	}

	@Override
	public String toString() {
		if (Configuration.isTerminalColored()) {
			return "[" + (this.getHandshake().getRequestedProtocol() == 2 ? "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "aGAME" : "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "ePING") + "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "7][" + (this.getHandshake().getRequestedProtocol() == 0 ? "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "6" + this.getName() : "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "c" + this.getAddress().getHostString()) + "" + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR + "7]";
		} else {
			return super.toString();
		}
	}
}
