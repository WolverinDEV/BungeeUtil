package dev.wolveringer.bungeeutil.bossbar;

import dev.wolveringer.bungeeutil.Configuration;
import dev.wolveringer.bungeeutil.bossbar.BossBarManager.BossBar;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketHandler;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar.Action;
import dev.wolveringer.bungeeutil.player.Player;

public class BossBarListener implements PacketHandler<PacketPlayOutBossBar>{
	private static BossBarListener listener;
	public static void init(){
		if(listener == null && Configuration.isBossBarhandleEnabled())
			PacketLib.addHandler(listener = new BossBarListener()); 
	}
	
	@Override
	public void handle(PacketHandleEvent<PacketPlayOutBossBar> e) {
		Player player = e.getPlayer();
		BossBarManager manager = player.getBossBarManager();
		BossBar bar = null;
		if(e.getPacket().getAction() != Action.CREATE){
			bar = manager.getBossBar(e.getPacket().getBarId());
			if(bar == null || !bar.isVisiable()) //Bar hidden / removed. Client dont need to get them
				e.setCancelled(true);
		}
		
		switch (e.getPacket().getAction()) {
			case CREATE:
				manager.bars.add(new BossBar(manager, e.getPacket().getBarId(), e.getPacket().getColor(), e.getPacket().getDivision(), e.getPacket().getHealth(), e.getPacket().getTitle(), true));
				break;
			case DELETE:
				if(bar != null)
					manager.bars.remove(bar);
				break;
			case UPDATE_HEALTH:
				if(bar != null){
					bar.setHealth(e.getPacket().getHealth());
				}
				break;
			case UPDATE_STYLE:
				if(bar != null){
					bar.setColor(e.getPacket().getColor());
					bar.setDivision(e.getPacket().getDivision());
				}
				break;
			case UPDATE_TITLE:
				if(bar != null){
					bar.setMessage(e.getPacket().getTitle());
				}
				break;
			default:
				break;
		}
	}
}
