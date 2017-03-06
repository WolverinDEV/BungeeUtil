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
import net.md_5.bungee.api.ChatColor;
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
			this.uuid = UUID.randomUUID();
			this.color = BarColor.PING;
			this.division = BarDivision.TEN_DIVISIONS;
			this.health = 0.5F;
			this.message = TextComponent.fromLegacyText(ChatColor.RED+"undefined")[0];
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

		public void display() {
			if (this.manager.getActiveBossBars().size() + 1 > this.manager.getBarLimit()) {
				throw new RuntimeException("BossBar limit is reached!");
			}
			if (this.visiable) {
				return;
			}
			this.visiable = true;
			this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.CREATE).color(this.color).division(this.division).health(this.health).title(this.message).build());
		}

		public void dynamicChangeHealth(float health,int time,TimeUnit unit){
			if(this.curruntTask != null) {
				this.curruntTask.stop();
			}
			float diff = this.health-health;
			if(diff == 0) {
				return;
			}
			final float base = this.health;
			int stepCount = (int) (diff/steps);
			final float addPerStep = diff/stepCount;
			int millis = (int) unit.toMillis(time);
			int loopsTime = millis/stepCount;

			(this.curruntTask = new LimetedScheduller(millis,Math.abs(loopsTime),TimeUnit.MILLISECONDS) {
				@Override
				public void run(int count) {
					BossBar.this.setHealth(base+addPerStep*count);
				}
			}).start();
		}

		public void hide() {
			if (!this.visiable) {
				return;
			}
			this.visiable = false;
			this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.DELETE).build());
		}

		public void setColor(PacketPlayOutBossBar.BarColor color) {
			this.color = color;
			if (this.visiable) {
				this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.UPDATE_STYLE).color(color).division(this.division).build());
			}
		}

		public void setDivision(PacketPlayOutBossBar.BarDivision division) {
			this.division = division;
			if (this.visiable) {
				this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.UPDATE_STYLE).color(this.color).division(division).build());
			}
		}

		public void setHealth(float value) {
			this.health = value;
			if (this.visiable) {
				this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.UPDATE_HEALTH).health(value).build());
			}
		}

		public void setMessage(BaseComponent message) {
			this.message = message;
			if (this.visiable) {
				this.manager.player.sendPacket(PacketPlayOutBossBar.builder().barId(this.uuid).action(Action.UPDATE_TITLE).title(message).build());
			}
		}
	}

	private Player player;
	protected ArrayList<BossBar> bars = new ArrayList<>();
	private int limit = -1;

	public BossBarManager(Player player) {
		this.player = player;
	}

	public BossBar createNewBossBar() {
		BossBar _new = new BossBar(this);
		this.bars.add(_new);
		return _new;
	}

	public void deleteBossBar(BossBar bar) {
		if(bar == null) {
			return;
		}
		this.bars.remove(bar);
		bar.hide();
	}

	public List<BossBar> getActiveBossBars() {
		List<BossBar> bars = new ArrayList<>();
		for (BossBar bar : this.bars) {
			if (bar.isVisiable()) {
				bars.add(bar);
			}
		}
		return Collections.unmodifiableList(bars);
	}

	public List<BossBar> getAllBossBars() {
		return Collections.unmodifiableList(this.bars);
	}

	public int getBarLimit() {
		return this.limit == -1 ? Integer.MAX_VALUE : this.limit;
	}

	public BossBar getBossBar(UUID uuid) {
		for (BossBar b : this.bars) {
			if (b.uuid.equals(uuid)) {
				return b;
			}
		}
		return null;
	}

	public void setBarLimit(int limit) {
		this.limit = limit;
	}
}
