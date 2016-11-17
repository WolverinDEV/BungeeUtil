package dev.wolveringer.bungeeutil.bossbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import dev.wolveringer.bungeeutil.animations.inventory.LimetedScheduller;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar.BarColor;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutBossBar.BarDivision;
import dev.wolveringer.bungeeutil.player.Player;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BossBarManager {
	
	@Getter
	public static class BossBar {
		private static float steps = 0.01F;
		private BossBarManager manager;
		private UUID uuid;
		private PacketPlayOutBossBar.BarColor color;
		private PacketPlayOutBossBar.BarDivision division;
		private float health;
		private BaseComponent message;
		private boolean visiable;
		private LimetedScheduller curruntTask;
		
		private BossBar(BossBarManager manager) {
			this.manager = manager;
			uuid = UUID.randomUUID();
			color = BarColor.PING;
			division = BarDivision.TEN_DIVISIONS;
			health = 0.5F;
			message = TextComponent.fromLegacyText("§cundefined")[0];
		}
		
		public void setColor(PacketPlayOutBossBar.BarColor color) {
			this.color = color;
			if (visiable) this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.UPDATE_STYLE).setColor(color).setDivision(division));
		}
		
		public void setDivision(PacketPlayOutBossBar.BarDivision division) {
			this.division = division;
			if (visiable) this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.UPDATE_STYLE).setColor(color).setDivision(division));
		}
		
		public void setHealth(float value) {
			this.health = value;
			if (visiable) this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.UPDATE_HEALTH).setHealth(value));
		}
		
		public void setMessage(BaseComponent message) {
			this.message = message;
			if (visiable) this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.UPDATE_TITLE).setTitle(message));
		}
		
		public void display() {
			if (manager.getActiveBossBars().size() + 1 > manager.getBarLimit()) throw new RuntimeException("BossBar limit is reached!");
			if (visiable) return;
			visiable = true;
			this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.CREATE).setTitle(message).setHealth(health).setColor(color).setDivision(division));
		}
		
		public void hide() {
			if (!visiable) return;
			visiable = false;
			this.manager.player.sendPacket(new PacketPlayOutBossBar().setBarId(uuid).setAction(Action.DELETE));
		}
		
		public void dynamicChangeHealth(float health,int time,TimeUnit unit){
			if(curruntTask != null)
				curruntTask.stop();
			float diff = this.health-health;
			if(diff == 0)
				return;
			final float base = this.health;
			int stepCount = (int) (diff/steps);
			final float addPerStep = diff/stepCount;
			int millis = (int) unit.toMillis(time);
			int loopsTime = (int) (millis/stepCount);
			
			(curruntTask = new LimetedScheduller(millis,Math.abs(loopsTime),TimeUnit.MILLISECONDS) {
				@Override
				public void run(int count) {
					setHealth(base+(addPerStep*count));
				}
			}).start();
		}
		
		protected BossBar(BossBarManager manager, UUID uuid, BarColor color, BarDivision division, float value, BaseComponent message, boolean visiable) {
			this.manager = manager;
			this.uuid = uuid;
			this.color = color;
			this.division = division;
			this.health = value;
			this.message = message;
			this.visiable = visiable;
		}
	}
	
	private Player player;
	protected ArrayList<BossBar> bars = new ArrayList<>();
	private int limit = -1;
	
	public BossBarManager(Player player) {
		this.player = player;
	}
	
	public BossBar getBossBar(UUID uuid) {
		for (BossBar b : bars)
			if (b.uuid.equals(uuid)) return b;
		return null;
	}
	
	public int getBarLimit() {
		return limit == -1 ? Integer.MAX_VALUE : limit;
	}
	
	public void setBarLimit(int limit) {
		this.limit = limit;
	}
	
	public BossBar createNewBossBar() {
		BossBar _new = new BossBar(this);
		bars.add(_new);
		return _new;
	}
	
	public List<BossBar> getActiveBossBars() {
		List<BossBar> bars = new ArrayList<>();
		for (BossBar bar : this.bars)
			if (bar.isVisiable()) bars.add(bar);
		return Collections.unmodifiableList(bars);
	}
	
	public List<BossBar> getAllBossBars() {
		return Collections.unmodifiableList(bars);
	}
	
	public void deleteBossBar(BossBar bar) {
		if(bar == null)
			return;
		bars.remove(bar);
		bar.hide();
	}
}
