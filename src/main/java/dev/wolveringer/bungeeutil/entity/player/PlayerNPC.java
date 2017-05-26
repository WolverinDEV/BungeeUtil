package dev.wolveringer.bungeeutil.entity.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import dev.wolveringer.bungeeutil.entity.datawatcher.HumanEntityDataWatcher;
import dev.wolveringer.bungeeutil.packetlib.PacketHandleEvent;
import dev.wolveringer.bungeeutil.packetlib.PacketHandler;
import dev.wolveringer.bungeeutil.packetlib.PacketLib;
import dev.wolveringer.bungeeutil.packets.Packet;
import dev.wolveringer.bungeeutil.packets.PacketPlayInUseEntity;
import dev.wolveringer.bungeeutil.packets.PacketPlayInUseEntity.Action;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityDestroy;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityHeadRotation;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutEntityTeleport;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutSpawnPlayer;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerInfo;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.bungeeutil.packets.PacketPlayOutScoreboardTeam.NameTag;
import dev.wolveringer.bungeeutil.packets.types.PacketPlayOut;
import dev.wolveringer.bungeeutil.player.Player;
import dev.wolveringer.bungeeutil.position.Location;
import dev.wolveringer.bungeeutil.profile.GameProfile;
import dev.wolveringer.bungeeutil.profile.PlayerInfoData;
import dev.wolveringer.bungeeutil.profile.Skin;
import net.md_5.bungee.api.chat.BaseComponent;

class NameSpliter {
	private String orginal;
	private String main, prefix, suffix;

	public String getMain() {
		return this.main;
	}

	public String getOrginal() {
		return this.orginal;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	private void recalculate() {
		if (this.orginal.length() > 16) {
			this.prefix = this.orginal.length() > 30 ? this.orginal.substring(0, 15) : "";
			this.main = this.orginal.length() > 30 ? this.orginal.substring(15, this.orginal.length() < 30 ? this.orginal.length() : 30) : this.orginal.substring(0, 15);
			this.suffix = this.orginal.length() > 30 ? this.orginal.substring(30, this.orginal.length() > 45 ? 45 : this.orginal.length()) : this.orginal.substring(15, this.orginal.length() > 30 ? 30 : this.orginal.length());
		}
		else {
			this.prefix = this.suffix = "";
			this.main = this.orginal;
		}
	}

	public void setOrginal(String orginal) {
		this.orginal = orginal;
		this.recalculate();
	}
}

public final class PlayerNPC {
	private static final Random RND_NAME = new Random();
	private static ArrayList<String> base_names = new ArrayList<>();
	private static int npc_count = 1000;
	private PacketPlayOutEntityDestroy p2;

	private int ID;
	private MultiVersionPlayerDatawatcher datawatcher = new MultiVersionPlayerDatawatcher();

	private ArrayList<Player> viewer = new ArrayList<Player>();
	private NameSpliter name;
	private String base_name;
	private Location location;

	private int ping = 20;

	private GameProfile profile = new GameProfile(UUID.randomUUID(), "");

	private boolean tab;

	private ArrayList<InteractListener> listener = new ArrayList<InteractListener>();

	private BaseComponent tabname = null;

	private PacketHandler<Packet> handler;

	private Equipment equipment = new Equipment(this);

	public PlayerNPC() {
		this((System.currentTimeMillis() + "_0x" + Integer.toHexString(RND_NAME.nextInt()) + "00000000000000000").substring(0, 16));
	}

	public PlayerNPC(String base_name) {
		if (base_name.length() > 16) {
			throw new NullPointerException("Base Name cant be longer than 16!");
		}
		if (base_names.contains(base_name)) {
			throw new IllegalArgumentException("Base name is alredy in use!");
		}
		base_names.add(base_name);
		this.ID = npc_count++;
		this.base_name = base_name;

		this.name = new NameSpliter();
		this.name.setOrginal(base_name);

		this.profile.setName(base_name);
		this.location = new Location(0, 0, 0);
		this.handler = new PacketHandler<Packet>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(PacketHandleEvent<Packet> e) {
				if (e.getPacket() instanceof PacketPlayInUseEntity) {
					PacketPlayInUseEntity packet = (PacketPlayInUseEntity) e.getPacket();
					if (packet.getTarget() == PlayerNPC.this.ID) {
						if (packet.getAction() == Action.ATTACK) {
							for (InteractListener listener : (List<InteractListener>) PlayerNPC.this.listener.clone()) {
								listener.leftClick(e.getPlayer());
							}
						} else if (packet.getAction() == Action.INTERACT_AT && packet.getHand() == 0) {
							for (InteractListener listener : (List<InteractListener>) PlayerNPC.this.listener.clone()) {
								listener.rightClick(e.getPlayer());
							}
						}
						e.setCancelled(true);
					}
				}
			}
		};
		this.datawatcher.injektDefault();
		PacketLib.addHandler(this.handler);
		this.rebuild();
	}

	public void addListener(InteractListener listener) {
		this.listener.add(listener);
	}

