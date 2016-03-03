package dev.wolveringer.api.particel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum Particle {
	BARRIER("barrier", 35, 8),
	BLOCK_CRACK("blockcrack", 37, -1),
	BLOCK_DUST("blockdust", 38, 7),
	CLOUD("cloud", 29, -1),
	CRIT("crit", 9, -1),
	CRIT_MAGIC("magicCrit", 10, -1),
	DRIP_LAVA("dripLava", 19, -1),
	DRIP_WATER("dripWater", 18, -1),
	ENCHANTMENT_TABLE("enchantmenttable", 25, -1),
	EXPLOSION_HUGE("hugeexplosion", 2, -1),
	EXPLOSION_LARGE("largeexplode", 1, -1),
	EXPLOSION_NORMAL("explode", 0, -1),
	FIREWORKS_SPARK("fireworksSpark", 3, -1),
	FLAME("flame", 26, -1),
	FOOTSTEP("footstep", 28, -1),
	HEART("heart", 34, -1), //COLOR
	ITEM_CRACK("iconcrack", 36, -1), //COLOR
	ITEM_TAKE("take", 40, 8),
	LAVA("lava", 27, -1),
	MOB_APPEARANCE("mobappearance", 41, 8),
	NOTE("note", 23, -1),
	PORTAL("portal", 24, -1),
	REDSTONE("reddust", 30, -1),
	SLIME("slime", 33, -1), //COLOR
	SMOKE_LARGE("largesmoke", 12, -1),
	SMOKE_NORMAL("smoke", 11, -1),
	SNOW_SHOVEL("snowshovel", 32, -1),
	SNOWBALL("snowballpoof", 31, -1),
	SPELL("spell", 13, -1),
	SPELL_INSTANT("instantSpell", 14, -1),
	SPELL_MOB("mobSpell", 15, -1), //COLOR
	SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1),
	SPELL_WITCH("witchMagic", 17, -1),
	SUSPENDED("suspended", 7, -1),
	SUSPENDED_DEPTH("depthSuspend", 8, -1),
	TOWN_AURA("townaura", 22, -1),
	VILLAGER_ANGRY("angryVillager", 20, -1),
	VILLAGER_HAPPY("happyVillager", 21, -1),
	WATER_BUBBLE("bubble", 4, -1),
	WATER_DROP("droplet", 39, 8),
	WATER_SPLASH("splash", 5, -1),
	WATER_WAKE("wake", 6, 7);

	private static final Map<Integer, Particle> ID_MAP = new HashMap<Integer, Particle>();
	private static final Map<String, Particle> NAME_MAP = new HashMap<String, Particle>();
	// Initialize map for quick name and id lookup
	static{
		for(Particle effect : values()){
			NAME_MAP.put(effect.name, effect);
			ID_MAP.put(effect.id, effect);
		}
	}

	public static Particle fromId(int id) {
		for(Entry<Integer, Particle> entry : ID_MAP.entrySet()){
			if(entry.getKey() != id){
				continue;
			}
			return entry.getValue();
		}
		return null;
	}

	public static Particle fromName(String name) {
		for(Entry<String, Particle> entry : NAME_MAP.entrySet()){
			if(!entry.getKey().equalsIgnoreCase(name)){
				continue;
			}
			return entry.getValue();
		}
		return null;
	}

	private final int id;

	private final String name;

	private final int requiredVersion;

	private Particle(String name, int id, int requiredVersion) {
		this.name = name;
		this.id = id;
		this.requiredVersion = requiredVersion;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRequiredVersion() {
		return requiredVersion;
	}
}