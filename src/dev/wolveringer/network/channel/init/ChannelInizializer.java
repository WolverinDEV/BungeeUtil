package dev.wolveringer.network.channel.init;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.netty.PipelineUtils;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.chat.ChatColor.ChatColorUtils;
import dev.wolveringer.network.IIInitialHandler;

public abstract class ChannelInizializer extends ChannelInitializer<Channel> {
	private static ChannelInizializer init;
	
	public static void setChannelInitializer(ChannelInizializer init) {
		Main.sendMessage("Set channel inizializer to "+init.getClass().getName());
		ChannelInizializer.init = init;
	}
	
	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelInizializer.init.initialize(channel);
	}
	
	public abstract void initialize(Channel channel) throws Exception;
	
	public static void init() {
		if(init == null)
			setChannelInitializer(new BungeeUtilChannelInit<IIInitialHandler>(IIInitialHandler.class));
		try {
			setStaticFinalValue(PipelineUtils.class.getDeclaredField("SERVER_CHILD"), new ChannelInizializer() {
				@Override
				public void initialize(Channel channel) throws Exception {
					throw new NullPointerException();
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"e"+ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+ChatColorUtils.COLOR_CHAR+"7] "+ChatColorUtils.COLOR_CHAR+"cError while loading ProtocolLIB "+ChatColorUtils.COLOR_CHAR+"4Code: 002");
			BungeeCord.getInstance().getConsole().sendMessage(ChatColorUtils.COLOR_CHAR+"e"+ChatColorUtils.COLOR_CHAR+"7[BungeeUntil"+ChatColorUtils.COLOR_CHAR+"7] "+ChatColorUtils.COLOR_CHAR+"cDisable ProtocolLIB");
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
