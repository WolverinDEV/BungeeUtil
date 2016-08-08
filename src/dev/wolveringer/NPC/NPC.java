package dev.wolveringer.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import dev.wolveringer.BungeeUtil.ClientVersion.BigClientVersion;
import dev.wolveringer.BungeeUtil.PacketHandleEvent;
import dev.wolveringer.BungeeUtil.PacketHandler;
import dev.wolveringer.BungeeUtil.PacketLib;
import dev.wolveringer.BungeeUtil.Player;
import dev.wolveringer.BungeeUtil.gameprofile.GameProfile;
import dev.wolveringer.BungeeUtil.gameprofile.PlayerInfoData;
import dev.wolveringer.BungeeUtil.gameprofile.Skin;
import dev.wolveringer.BungeeUtil.gameprofile.SkinFactory;
import dev.wolveringer.BungeeUtil.packets.Packet;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInUseEntity;
import dev.wolveringer.BungeeUtil.packets.PacketPlayInUseEntity.Action;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityDestroy;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityHeadRotation;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutEntityTeleport;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutNamedEntitySpawn;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerInfo;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam;
import dev.wolveringer.BungeeUtil.packets.PacketPlayOutScoreboardTeam.NameTag;
import dev.wolveringer.BungeeUtil.packets.Abstract.PacketPlayOut;
import dev.wolveringer.api.datawatcher.DataWatcher;
import dev.wolveringer.api.datawatcher.HumanDataWatcher;
import dev.wolveringer.api.position.Location;
import dev.wolveringer.chat.IChatBaseComponent;

public final class NPC {
	private static final Random RND_NAME = new Random();
	private static ArrayList<String> base_names = new ArrayList<>();
	private static int npc_count = 1000;
	private PacketPlayOutEntityDestroy p2;
	
	private int ID;
	private NPCData datawatcher = new NPCData();
	
	private ArrayList<Player> viewer = new ArrayList<Player>();
	private NameSpliter name;
	private String base_name;
	private Location location;
	
	private int ping = 20;
	
	private GameProfile profile = new GameProfile(UUID.randomUUID(), "");
	
	private boolean tab;
	
	private ArrayList<InteractListener> listener = new ArrayList<InteractListener>();
	
	private IChatBaseComponent tabname = null;
	
	private PacketHandler<Packet> handler;
	
	private Equipment equipment = new Equipment(this);
	
	public NPC() {
		this((System.currentTimeMillis() + "_0x" + Integer.toHexString(RND_NAME.nextInt()) + "00000000000000000").substring(0, 16));
	}
	
