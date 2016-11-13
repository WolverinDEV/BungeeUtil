package dev.wolveringer.bungeeutil.netty;

import io.netty.channel.Channel;

import java.lang.reflect.Field;
import java.util.Collection;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.protocol.MinecraftDecoder;
import net.md_5.bungee.protocol.MinecraftEncoder;
import net.md_5.bungee.protocol.Protocol;

public class NettyUtil {
	@SuppressWarnings("unchecked")
	public static Collection<Channel> getChannels() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field f = BungeeCord.getInstance().getClass().getField("saveThread");
		f.setAccessible(true);
		return (Collection<Channel>) f.get(BungeeCord.class);
	}

	public static Protocol getProtocol(Object o) {
		try{
			if(o == null)
				throw new RuntimeException("Objekt is null");
			Field f = (o instanceof MinecraftDecoder ? MinecraftDecoder.class : MinecraftEncoder.class).getDeclaredField("protocol");
			f.setAccessible(true);
			return (Protocol) f.get(o);
		}catch (NoSuchFieldException e){
			return Protocol.HANDSHAKE;
		}catch (Exception e){
			e.printStackTrace();
			return Protocol.HANDSHAKE;
		}
	}
}