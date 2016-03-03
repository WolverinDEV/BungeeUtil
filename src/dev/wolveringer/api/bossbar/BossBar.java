package dev.wolveringer.api.bossbar;

import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.Vector;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInFlying;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInLook;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPosition;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInPositionLook;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityMetadata;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityTeleport;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutSpawnEntityLiving;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.LivingEntityDataWatcher;
import dev.wolveringer.api.position.Location;

public class BossBar {
	Player p;
	int eid = 8000;
	String name = "null";
	float health =300F;
	Location loc;
	DataWatcher data = new DataWatcher();
	int add = 4;

	public BossBar(Player p) {
		this.loc = p.getLocation();
		this.p = p;
		PacketLib.addHandler(new PacketHandler<PacketPlayInPosition>() {
			@Override
			public void handle(PacketHandleEvent<PacketPlayInPosition> e) {
				if(e.getPlayer().equals(BossBar.this.p)){
					playerMove(e.getPacket());
				}
			}
		});
		PacketLib.addHandler(new PacketHandler<PacketPlayInPositionLook>() {
			@Override
			public void handle(PacketHandleEvent<PacketPlayInPositionLook> e) {
				if(e.getPlayer().equals(BossBar.this.p)){
					playerMove(e.getPacket());
				}
			}
		});
		PacketLib.addHandler(new PacketHandler<PacketPlayInLook>() {
			@Override
			public void handle(PacketHandleEvent<PacketPlayInLook> e) {
				if(e.getPlayer().equals(BossBar.this.p)){
					playerMove(e.getPacket());
				}
			}
		});
		LivingEntityDataWatcher watcher = data.getSpecialDataWatcher(LivingEntityDataWatcher.class);
		watcher.injektDefault();
		watcher.setCustomName(name);
		watcher.setCustomNameVisible(true);
		watcher.setInvisible(true);
		watcher.setParticelVisiable(false);
		//watcher.setParicelColor(0xFF);
		watcher.setHealth(health);

		watcher.setSprinting(false);
		watcher.setSneaking(false);

		spawn();
	}

	private void spawn() {
		PacketPlayOutSpawnEntityLiving e = new PacketPlayOutSpawnEntityLiving();
		e.setHeadRotation(0);
		e.setLocation(loc);
		e.setYaw(0);
		e.setPitch(0);
		e.setType((byte) 64);
		e.setVector(new Vector(0, 0, 0));
		e.setEntityID(eid);
		e.setMeta(data);
		p.sendPacket(e);
	}

	private void updateMeta() {
		p.sendPacket(new PacketPlayOutEntityMetadata(eid, data));
	}

	public void playerMove(PacketPlayInFlying packet) {
		this.loc = p.getLocation().add(0, add, 0);
		loc.setYaw(0F);
		loc.setPitch(0F);
		teleport();
	}

	private void teleport() {
		p.sendPacket(new PacketPlayOutEntityTeleport(eid, loc));
	}

	public void setName(String name) {
		this.name = name;
		data.setValue(2, name);
		updateMeta();
	}

	public void setHealth(float health) {
		this.health = health;
		data.setValue(6, health);
		updateMeta();
	}
}