	private void broadcastPacket(Packet p) {
		for (Player pl : this.viewer) {
			pl.sendPacket((PacketPlayOut) p);
		}
	}

	public boolean canSee(Player p) {
		return this.viewer.contains(p);
	}

	public void delete() {
		for (Player p : this.viewer) {
			this.sendDestroy(p);
		}
		this.viewer.clear();
		try {
			this.finalize();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		PacketLib.removeHandler(this.handler);
		base_names.remove(this.base_name);
		super.finalize();
	}

	public HumanEntityDataWatcher getDatawatcher() {
		return this.datawatcher;
	}

	public int getEntityID() {
		return this.ID;
	}

	public Equipment getEquipment() {
		return this.equipment;
	}

	public Location getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name.getOrginal();
	}

	public int getPing() {
		return this.ping;
	}

	public BaseComponent getPlayerListName() {
		return this.tabname;
	}

	public GameProfile getProfile() {
		return this.profile;
	}

	protected ArrayList<Player> getViewer() {
		return this.viewer;
	}

	private void hide(Player p) {
		this.viewer.remove(p);
		this.sendDestroy(p);
	}

	public boolean isTabListed() {
		return this.tab;
	}

	protected void rebuild() {
		if (this.profile == null) {
			System.err.print("NPC Profile is null. it will crash the Client");
			this.profile = new GameProfile(UUID.randomUUID(), this.name.getMain());
		}
		this.p2 = new PacketPlayOutEntityDestroy(new int[]{this.ID});
	}

	public void removeListener(InteractListener listener) {
		this.listener.remove(listener);
	}

	private void sendDestroy(Player p) {
		if (this.tab) {
			p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(this.profile, this.ping, 0, (BaseComponent) null)));
		}
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
		team.setTeam(this.base_name);
		team.setAction(PacketPlayOutScoreboardTeam.Action.REMOVE);
		p.sendPacket(team);
		p.sendPacket(this.p2);
	}

	private void sendSpawn(final Player p) {
		p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
		p.sendPacket(new PacketPlayOutSpawnPlayer(this.ID, this.profile, this.location, 0, this.datawatcher.getWatcher(p.getVersion().getBigVersion())));

		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
		team.setTeam(this.base_name);
		team.setDisplayName("Error (NCP-Team): 002");
		team.setAction(PacketPlayOutScoreboardTeam.Action.CREATE);
		team.setPrefix(this.name.getPrefix());
		team.setSuffix(this.name.getSuffix());
		team.setFriendlyFire(0);
		team.setPlayer(new String[] { this.profile.getName() });
		team.setTag(NameTag.VISIABLE);
		p.sendPacket(team);

		p.sendPacket(new PacketPlayOutEntityHeadRotation(this.ID, p.getLocation().getYaw()));

		if (!this.tab) {
			p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
		} else {
			p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
		}
		this.equipment.sendItems(p);
	}

	public void setLocation(Location location) {
		this.location = location;
		this.rebuild();
		this.broadcastPacket(new PacketPlayOutEntityTeleport(location, this.ID, false));
		this.broadcastPacket(new PacketPlayOutEntityHeadRotation(this.ID, location.getYaw()));
	}

	public void setName(String name) {
		if (name.length() > 48) {
			throw new NullPointerException(name.length() + " > 48");
		}
		this.name.setOrginal(name);
		this.profile.setName(this.name.getMain());
		this.rebuild();
		for (Player p : this.viewer) {
			this.sendDestroy(p);
			this.sendSpawn(p);
		}
	}

	public void setPing(int ping) {
		this.ping = ping;
		if (this.tab) {
			this.broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_PING, new PlayerInfoData(this.profile, ping, 0, this.tabname)));
		}
	}

	public void setPlayerListName(BaseComponent name) {
		this.tabname = name;
		this.broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
	}

	public void setProfile(GameProfile profile) {
		this.profile = profile;
		if (this.profile == null) {
			this.profile = new GameProfile(UUID.randomUUID(), this.name.getMain());
		}
		if (this.profile.getName() == null) {
			System.err.print("Name of the GameProfile cant be null");
			this.profile.setName("error");
		}
		this.rebuild();
		ArrayList<Player> player = new ArrayList<>(this.viewer);
		for (Player p : player) {
			this.hide(p);
			this.show(p);
		}
	}

	public void setSkin(Skin skin) {
		skin.applay(this.profile);
		this.setProfile(this.profile);
	}

	public void setTabListed(boolean listed) {
		if (this.tab != listed) {
			if (this.tab) {
				this.broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
			} else {
				this.broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(this.profile, this.ping, 0, this.tabname)));
			}
		}
		this.tab = listed;
	}

	public void setVisiable(Player p, boolean b) {
		if (b) {
			if (!this.viewer.contains(p)) {
				this.show(p);
			} else {
				;
			}
		} else if (this.viewer.contains(p)) {
			this.hide(p);
		}
	}

	private void show(Player p) {
		this.viewer.add(p);
		this.sendSpawn(p);
	}
}
