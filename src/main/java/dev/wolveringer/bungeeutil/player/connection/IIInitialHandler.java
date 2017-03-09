package dev.wolveringer.bungeeutil.player.connection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import javax.crypto.SecretKey;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftDecoder;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftEncoder;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.ProxiedPlayerUserConnection;
import dev.wolveringer.bungeeutil.plugin.Main;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.connection.UpstreamBridge;
import net.md_5.bungee.http.HttpClient;
import net.md_5.bungee.jni.cipher.BungeeCipher;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.HandlerBoss;
import net.md_5.bungee.netty.cipher.CipherDecoder;
import net.md_5.bungee.netty.cipher.CipherEncoder;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.LoginSuccess;

public class IIInitialHandler extends IInitialHandler {
	private static int redifned_count = 0;
	private static ClassPool pool;
	static Class<?> base_class_connection;
	static Class<?> class_connection;
	public static void addPlugin(Plugin plugin) {
		try{
			loadClassesFromJar(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static ClassLoader getClassLoader(){
		return IIInitialHandler.class.getClassLoader();
	}
	@SuppressWarnings("deprecation")
	public static void init(Class<?> base) {
		if(base == ProxiedPlayerUserConnection.class){
			try{
				ClassPool cp = pool();
				cp.appendClassPath(new ClassClassPath(base));
				CtClass clazz = cp.get(base.getName());
				clazz.setName("ProxiedPlayerUserConnectionRedefined" + (redifned_count == 0 ? "" : redifned_count));
				clazz.setSuperclass(cp.get(UserConnection.class.getName()));
				base_class_connection = class_connection = clazz.toClass(getClassLoader());
				BungeeUtil.getInstance().sendMessage(ChatColor.COLOR_CHAR+"aInitialized base ProxiedPlayerUserConnection class");
				redifned_count++;
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			if(base_class_connection == null) {
				throw new NullPointerException("Base class isn't init");
			}
			if(!ProxiedPlayerUserConnection.class.isAssignableFrom(base)) {
				throw new RuntimeException("Class ("+base.getCanonicalName()+") isnt an instance of ProxiedPlayerUserConnection");
			}
			try{
				ClassPool cp = pool();
				cp.appendClassPath(new ClassClassPath(base));
				CtClass clazz = cp.get(base.getName());
				CtClass super_class = cp.getCtClass(base_class_connection.getName()); //Get last redefined class
				if(super_class == null || super_class.getName() == null) {
					throw new NullPointerException("Base class not found.");
				}
				clazz.setSuperclass(super_class);
				clazz.setName("ProxiedPlayerUserConnectionRedefined_" + (redifned_count == 0 ? "" : redifned_count));
				class_connection = clazz.toClass(getClassLoader()); //Create the class
				BungeeUtil.getInstance().sendMessage(ChatColor.COLOR_CHAR+"aaInitialized extra ProxiedPlayerUserConnection class " + class_connection.getSuperclass());
				redifned_count++;
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	private static void loadClassesFromJar(String path) {
		if(pool == null) {
			throw new NullPointerException("Class Pool is null!");
		}
		if(new File(path).isDirectory()) {
			return;
		}
		try{
			JarFile jarFile = new JarFile(path);
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + path + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls, IIInitialHandler.class.getClassLoader());
			while (e.hasMoreElements()){
				JarEntry je = e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class")){ //isnt a class file
					continue;
				}
				String className = je.getName().substring(0, je.getName().length() - 6);
				className = className.replace('/', '.');
				if(!className.startsWith("com.ea") && 
						!className.startsWith("dev.wolveringer.bungeeutil.plugin.bukkit")) {
					pool.insertClassPath(new ClassClassPath(cl.loadClass(className)));
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static ClassPool pool() {
		if(pool != null) {
			return pool;
		}
		try{
			pool = ClassPool.getDefault();
			loadClassesFromJar(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()); //Load BungeeUtil in the system
		}catch (Exception e){
			e.printStackTrace();
		}
		return pool;
	}

	private UserConnection conn;

	public IIInitialHandler(ProxyServer instance, ListenerInfo listenerInfo, WarpedMinecraftDecoder a, WarpedMinecraftEncoder b) {
		super(instance, listenerInfo, a, b);
	}

	@Override
	public void exception(Throwable t) throws Exception {
		super.exception(t);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	protected void finish() {
		if(this.isOnlineMode()){
			ProxiedPlayer oldName = ProxyServer.getInstance().getPlayer(this.getName());
			if(oldName != null) {
				oldName.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
			}
			ProxiedPlayer oldID = ProxyServer.getInstance().getPlayer(this.getUniqueId());
			if(oldID != null) {
				oldID.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
			}
		}else{
			ProxiedPlayer oldName = ProxyServer.getInstance().getPlayer(this.getName());
			if(oldName != null){
				this.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
				return;
			}
		}
		this.set("offlineId", UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.getName()).getBytes(com.google.common.base.Charsets.UTF_8)));
		if((UUID) this.get("uniqueId") == null){
			this.set("uniqueId", this.get("offlineId"));
		}

		Callback<LoginEvent> complete = new Callback() {
			public void done(LoginEvent result, Throwable error) {
				if(result.isCancelled()){
					IIInitialHandler.this.disconnect(result.getCancelReason());
					return;
				}
				if(IIInitialHandler.this.getChannel().isClosed()){
					return;
				}

				IIInitialHandler.this.getChannel().getHandle().eventLoop().execute(() -> {
					if(IIInitialHandler.this.getChannel().getHandle().isActive()){
						UserConnection userCon;
						try{
							userCon = (UserConnection) class_connection.getConstructor(ProxyServer.class, ChannelWrapper.class, String.class, InitialHandler.class).newInstance(BungeeCord.getInstance(), IIInitialHandler.this.getChannel(), IIInitialHandler.this.getName(), IIInitialHandler.this);
						}catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex){
							ex.printStackTrace();
							throw new RuntimeException();
						}

						userCon.setCompressionThreshold(BungeeCord.getInstance().config.getCompressionThreshold());
						userCon.init();
						IIInitialHandler.this.conn = userCon;
						if(IIInitialHandler.this.getVersion() >= 5){
							IIInitialHandler.this.unsafe().sendPacket(new LoginSuccess(IIInitialHandler.this.getUniqueId().toString(), IIInitialHandler.this.getName()));
						}else{
							IIInitialHandler.this.unsafe().sendPacket(new LoginSuccess(IIInitialHandler.this.getUUID(), IIInitialHandler.this.getName()));
						}
						IIInitialHandler.this.getChannel().setProtocol(net.md_5.bungee.protocol.Protocol.GAME);
						IIInitialHandler.this.getChannel().getHandle().pipeline().get(HandlerBoss.class).setHandler(new UpstreamBridge(ProxyServer.getInstance(), userCon));

						ProxyServer.getInstance().getPluginManager().callEvent(new PostLoginEvent(userCon));

						ServerInfo server;
						if(ProxyServer.getInstance().getReconnectHandler() != null){
							server = ProxyServer.getInstance().getReconnectHandler().getServer(userCon);
						}else{
							server = net.md_5.bungee.api.AbstractReconnectHandler.getForcedHost(IIInitialHandler.this);
						}
						if(server == null){
							server = ProxyServer.getInstance().getServerInfo(((ListenerInfo) IIInitialHandler.this.get("listener")).getDefaultServer());
						}

						userCon.connect(server, null, true);
					}

				});
			}

			@Override
			public void done(Object o, Throwable t) {
				if(o instanceof LoginEvent) {
					this.done((LoginEvent) o, t);
				}
			}
		};
		ProxyServer.getInstance().getPluginManager().callEvent(new LoginEvent(this, complete));
	}

	protected Object get(String a) {
		Field f = null;
		try{
			f = InitialHandler.class.getDeclaredField(a);
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

	@Override
	public Player getPlayer() {
		return (Player) this.conn;
	}

	@Override
	public void handle(EncryptionResponse encryptResponse) throws Exception {
		SecretKey sharedKey = EncryptionUtil.getSecret(encryptResponse, (EncryptionRequest) this.get("request"));
		BungeeCipher decrypt = EncryptionUtil.getCipher(false, sharedKey);
		this.getChannel().addBefore("frame-decoder", "decrypt", new CipherDecoder(decrypt));
		
		BungeeCipher encrypt = EncryptionUtil.getCipher(true, sharedKey);
		this.getChannel().addBefore("frame-prepender", "encrypt", new CipherEncoder(encrypt));

		String encName = URLEncoder.encode(this.getName(), "UTF-8");

		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		
		for(byte[] bit : new byte[][] { ((EncryptionRequest) this.get("request")).getServerId().getBytes("ISO_8859_1"), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded() }) {
			sha.update(bit);
		}
		
		String encodedHash = URLEncoder.encode(new BigInteger(sha.digest()).toString(16), "UTF-8");

		String authURL = new StringBuilder().append("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=").append(encName).append("&serverId=").append(encodedHash).toString();

		Callback<String> handler = (result, error) -> {
			if(error == null){
				LoginResult obj = (LoginResult) BungeeCord.getInstance().gson.fromJson(result, LoginResult.class);
				if(obj != null){
					try{
						IIInitialHandler.this.set("uniqueId", Util.getUUID(obj.getId()));
						IIInitialHandler.this.set("loginProfile", obj);
					}catch(Exception e){
						e.printStackTrace();
					}
					IIInitialHandler.this.finish();
					return;
				}
				IIInitialHandler.this.disconnect("Not authenticated with Minecraft.net");
			}else{
				IIInitialHandler.this.disconnect(BungeeCord.getInstance().getTranslation("mojang_fail", new Object[0]));
				BungeeCord.getInstance().getLogger().log(Level.SEVERE, "Error authenticating " + IIInitialHandler.this.getName() + " with minecraft.net", error);

			}
		};
		HttpClient.get(authURL, ((ChannelWrapper) this.get("ch")).getHandle().eventLoop(), handler);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handle(LoginRequest loginRequest) throws Exception {
		this.set("loginRequest", loginRequest);
		ClientVersion version = ClientVersion.fromProtocoll(this.getHandshake().getProtocolVersion());
		if(version == null || !version.getProtocollVersion().isSupported()){
			//disconnect(ProxyServer.getInstance().getTranslation("outdated_server", new Object[0]));
			this.disconnect(ChatColor.COLOR_CHAR+"cBungeeUtil cant handle your client version.");
			return;
		}

		if(this.getName().contains(".")){
			this.disconnect(ProxyServer.getInstance().getTranslation("name_invalid", new Object[] { this.getName() }));
			return;
		}

		if(this.getName().length() > 16){
			this.disconnect(ProxyServer.getInstance().getTranslation("name_too_long", new Object[] { this.getName() }));
			return;
		}

		int limit = BungeeCord.getInstance().config.getPlayerLimit();
		if(limit > 0 && ProxyServer.getInstance().getOnlineCount() > limit){
			this.disconnect(ProxyServer.getInstance().getTranslation("proxy_full", new Object[0]));
			return;
		}

		if(!this.isOnlineMode() && ProxyServer.getInstance().getPlayer(this.getUniqueId()) != null){
			this.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
			return;
		}

		this.setProtocol(Protocol.LOGIN);
		Callback<PreLoginEvent> callback = new Callback() {
			@Override
			public void done(Object paramV, Throwable paramThrowable) {
				if(paramV instanceof PreLoginEvent) {
					this.done((PreLoginEvent) paramV, paramThrowable);
				}
			}

			@SuppressWarnings("deprecation")
			public void done(PreLoginEvent result, Throwable error) {

				if(result.isCancelled()){
					IIInitialHandler.this.disconnect(result.getCancelReason());
					return;
				}
				if(IIInitialHandler.this.getChannel().isClosed()){
					return;
				}
				if(IIInitialHandler.this.isOnlineMode() == true){
					IIInitialHandler.this.set("request", EncryptionUtil.encryptRequest());
					IIInitialHandler.this.unsafe().sendPacket((DefinedPacket) IIInitialHandler.this.get("request"));
				}else{
					IIInitialHandler.this.finish();
				}
			}

		};
		ProxyServer.getInstance().getPluginManager().callEvent(new PreLoginEvent(this, callback));
	}

	protected void set(String a, Object b) {
		Field f = null;
		try{
			f = InitialHandler.class.getDeclaredField(a);
		}catch (NoSuchFieldException | SecurityException e){
			e.printStackTrace();
		}
		f.setAccessible(true);
		try{
			f.set(this, b);
		}catch (IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
	}
}
