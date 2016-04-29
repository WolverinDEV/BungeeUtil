package dev.wolveringer.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.PlatformDependent;

import java.lang.reflect.Field;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import dev.wolveringer.BungeeUtil.ClientVersion;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.item.Item;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInChat;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutCloseWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutNamedSoundEffect;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutOpenWindow;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSetSlot;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutWindowItems;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayIn;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.SoundEffect;
import dev.wolveringer.api.SoundEffect.SoundCategory;
import dev.wolveringer.api.bossbar.BossBarManager;
import dev.wolveringer.api.inventory.Inventory;
import dev.wolveringer.api.inventory.InventoryType;
import dev.wolveringer.api.inventory.PlayerInventory;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.api.scoreboard.Scoreboard;
import dev.wolveringer.bungee.AbstraktUserConnection;
import dev.wolveringer.chat.IChatBaseComponent;
import dev.wolveringer.network.inject.XChannelFutureListener;
import dev.wolveringer.network.inject.XChannelInitializer;

public class ProxiedPlayerUserConnection extends AbstraktUserConnection implements Player {
	private IInitialHandler i;
	private Inventory inv;
	private Location loc;
	private Location last_loc;
	private PlayerInventory p_inv;
	private int slot;
	private Scoreboard board;
	private String[] tab = new String[2];
	private BossBarManager bossBarManager;
	
	public ProxiedPlayerUserConnection(ProxyServer bungee, ChannelWrapper ch, String name, InitialHandler pendingConnection) {
		super(bungee, ch, name, pendingConnection);
		this.i = (IInitialHandler) pendingConnection;
		p_inv = new PlayerInventory(this);
		p_inv.getViewer().add(this);
		board = new Scoreboard(this);
		loc = last_loc = new Location(0, 0, 0);
		bossBarManager = new BossBarManager(this);
	}

	public IInitialHandler getInitialHandler() {
		return i;
	}

	public void closeInventory() {
		closeInventory(true);
		updateInventory();
	}

	private void closeInventory(boolean b) {
		if(inv == null)
			return;
		if(b)
			sendPacket(new PacketPlayOutCloseWindow(Inventory.ID));
		inv.getViewer().remove(this);
		inv = null;
	}

	public Location getLocation() {
		return loc.clone();
	}

	public Location getLastLocation() {
		return last_loc.clone();
	}

	public void performCommand(String command) {
		sendPacketToServer(new PacketPlayInChat((command.startsWith("/") ? "" : "/") + command));
	}

	@Deprecated
	public void sendPacketToServer(PacketPlayIn p) {
		i.sendPacketToServer((Packet) p);
	}

	public boolean isInventoryOpened() {
		return inv != null;
	}

	public void openInventory(Inventory inv) {
		if(isInventoryOpened())
			closeInventory(true);
		PacketPlayOutOpenWindow e = new PacketPlayOutOpenWindow(Inventory.ID, inv.getType().getType(getVersion()), inv.getName(), inv.getType() == InventoryType.Chest ? inv.getSlots() : inv.getType().getDefaultSlots(), false);
		e.UTF_8 = true;
		sendPacket(e);
		sendPacket(new PacketPlayOutWindowItems(Inventory.ID, inv.getContains()));
		inv.getViewer().add(this);
		this.inv = inv;
	}

	public void updateInventory() {
		int window = 0;
		int dslot = 0;
		if(isInventoryOpened()){
			window = Inventory.ID;
			dslot = getInventoryView().getSlots();
		}
		Item[] items = p_inv.getContains();
		for(int i = 0;i < items.length;i++){
			if(isInventoryOpened() && i-9 < 0)
				continue;
			Item item = items[i];
			sendPacket(new PacketPlayOutSetSlot(item, window, dslot+i-(isInventoryOpened()?9:0))); //-9 Player crafting and armor
		}
	}

	public void setCursorItem(Item is) {
		sendPacket(new PacketPlayOutSetSlot(is, -1, -1));
		getPlayerInventory().setItem(999, is);
	}

	public Item getCursorItem() {
		return getPlayerInventory().getItem(999);
	}

	public Item getOffHandItem() {
		return getPlayerInventory().getItem(45);
	}
	
	public PlayerInventory getPlayerInventory() {
		return p_inv;
	}

