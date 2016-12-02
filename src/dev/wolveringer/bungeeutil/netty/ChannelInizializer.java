package dev.wolveringer.bungeeutil.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.naming.OperationNotSupportedException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.netty.PipelineUtils;
import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.chat.ChatColorUtils;
import dev.wolveringer.bungeeutil.player.connection.IIInitialHandler;

public abstract class ChannelInizializer extends ChannelInitializer<Channel> {
	private static ChannelInizializer init;
	
	public static ChannelInizializer getChannelInitializer(){
		return init;
	}
	
	public static void setChannelInitializer(ChannelInizializer init) {
		BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Set channel inizializer to "+init.getClass().getName());
		ChannelInizializer.init = init;
	}
	
	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelInizializer.init.initialize(channel);
	}
	
	public abstract void initialize(Channel channel) throws Exception;
	
	public static void init() {
		if(init == null)
			setChannelInitializer(new BungeeUtilChannelInizializer<IIInitialHandler>(IIInitialHandler.class));
		try {
			setStaticFinalValue(PipelineUtils.class.getDeclaredField("SERVER_CHILD"), new ChannelInizializer() {
				@Override
				public void initialize(Channel channel) throws Exception {
					throw new OperationNotSupportedException();
				}
			});
		}
		catch (Exception e) {
			BungeeUtil.debug(e);
			BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"cAn error happend while loading the ChannelInizializer. Message: "+e.getMessage());
			BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"cFor more details please enable the debug mode.");
			BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"cDisabling ProtocolLIB");
		}
	}
	
	private static void setStaticFinalValue(Field f, Object n) throws Exception {
		f.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
		f.set(null, n);
	}
}
