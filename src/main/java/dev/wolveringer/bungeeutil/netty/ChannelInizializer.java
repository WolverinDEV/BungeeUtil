package dev.wolveringer.bungeeutil.netty;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.naming.OperationNotSupportedException;

import dev.wolveringer.bungeeutil.BungeeUtil;
import dev.wolveringer.bungeeutil.player.connection.IIInitialHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.netty.PipelineUtils;

public abstract class ChannelInizializer extends ChannelInitializer<Channel> {
	private static ChannelInizializer init;

	public static ChannelInizializer getChannelInitializer(){
		return init;
	}

	@SuppressWarnings("deprecation")
	public static void init() {
		if(init == null) {
			setChannelInitializer(new BungeeUtilChannelInitializer<IIInitialHandler>(IIInitialHandler.class));
		}
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
			BungeeCord.getInstance().getConsole().sendMessage(ChatColor.RED+"An error happend while loading the ChannelInizializer. Message: "+e.getMessage());
			BungeeCord.getInstance().getConsole().sendMessage(ChatColor.RED+"For more details please enable the debug mode.");
			BungeeCord.getInstance().getConsole().sendMessage(ChatColor.RED+"Disabling ProtocolLIB");
		}
	}

	public static void setChannelInitializer(ChannelInizializer init) {
		BungeeUtil.getInstance().sendMessage(ChatColor.GREEN+"Set channel inizializer to "+init.getClass().getName());
		ChannelInizializer.init = init;
	}

	private static void setStaticFinalValue(Field f, Object n) throws Exception {
		f.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
		f.set(null, n);
	}

	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelInizializer.init.initialize(channel);
	}

	public abstract void initialize(Channel channel) throws Exception;
}