	public ClientVersion getVersion() {
		return ClientVersion.fromProtocoll(i.getHandshake() == null ? -1 : i.getHandshake().getProtocolVersion());
	}

	public Inventory getInventoryView() {
		return inv;
	}

	public void sendPacket(PacketPlayOut packet) {
		Packet p = (Packet) packet;
		if(p == null)
			return;
		i.sendPacket(p);
	}

	public void setLocation(Location loc) {
		this.last_loc = this.loc.clone();
		this.loc = loc;
	}

	public void setSelectedSlot(int slot) {
		this.slot = slot;
	}

	public int getSelectedSlot() {
		return slot;
	}

	public Scoreboard getScoreboard() {
		return board;
	}

	@Override
	public IChatBaseComponent[] getTabHeader() {
		return getInitialHandler().getTabHeader();
	}

	@Override
	public void setTabHeader(IChatBaseComponent header, IChatBaseComponent footer) {
		getInitialHandler().setTabHeader(header, footer);
	}
	
	@Override
	public void disconnect(Exception e) {
		getInitialHandler().disconnect(e);
	}

	@Override
	public String toString() {
		return "Player{name=\""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r" + getName() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r\" DisplayName=\""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r" + getDisplayName() + ""+dev.wolveringer.chat.ChatColor.ChatColorUtils.COLOR_CHAR+"r\" ping=\"" + getPing() + "\"}";
	}

	@Override
	public void connect(ServerInfo info, Callback<Boolean> callback, boolean retry) {
		connect0(info, callback, retry);
	}

	public void connect0(ServerInfo info, final Callback<Boolean> callback, final boolean retry) {
		Preconditions.checkNotNull(info, "info");

		ServerConnectEvent event = new ServerConnectEvent(this, info);
		if(((ServerConnectEvent) BungeeCord.getInstance().getPluginManager().callEvent(event)).isCancelled()){
			return;
		}

		final BungeeServerInfo target = (BungeeServerInfo) event.getTarget();
		if((getServer() != null) && (Objects.equal(getServer().getInfo(), target))){
			sendMessage(BungeeCord.getInstance().getTranslation("already_connected", new Object[0]));
			return;
		}
		if(getPendingConnects().contains(target)){
			sendMessage(BungeeCord.getInstance().getTranslation("already_connecting", new Object[0]));
			return;
		}
		getPendingConnects().add(target);
		ChannelInitializer<Channel> initializer = new XChannelInitializer(getUserconnection(), target);
		ChannelFutureListener listener = new XChannelFutureListener(callback, getUserconnection(), target, retry);
		Bootstrap b = ((Bootstrap) ((Bootstrap) ((Bootstrap) ((Bootstrap) new Bootstrap().channel(PipelineUtils.getChannel())).group(get("ch", ChannelWrapper.class).getHandle().eventLoop())).handler(initializer)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(5000))).remoteAddress(target.getAddress());
		if((getPendingConnection().getListener().isSetLocalAddress()) && (!(PlatformDependent.isWindows()))){
			b.localAddress(getPendingConnection().getListener().getHost().getHostString(), 0);
		}
		b.connect().addListener(listener);
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

	private <T> T get(String a, Class<T> ref) {
		return (T) get(a);
	}
	
	private UserConnection getUserconnection(){
		return (UserConnection) ((ProxiedPlayer)this);
	}

	@Override
	public boolean isConnected() {
		return getUserconnection().isConnected();
	}

	@Override
	public Item getHandItem() {
		return getPlayerInventory().getItem(36+slot);
	}
	@Override
	public void playSound(SoundEffect effect, Location location, float volume, float pitch) {
		playSound(effect, SoundCategory.MASTER, location, volume, pitch);
	}

	public void playSound(SoundEffect effect,SoundCategory category, Location location, float volume, float pitch) {
		if(!effect.isAvariable(getVersion().getBigVersion()))
			throw new RuntimeException("Sound not avariable for client version");
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect();
		packet.setLoc(location);
		packet.setVolume(volume);
		packet.setSoundCategory(category.ordinal());
		packet.setSound(effect.getName());
		sendPacket(packet);
	}

	@Override
	public BossBarManager getBossBarManager() {
		return bossBarManager;
	}
}
