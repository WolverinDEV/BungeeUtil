package dev.wolveringer.chat;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import dev.wolveringer.BungeeUtil.BungeeUtil;
import dev.wolveringer.BungeeUtil.Main;
import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInChat;

public class PlayerChatListener implements PacketHandler<Packet> {
	static{
		PacketLib.addHandler(instance = new PlayerChatListener());
	}

	private static final PlayerChatListener instance;
	private HashMap<String, IChatBaseComponent> chats = new HashMap<String, IChatBaseComponent>();
	private HashMap<String, Date> out = new HashMap<String, Date>();

	public PlayerChatListener() {
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtil.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				while (true){
					try{
						Thread.sleep(30 * 1000);
					}catch (InterruptedException e){
					}
					Date last = new Date();
					last = new Date(last.getTime() - TimeUnit.MINUTES.toMillis(5));
					for(String key : out.keySet())
						if(out.get(key).before(last)){
							chats.remove(key);
							out.remove(key);
						}
				}
			}
		});
	}

	@Override
	public void handle(PacketHandleEvent<Packet> e) {
		if(e.getPacket() instanceof PacketPlayInChat){
			String message;
			ChatClickListener listener;
			if(chats.containsKey(message = ((PacketPlayInChat) e.getPacket()).getMessage()))
				if((listener = chats.get(message).run(message)) != null)
					listener.click(e.getPlayer());
		}
	}

	public static void addListener(IChatBaseComponent comp) {
		Date date = new Date();
		for(String s : comp.getClickSignature()){
			instance.out.put(s, date);
			instance.chats.put(s, comp);
		}
	}
}
