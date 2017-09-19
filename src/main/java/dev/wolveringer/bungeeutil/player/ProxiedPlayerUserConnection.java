package dev.wolveringer.bungeeutil.player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;

import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.bossbar.BossBarManager;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.inventory.CloseReason;
import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.inventory.InventoryListener;
import dev.wolveringer.bungeeutil.inventory.InventoryType;
import dev.wolveringer.bungeeutil.inventory.PlayerInventory;
import dev.wolveringer.bungeeutil.item.Item;
import dev.wolveringer.bungeeutil.netty.WarpedChannelFutureListener;
import dev.wolveringer.bungeeutil.netty.WarpedChannelInitializer;
import dev.wolveringer.bungeeutil.netty.WarpedChannelWrapper;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInChat;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutCloseWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutNamedSoundEffect;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayIn;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.connection.IInitialHandler;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.profile.Skin;
import dev.wolveringer.bungeeutil.scoreboard.Scoreboard;
import dev.wolveringer.bungeeutil.sound.SoundCategory;
import dev.wolveringer.bungeeutil.sound.SoundEffect;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.PlatformDependent;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.packet.Kick;

public class ProxiedPlayerUserConnection extends UserConnection implements Player {
	private static final int CURSOR_ITEM_SLOT = 50;
	private IInitialHandler i;
	private Inventory inv;
	private Location loc;
	private Location last_loc;
	private PlayerInventory p_inv;
	private int slot;
	private Scoreboard board;
	private BossBarManager bossBarManager;

	public ProxiedPlayerUserConnection(ProxyServer bungee, ChannelWrapper ch, String name, InitialHandler pendingConnection) {
		super(bungee, ch, name, pendingConnection);
		this.i = (IInitialHandler) pendingConnection;
		this.p_inv = new PlayerInventory(this);
		this.p_inv.getViewer().add(this);
		this.board = new Scoreboard(this);
		this.loc = this.last_loc = new Location(0, 0, 0);
		this.bossBarManager = new BossBarManager(this);
	}

	@Override
	public void closeInventory() {
		this.closeInventory(CloseReason.PLUGIN_CLOSED);
	}

	private void closeInventory(boolean b) {
		if(this.inv == null) {
			return;
		}
		if(b) {
			this.sendPacket(new PacketPlayOutCloseWindow(Inventory.ID));
		}
		this.inv.unsave().getModificableViewerList().remove(this);
		this.inv = null;
	}

	@Override
	public void closeInventory(CloseReason reason) {
		if(this.getInventoryView() == null) {
			return;
		}
		for(InventoryListener l : new ArrayList<>(this.getInventoryView().getInventoryListener())) {
			l.onClose(this.getInventoryView(), this, reason);
		}
		this.closeInventory(true);
		this.updateInventory();
	}

	@Override
	public void connect(ServerInfo info, Callback<Boolean> callback, boolean retry) {
		this.connect0(info, callback, retry);
	}