	public NPC(String base_name) {
		if (base_name.length() > 16) throw new NullPointerException("Base Name cant be longer than 16!");
		if (base_names.contains(base_name)) throw new IllegalArgumentException("Base name is alredy in use!");
		base_names.add(base_name);
		this.ID = npc_count++;
		this.base_name = base_name;
		
		this.name = new NameSpliter();
		name.setOrginal(base_name);
		
		this.profile.setName(base_name);
		this.location = new Location(0, 0, 0);
		handler = new PacketHandler<Packet>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(PacketHandleEvent<Packet> e) {
				if (e.getPacket() instanceof PacketPlayInUseEntity) {
					PacketPlayInUseEntity packet = (PacketPlayInUseEntity) e.getPacket();
					if (packet.getTarget() == ID) {
						if (packet.getAction() == Action.ATTACK) for (InteractListener listener : (List<InteractListener>) NPC.this.listener.clone())
							listener.leftClick(e.getPlayer());
						else if (packet.getAction() == Action.INTERACT_AT && packet.getHand() == 0) // 1.9 sends 2 Packets (Hand 0 and hand 1)
		                    for (InteractListener listener : (List<InteractListener>) NPC.this.listener.clone())
							listener.rightClick(e.getPlayer());
						e.setCancelled(true);
					}
				}
			}
		};
		datawatcher.injektDefault();
		PacketLib.addHandler(handler);
		rebuild();
	}
	
	public void setPing(int ping) {
		this.ping = ping;
		if (tab) broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_PING, new PlayerInfoData(profile, ping, 0, tabname)));
	}
	
	public int getPing() {
		return ping;
	}
	
	public void setVisiable(Player p, boolean b) {
		if (b) if (!viewer.contains(p)) show(p);
		else;
		else if (viewer.contains(p)) hide(p);
	}
	
	public boolean canSee(Player p) {
		return viewer.contains(p);
	}
	
	public String getName() {
		return name.getOrginal();
	}
	
	public void setName(String name) {
		if (name.length() > 48) throw new NullPointerException(name.length() + " > 48");
		this.name.setOrginal(name);
		this.profile.setName(this.name.getMain());
		rebuild();
		for (Player p : viewer) {
			sendDestroy(p);
			sendSpawn(p);
		}
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
		rebuild();
		broadcastPacket(new PacketPlayOutEntityTeleport(ID, location));
		broadcastPacket(new PacketPlayOutEntityHeadRotation(ID, location.getYaw()));
	}
	
	public HumanDataWatcher getDatawatcher() {
		return datawatcher;
	}
	
	public GameProfile getProfile() {
		return profile;
	}
	
	public void setSkin(Skin skin) {
		skin.applay(this.profile);
		setProfile(this.profile);
	}
	
	public void setProfile(GameProfile profile) {
		this.profile = profile;
		if (this.profile == null) this.profile = new GameProfile(UUID.randomUUID(), this.name.getMain());
		if (this.profile.getName() == null) {
			System.err.print("Name of the GameProfile cant be null");
			this.profile.setName("error");
		}
		rebuild();
		ArrayList<Player> player = new ArrayList<>(viewer);
		for (Player p : player) {
			hide(p);
			show(p);
		}
	}
	
	public void setTabListed(boolean listed) {
		if (tab != listed) if (tab) broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new PlayerInfoData(profile, ping, 0, (IChatBaseComponent) tabname)));
		else broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(profile, ping, 0, tabname)));
		this.tab = listed;
	}
	
	public boolean isTabListed() {
		return tab;
	}
	
	public void setPlayerListName(IChatBaseComponent name) {
		this.tabname = name;
		broadcastPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new PlayerInfoData(profile, ping, 0, tabname)));
	}
	
	public IChatBaseComponent getPlayerListName() {
		return tabname;
	}
	
	public void addListener(InteractListener listener) {
		this.listener.add(listener);
	}
	
	public void removeListener(InteractListener listener) {
		this.listener.remove(listener);
	}
	
	public Equipment getEquipment() {
		return equipment;
	}
	
	private void hide(Player p) {
		viewer.remove(p);
		sendDestroy(p);
	}
	
	private void show(Player p) {
		viewer.add(p);
		sendSpawn(p);
	}

	/**
	 * Contains spelling mistake
	 * @deprecated Use {@link #broadcastPacket(Packet a)} instead.  
	 */
	@Deprecated
	private void brotcastPacket(Packet p) {
		broadcastPacket(p);
	}
	
	private void broadcastPacket(Packet p) {
		for (Player pl : viewer)
			pl.sendPacket((PacketPlayOut) p);
	}
	
	private void sendSpawn(final Player p) {
		p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new PlayerInfoData(profile, ping, 0, (IChatBaseComponent) tabname)));
		p.sendPacket(new PacketPlayOutNamedEntitySpawn(ID, profile, location, 0, datawatcher.getWatcher(p.getVersion().getBigVersion())));
		
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
		team.setTeam(base_name);
		team.setDisplayName("Error (NCP-Team): 002");
		team.setAction(PacketPlayOutScoreboardTeam.Action.CREATE);
		team.setPrefix(name.getPrefix());
		team.setSuffix(name.getSuffix());
		team.setFriendlyFire(0);
		team.setPlayers(new String[] { profile.getName() });
		team.setTag(NameTag.VISIABLE);
		p.sendPacket(team);
		
		p.sendPacket(new PacketPlayOutEntityHeadRotation(ID, p.getLocation().getYaw()));
		
		if (!tab) p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(profile, ping, 0, tabname)));
		else p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new PlayerInfoData(profile, ping, 0, (IChatBaseComponent) tabname)));
		equipment.sendItems(p);
	}
	
	private void sendDestroy(Player p) {
		if (tab) p.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new PlayerInfoData(profile, ping, 0, (IChatBaseComponent) null)));
		PacketPlayOutScoreboardTeam team = new PacketPlayOutScoreboardTeam();
		team.setTeam(base_name);
		team.setAction(PacketPlayOutScoreboardTeam.Action.REMOVE);
		p.sendPacket(team);
		p.sendPacket(p2);
	}
	
	protected void rebuild() {
		if (this.profile == null) {
			System.err.print("NPC Profile is null. it will crash the Client");
			this.profile = new GameProfile(UUID.randomUUID(), this.name.getMain());
		}
		p2 = new PacketPlayOutEntityDestroy(ID);
	}
	
	public void delete() {
		for (Player p : viewer)
			sendDestroy(p);
		viewer.clear();
		try {
			finalize();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public int getEntityID() {
		return ID;
	}
	
	protected ArrayList<Player> getViewer() {
		return viewer;
	}
	
	@Override
	protected void finalize() throws Throwable {
		PacketLib.removeHandler(handler);
		base_names.remove(base_name);
		super.finalize();
	}
}

class NameSpliter {
	private String orginal;
	private String main, prefix, suffix;
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public String getMain() {
		return main;
	}
	
	public String getOrginal() {
		return orginal;
	}
	
	public void setOrginal(String orginal) {
		this.orginal = orginal;
		recalculate();
	}
	
	private void recalculate() {
		if (orginal.length() > 16) {
			prefix = orginal.length() > 30 ? orginal.substring(0, 15) : "";
			main = orginal.length() > 30 ? orginal.substring(15, orginal.length() < 30 ? orginal.length() : 30) : orginal.substring(0, 15);
			suffix = orginal.length() > 30 ? orginal.substring(30, orginal.length() > 45 ? 45 : orginal.length()) : orginal.substring(15, orginal.length() > 30 ? 30 : orginal.length());
		}
		else {
			prefix = suffix = "";
			main = orginal;
		}
	}
}
