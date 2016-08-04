package dev.wolveringer.bungee;

import java.beans.ConstructorProperties;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dev.wolveringer.BungeeUtil.Player;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.entitymap.EntityMap;
import net.md_5.bungee.forge.ForgeClientHandler;
import net.md_5.bungee.forge.ForgeServerHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.ClientSettings;
import net.md_5.bungee.tab.TabList;

public class AbstraktUserConnection implements ProxiedPlayer{
	@ConstructorProperties({ "bungee", "ch", "name", "pendingConnection" })
	public AbstraktUserConnection(ProxyServer bungee, ChannelWrapper ch, String name, InitialHandler pendingConnection) {
	}

	public String getName(){
		return null;
	}

	public InitialHandler getPendingConnection() {
		return null;
	}

	public void setServer(ServerConnection server) {
	}

	public boolean isDimensionChange() {
		return false;
	}

	public void setDimensionChange(boolean dimensionChange) {
	}

	public Collection<ServerInfo> getPendingConnects() {
		return null;
	}

	public int getSentPingId() {
		return 0;
	}

	public void setSentPingId(int sentPingId) {
	}

	public long getSentPingTime() {
		return 0L;
	}

	public void setSentPingTime(long sentPingTime) {
	}

	public int getPing() {
		return 0;
	}

	public void setPing(int ping) {
	}

	public ServerInfo getReconnectServer() {
		return null;
	}

	public void setReconnectServer(ServerInfo reconnectServer) {
	}

	public TabList getTabListHandler() {
		return null;
	}

	public int getGamemode() {
		return 0;
	}

	public void setGamemode(int gamemode) {
	}

	public int getCompressionThreshold() {
		return 0;
	}

	public int getClientEntityId() {
		return 0;
	}

	public void setClientEntityId(int clientEntityId) {
	}

	public int getServerEntityId() {
		return 0;
	}

	public void setServerEntityId(int serverEntityId) {
	}

	public ClientSettings getSettings() {
		return null;
	}

	public Scoreboard getServerSentScoreboard() {
		return null;
	}

	public String getDisplayName() {
		return null;
	}

	public EntityMap getEntityRewrite() {
		return null;
	}

	public ForgeClientHandler getForgeClientHandler() {
		return null;
	}

	public void setForgeClientHandler(ForgeClientHandler forgeClientHandler) {
	}

	public ForgeServerHandler getForgeServerHandler() {
		return null;
	}

	public void setForgeServerHandler(ForgeServerHandler forgeServerHandler) {
	}

	public void init() {
	}

	public void sendPacket(PacketWrapper packet) {
	}

	@Deprecated
	public boolean isActive() {
		return false;
	}

	public void setDisplayName(String name) {
	}

	public void connect(ServerInfo target) {
	}

	public void connect(ServerInfo target, Callback<Boolean> callback) {
	}

	public void connectNow(ServerInfo target) {
	}

	public void connect(ServerInfo info, final Callback<Boolean> callback, final boolean retry) {
	}

	public void disconnect(String reason) {
	}

	public void disconnect(BaseComponent... reason) {
	}

	public void disconnect(BaseComponent reason) {
	}

	public void disconnect0(final BaseComponent... reason) {
	}

	public void chat(String message) {
	}

	public void sendMessage(String message) {
	}

	public void sendMessages(String... messages) {
	}

	public void sendMessage(BaseComponent... message) {
	}

	public void sendMessage(BaseComponent message) {
	}

	public void sendMessage(ChatMessageType position, BaseComponent... message) {
	}

	public void sendMessage(ChatMessageType position, BaseComponent message) {
	}

	public void sendData(String channel, byte[] data) {
	}

	public InetSocketAddress getAddress() {
		return null;
	}

	public Collection<String> getGroups() {
		return null;
	}

	public void addGroups(String... groups) {
	}

	public void removeGroups(String... groups) {
	}

	public boolean hasPermission(String permission) {
		return false;
	}

	public void setPermission(String permission, boolean value) {
	}

	public Collection<String> getPermissions() {
		return null;
	}

	public String toString() {
		return null;
	}

	public Connection.Unsafe unsafe() {
		return null;
	}

	public String getUUID() {
		return null;
	}

	public UUID getUniqueId() {
		return null;
	}

	public void setSettings(ClientSettings settings) {
	}

	public Locale getLocale() {
		return null;
	}

	public boolean isForgeUser() {
		return false;
	}

	public Map<String, String> getModList() {
		return null;
	}

	public void setTabHeader(BaseComponent header, BaseComponent footer) {
	}

	public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
	}

	public void resetTabHeader() {
	}

	public void sendTitle(Title title) {
	}

	public String getExtraDataInHandshake() {
		return null;
	}

	public void setCompressionThreshold(int compressionThreshold) {
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public Server getServer() {
		return null;
	}
}