	public void connect0(ServerInfo info, final Callback<Boolean> callback, final boolean retry) {
		Preconditions.checkNotNull(info, "info");

		ServerConnectEvent event = new ServerConnectEvent(this, info);
		if(BungeeCord.getInstance().getPluginManager().callEvent(event).isCancelled()){
			return;
		}

		final BungeeServerInfo target = (BungeeServerInfo) event.getTarget();
		if(this.getServer() != null && Objects.equal(this.getServer().getInfo(), target)){
			this.sendMessage(BungeeCord.getInstance().getTranslation("already_connected", new Object[0]));
			return;
		}
		if(this.getPendingConnects().contains(target)){
			this.sendMessage(BungeeCord.getInstance().getTranslation("already_connecting", new Object[0]));
			return;
		}
		this.getPendingConnects().add(target);
		ChannelInitializer<Channel> initializer = new WarpedChannelInitializer(this.getUserconnection(), target);
		ChannelFutureListener listener = new WarpedChannelFutureListener(callback, this.getUserconnection(), target, retry);
		Bootstrap b = new Bootstrap().channel(PipelineUtils.getChannel()).group(this.get("ch", ChannelWrapper.class).getHandle().eventLoop()).handler(initializer).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(5000)).remoteAddress(target.getAddress());
		if(this.getPendingConnection().getListener().isSetLocalAddress() && !PlatformDependent.isWindows()){
			b.localAddress(this.getPendingConnection().getListener().getHost().getHostString(), 0);
		}
		b.connect().addListener(listener);
	}

	@Override
	public void disconnect(Exception e) {
		this.getInitialHandler().disconnect(e);
	}
	
	@Override
	public void disconnect0(BaseComponent... reason) {
		if (this.get("ch", WarpedChannelWrapper.class).isClosing())
			return;
		BungeeCord.getInstance().getLogger().log(Level.INFO, "[{0}] disconnected with: {1}", new Object[] { getName(), BaseComponent.toLegacyText(reason) });

		this.get("ch", WarpedChannelWrapper.class).delayedClose(new Kick(ComponentSerializer.toString(reason)));

		if (getServer() == null)
			return;
		getServer().setObsolete(true);
		getServer().disconnect("Quitting");
	}

	private Object get(String a) {
		Field f = null;
		try{
			f = UserConnection.class.getDeclaredField(a);
		}catch (NoSuchFieldException | SecurityException e){
			e.printStackTrace();
		}
		f.setAccessible(true);
		try{
			return f.get(this);
		}catch (IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T get(String a, Class<T> ref) {
		return (T) this.get(a);
	}

	@Override
	public BossBarManager getBossBarManager() {
		if(Configuration.isBossBarhandleEnabled()) {
			return this.bossBarManager;
		}
		throw new RuntimeException("The BossBar manager isnt enabled in the configuration!");
	}

	@Override
	public Item getCursorItem() {
		return this.getPlayerInventory().getItem(CURSOR_ITEM_SLOT);
	}

	@Override
	public Item getHandItem() {
		return this.getPlayerInventory().getItem(36+this.slot);
	}

	@Override
	public IInitialHandler getInitialHandler() {
		return this.i;
	}

	@Override
	public Inventory getInventoryView() {
		return this.inv;
	}

	@Override
	public Location getLastLocation() {
		return this.last_loc.clone();
	}

	@Override
	public Location getLocation() {
		return this.loc.clone();
	}

	@Override
	public Item getOffHandItem() {
		return this.getPlayerInventory().getItem(45);
	}

	@Override
	public PlayerInventory getPlayerInventory() {
		return this.p_inv;
	}

	@Override
	public Scoreboard getScoreboard() {
		if(Configuration.isScoreboardhandleEnabled()) {
			return this.board;
		}
		throw new RuntimeException("The Scoreboard manager isnt enabled in the configuration!");
	}

	@Override
	public int getSelectedSlot() {
		return this.slot;
	}

	@Override
	public BaseComponent[] getTabHeader() {
		return this.getInitialHandler().getTabHeader();
	}

	private UserConnection getUserconnection(){
		return (UserConnection) this;
	}

	@Override
	public ClientVersion getVersion() {
		return ClientVersion.fromProtocoll(this.i.getHandshake() == null ? -1 : this.i.getHandshake().getProtocolVersion());
	}

	@Override
	public boolean isConnected() {
		return !((ChannelWrapper)this.get("ch")).isClosed();
	}

	@Override
	public boolean isInventoryOpened() {
		return this.inv != null;
	}

	@Override
	public void openInventory(Inventory inv) {
		openInventory(inv, false);
	}

	@Override
	public void openInventory(Inventory inv, boolean resetCoursor) {
		if(this.isInventoryOpened()) {
			this.closeInventory(resetCoursor);
		}
		PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, inv.getType().getType(this.getVersion()), inv.getName(), inv.getType() == InventoryType.Chest ? inv.getSlots() : inv.getType().getDefaultSlots(), false);
		e.UTF_8 = true;
		this.sendPacket(e);
		this.sendPacket(new PacketPlayOutWindowItems(Inventory.ID, inv.getContains()));
		inv.unsave().getModificableViewerList().add(this);
		this.inv = inv;
	}
	
	@Override
	public void performCommand(String command) {
		this.sendPacketToServer(new PacketPlayInChat((command.startsWith("/") ? "" : "/") + command));
	}

	@Override
	public void playSound(SoundEffect effect) {
		this.playSound(effect, 1F);
	}

	@Override
	public void playSound(SoundEffect effect, float volume) {
		this.playSound(effect, volume, 0);
	}


	@Override
	public void playSound(SoundEffect effect, float volume, float pitch) {
		this.playSound(effect, this.getLocation() , volume, pitch);

	}

	@Override
	public void playSound(SoundEffect effect, Location location, float volume, float pitch) {
		this.playSound(effect, SoundCategory.MASTER, location, volume, pitch);
	}

	@Override
	public void playSound(SoundEffect effect,SoundCategory category, Location location, float volume, float pitch) {
		if(!effect.isAvariable(this.getVersion().getBigVersion())) {
			throw new RuntimeException("Sound not avariable for client version");
		}
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect();
		packet.setLoc(location);
		packet.setVolume(volume);
		packet.setSoundCategory(category.ordinal());
		packet.setSound(effect.getId(this.getVersion().getBigVersion()));
		this.sendPacket(packet);
	}

	@Override
	public void sendPacket(PacketPlayOut packet) {
		Packet p = (Packet) packet;
		if(p == null) {
			return;
		}
		this.i.sendPacket(p);
	}

	@Override
	@Deprecated
	public void sendPacketToServer(PacketPlayIn p) {
		this.i.sendPacketToServer((Packet) p);
	}

	@Override
	public void setCursorItem(Item is) {
		this.sendPacket(new PacketPlayOutSetSlot(is, -1, -1));
		this.getPlayerInventory().setItem(CURSOR_ITEM_SLOT, is);
	}

	@Override
	public void setLocation(Location loc) {
		this.last_loc = this.loc.clone();
		this.loc = loc;
	}

	@Override
	public void setSelectedSlot(int slot) {
		this.slot = slot;
	}

	@Override
	public void setTabHeader(BaseComponent header, BaseComponent footer) {
		this.getInitialHandler().setTabHeader(header, footer);
	}

	@Override
	public String toString() {
		return "Player{name=\""+ChatColorUtils.COLOR_CHAR+"r" + this.getName() + ChatColorUtils.COLOR_CHAR+"r\" DisplayName=\""+ChatColorUtils.COLOR_CHAR+"r" + this.getDisplayName() + dev.wolveringer.bungeeutil.chat.ChatColorUtils.COLOR_CHAR+"r\" ping=\"" + this.getPing() + "\"}";
	}

	@Override
	public void updateInventory() {
		//int window = 0;
		//int dslot = 0;
		Item[] items = this.p_inv.getContains();
		BungeeUtil.debug("Updating "+items.length+" slots of the player inventory.");
		/*
		if(isInventoryOpened()){
			window = Inventory.ID;
			dslot = getInventoryView().getSlots();
		}
		for(int i = 0;i < items.length;i++){
			if(isInventoryOpened() && i-9 < 0)
				continue;
			Item item = items[i];
			sendPacket(new PacketPlayOutSetSlot(item, window, dslot+i-(isInventoryOpened()?/*9*//*0:0))); //-9 Player crafting and armor
		}
		*/
		this.sendPacket(new PacketPlayOutWindowItems(0, items));
	}
	
	@Override
	public void setSkin(@NonNull Skin skin) {
		Validate.notNull(getPendingConnection(), "No prending connection found!");
		Validate.notNull(getPendingConnection().getLoginProfile(), "No valid login profile found! (Please wait until join)");
		getPendingConnection().getLoginProfile().setProperties(new LoginResult.Property[]{new LoginResult.Property("textures", skin.getRawData(), skin.getSignature())});
	}
}
