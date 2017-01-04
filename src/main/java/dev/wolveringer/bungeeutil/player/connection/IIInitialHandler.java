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

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import javax.crypto.SecretKey;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.Callback;
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
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftDecoder;
import dev.wolveringer.bungeeutil.netty.WarpedMinecraftEncoder;
import dev.wolveringer.bungeeutil.player.ClientVersion;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.player.ProxiedPlayerUserConnection;
import dev.wolveringer.bungeeutil.plugin.Main;

public class IIInitialHandler extends IInitialHandler {
	private static int redifned_count = 0;
	private static ClassPool pool;
	static Class<?> base_class_connection;
	static Class<?> class_connection;
	private UserConnection conn;

	public static ClassPool pool() {
		if(pool != null)
			return pool;
		try{
			pool = ClassPool.getDefault();
			loadClassesFromJar(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()); //Load BungeeUtil in the system
		}catch (Exception e){
			e.printStackTrace();
		}
		return pool;
	}
	public static ClassLoader getClassLoader(){
		return IIInitialHandler.class.getClassLoader();
	}

	public static void addPlugin(Plugin plugin) {
		try{
			loadClassesFromJar(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void loadClassesFromJar(String path) {
		if(pool == null)
			throw new NullPointerException("Class Pool is null!");
		if(new File(path).isDirectory())
			return;
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
				if(!className.startsWith("com.ea"))
				pool.insertClassPath(new ClassClassPath(cl.loadClass(className)));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void init(Class<?> base) {
		if(base == ProxiedPlayerUserConnection.class){
			try{
				ClassPool cp = pool();
				cp.appendClassPath(new ClassClassPath(base));
				CtClass clazz = cp.get(base.getName());
				clazz.setName("ProxiedPlayerUserConnectionRedefined" + (redifned_count == 0 ? "" : redifned_count));
				clazz.setSuperclass(cp.get(UserConnection.class.getName()));
				base_class_connection = class_connection = clazz.toClass(getClassLoader());
				BungeeUtil.getInstance().sendMessage("§aInitialized base ProxiedPlayerUserConnection class");
				redifned_count++;
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			if(base_class_connection == null)
				throw new NullPointerException("Base class isn't init");
			if(!ProxiedPlayerUserConnection.class.isAssignableFrom(base))
				throw new RuntimeException("Class ("+base.getCanonicalName()+") isnt an instance of ProxiedPlayerUserConnection");
			try{
				ClassPool cp = pool();
				cp.appendClassPath(new ClassClassPath(base));
				CtClass clazz = cp.get(base.getName());
				CtClass super_class = cp.getCtClass(base_class_connection.getName()); //Get last redefined class
				if(super_class == null || super_class.getName() == null)
					throw new NullPointerException("Base class not found.");
				clazz.setSuperclass(super_class);
				clazz.setName("ProxiedPlayerUserConnectionRedefined_" + (redifned_count == 0 ? "" : redifned_count));
				class_connection = clazz.toClass(getClassLoader()); //Create the class
				BungeeUtil.getInstance().sendMessage("§aaInitialized extra ProxiedPlayerUserConnection class " + class_connection.getSuperclass());
				redifned_count++;
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public IIInitialHandler(ProxyServer instance, ListenerInfo listenerInfo, WarpedMinecraftDecoder a, WarpedMinecraftEncoder b) {
		super(instance, listenerInfo, a, b);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	protected void finish() {
		if(isOnlineMode()){
			ProxiedPlayer oldName = ProxyServer.getInstance().getPlayer(getName());
			if(oldName != null)
				oldName.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
			ProxiedPlayer oldID = ProxyServer.getInstance().getPlayer(getUniqueId());
			if(oldID != null)
				oldID.disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
		}else{
			ProxiedPlayer oldName = ProxyServer.getInstance().getPlayer(getName());
			if(oldName != null){
				disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
				return;
			}
		}
		set("offlineId", UUID.nameUUIDFromBytes(("OfflinePlayer:" + getName()).getBytes(com.google.common.base.Charsets.UTF_8)));
		if((UUID) get("uniqueId") == null){
			set("uniqueId", (UUID) get("offlineId"));
		}

		Callback<LoginEvent> complete = new Callback() {
			public void done(LoginEvent result, Throwable error) {
				if(result.isCancelled()){
					IIInitialHandler.this.disconnect(result.getCancelReason());
					return;
				}
				if(getChannel().isClosed()){
					return;
				}

				getChannel().getHandle().eventLoop().execute(new Runnable() {
					public void run() {
						if(getChannel().getHandle().isActive()){
							UserConnection userCon;
							try{
								userCon = (UserConnection) class_connection.getConstructor(ProxyServer.class, ChannelWrapper.class, String.class, InitialHandler.class).newInstance(BungeeCord.getInstance(), IIInitialHandler.this.getChannel(), IIInitialHandler.this.getName(), IIInitialHandler.this);
							}catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex){
								ex.printStackTrace();
								throw new RuntimeException();
							}
							
							userCon.setCompressionThreshold(BungeeCord.getInstance().config.getCompressionThreshold());
							userCon.init();
							conn = userCon;
							if(IIInitialHandler.this.getVersion() >= 5){
								IIInitialHandler.this.unsafe().sendPacket(new LoginSuccess(IIInitialHandler.this.getUniqueId().toString(), IIInitialHandler.this.getName()));
							}else{
								IIInitialHandler.this.unsafe().sendPacket(new LoginSuccess(IIInitialHandler.this.getUUID(), IIInitialHandler.this.getName()));
							}
							getChannel().setProtocol(net.md_5.bungee.protocol.Protocol.GAME);
							((HandlerBoss) getChannel().getHandle().pipeline().get(HandlerBoss.class)).setHandler(new UpstreamBridge(ProxyServer.getInstance(), userCon));
							
							ProxyServer.getInstance().getPluginManager().callEvent(new PostLoginEvent(userCon));
							
							ServerInfo server;
							if(ProxyServer.getInstance().getReconnectHandler() != null){
								server = ProxyServer.getInstance().getReconnectHandler().getServer(userCon);
							}else{
								server = net.md_5.bungee.api.AbstractReconnectHandler.getForcedHost(IIInitialHandler.this);
							}
							if(server == null){
								server = ProxyServer.getInstance().getServerInfo(((ListenerInfo) get("listener")).getDefaultServer());
							}

							userCon.connect(server, null, true);
						}

					}

				});
			}

			@Override
			public void done(Object o, Throwable t) {
				if(o instanceof LoginEvent)
					done((LoginEvent) o, t);
			}
		};
		ProxyServer.getInstance().getPluginManager().callEvent(new LoginEvent(this, complete));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handle(LoginRequest loginRequest) throws Exception {
		set("loginRequest", loginRequest);
		ClientVersion version = ClientVersion.fromProtocoll(getHandshake().getProtocolVersion());
		if(version == null || !version.getProtocollVersion().isSupported()){
			//disconnect(ProxyServer.getInstance().getTranslation("outdated_server", new Object[0]));
			disconnect("§cBungeeUtil cant handle your client version.");
			return;
		}

		if(getName().contains(".")){
			disconnect(ProxyServer.getInstance().getTranslation("name_invalid", new Object[] { getName() }));
			return;
		}

		if(getName().length() > 16){
			disconnect(ProxyServer.getInstance().getTranslation("name_too_long", new Object[] { getName() }));
			return;
		}

		int limit = BungeeCord.getInstance().config.getPlayerLimit();
		if((limit > 0) && (ProxyServer.getInstance().getOnlineCount() > limit)){
			disconnect(ProxyServer.getInstance().getTranslation("proxy_full", new Object[0]));
			return;
		}

		if((!isOnlineMode()) && (ProxyServer.getInstance().getPlayer(getUniqueId()) != null)){
			disconnect(ProxyServer.getInstance().getTranslation("already_connected", new Object[0]));
			return;
		}
		
		setProtocol(Protocol.LOGIN);
		Callback<PreLoginEvent> callback = new Callback() {
			@Override
			public void done(Object paramV, Throwable paramThrowable) {
				if(paramV instanceof PreLoginEvent)
					done((PreLoginEvent) paramV, paramThrowable);
			}

			public void done(PreLoginEvent result, Throwable error) {

				if(result.isCancelled()){
					IIInitialHandler.this.disconnect(result.getCancelReason());
					return;
				}
				if(getChannel().isClosed()){
					return;
				}
				if(isOnlineMode() == true){
					set("request", EncryptionUtil.encryptRequest());
					IIInitialHandler.this.unsafe().sendPacket((DefinedPacket) get("request"));
				}else{
					IIInitialHandler.this.finish();
				}
			}

		};
		ProxyServer.getInstance().getPluginManager().callEvent(new PreLoginEvent(this, callback));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void handle(EncryptionResponse encryptResponse) throws Exception {
		SecretKey sharedKey = EncryptionUtil.getSecret(encryptResponse, (EncryptionRequest) get("request"));
		BungeeCipher decrypt = EncryptionUtil.getCipher(false, sharedKey);
		((ChannelWrapper) get("ch")).addBefore("frame-decoder", "decrypt", new CipherDecoder(decrypt));
		BungeeCipher encrypt = EncryptionUtil.getCipher(true, sharedKey);
		((ChannelWrapper) get("ch")).addBefore("frame-prepender", "encrypt", new CipherEncoder(encrypt));

		String encName = URLEncoder.encode(getName(), "UTF-8");

		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		for(byte[] bit : new byte[][] { (((EncryptionRequest) get("request"))).getServerId().getBytes("ISO_8859_1"), sharedKey.getEncoded(), EncryptionUtil.keys.getPublic().getEncoded() })

		{
			sha.update(bit);
		}
		String encodedHash = URLEncoder.encode(new BigInteger(sha.digest()).toString(16), "UTF-8");

		String authURL = new StringBuilder().append("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=").append(encName).append("&serverId=").append(encodedHash).toString();

		Callback handler = new Callback<String>() {
			public void done(String result, Throwable error) {
				if(error == null){
					LoginResult obj = (LoginResult) BungeeCord.getInstance().gson.fromJson(result, LoginResult.class);
					if(obj != null){
						try{
							set("uniqueId", (UUID) Util.getUUID(obj.getId()));
							set("loginProfile", obj);
						}catch(Exception e){
							e.printStackTrace();
						}
						finish();
						return;
					}
					IIInitialHandler.this.disconnect("Not authenticated with Minecraft.net");
				}else{
					IIInitialHandler.this.disconnect(BungeeCord.getInstance().getTranslation("mojang_fail", new Object[0]));
					BungeeCord.getInstance().getLogger().log(Level.SEVERE, "Error authenticating " + IIInitialHandler.this.getName() + " with minecraft.net", error);

				}
			}
		};
		HttpClient.get(authURL, ((ChannelWrapper) get("ch")).getHandle().eventLoop(), handler);
	}
	
	@Override
	public void exception(Throwable t) throws Exception {
		super.exception(t);
	}

	@Override
	public Player getPlayer() {
		return (Player) ((ProxiedPlayer) conn);
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
}
