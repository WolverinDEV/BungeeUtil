package dev.wolveringer.api.bossbar;

import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutBossBar.Action;
import dev.wolveringer.api.bossbar.BossBarManager.BossBar;

public class BossBarListener implements PacketHandler<PacketPlayOutBossBar>{
	private static BossBarListener listener;
	public static void init(){
		if(listener == null)
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
