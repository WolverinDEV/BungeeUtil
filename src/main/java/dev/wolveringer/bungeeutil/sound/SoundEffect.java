package dev.wolveringer.bungeeutil.sound;

import java.util.HashMap;

import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

@SuppressWarnings("deprecation")
public enum SoundEffect {

	/**
	 * @author essem
	 */

	AMBIENT_CAVE,
	BLOCK_ANVIL_BREAK,
	BLOCK_ANVIL_DESTROY,
	BLOCK_ANVIL_FALL,
	BLOCK_ANVIL_HIT,
	BLOCK_ANVIL_LAND,
	BLOCK_ANVIL_PLACE,
	BLOCK_ANVIL_STEP,
	BLOCK_ANVIL_USE,
	BLOCK_BREWING_STAND_BREW,
	BLOCK_CHEST_CLOSE,
	BLOCK_CHEST_LOCKED,
	BLOCK_CHEST_OPEN,
	BLOCK_CHORUS_FLOWER_DEATH,
	BLOCK_CHORUS_FLOWER_GROW,
	BLOCK_CLOTH_BREAK,
	BLOCK_CLOTH_FALL,
	BLOCK_CLOTH_HIT,
	BLOCK_CLOTH_PLACE,
	BLOCK_CLOTH_STEP,
	BLOCK_COMPARATOR_CLICK,
	BLOCK_DISPENSER_DISPENSE,
	BLOCK_DISPENSER_FAIL,
	BLOCK_DISPENSER_LAUNCH,
	BLOCK_ENCHANTMENT_TABLE_USE,
	BLOCK_END_GATEWAY_SPAWN,
	BLOCK_END_PORTAL_FRAME_FILL,
	BLOCK_END_PORTAL_SPAWN,
	BLOCK_ENDERCHEST_CLOSE,
	BLOCK_ENDERCHEST_OPEN,
	BLOCK_FENCE_GATE_CLOSE,
	BLOCK_FENCE_GATE_OPEN,
	BLOCK_FIRE_AMBIENT,
	BLOCK_FIRE_EXTINGUISH,
	BLOCK_FURNACE_FIRE_CRACKLE,
	BLOCK_GLASS_BREAK,
	BLOCK_GLASS_FALL,
	BLOCK_GLASS_HIT,
	BLOCK_GLASS_PLACE,
	BLOCK_GLASS_STEP,
	BLOCK_GRASS_BREAK,
	BLOCK_GRASS_FALL,
	BLOCK_GRASS_HIT,
	BLOCK_GRASS_PLACE,
	BLOCK_GRASS_STEP,
	BLOCK_GRAVEL_BREAK,
	BLOCK_GRAVEL_FALL,
	BLOCK_GRAVEL_HIT,
	BLOCK_GRAVEL_PLACE,
	BLOCK_GRAVEL_STEP,
	BLOCK_IRON_DOOR_CLOSE,
	BLOCK_IRON_DOOR_OPEN,
	BLOCK_IRON_TRAPDOOR_CLOSE,
	BLOCK_IRON_TRAPDOOR_OPEN,
	BLOCK_LADDER_BREAK,
	BLOCK_LADDER_FALL,
	BLOCK_LADDER_HIT,
	BLOCK_LADDER_PLACE,
	BLOCK_LADDER_STEP,
	BLOCK_LAVA_AMBIENT,
	BLOCK_LAVA_EXTINGUISH,
	BLOCK_LAVA_POP,
	BLOCK_LEVER_CLICK,
	BLOCK_METAL_BREAK,
	BLOCK_METAL_FALL,
	BLOCK_METAL_HIT,
	BLOCK_METAL_PLACE,
	BLOCK_METAL_PRESSUREPLATE_CLICK_OFF,
	BLOCK_METAL_PRESSUREPLATE_CLICK_ON,
	BLOCK_METAL_STEP,
	BLOCK_NOTE_BASEDRUM,
	BLOCK_NOTE_BASS,
	BLOCK_NOTE_BELL,
	BLOCK_NOTE_CHIME,
	BLOCK_NOTE_FLUTE,
	BLOCK_NOTE_GUITAR,
	BLOCK_NOTE_HARP,
	BLOCK_NOTE_HAT,
	BLOCK_NOTE_PLING,
	BLOCK_NOTE_SNARE,
	BLOCK_NOTE_XYLOPHONE,
	BLOCK_PISTON_CONTRACT,
	BLOCK_PISTON_EXTEND,
	BLOCK_PORTAL_AMBIENT,
	BLOCK_PORTAL_TRAVEL,
	BLOCK_PORTAL_TRIGGER,
	BLOCK_REDSTONE_TORCH_BURNOUT,
	BLOCK_SAND_BREAK,
	BLOCK_SAND_FALL,
	BLOCK_SAND_HIT,
	BLOCK_SAND_PLACE,
	BLOCK_SAND_STEP,
	BLOCK_SHULKER_BOX_CLOSE,
	BLOCK_SHULKER_BOX_OPEN,
	BLOCK_SLIME_BREAK,
	BLOCK_SLIME_FALL,
	BLOCK_SLIME_HIT,
	BLOCK_SLIME_PLACE,
	BLOCK_SLIME_STEP,
	BLOCK_SNOW_BREAK,
	BLOCK_SNOW_FALL,
	BLOCK_SNOW_HIT,
	BLOCK_SNOW_PLACE,
	BLOCK_SNOW_STEP,
	BLOCK_STONE_BREAK,
	BLOCK_STONE_BUTTON_CLICK_OFF,
	BLOCK_STONE_BUTTON_CLICK_ON,
	BLOCK_STONE_FALL,
	BLOCK_STONE_HIT,
	BLOCK_STONE_PLACE,
	BLOCK_STONE_PRESSUREPLATE_CLICK_OFF,
	BLOCK_STONE_PRESSUREPLATE_CLICK_ON,
	BLOCK_STONE_STEP,
	BLOCK_TRIPWIRE_ATTACH,
	BLOCK_TRIPWIRE_CLICK_OFF,
	BLOCK_TRIPWIRE_CLICK_ON,
	BLOCK_TRIPWIRE_DETACH,
	BLOCK_WATER_AMBIENT,
	BLOCK_WATERLILY_PLACE,
	BLOCK_WOOD_BREAK,
	BLOCK_WOOD_BUTTON_CLICK_OFF,
	BLOCK_WOOD_BUTTON_CLICK_ON,
	BLOCK_WOOD_FALL,
	BLOCK_WOOD_HIT,
	BLOCK_WOOD_PLACE,
	BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF,
	BLOCK_WOOD_PRESSUREPLATE_CLICK_ON,
	BLOCK_WOOD_STEP,
	BLOCK_WOODEN_DOOR_CLOSE,
	BLOCK_WOODEN_DOOR_OPEN,
	BLOCK_WOODEN_TRAPDOOR_CLOSE,
	BLOCK_WOODEN_TRAPDOOR_OPEN,
	ENCHANT_THORNS_HIT,
	ENTITY_ARMORSTAND_BREAK,
	ENTITY_ARMORSTAND_FALL,
	ENTITY_ARMORSTAND_HIT,
	ENTITY_ARMORSTAND_PLACE,
	ENTITY_ARROW_HIT,
	ENTITY_ARROW_HIT_PLAYER,
	ENTITY_ARROW_SHOOT,
	ENTITY_BAT_AMBIENT,
	ENTITY_BAT_DEATH,
	ENTITY_BAT_HURT,
	ENTITY_BAT_LOOP,
	ENTITY_BAT_TAKEOFF,
	ENTITY_BLAZE_AMBIENT,
	ENTITY_BLAZE_BURN,
	ENTITY_BLAZE_DEATH,
	ENTITY_BLAZE_HURT,
	ENTITY_BLAZE_SHOOT,
	ENTITY_BOAT_PADDLE_LAND,
	ENTITY_BOAT_PADDLE_WATER,
	ENTITY_BOBBER_RETRIEVE,
	ENTITY_BOBBER_SPLASH,
	ENTITY_BOBBER_THROW,
	ENTITY_CAT_AMBIENT,
	ENTITY_CAT_DEATH,
	ENTITY_CAT_HISS,
	ENTITY_CAT_HURT,
	ENTITY_CAT_PURR,
	ENTITY_CAT_PURREOW,
	ENTITY_CHICKEN_AMBIENT,
	ENTITY_CHICKEN_DEATH,
	ENTITY_CHICKEN_EGG,
	ENTITY_CHICKEN_HURT,
	ENTITY_CHICKEN_STEP,
	ENTITY_COW_AMBIENT,
	ENTITY_COW_DEATH,
	ENTITY_COW_HURT,
	ENTITY_COW_MILK,
	ENTITY_COW_STEP,
	ENTITY_CREEPER_DEATH,
	ENTITY_CREEPER_HURT,
	ENTITY_CREEPER_PRIMED,
	ENTITY_DONKEY_AMBIENT,
	ENTITY_DONKEY_ANGRY,
	ENTITY_DONKEY_CHEST,
	ENTITY_DONKEY_DEATH,
	ENTITY_DONKEY_HURT,
	ENTITY_EGG_THROW,
	ENTITY_ELDER_GUARDIAN_AMBIENT,
	ENTITY_ELDER_GUARDIAN_AMBIENT_LAND,
	ENTITY_ELDER_GUARDIAN_CURSE,
	ENTITY_ELDER_GUARDIAN_DEATH,
	ENTITY_ELDER_GUARDIAN_DEATH_LAND,
	ENTITY_ELDER_GUARDIAN_FLOP,
	ENTITY_ELDER_GUARDIAN_HURT,
	ENTITY_ELDER_GUARDIAN_HURT_LAND,
	ENTITY_ENDERDRAGON_AMBIENT,
	ENTITY_ENDERDRAGON_DEATH,
	ENTITY_ENDERDRAGON_FIREBALL_EXPLODE,
	ENTITY_ENDERDRAGON_FLAP,
	ENTITY_ENDERDRAGON_GROWL,
	ENTITY_ENDERDRAGON_HURT,
	ENTITY_ENDERDRAGON_SHOOT,
	ENTITY_ENDEREYE_DEATH,
	ENTITY_ENDEREYE_LAUNCH,
	ENTITY_ENDERMEN_AMBIENT,
	ENTITY_ENDERMEN_DEATH,
	ENTITY_ENDERMEN_HURT,
	ENTITY_ENDERMEN_SCREAM,
	ENTITY_ENDERMEN_STARE,
	ENTITY_ENDERMEN_TELEPORT,
	ENTITY_ENDERMITE_AMBIENT,
	ENTITY_ENDERMITE_DEATH,
	ENTITY_ENDERMITE_HURT,
	ENTITY_ENDERMITE_STEP,
	ENTITY_ENDERPEARL_THROW,
    ENTITY_EVOCATION_FANGS_ATTACK,
    ENTITY_EVOCATION_ILLAGER_AMBIENT,
    ENTITY_EVOCATION_ILLAGER_CAST_SPELL,
    ENTITY_EVOCATION_ILLAGER_DEATH,
    ENTITY_EVOCATION_ILLAGER_HURT,
    ENTITY_EVOCATION_ILLAGER_PREPARE_ATTACK,
    ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON,
    ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO,
	ENTITY_EXPERIENCE_BOTTLE_THROW,
	ENTITY_EXPERIENCE_ORB_PICKUP,
	ENTITY_EXPERIENCE_ORB_TOUCH,
	ENTITY_FIREWORK_BLAST,
	ENTITY_FIREWORK_BLAST_FAR,
	ENTITY_FIREWORK_LARGE_BLAST,
	ENTITY_FIREWORK_LARGE_BLAST_FAR,
	ENTITY_FIREWORK_LAUNCH,
	ENTITY_FIREWORK_SHOOT,
	ENTITY_FIREWORK_TWINKLE,
	ENTITY_FIREWORK_TWINKLE_FAR,
	ENTITY_GENERIC_BIG_FALL,
	ENTITY_GENERIC_BURN,
	ENTITY_GENERIC_DEATH,
	ENTITY_GENERIC_DRINK,
	ENTITY_GENERIC_EAT,
	ENTITY_GENERIC_EXPLODE,
	ENTITY_GENERIC_EXTINGUISH_FIRE,
	ENTITY_GENERIC_HURT,
	ENTITY_GENERIC_SMALL_FALL,
	ENTITY_GENERIC_SPLASH,
	ENTITY_GENERIC_SWIM,
	ENTITY_GHAST_AMBIENT,
	ENTITY_GHAST_DEATH,
	ENTITY_GHAST_HURT,
	ENTITY_GHAST_SCREAM,
	ENTITY_GHAST_SHOOT,
	ENTITY_GHAST_WARN,
	ENTITY_GUARDIAN_AMBIENT,
	ENTITY_GUARDIAN_AMBIENT_LAND,
	ENTITY_GUARDIAN_ATTACK,
	ENTITY_GUARDIAN_DEATH,
	ENTITY_GUARDIAN_DEATH_LAND,
	ENTITY_GUARDIAN_FLOP,
	ENTITY_GUARDIAN_HURT,
	ENTITY_GUARDIAN_HURT_LAND,
	ENTITY_HORSE_AMBIENT,
	ENTITY_HORSE_ANGRY,
	ENTITY_HORSE_ARMOR,
	ENTITY_HORSE_BREATHE,
	ENTITY_HORSE_DEATH,
	ENTITY_HORSE_EAT,
	ENTITY_HORSE_GALLOP,
	ENTITY_HORSE_HURT,
	ENTITY_HORSE_JUMP,
	ENTITY_HORSE_LAND,
	ENTITY_HORSE_SADDLE,
	ENTITY_HORSE_STEP,
	ENTITY_HORSE_STEP_WOOD,
	ENTITY_HOSTILE_BIG_FALL,
	ENTITY_HOSTILE_DEATH,
	ENTITY_HOSTILE_HURT,
	ENTITY_HOSTILE_SMALL_FALL,
	ENTITY_HOSTILE_SPLASH,
	ENTITY_HOSTILE_SWIM,
	ENTITY_HUSK_AMBIENT,
	ENTITY_HUSK_DEATH,
	ENTITY_HUSK_HURT,
	ENTITY_HUSK_STEP,
	ENTITY_ILLUSION_ILLAGER_AMBIENT,
	ENTITY_ILLUSION_ILLAGER_CAST_SPELL,
	ENTITY_ILLUSION_ILLAGER_DEATH,
	ENTITY_ILLUSION_ILLAGER_HURT,
	ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE,
	ENTITY_ILLUSION_ILLAGER_PREPARE_BLINDNESS,
	ENTITY_ILLUSION_ILLAGER_PREPARE_MIRROR,
	ENTITY_IRONGOLEM_ATTACK,
	ENTITY_IRONGOLEM_DEATH,
	ENTITY_IRONGOLEM_HURT,
	ENTITY_IRONGOLEM_STEP,
	ENTITY_ITEM_BREAK,
	ENTITY_ITEM_PICKUP,
	ENTITY_ITEMFRAME_ADD_ITEM,
	ENTITY_ITEMFRAME_BREAK,
	ENTITY_ITEMFRAME_PLACE,
	ENTITY_ITEMFRAME_REMOVE_ITEM,
	ENTITY_ITEMFRAME_ROTATE_ITEM,
	ENTITY_LEASHKNOT_BREAK,
	ENTITY_LEASHKNOT_PLACE,
	ENTITY_LIGHTNING_IMPACT,
	ENTITY_LIGHTNING_THUNDER,
	ENTITY_LINGERINGPOTION_THROW,
    ENTITY_LLAMA_AMBIENT,
    ENTITY_LLAMA_ANGRY,
    ENTITY_LLAMA_CHEST,
    ENTITY_LLAMA_DEATH,
    ENTITY_LLAMA_EAT,
    ENTITY_LLAMA_HURT,
    ENTITY_LLAMA_SPIT,
    ENTITY_LLAMA_STEP,
    ENTITY_LLAMA_SWAG,
	ENTITY_MAGMACUBE_DEATH,
	ENTITY_MAGMACUBE_HURT,
	ENTITY_MAGMACUBE_JUMP,
	ENTITY_MAGMACUBE_SQUISH,
	ENTITY_MINECART_INSIDE,
	ENTITY_MINECART_RIDING,
	ENTITY_MOOSHROOM_SHEAR,
	ENTITY_MULE_AMBIENT,
	ENTITY_MULE_CHEST,
	ENTITY_MULE_DEATH,
	ENTITY_MULE_HURT,
	ENTITY_PAINTING_BREAK,
	ENTITY_PAINTING_PLACE,
    ENTITY_PARROT_AMBIENT,
    ENTITY_PARROT_DEATH,
    ENTITY_PARROT_EAT,
    ENTITY_PARROT_FLY,
    ENTITY_PARROT_HURT,
    ENTITY_PARROT_IMITATE_BLAZE,
    ENTITY_PARROT_IMITATE_CREEPER,
    ENTITY_PARROT_IMITATE_ELDER_GUARDIAN,
    ENTITY_PARROT_IMITATE_ENDERDRAGON,
    ENTITY_PARROT_IMITATE_ENDERMAN,
    ENTITY_PARROT_IMITATE_ENDERMITE,
    ENTITY_PARROT_IMITATE_EVOCATION_ILLAGER,
    ENTITY_PARROT_IMITATE_GHAST,
    ENTITY_PARROT_IMITATE_HUSK,
    ENTITY_PARROT_IMITATE_ILLUSION_ILLAGER,
    ENTITY_PARROT_IMITATE_MAGMACUBE,
    ENTITY_PARROT_IMITATE_POLAR_BEAR,
    ENTITY_PARROT_IMITATE_SHULKER,
    ENTITY_PARROT_IMITATE_SILVERFISH,
    ENTITY_PARROT_IMITATE_SKELETON,
    ENTITY_PARROT_IMITATE_SLIME,
    ENTITY_PARROT_IMITATE_SPIDER,
    ENTITY_PARROT_IMITATE_STRAY,
    ENTITY_PARROT_IMITATE_VEX,
    ENTITY_PARROT_IMITATE_VINDICATION_ILLAGER,
    ENTITY_PARROT_IMITATE_WITCH,
    ENTITY_PARROT_IMITATE_WITHER,
    ENTITY_PARROT_IMITATE_WITHER_SKELETON,
    ENTITY_PARROT_IMITATE_WOLF,
    ENTITY_PARROT_IMITATE_ZOMBIE,
    ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN,
    ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER,
    ENTITY_PARROT_STEP,
	ENTITY_PIG_AMBIENT,
	ENTITY_PIG_DEATH,
	ENTITY_PIG_HURT,
	ENTITY_PIG_SADDLE,
	ENTITY_PIG_STEP,
	ENTITY_PLAYER_ATTACK_CRIT,
	ENTITY_PLAYER_ATTACK_KNOCKBACK,
	ENTITY_PLAYER_ATTACK_NODAMAGE,
	ENTITY_PLAYER_ATTACK_STRONG,
	ENTITY_PLAYER_ATTACK_SWEEP,
	ENTITY_PLAYER_ATTACK_WEAK,
	ENTITY_PLAYER_BIG_FALL,
	ENTITY_PLAYER_BREATH,
	ENTITY_PLAYER_BURP,
	ENTITY_PLAYER_DEATH,
	ENTITY_PLAYER_HURT,
	ENTITY_PLAYER_HURT_DROWN,
	ENTITY_PLAYER_HURT_ON_FIRE,
	ENTITY_PLAYER_LEVELUP,
	ENTITY_PLAYER_SMALL_FALL,
	ENTITY_PLAYER_SPLASH,
	ENTITY_PLAYER_SWIM,
	ENTITY_POLAR_BEAR_AMBIENT,
	ENTITY_POLAR_BEAR_BABY_AMBIENT,
	ENTITY_POLAR_BEAR_DEATH,
	ENTITY_POLAR_BEAR_HURT,
	ENTITY_POLAR_BEAR_STEP,
	ENTITY_POLAR_BEAR_WARNING,
	ENTITY_RABBIT_AMBIENT,
	ENTITY_RABBIT_ATTACK,
	ENTITY_RABBIT_DEATH,
	ENTITY_RABBIT_HURT,
	ENTITY_RABBIT_JUMP,
	ENTITY_SHEEP_AMBIENT,
	ENTITY_SHEEP_DEATH,
	ENTITY_SHEEP_HURT,
	ENTITY_SHEEP_SHEAR,
	ENTITY_SHEEP_STEP,
	ENTITY_SHULKER_AMBIENT,
	ENTITY_SHULKER_BULLET_HIT,
	ENTITY_SHULKER_BULLET_HURT,
	ENTITY_SHULKER_CLOSE,
	ENTITY_SHULKER_DEATH,
	ENTITY_SHULKER_HURT,
	ENTITY_SHULKER_HURT_CLOSED,
	ENTITY_SHULKER_OPEN,
	ENTITY_SHULKER_SHOOT,
	ENTITY_SHULKER_TELEPORT,
	ENTITY_SILVERFISH_AMBIENT,
	ENTITY_SILVERFISH_DEATH,
	ENTITY_SILVERFISH_HURT,
	ENTITY_SILVERFISH_STEP,
	ENTITY_SKELETON_AMBIENT,
	ENTITY_SKELETON_DEATH,
	ENTITY_SKELETON_HORSE_AMBIENT,
	ENTITY_SKELETON_HORSE_DEATH,
	ENTITY_SKELETON_HORSE_HURT,
	ENTITY_SKELETON_HURT,
	ENTITY_SKELETON_SHOOT,
	ENTITY_SKELETON_STEP,
	ENTITY_SLIME_ATTACK,
	ENTITY_SLIME_DEATH,
	ENTITY_SLIME_HURT,
	ENTITY_SLIME_JUMP,
	ENTITY_SLIME_SQUISH,
	ENTITY_SMALL_MAGMACUBE_DEATH,
	ENTITY_SMALL_MAGMACUBE_HURT,
	ENTITY_SMALL_MAGMACUBE_SQUISH,
	ENTITY_SMALL_SLIME_DEATH,
	ENTITY_SMALL_SLIME_HURT,
	ENTITY_SMALL_SLIME_JUMP,
	ENTITY_SMALL_SLIME_SQUISH,
	ENTITY_SNOWBALL_THROW,
	ENTITY_SNOWMAN_AMBIENT,
	ENTITY_SNOWMAN_DEATH,
	ENTITY_SNOWMAN_HURT,
	ENTITY_SNOWMAN_SHOOT,
	ENTITY_SPIDER_AMBIENT,
	ENTITY_SPIDER_DEATH,
	ENTITY_SPIDER_HURT,
	ENTITY_SPIDER_STEP,
	ENTITY_SPLASH_POTION_BREAK,
	ENTITY_SPLASH_POTION_THROW,
	ENTITY_SQUID_AMBIENT,
	ENTITY_SQUID_DEATH,
	ENTITY_SQUID_HURT,
	ENTITY_STRAY_AMBIENT,
	ENTITY_STRAY_DEATH,
	ENTITY_STRAY_HURT,
	ENTITY_STRAY_STEP,
	ENTITY_TNT_PRIMED,
    ENTITY_VEX_AMBIENT,
    ENTITY_VEX_CHARGE,
    ENTITY_VEX_DEATH,
    ENTITY_VEX_HURT,
	ENTITY_VILLAGER_AMBIENT,
	ENTITY_VILLAGER_DEATH,
	ENTITY_VILLAGER_HURT,
	ENTITY_VILLAGER_NO,
	ENTITY_VILLAGER_TRADING,
	ENTITY_VILLAGER_YES,
    ENTITY_VINDICATION_ILLAGER_AMBIENT,
    ENTITY_VINDICATION_ILLAGER_DEATH,
    ENTITY_VINDICATION_ILLAGER_HURT,
	ENTITY_WITCH_AMBIENT,
	ENTITY_WITCH_DEATH,
	ENTITY_WITCH_DRINK,
	ENTITY_WITCH_HURT,
	ENTITY_WITCH_THROW,
	ENTITY_WITHER_AMBIENT,
	ENTITY_WITHER_BREAK_BLOCK,
	ENTITY_WITHER_DEATH,
	ENTITY_WITHER_HURT,
	ENTITY_WITHER_SHOOT,
	ENTITY_WITHER_SKELETON_AMBIENT,
	ENTITY_WITHER_SKELETON_DEATH,
	ENTITY_WITHER_SKELETON_HURT,
	ENTITY_WITHER_SKELETON_STEP,
	ENTITY_WITHER_SPAWN,
	ENTITY_WOLF_AMBIENT,
	ENTITY_WOLF_DEATH,
	ENTITY_WOLF_GROWL,
	ENTITY_WOLF_HOWL,
	ENTITY_WOLF_HURT,
	ENTITY_WOLF_PANT,
	ENTITY_WOLF_SHAKE,
	ENTITY_WOLF_STEP,
	ENTITY_WOLF_WHINE,
	ENTITY_ZOMBIE_AMBIENT,
	ENTITY_ZOMBIE_ATTACK_DOOR_WOOD,
	ENTITY_ZOMBIE_ATTACK_IRON_DOOR,
	ENTITY_ZOMBIE_BREAK_DOOR_WOOD,
	ENTITY_ZOMBIE_DEATH,
	ENTITY_ZOMBIE_HORSE_AMBIENT,
	ENTITY_ZOMBIE_HORSE_DEATH,
	ENTITY_ZOMBIE_HORSE_HURT,
	ENTITY_ZOMBIE_HURT,
	ENTITY_ZOMBIE_INFECT,
	ENTITY_ZOMBIE_PIG_AMBIENT,
	ENTITY_ZOMBIE_PIG_ANGRY,
	ENTITY_ZOMBIE_PIG_DEATH,
	ENTITY_ZOMBIE_PIG_HURT,
	ENTITY_ZOMBIE_STEP,
	ENTITY_ZOMBIE_VILLAGER_AMBIENT,
	ENTITY_ZOMBIE_VILLAGER_CONVERTED,
	ENTITY_ZOMBIE_VILLAGER_CURE,
	ENTITY_ZOMBIE_VILLAGER_DEATH,
	ENTITY_ZOMBIE_VILLAGER_HURT,
	ENTITY_ZOMBIE_VILLAGER_STEP,
	ITEM_ARMOR_EQUIP_CHAIN,
	ITEM_ARMOR_EQUIP_DIAMOND,
	ITEM_ARMOR_EQUIP_ELYTRA,
	ITEM_ARMOR_EQUIP_GENERIC,
	ITEM_ARMOR_EQUIP_GOLD,
	ITEM_ARMOR_EQUIP_IRON,
	ITEM_ARMOR_EQUIP_LEATHER,
	ITEM_BOTTLE_EMPTY,
	ITEM_BOTTLE_FILL,
	ITEM_BOTTLE_FILL_DRAGONBREATH,
	ITEM_BUCKET_EMPTY,
	ITEM_BUCKET_EMPTY_LAVA,
	ITEM_BUCKET_FILL,
	ITEM_BUCKET_FILL_LAVA,
	ITEM_CHORUS_FRUIT_TELEPORT,
	ITEM_ELYTRA_FLYING,
	ITEM_FIRECHARGE_USE,
	ITEM_FLINTANDSTEEL_USE,
	ITEM_HOE_TILL,
	ITEM_SHIELD_BLOCK,
	ITEM_SHIELD_BREAK,
	ITEM_SHOVEL_FLATTEN,
	ITEM_TOTEM_USE,
	MUSIC_CREATIVE,
	MUSIC_CREDITS,
	MUSIC_DRAGON,
	MUSIC_END,
	MUSIC_GAME,
	MUSIC_MENU,
	MUSIC_NETHER,
	RECORD_11,
	RECORD_13,
	RECORD_BLOCKS,
	RECORD_CAT,
	RECORD_CHIRP,
	RECORD_FAR,
	RECORD_MALL,
	RECORD_MELLOHI,
	RECORD_STAL,
	RECORD_STRAD,
	RECORD_WAIT,
	RECORD_WARD,
	UI_BUTTON_CLICK,
	WEATHER_RAIN,
	WEATHER_RAIN_ABOVE;
	//...

	static {
		//Good Source: https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/CraftSound.java
		
		//1.12
		addSoundFromNow(BLOCK_END_PORTAL_FRAME_FILL, BigClientVersion.v1_12, "block.end_portal_frame.fill");
		addSoundFromNow(BLOCK_END_PORTAL_SPAWN, BigClientVersion.v1_12, "block.end_portal.spawn");
		
		addSoundFromNow(BLOCK_NOTE_BELL, BigClientVersion.v1_12, "block.note.bell");
		addSoundFromNow(BLOCK_NOTE_CHIME, BigClientVersion.v1_12, "block.note.chime");
		addSoundFromNow(BLOCK_NOTE_FLUTE, BigClientVersion.v1_12, "block.note.flute");
		addSoundFromNow(BLOCK_NOTE_GUITAR, BigClientVersion.v1_12, "block.note.guitar");
		
		addSoundFromNow(BLOCK_END_PORTAL_SPAWN, BigClientVersion.v1_12, "block.note.xylophone");
		
		addSoundFromNow(ENTITY_BOAT_PADDLE_LAND, BigClientVersion.v1_12, "entity.boat.paddle_land");
		addSoundFromNow(ENTITY_BOAT_PADDLE_WATER, BigClientVersion.v1_12, "entity.boat.paddle_water");
		addSoundFromNow(ENTITY_BOBBER_RETRIEVE, BigClientVersion.v1_12, "entity.bobber.retrieve");
		
		addSoundFromNow(ENTITY_ENDEREYE_DEATH, BigClientVersion.v1_12, "entity.endereye.death");
		
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_AMBIENT, BigClientVersion.v1_12, "entity.illusion_illager.ambient");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_CAST_SPELL, BigClientVersion.v1_12, "entity.illusion_illager.cast_spell");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_DEATH, BigClientVersion.v1_12, "entity.illusion_illager.death");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_HURT, BigClientVersion.v1_12, "entity.illusion_illager.hurt");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE, BigClientVersion.v1_12, "entity.illusion_illager.mirror_move");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_PREPARE_BLINDNESS, BigClientVersion.v1_12, "entity.illusion_illager.prepare_blindness");
		addSoundFromNow(ENTITY_ILLUSION_ILLAGER_PREPARE_MIRROR, BigClientVersion.v1_12, "entity.illusion_illager.prepare_mirror");
		
		addSoundFromNow(ENTITY_PARROT_AMBIENT, BigClientVersion.v1_12, "entity.parrot.ambient");
		addSoundFromNow(ENTITY_PARROT_DEATH, BigClientVersion.v1_12, "entity.parrot.death");
		addSoundFromNow(ENTITY_PARROT_EAT, BigClientVersion.v1_12, "entity.parrot.eat");
		addSoundFromNow(ENTITY_PARROT_FLY, BigClientVersion.v1_12, "entity.parrot.fly");
		addSoundFromNow(ENTITY_PARROT_HURT, BigClientVersion.v1_12, "entity.parrot.hurt");
		addSoundFromNow(ENTITY_PARROT_IMITATE_BLAZE, BigClientVersion.v1_12, "entity.parrot.imitate.blaze");
		addSoundFromNow(ENTITY_PARROT_IMITATE_CREEPER, BigClientVersion.v1_12, "entity.parrot.imitate.creeper");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ELDER_GUARDIAN, BigClientVersion.v1_12, "entity.parrot.imitate.elder_guardian");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ENDERDRAGON, BigClientVersion.v1_12, "entity.parrot.imitate.enderdragon");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ENDERMAN, BigClientVersion.v1_12, "entity.parrot.imitate.enderman");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ENDERMITE, BigClientVersion.v1_12, "entity.parrot.imitate.endermite");
		addSoundFromNow(ENTITY_PARROT_IMITATE_EVOCATION_ILLAGER, BigClientVersion.v1_12, "entity.parrot.imitate.evocation_illager");
		addSoundFromNow(ENTITY_PARROT_IMITATE_GHAST, BigClientVersion.v1_12, "entity.parrot.imitate.ghast");
		addSoundFromNow(ENTITY_PARROT_IMITATE_HUSK, BigClientVersion.v1_12, "entity.parrot.imitate.husk");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ILLUSION_ILLAGER, BigClientVersion.v1_12, "entity.parrot.imitate.illusion_illager");
		addSoundFromNow(ENTITY_PARROT_IMITATE_MAGMACUBE, BigClientVersion.v1_12, "entity.parrot.imitate.magmacube");
		addSoundFromNow(ENTITY_PARROT_IMITATE_POLAR_BEAR, BigClientVersion.v1_12, "entity.parrot.imitate.polar_bear");
		addSoundFromNow(ENTITY_PARROT_IMITATE_SHULKER, BigClientVersion.v1_12, "entity.parrot.imitate.shulker");
		addSoundFromNow(ENTITY_PARROT_IMITATE_SILVERFISH, BigClientVersion.v1_12, "entity.parrot.imitate.silverfish");
		addSoundFromNow(ENTITY_PARROT_IMITATE_SKELETON, BigClientVersion.v1_12, "entity.parrot.imitate.skeleton");
		addSoundFromNow(ENTITY_PARROT_IMITATE_SLIME, BigClientVersion.v1_12, "entity.parrot.imitate.slime");
		addSoundFromNow(ENTITY_PARROT_IMITATE_SPIDER, BigClientVersion.v1_12, "entity.parrot.imitate.spider");
		addSoundFromNow(ENTITY_PARROT_IMITATE_STRAY, BigClientVersion.v1_12, "entity.parrot.imitate.stray");
		addSoundFromNow(ENTITY_PARROT_IMITATE_VEX, BigClientVersion.v1_12, "entity.parrot.imitate.vex");
		addSoundFromNow(ENTITY_PARROT_IMITATE_VINDICATION_ILLAGER, BigClientVersion.v1_12, "entity.parrot.imitate.vindication_illager");
		addSoundFromNow(ENTITY_PARROT_IMITATE_WITCH, BigClientVersion.v1_12, "entity.parrot.imitate.witch");
		addSoundFromNow(ENTITY_PARROT_IMITATE_WITHER, BigClientVersion.v1_12, "entity.parrot.imitate.wither");
		addSoundFromNow(ENTITY_PARROT_IMITATE_WITHER_SKELETON, BigClientVersion.v1_12, "entity.parrot.imitate.wither_skeleton");
		addSoundFromNow(ENTITY_PARROT_IMITATE_WOLF, BigClientVersion.v1_12, "entity.parrot.imitate.wolf");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ZOMBIE, BigClientVersion.v1_12, "entity.parrot.imitate.zombie");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN, BigClientVersion.v1_12, "entity.parrot.imitate.zombie_pigman");
		addSoundFromNow(ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER, BigClientVersion.v1_12, "entity.parrot.imitate.zombie_villager");
		addSoundFromNow(ENTITY_PARROT_STEP, BigClientVersion.v1_12, "entity.parrot.step");
		
		addSoundFromNow(ENTITY_PLAYER_HURT_DROWN, BigClientVersion.v1_12, "entity.player.hurt_drown");
		addSoundFromNow(ENTITY_PLAYER_HURT_ON_FIRE, BigClientVersion.v1_12, "entity.player.hurt_on_fire");
		
		
		//1.11

		addSoundFromNow(BLOCK_SHULKER_BOX_CLOSE, BigClientVersion.v1_11, "block.shulker_box.close");
		addSoundFromNow(BLOCK_SHULKER_BOX_OPEN, BigClientVersion.v1_11, "block.shulker_box.open");
		
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_FLOP, BigClientVersion.v1_11, "entity.elder_guardian.flop");
		
		addSoundFromNow(ENTITY_EVOCATION_FANGS_ATTACK, BigClientVersion.v1_11, "entity.evocation_fangs.attack");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_AMBIENT, BigClientVersion.v1_11, "entity.evocation_illager.ambient");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_CAST_SPELL, BigClientVersion.v1_11, "entity.evocation_illager.cast_spell");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_DEATH, BigClientVersion.v1_11, "entity.evocation_illager.death");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_HURT, BigClientVersion.v1_11, "entity.evocation_illager.hurt");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_PREPARE_ATTACK, BigClientVersion.v1_11, "entity.evocation_illager.prepare_attack");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON, BigClientVersion.v1_11, "entity.evocation_illager.prepare_summon");
		addSoundFromNow(ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO, BigClientVersion.v1_11, "entity.evocation_illager.prepare_wololo");
		
		removeSoundFromNow(ENTITY_EXPERIENCE_ORB_TOUCH, BigClientVersion.v1_11); //REMOVED
		
		addSoundFromNow(ENTITY_LLAMA_AMBIENT, BigClientVersion.v1_11, "entity.llama.ambient");
		addSoundFromNow(ENTITY_LLAMA_ANGRY, BigClientVersion.v1_11, "entity.llama.angry");
		addSoundFromNow(ENTITY_LLAMA_CHEST, BigClientVersion.v1_11, "entity.llama.chest");
		addSoundFromNow(ENTITY_LLAMA_DEATH, BigClientVersion.v1_11, "entity.llama.death");
		addSoundFromNow(ENTITY_LLAMA_EAT, BigClientVersion.v1_11, "entity.llama.eat");
		addSoundFromNow(ENTITY_LLAMA_HURT, BigClientVersion.v1_11, "entity.llama.hurt");
		addSoundFromNow(ENTITY_LLAMA_SPIT, BigClientVersion.v1_11, "entity.llama.spit");
		addSoundFromNow(ENTITY_LLAMA_STEP, BigClientVersion.v1_11, "entity.llama.step");
		addSoundFromNow(ENTITY_LLAMA_SWAG, BigClientVersion.v1_11, "entity.llama.swag");

		addSoundFromNow(ENTITY_MULE_CHEST, BigClientVersion.v1_11, "entity.mule.chest");
		
		addSoundFromNow(ENTITY_TNT_PRIMED, BigClientVersion.v1_11, "entity.tnt.primed");
		addSoundFromNow(ENTITY_VEX_AMBIENT, BigClientVersion.v1_11, "entity.vex.ambient");
		addSoundFromNow(ENTITY_VEX_CHARGE, BigClientVersion.v1_11, "entity.vex.charge");
		addSoundFromNow(ENTITY_VEX_DEATH, BigClientVersion.v1_11, "entity.vex.death");
		addSoundFromNow(ENTITY_VEX_HURT, BigClientVersion.v1_11, "entity.vex.hurt");
		
		addSoundFromNow(ENTITY_VINDICATION_ILLAGER_AMBIENT, BigClientVersion.v1_11, "entity.vindication_illager.ambient");
		addSoundFromNow(ENTITY_VINDICATION_ILLAGER_DEATH, BigClientVersion.v1_11, "entity.vindication_illager.death");
		addSoundFromNow(ENTITY_VINDICATION_ILLAGER_HURT, BigClientVersion.v1_11, "entity.vindication_illager.hurt");
		
		addSoundFromNow(ITEM_ARMOR_EQUIP_ELYTRA, BigClientVersion.v1_11, "item.armor.equip_elytra");
		
		addSoundFromNow(ITEM_BOTTLE_EMPTY, BigClientVersion.v1_11, "item.bottle.empty");
		
		addSoundFromNow(ITEM_TOTEM_USE, BigClientVersion.v1_11, "item.totem.use");
		
		//1.10
		addSoundFromNow(BLOCK_ENCHANTMENT_TABLE_USE, BigClientVersion.v1_10, "block.enchantment_table.use");
		
		addSoundFromNow(ENTITY_HUSK_AMBIENT, BigClientVersion.v1_10, "entity.husk.ambient");
		addSoundFromNow(ENTITY_HUSK_DEATH, BigClientVersion.v1_10, "entity.husk.death");
		addSoundFromNow(ENTITY_HUSK_HURT, BigClientVersion.v1_10, "entity.husk.hurt");
		addSoundFromNow(ENTITY_HUSK_STEP, BigClientVersion.v1_10, "entity.husk.step");
		
		addSoundFromNow(ENTITY_POLAR_BEAR_AMBIENT, BigClientVersion.v1_10, "entity.polar_bear.ambient");
		addSoundFromNow(ENTITY_POLAR_BEAR_BABY_AMBIENT, BigClientVersion.v1_10, "entity.polar_bear.baby_ambient");
		addSoundFromNow(ENTITY_POLAR_BEAR_DEATH, BigClientVersion.v1_10, "entity.polar_bear.death");
		addSoundFromNow(ENTITY_POLAR_BEAR_HURT, BigClientVersion.v1_10, "entity.polar_bear.hurt");
		addSoundFromNow(ENTITY_POLAR_BEAR_STEP, BigClientVersion.v1_10, "entity.polar_bear.step");
		addSoundFromNow(ENTITY_POLAR_BEAR_WARNING, BigClientVersion.v1_10, "entity.polar_bear.warning");
		
		addSoundFromNow(ENTITY_STRAY_AMBIENT, BigClientVersion.v1_10, "entity.stray.ambient");
		addSoundFromNow(ENTITY_STRAY_DEATH, BigClientVersion.v1_10, "entity.stray.death");
		addSoundFromNow(ENTITY_STRAY_HURT, BigClientVersion.v1_10, "entity.stray.hurt");
		addSoundFromNow(ENTITY_STRAY_STEP, BigClientVersion.v1_10, "entity.stray.step");
		
		addSoundFromNow(ENTITY_WITHER_SKELETON_AMBIENT, BigClientVersion.v1_10, "entity.wither_skeleton.ambient");
		addSoundFromNow(ENTITY_WITHER_SKELETON_DEATH, BigClientVersion.v1_10, "entity.wither_skeleton.death");
		addSoundFromNow(ENTITY_WITHER_SKELETON_HURT, BigClientVersion.v1_10, "entity.wither_skeleton.hurt");
		addSoundFromNow(ENTITY_WITHER_SKELETON_STEP, BigClientVersion.v1_10, "entity.wither_skeleton.step");

		//1.9
		addSoundFromNow(AMBIENT_CAVE, BigClientVersion.v1_9, "ambient.cave");
		addSoundFromNow(BLOCK_ANVIL_BREAK, BigClientVersion.v1_9, "block.anvil.break");
		addSoundFromNow(BLOCK_ANVIL_DESTROY, BigClientVersion.v1_9, "block.anvil.destroy");
		addSoundFromNow(BLOCK_ANVIL_FALL, BigClientVersion.v1_9, "block.anvil.fall");
		addSoundFromNow(BLOCK_ANVIL_HIT, BigClientVersion.v1_9, "block.anvil.hit");
		addSoundFromNow(BLOCK_ANVIL_LAND, BigClientVersion.v1_9, "block.anvil.land");
		addSoundFromNow(BLOCK_ANVIL_PLACE, BigClientVersion.v1_9, "block.anvil.place");
		addSoundFromNow(BLOCK_ANVIL_STEP, BigClientVersion.v1_9, "block.anvil.step");
		addSoundFromNow(BLOCK_ANVIL_USE, BigClientVersion.v1_9, "block.anvil.use");
		addSoundFromNow(BLOCK_BREWING_STAND_BREW, BigClientVersion.v1_9, "block.brewing_stand.brew");
		addSoundFromNow(BLOCK_CHEST_CLOSE, BigClientVersion.v1_9, "block.chest.close");
		addSoundFromNow(BLOCK_CHEST_LOCKED, BigClientVersion.v1_9, "block.chest.locked");
		addSoundFromNow(BLOCK_CHEST_OPEN, BigClientVersion.v1_9, "block.chest.open");
		addSoundFromNow(BLOCK_CHORUS_FLOWER_DEATH, BigClientVersion.v1_9, "block.chorus_flower.death");
		addSoundFromNow(BLOCK_CHORUS_FLOWER_GROW, BigClientVersion.v1_9, "block.chorus_flower.grow");
		addSoundFromNow(BLOCK_CLOTH_BREAK, BigClientVersion.v1_9, "block.cloth.break");
		addSoundFromNow(BLOCK_CLOTH_FALL, BigClientVersion.v1_9, "block.cloth.fall");
		addSoundFromNow(BLOCK_CLOTH_HIT, BigClientVersion.v1_9, "block.cloth.hit");
		addSoundFromNow(BLOCK_CLOTH_PLACE, BigClientVersion.v1_9, "block.cloth.place");
		addSoundFromNow(BLOCK_CLOTH_STEP, BigClientVersion.v1_9, "block.cloth.step");
		addSoundFromNow(BLOCK_COMPARATOR_CLICK, BigClientVersion.v1_9, "block.comparator.click");
		addSoundFromNow(BLOCK_DISPENSER_DISPENSE, BigClientVersion.v1_9, "block.dispenser.dispense");
		addSoundFromNow(BLOCK_DISPENSER_FAIL, BigClientVersion.v1_9, "block.dispenser.fail");
		addSoundFromNow(BLOCK_DISPENSER_LAUNCH, BigClientVersion.v1_9, "block.dispenser.launch");
		addSoundFromNow(BLOCK_END_GATEWAY_SPAWN, BigClientVersion.v1_9, "block.end_gateway.spawn");
		addSoundFromNow(BLOCK_ENDERCHEST_CLOSE, BigClientVersion.v1_9, "block.enderchest.close");
		addSoundFromNow(BLOCK_ENDERCHEST_OPEN, BigClientVersion.v1_9, "block.enderchest.open");
		addSoundFromNow(BLOCK_FENCE_GATE_CLOSE, BigClientVersion.v1_9, "block.fence_gate.close");
		addSoundFromNow(BLOCK_FENCE_GATE_OPEN, BigClientVersion.v1_9, "block.fence_gate.open");
		addSoundFromNow(BLOCK_FIRE_AMBIENT, BigClientVersion.v1_9, "block.fire.ambient");
		addSoundFromNow(BLOCK_FIRE_EXTINGUISH, BigClientVersion.v1_9, "block.fire.extinguish");
		addSoundFromNow(BLOCK_FURNACE_FIRE_CRACKLE, BigClientVersion.v1_9, "block.furnace.fire_crackle");
		addSoundFromNow(BLOCK_GLASS_BREAK, BigClientVersion.v1_9, "block.glass.break");
		addSoundFromNow(BLOCK_GLASS_FALL, BigClientVersion.v1_9, "block.glass.fall");
		addSoundFromNow(BLOCK_GLASS_HIT, BigClientVersion.v1_9, "block.glass.hit");
		addSoundFromNow(BLOCK_GLASS_PLACE, BigClientVersion.v1_9, "block.glass.place");
		addSoundFromNow(BLOCK_GLASS_STEP, BigClientVersion.v1_9, "block.glass.step");
		addSoundFromNow(BLOCK_GRASS_BREAK, BigClientVersion.v1_9, "block.grass.break");
		addSoundFromNow(BLOCK_GRASS_FALL, BigClientVersion.v1_9, "block.grass.fall");
		addSoundFromNow(BLOCK_GRASS_HIT, BigClientVersion.v1_9, "block.grass.hit");
		addSoundFromNow(BLOCK_GRASS_PLACE, BigClientVersion.v1_9, "block.grass.place");
		addSoundFromNow(BLOCK_GRASS_STEP, BigClientVersion.v1_9, "block.grass.step");
		addSoundFromNow(BLOCK_GRAVEL_BREAK, BigClientVersion.v1_9, "block.gravel.break");
		addSoundFromNow(BLOCK_GRAVEL_FALL, BigClientVersion.v1_9, "block.gravel.fall");
		addSoundFromNow(BLOCK_GRAVEL_HIT, BigClientVersion.v1_9, "block.gravel.hit");
		addSoundFromNow(BLOCK_GRAVEL_PLACE, BigClientVersion.v1_9, "block.gravel.place");
		addSoundFromNow(BLOCK_GRAVEL_STEP, BigClientVersion.v1_9, "block.gravel.step");
		addSoundFromNow(BLOCK_IRON_DOOR_CLOSE, BigClientVersion.v1_9, "block.iron_door.close");
		addSoundFromNow(BLOCK_IRON_DOOR_OPEN, BigClientVersion.v1_9, "block.iron_door.open");
		addSoundFromNow(BLOCK_IRON_TRAPDOOR_CLOSE, BigClientVersion.v1_9, "block.iron_trapdoor.close");
		addSoundFromNow(BLOCK_IRON_TRAPDOOR_OPEN, BigClientVersion.v1_9, "block.iron_trapdoor.open");
		addSoundFromNow(BLOCK_LADDER_BREAK, BigClientVersion.v1_9, "block.ladder.break");
		addSoundFromNow(BLOCK_LADDER_FALL, BigClientVersion.v1_9, "block.ladder.fall");
		addSoundFromNow(BLOCK_LADDER_HIT, BigClientVersion.v1_9, "block.ladder.hit");
		addSoundFromNow(BLOCK_LADDER_PLACE, BigClientVersion.v1_9, "block.ladder.place");
		addSoundFromNow(BLOCK_LADDER_STEP, BigClientVersion.v1_9, "block.ladder.step");
		addSoundFromNow(BLOCK_LAVA_AMBIENT, BigClientVersion.v1_9, "block.lava.ambient");
		addSoundFromNow(BLOCK_LAVA_EXTINGUISH, BigClientVersion.v1_9, "block.lava.extinguish");
		addSoundFromNow(BLOCK_LAVA_POP, BigClientVersion.v1_9, "block.lava.pop");
		addSoundFromNow(BLOCK_LEVER_CLICK, BigClientVersion.v1_9, "block.lever.click");
		addSoundFromNow(BLOCK_METAL_BREAK, BigClientVersion.v1_9, "block.metal.break");
		addSoundFromNow(BLOCK_METAL_FALL, BigClientVersion.v1_9, "block.metal.fall");
		addSoundFromNow(BLOCK_METAL_HIT, BigClientVersion.v1_9, "block.metal.hit");
		addSoundFromNow(BLOCK_METAL_PLACE, BigClientVersion.v1_9, "block.metal.place");
		addSoundFromNow(BLOCK_METAL_STEP, BigClientVersion.v1_9, "block.metal.step");
		addSoundFromNow(BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.metal_pressureplate.click_off");
		addSoundFromNow(BLOCK_METAL_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.metal_pressureplate.click_on");
		addSoundFromNow(BLOCK_NOTE_BASEDRUM, BigClientVersion.v1_9, "block.note.basedrum");
		addSoundFromNow(BLOCK_NOTE_BASS, BigClientVersion.v1_9, "block.note.bass");
		addSoundFromNow(BLOCK_NOTE_HARP, BigClientVersion.v1_9, "block.note.harp");
		addSoundFromNow(BLOCK_NOTE_HAT, BigClientVersion.v1_9, "block.note.hat");
		addSoundFromNow(BLOCK_NOTE_PLING, BigClientVersion.v1_9, "block.note.pling");
		addSoundFromNow(BLOCK_NOTE_SNARE, BigClientVersion.v1_9, "block.note.snare");
		addSoundFromNow(BLOCK_PISTON_CONTRACT, BigClientVersion.v1_9, "block.piston.contract");
		addSoundFromNow(BLOCK_PISTON_EXTEND, BigClientVersion.v1_9, "block.piston.extend");
		addSoundFromNow(BLOCK_PORTAL_AMBIENT, BigClientVersion.v1_9, "block.portal.ambient");
		addSoundFromNow(BLOCK_PORTAL_TRAVEL, BigClientVersion.v1_9, "block.portal.travel");
		addSoundFromNow(BLOCK_PORTAL_TRIGGER, BigClientVersion.v1_9, "block.portal.trigger");
		addSoundFromNow(BLOCK_REDSTONE_TORCH_BURNOUT, BigClientVersion.v1_9, "block.redstone_torch.burnout");
		addSoundFromNow(BLOCK_SAND_BREAK, BigClientVersion.v1_9, "block.sand.break");
		addSoundFromNow(BLOCK_SAND_FALL, BigClientVersion.v1_9, "block.sand.fall");
		addSoundFromNow(BLOCK_SAND_HIT, BigClientVersion.v1_9, "block.sand.hit");
		addSoundFromNow(BLOCK_SAND_PLACE, BigClientVersion.v1_9, "block.sand.place");
		addSoundFromNow(BLOCK_SAND_STEP, BigClientVersion.v1_9, "block.sand.step");
		addSoundFromNow(BLOCK_SLIME_BREAK, BigClientVersion.v1_9, "block.slime.break");
		addSoundFromNow(BLOCK_SLIME_FALL, BigClientVersion.v1_9, "block.slime.fall");
		addSoundFromNow(BLOCK_SLIME_HIT, BigClientVersion.v1_9, "block.slime.hit");
		addSoundFromNow(BLOCK_SLIME_PLACE, BigClientVersion.v1_9, "block.slime.place");
		addSoundFromNow(BLOCK_SLIME_STEP, BigClientVersion.v1_9, "block.slime.step");
		addSoundFromNow(BLOCK_SNOW_BREAK, BigClientVersion.v1_9, "block.snow.break");
		addSoundFromNow(BLOCK_SNOW_FALL, BigClientVersion.v1_9, "block.snow.fall");
		addSoundFromNow(BLOCK_SNOW_HIT, BigClientVersion.v1_9, "block.snow.hit");
		addSoundFromNow(BLOCK_SNOW_PLACE, BigClientVersion.v1_9, "block.snow.place");
		addSoundFromNow(BLOCK_SNOW_STEP, BigClientVersion.v1_9, "block.snow.step");
		addSoundFromNow(BLOCK_STONE_BREAK, BigClientVersion.v1_9, "block.stone.break");
		addSoundFromNow(BLOCK_STONE_FALL, BigClientVersion.v1_9, "block.stone.fall");
		addSoundFromNow(BLOCK_STONE_HIT, BigClientVersion.v1_9, "block.stone.hit");
		addSoundFromNow(BLOCK_STONE_PLACE, BigClientVersion.v1_9, "block.stone.place");
		addSoundFromNow(BLOCK_STONE_STEP, BigClientVersion.v1_9, "block.stone.step");
		addSoundFromNow(BLOCK_STONE_BUTTON_CLICK_OFF, BigClientVersion.v1_9, "block.stone_button.click_off");
		addSoundFromNow(BLOCK_STONE_BUTTON_CLICK_ON, BigClientVersion.v1_9, "block.stone_button.click_on");
		addSoundFromNow(BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.stone_pressureplate.click_off");
		addSoundFromNow(BLOCK_STONE_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.stone_pressureplate.click_on");
		addSoundFromNow(BLOCK_TRIPWIRE_ATTACH, BigClientVersion.v1_9, "block.tripwire.attach");
		addSoundFromNow(BLOCK_TRIPWIRE_CLICK_OFF, BigClientVersion.v1_9, "block.tripwire.click_off");
		addSoundFromNow(BLOCK_TRIPWIRE_CLICK_ON, BigClientVersion.v1_9, "block.tripwire.click_on");
		addSoundFromNow(BLOCK_TRIPWIRE_DETACH, BigClientVersion.v1_9, "block.tripwire.detach");
		addSoundFromNow(BLOCK_WATER_AMBIENT, BigClientVersion.v1_9, "block.water.ambient");
		addSoundFromNow(BLOCK_WATERLILY_PLACE, BigClientVersion.v1_9, "block.waterlily.place");
		addSoundFromNow(BLOCK_WOOD_BREAK, BigClientVersion.v1_9, "block.wood.break");
		addSoundFromNow(BLOCK_WOOD_FALL, BigClientVersion.v1_9, "block.wood.fall");
		addSoundFromNow(BLOCK_WOOD_HIT, BigClientVersion.v1_9, "block.wood.hit");
		addSoundFromNow(BLOCK_WOOD_PLACE, BigClientVersion.v1_9, "block.wood.place");
		addSoundFromNow(BLOCK_WOOD_STEP, BigClientVersion.v1_9, "block.wood.step");
		addSoundFromNow(BLOCK_WOOD_BUTTON_CLICK_OFF, BigClientVersion.v1_9, "block.wood_button.click_off");
		addSoundFromNow(BLOCK_WOOD_BUTTON_CLICK_ON, BigClientVersion.v1_9, "block.wood_button.click_on");
		addSoundFromNow(BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.wood_pressureplate.click_off");
		addSoundFromNow(BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.wood_pressureplate.click_on");
		addSoundFromNow(BLOCK_WOODEN_DOOR_CLOSE, BigClientVersion.v1_9, "block.wooden_door.close");
		addSoundFromNow(BLOCK_WOODEN_DOOR_OPEN, BigClientVersion.v1_9, "block.wooden_door.open");
		addSoundFromNow(BLOCK_WOODEN_TRAPDOOR_CLOSE, BigClientVersion.v1_9, "block.wooden_trapdoor.close");
		addSoundFromNow(BLOCK_WOODEN_TRAPDOOR_OPEN, BigClientVersion.v1_9, "block.wooden_trapdoor.open");
		addSoundFromNow(ENCHANT_THORNS_HIT, BigClientVersion.v1_9, "enchant.thorns.hit");
		addSoundFromNow(ENTITY_ARMORSTAND_BREAK, BigClientVersion.v1_9, "entity.armorstand.break");
		addSoundFromNow(ENTITY_ARMORSTAND_FALL, BigClientVersion.v1_9, "entity.armorstand.fall");
		addSoundFromNow(ENTITY_ARMORSTAND_HIT, BigClientVersion.v1_9, "entity.armorstand.hit");
		addSoundFromNow(ENTITY_ARMORSTAND_PLACE, BigClientVersion.v1_9, "entity.armorstand.place");
		addSoundFromNow(ENTITY_ARROW_HIT, BigClientVersion.v1_9, "entity.arrow.hit");
		addSoundFromNow(ENTITY_ARROW_HIT_PLAYER, BigClientVersion.v1_9, "entity.arrow.hit_player");
		addSoundFromNow(ENTITY_ARROW_SHOOT, BigClientVersion.v1_9, "entity.arrow.shoot");
		addSoundFromNow(ENTITY_BAT_AMBIENT, BigClientVersion.v1_9, "entity.bat.ambient");
		addSoundFromNow(ENTITY_BAT_DEATH, BigClientVersion.v1_9, "entity.bat.death");
		addSoundFromNow(ENTITY_BAT_HURT, BigClientVersion.v1_9, "entity.bat.hurt");
		addSoundFromNow(ENTITY_BAT_LOOP, BigClientVersion.v1_9, "entity.bat.loop");
		addSoundFromNow(ENTITY_BAT_TAKEOFF, BigClientVersion.v1_9, "entity.bat.takeoff");
		addSoundFromNow(ENTITY_BLAZE_AMBIENT, BigClientVersion.v1_9, "entity.blaze.ambient");
		addSoundFromNow(ENTITY_BLAZE_BURN, BigClientVersion.v1_9, "entity.blaze.burn");
		addSoundFromNow(ENTITY_BLAZE_DEATH, BigClientVersion.v1_9, "entity.blaze.death");
		addSoundFromNow(ENTITY_BLAZE_HURT, BigClientVersion.v1_9, "entity.blaze.hurt");
		addSoundFromNow(ENTITY_BLAZE_SHOOT, BigClientVersion.v1_9, "entity.blaze.shoot");
		addSoundFromNow(ENTITY_BOBBER_SPLASH, BigClientVersion.v1_9, "entity.bobber.splash");
		addSoundFromNow(ENTITY_BOBBER_THROW, BigClientVersion.v1_9, "entity.bobber.throw");
		addSoundFromNow(ENTITY_CAT_AMBIENT, BigClientVersion.v1_9, "entity.cat.ambient");
		addSoundFromNow(ENTITY_CAT_DEATH, BigClientVersion.v1_9, "entity.cat.death");
		addSoundFromNow(ENTITY_CAT_HISS, BigClientVersion.v1_9, "entity.cat.hiss");
		addSoundFromNow(ENTITY_CAT_HURT, BigClientVersion.v1_9, "entity.cat.hurt");
		addSoundFromNow(ENTITY_CAT_PURR, BigClientVersion.v1_9, "entity.cat.purr");
		addSoundFromNow(ENTITY_CAT_PURREOW, BigClientVersion.v1_9, "entity.cat.purreow");
		addSoundFromNow(ENTITY_CHICKEN_AMBIENT, BigClientVersion.v1_9, "entity.chicken.ambient");
		addSoundFromNow(ENTITY_CHICKEN_DEATH, BigClientVersion.v1_9, "entity.chicken.death");
		addSoundFromNow(ENTITY_CHICKEN_EGG, BigClientVersion.v1_9, "entity.chicken.egg");
		addSoundFromNow(ENTITY_CHICKEN_HURT, BigClientVersion.v1_9, "entity.chicken.hurt");
		addSoundFromNow(ENTITY_CHICKEN_STEP, BigClientVersion.v1_9, "entity.chicken.step");
		addSoundFromNow(ENTITY_COW_AMBIENT, BigClientVersion.v1_9, "entity.cow.ambient");
		addSoundFromNow(ENTITY_COW_DEATH, BigClientVersion.v1_9, "entity.cow.death");
		addSoundFromNow(ENTITY_COW_HURT, BigClientVersion.v1_9, "entity.cow.hurt");
		addSoundFromNow(ENTITY_COW_MILK, BigClientVersion.v1_9, "entity.cow.milk");
		addSoundFromNow(ENTITY_COW_STEP, BigClientVersion.v1_9, "entity.cow.step");
		addSoundFromNow(ENTITY_CREEPER_DEATH, BigClientVersion.v1_9, "entity.creeper.death");
		addSoundFromNow(ENTITY_CREEPER_HURT, BigClientVersion.v1_9, "entity.creeper.hurt");
		addSoundFromNow(ENTITY_CREEPER_PRIMED, BigClientVersion.v1_9, "entity.creeper.primed");
		addSoundFromNow(ENTITY_DONKEY_AMBIENT, BigClientVersion.v1_9, "entity.donkey.ambient");
		addSoundFromNow(ENTITY_DONKEY_ANGRY, BigClientVersion.v1_9, "entity.donkey.angry");
		addSoundFromNow(ENTITY_DONKEY_CHEST, BigClientVersion.v1_9, "entity.donkey.chest");
		addSoundFromNow(ENTITY_DONKEY_DEATH, BigClientVersion.v1_9, "entity.donkey.death");
		addSoundFromNow(ENTITY_DONKEY_HURT, BigClientVersion.v1_9, "entity.donkey.hurt");
		addSoundFromNow(ENTITY_EGG_THROW, BigClientVersion.v1_9, "entity.egg.throw");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_AMBIENT, BigClientVersion.v1_9, "entity.elder_guardian.ambient");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_9, "entity.elder_guardian.ambient_land");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_CURSE, BigClientVersion.v1_9, "entity.elder_guardian.curse");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_DEATH, BigClientVersion.v1_9, "entity.elder_guardian.death");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_DEATH_LAND, BigClientVersion.v1_9, "entity.elder_guardian.death_land");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_HURT, BigClientVersion.v1_9, "entity.elder_guardian.hurt");
		addSoundFromNow(ENTITY_ELDER_GUARDIAN_HURT_LAND, BigClientVersion.v1_9, "entity.elder_guardian.hurt_land");
		addSoundFromNow(ENTITY_ENDERDRAGON_AMBIENT, BigClientVersion.v1_9, "entity.enderdragon.ambient");
		addSoundFromNow(ENTITY_ENDERDRAGON_DEATH, BigClientVersion.v1_9, "entity.enderdragon.death");
		addSoundFromNow(ENTITY_ENDERDRAGON_FLAP, BigClientVersion.v1_9, "entity.enderdragon.flap");
		addSoundFromNow(ENTITY_ENDERDRAGON_GROWL, BigClientVersion.v1_9, "entity.enderdragon.growl");
		addSoundFromNow(ENTITY_ENDERDRAGON_HURT, BigClientVersion.v1_9, "entity.enderdragon.hurt");
		addSoundFromNow(ENTITY_ENDERDRAGON_SHOOT, BigClientVersion.v1_9, "entity.enderdragon.shoot");
		addSoundFromNow(ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, BigClientVersion.v1_9, "entity.enderdragon_fireball.explode");
		addSoundFromNow(ENTITY_ENDEREYE_LAUNCH, BigClientVersion.v1_9, "entity.endereye.launch");
		addSoundFromNow(ENTITY_ENDERMEN_AMBIENT, BigClientVersion.v1_9, "entity.endermen.ambient");
		addSoundFromNow(ENTITY_ENDERMEN_DEATH, BigClientVersion.v1_9, "entity.endermen.death");
		addSoundFromNow(ENTITY_ENDERMEN_HURT, BigClientVersion.v1_9, "entity.endermen.hurt");
		addSoundFromNow(ENTITY_ENDERMEN_SCREAM, BigClientVersion.v1_9, "entity.endermen.scream");
		addSoundFromNow(ENTITY_ENDERMEN_STARE, BigClientVersion.v1_9, "entity.endermen.stare");
		addSoundFromNow(ENTITY_ENDERMEN_TELEPORT, BigClientVersion.v1_9, "entity.endermen.teleport");
		addSoundFromNow(ENTITY_ENDERMITE_AMBIENT, BigClientVersion.v1_9, "entity.endermite.ambient");
		addSoundFromNow(ENTITY_ENDERMITE_DEATH, BigClientVersion.v1_9, "entity.endermite.death");
		addSoundFromNow(ENTITY_ENDERMITE_HURT, BigClientVersion.v1_9, "entity.endermite.hurt");
		addSoundFromNow(ENTITY_ENDERMITE_STEP, BigClientVersion.v1_9, "entity.endermite.step");
		addSoundFromNow(ENTITY_ENDERPEARL_THROW, BigClientVersion.v1_9, "entity.enderpearl.throw");
		addSoundFromNow(ENTITY_EXPERIENCE_BOTTLE_THROW, BigClientVersion.v1_9, "entity.experience_bottle.throw");
		addSoundFromNow(ENTITY_EXPERIENCE_ORB_PICKUP, BigClientVersion.v1_9, "entity.experience_orb.pickup");
		addSoundFromNow(ENTITY_EXPERIENCE_ORB_TOUCH, BigClientVersion.v1_9, "entity.experience_orb.touch");
		addSoundFromNow(ENTITY_FIREWORK_BLAST, BigClientVersion.v1_9, "entity.firework.blast");
		addSoundFromNow(ENTITY_FIREWORK_BLAST_FAR, BigClientVersion.v1_9, "entity.firework.blast_far");
		addSoundFromNow(ENTITY_FIREWORK_LARGE_BLAST, BigClientVersion.v1_9, "entity.firework.large_blast");
		addSoundFromNow(ENTITY_FIREWORK_LARGE_BLAST_FAR, BigClientVersion.v1_9, "entity.firework.large_blast_far");
		addSoundFromNow(ENTITY_FIREWORK_LAUNCH, BigClientVersion.v1_9, "entity.firework.launch");
		addSoundFromNow(ENTITY_FIREWORK_SHOOT, BigClientVersion.v1_9, "entity.firework.shoot");
		addSoundFromNow(ENTITY_FIREWORK_TWINKLE, BigClientVersion.v1_9, "entity.firework.twinkle");
		addSoundFromNow(ENTITY_FIREWORK_TWINKLE_FAR, BigClientVersion.v1_9, "entity.firework.twinkle_far");
		addSoundFromNow(ENTITY_GENERIC_BIG_FALL, BigClientVersion.v1_9, "entity.generic.big_fall");
		addSoundFromNow(ENTITY_GENERIC_BURN, BigClientVersion.v1_9, "entity.generic.burn");
		addSoundFromNow(ENTITY_GENERIC_DEATH, BigClientVersion.v1_9, "entity.generic.death");
		addSoundFromNow(ENTITY_GENERIC_DRINK, BigClientVersion.v1_9, "entity.generic.drink");
		addSoundFromNow(ENTITY_GENERIC_EAT, BigClientVersion.v1_9, "entity.generic.eat");
		addSoundFromNow(ENTITY_GENERIC_EXPLODE, BigClientVersion.v1_9, "entity.generic.explode");
		addSoundFromNow(ENTITY_GENERIC_EXTINGUISH_FIRE, BigClientVersion.v1_9, "entity.generic.extinguish_fire");
		addSoundFromNow(ENTITY_GENERIC_HURT, BigClientVersion.v1_9, "entity.generic.hurt");
		addSoundFromNow(ENTITY_GENERIC_SMALL_FALL, BigClientVersion.v1_9, "entity.generic.small_fall");
		addSoundFromNow(ENTITY_GENERIC_SPLASH, BigClientVersion.v1_9, "entity.generic.splash");
		addSoundFromNow(ENTITY_GENERIC_SWIM, BigClientVersion.v1_9, "entity.generic.swim");
		addSoundFromNow(ENTITY_GHAST_AMBIENT, BigClientVersion.v1_9, "entity.ghast.ambient");
		addSoundFromNow(ENTITY_GHAST_DEATH, BigClientVersion.v1_9, "entity.ghast.death");
		addSoundFromNow(ENTITY_GHAST_HURT, BigClientVersion.v1_9, "entity.ghast.hurt");
		addSoundFromNow(ENTITY_GHAST_SCREAM, BigClientVersion.v1_9, "entity.ghast.scream");
		addSoundFromNow(ENTITY_GHAST_SHOOT, BigClientVersion.v1_9, "entity.ghast.shoot");
		addSoundFromNow(ENTITY_GHAST_WARN, BigClientVersion.v1_9, "entity.ghast.warn");
		addSoundFromNow(ENTITY_GUARDIAN_AMBIENT, BigClientVersion.v1_9, "entity.guardian.ambient");
		addSoundFromNow(ENTITY_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_9, "entity.guardian.ambient_land");
		addSoundFromNow(ENTITY_GUARDIAN_ATTACK, BigClientVersion.v1_9, "entity.guardian.attack");
		addSoundFromNow(ENTITY_GUARDIAN_DEATH, BigClientVersion.v1_9, "entity.guardian.death");
		addSoundFromNow(ENTITY_GUARDIAN_DEATH_LAND, BigClientVersion.v1_9, "entity.guardian.death_land");
		addSoundFromNow(ENTITY_GUARDIAN_FLOP, BigClientVersion.v1_9, "entity.guardian.flop");
		addSoundFromNow(ENTITY_GUARDIAN_HURT, BigClientVersion.v1_9, "entity.guardian.hurt");
		addSoundFromNow(ENTITY_GUARDIAN_HURT_LAND, BigClientVersion.v1_9, "entity.guardian.hurt_land");
		addSoundFromNow(ENTITY_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.horse.ambient");
		addSoundFromNow(ENTITY_HORSE_ANGRY, BigClientVersion.v1_9, "entity.horse.angry");
		addSoundFromNow(ENTITY_HORSE_ARMOR, BigClientVersion.v1_9, "entity.horse.armor");
		addSoundFromNow(ENTITY_HORSE_BREATHE, BigClientVersion.v1_9, "entity.horse.breathe");
		addSoundFromNow(ENTITY_HORSE_DEATH, BigClientVersion.v1_9, "entity.horse.death");
		addSoundFromNow(ENTITY_HORSE_EAT, BigClientVersion.v1_9, "entity.horse.eat");
		addSoundFromNow(ENTITY_HORSE_GALLOP, BigClientVersion.v1_9, "entity.horse.gallop");
		addSoundFromNow(ENTITY_HORSE_HURT, BigClientVersion.v1_9, "entity.horse.hurt");
		addSoundFromNow(ENTITY_HORSE_JUMP, BigClientVersion.v1_9, "entity.horse.jump");
		addSoundFromNow(ENTITY_HORSE_LAND, BigClientVersion.v1_9, "entity.horse.land");
		addSoundFromNow(ENTITY_HORSE_SADDLE, BigClientVersion.v1_9, "entity.horse.saddle");
		addSoundFromNow(ENTITY_HORSE_STEP, BigClientVersion.v1_9, "entity.horse.step");
		addSoundFromNow(ENTITY_HORSE_STEP_WOOD, BigClientVersion.v1_9, "entity.horse.step_wood");
		addSoundFromNow(ENTITY_HOSTILE_BIG_FALL, BigClientVersion.v1_9, "entity.hostile.big_fall");
		addSoundFromNow(ENTITY_HOSTILE_DEATH, BigClientVersion.v1_9, "entity.hostile.death");
		addSoundFromNow(ENTITY_HOSTILE_HURT, BigClientVersion.v1_9, "entity.hostile.hurt");
		addSoundFromNow(ENTITY_HOSTILE_SMALL_FALL, BigClientVersion.v1_9, "entity.hostile.small_fall");
		addSoundFromNow(ENTITY_HOSTILE_SPLASH, BigClientVersion.v1_9, "entity.hostile.splash");
		addSoundFromNow(ENTITY_HOSTILE_SWIM, BigClientVersion.v1_9, "entity.hostile.swim");
		addSoundFromNow(ENTITY_IRONGOLEM_ATTACK, BigClientVersion.v1_9, "entity.irongolem.attack");
		addSoundFromNow(ENTITY_IRONGOLEM_DEATH, BigClientVersion.v1_9, "entity.irongolem.death");
		addSoundFromNow(ENTITY_IRONGOLEM_HURT, BigClientVersion.v1_9, "entity.irongolem.hurt");
		addSoundFromNow(ENTITY_IRONGOLEM_STEP, BigClientVersion.v1_9, "entity.irongolem.step");
		addSoundFromNow(ENTITY_ITEM_BREAK, BigClientVersion.v1_9, "entity.item.break");
		addSoundFromNow(ENTITY_ITEM_PICKUP, BigClientVersion.v1_9, "entity.item.pickup");
		addSoundFromNow(ENTITY_ITEMFRAME_ADD_ITEM, BigClientVersion.v1_9, "entity.itemframe.add_item");
		addSoundFromNow(ENTITY_ITEMFRAME_BREAK, BigClientVersion.v1_9, "entity.itemframe.break");
		addSoundFromNow(ENTITY_ITEMFRAME_PLACE, BigClientVersion.v1_9, "entity.itemframe.place");
		addSoundFromNow(ENTITY_ITEMFRAME_REMOVE_ITEM, BigClientVersion.v1_9, "entity.itemframe.remove_item");
		addSoundFromNow(ENTITY_ITEMFRAME_ROTATE_ITEM, BigClientVersion.v1_9, "entity.itemframe.rotate_item");
		addSoundFromNow(ENTITY_LEASHKNOT_BREAK, BigClientVersion.v1_9, "entity.leashknot.break");
		addSoundFromNow(ENTITY_LEASHKNOT_PLACE, BigClientVersion.v1_9, "entity.leashknot.place");
		addSoundFromNow(ENTITY_LIGHTNING_IMPACT, BigClientVersion.v1_9, "entity.lightning.impact");
		addSoundFromNow(ENTITY_LIGHTNING_THUNDER, BigClientVersion.v1_9, "entity.lightning.thunder");
		addSoundFromNow(ENTITY_LINGERINGPOTION_THROW, BigClientVersion.v1_9, "entity.lingeringpotion.throw");
		addSoundFromNow(ENTITY_MAGMACUBE_DEATH, BigClientVersion.v1_9, "entity.magmacube.death");
		addSoundFromNow(ENTITY_MAGMACUBE_HURT, BigClientVersion.v1_9, "entity.magmacube.hurt");
		addSoundFromNow(ENTITY_MAGMACUBE_JUMP, BigClientVersion.v1_9, "entity.magmacube.jump");
		addSoundFromNow(ENTITY_MAGMACUBE_SQUISH, BigClientVersion.v1_9, "entity.magmacube.squish");
		addSoundFromNow(ENTITY_MINECART_INSIDE, BigClientVersion.v1_9, "entity.minecart.inside");
		addSoundFromNow(ENTITY_MINECART_RIDING, BigClientVersion.v1_9, "entity.minecart.riding");
		addSoundFromNow(ENTITY_MOOSHROOM_SHEAR, BigClientVersion.v1_9, "entity.mooshroom.shear");
		addSoundFromNow(ENTITY_MULE_AMBIENT, BigClientVersion.v1_9, "entity.mule.ambient");
		addSoundFromNow(ENTITY_MULE_DEATH, BigClientVersion.v1_9, "entity.mule.death");
		addSoundFromNow(ENTITY_MULE_HURT, BigClientVersion.v1_9, "entity.mule.hurt");
		addSoundFromNow(ENTITY_PAINTING_BREAK, BigClientVersion.v1_9, "entity.painting.break");
		addSoundFromNow(ENTITY_PAINTING_PLACE, BigClientVersion.v1_9, "entity.painting.place");
		addSoundFromNow(ENTITY_PIG_AMBIENT, BigClientVersion.v1_9, "entity.pig.ambient");
		addSoundFromNow(ENTITY_PIG_DEATH, BigClientVersion.v1_9, "entity.pig.death");
		addSoundFromNow(ENTITY_PIG_HURT, BigClientVersion.v1_9, "entity.pig.hurt");
		addSoundFromNow(ENTITY_PIG_SADDLE, BigClientVersion.v1_9, "entity.pig.saddle");
		addSoundFromNow(ENTITY_PIG_STEP, BigClientVersion.v1_9, "entity.pig.step");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_CRIT, BigClientVersion.v1_9, "entity.player.attack.crit");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_KNOCKBACK, BigClientVersion.v1_9, "entity.player.attack.knockback");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_NODAMAGE, BigClientVersion.v1_9, "entity.player.attack.nodamage");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_STRONG, BigClientVersion.v1_9, "entity.player.attack.strong");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_SWEEP, BigClientVersion.v1_9, "entity.player.attack.sweep");
		addSoundFromNow(ENTITY_PLAYER_ATTACK_WEAK, BigClientVersion.v1_9, "entity.player.attack.weak");
		addSoundFromNow(ENTITY_PLAYER_BIG_FALL, BigClientVersion.v1_9, "entity.player.big_fall");
		addSoundFromNow(ENTITY_PLAYER_BREATH, BigClientVersion.v1_9, "entity.player.breath");
		addSoundFromNow(ENTITY_PLAYER_BURP, BigClientVersion.v1_9, "entity.player.burp");
		addSoundFromNow(ENTITY_PLAYER_DEATH, BigClientVersion.v1_9, "entity.player.death");
		addSoundFromNow(ENTITY_PLAYER_HURT, BigClientVersion.v1_9, "entity.player.hurt");
		addSoundFromNow(ENTITY_PLAYER_LEVELUP, BigClientVersion.v1_9, "entity.player.levelup");
		addSoundFromNow(ENTITY_PLAYER_SMALL_FALL, BigClientVersion.v1_9, "entity.player.small_fall");
		addSoundFromNow(ENTITY_PLAYER_SPLASH, BigClientVersion.v1_9, "entity.player.splash");
		addSoundFromNow(ENTITY_PLAYER_SWIM, BigClientVersion.v1_9, "entity.player.swim");
		addSoundFromNow(ENTITY_RABBIT_AMBIENT, BigClientVersion.v1_9, "entity.rabbit.ambient");
		addSoundFromNow(ENTITY_RABBIT_ATTACK, BigClientVersion.v1_9, "entity.rabbit.attack");
		addSoundFromNow(ENTITY_RABBIT_DEATH, BigClientVersion.v1_9, "entity.rabbit.death");
		addSoundFromNow(ENTITY_RABBIT_HURT, BigClientVersion.v1_9, "entity.rabbit.hurt");
		addSoundFromNow(ENTITY_RABBIT_JUMP, BigClientVersion.v1_9, "entity.rabbit.jump");
		addSoundFromNow(ENTITY_SHEEP_AMBIENT, BigClientVersion.v1_9, "entity.sheep.ambient");
		addSoundFromNow(ENTITY_SHEEP_DEATH, BigClientVersion.v1_9, "entity.sheep.death");
		addSoundFromNow(ENTITY_SHEEP_HURT, BigClientVersion.v1_9, "entity.sheep.hurt");
		addSoundFromNow(ENTITY_SHEEP_SHEAR, BigClientVersion.v1_9, "entity.sheep.shear");
		addSoundFromNow(ENTITY_SHEEP_STEP, BigClientVersion.v1_9, "entity.sheep.step");
		addSoundFromNow(ENTITY_SHULKER_AMBIENT, BigClientVersion.v1_9, "entity.shulker.ambient");
		addSoundFromNow(ENTITY_SHULKER_CLOSE, BigClientVersion.v1_9, "entity.shulker.close");
		addSoundFromNow(ENTITY_SHULKER_DEATH, BigClientVersion.v1_9, "entity.shulker.death");
		addSoundFromNow(ENTITY_SHULKER_HURT, BigClientVersion.v1_9, "entity.shulker.hurt");
		addSoundFromNow(ENTITY_SHULKER_HURT_CLOSED, BigClientVersion.v1_9, "entity.shulker.hurt_closed");
		addSoundFromNow(ENTITY_SHULKER_OPEN, BigClientVersion.v1_9, "entity.shulker.open");
		addSoundFromNow(ENTITY_SHULKER_SHOOT, BigClientVersion.v1_9, "entity.shulker.shoot");
		addSoundFromNow(ENTITY_SHULKER_TELEPORT, BigClientVersion.v1_9, "entity.shulker.teleport");
		addSoundFromNow(ENTITY_SHULKER_BULLET_HIT, BigClientVersion.v1_9, "entity.shulker_bullet.hit");
		addSoundFromNow(ENTITY_SHULKER_BULLET_HURT, BigClientVersion.v1_9, "entity.shulker_bullet.hurt");
		addSoundFromNow(ENTITY_SILVERFISH_AMBIENT, BigClientVersion.v1_9, "entity.silverfish.ambient");
		addSoundFromNow(ENTITY_SILVERFISH_DEATH, BigClientVersion.v1_9, "entity.silverfish.death");
		addSoundFromNow(ENTITY_SILVERFISH_HURT, BigClientVersion.v1_9, "entity.silverfish.hurt");
		addSoundFromNow(ENTITY_SILVERFISH_STEP, BigClientVersion.v1_9, "entity.silverfish.step");
		addSoundFromNow(ENTITY_SKELETON_AMBIENT, BigClientVersion.v1_9, "entity.skeleton.ambient");
		addSoundFromNow(ENTITY_SKELETON_DEATH, BigClientVersion.v1_9, "entity.skeleton.death");
		addSoundFromNow(ENTITY_SKELETON_HURT, BigClientVersion.v1_9, "entity.skeleton.hurt");
		addSoundFromNow(ENTITY_SKELETON_SHOOT, BigClientVersion.v1_9, "entity.skeleton.shoot");
		addSoundFromNow(ENTITY_SKELETON_STEP, BigClientVersion.v1_9, "entity.skeleton.step");
		addSoundFromNow(ENTITY_SKELETON_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.skeleton_horse.ambient");
		addSoundFromNow(ENTITY_SKELETON_HORSE_DEATH, BigClientVersion.v1_9, "entity.skeleton_horse.death");
		addSoundFromNow(ENTITY_SKELETON_HORSE_HURT, BigClientVersion.v1_9, "entity.skeleton_horse.hurt");
		addSoundFromNow(ENTITY_SLIME_ATTACK, BigClientVersion.v1_9, "entity.slime.attack");
		addSoundFromNow(ENTITY_SLIME_DEATH, BigClientVersion.v1_9, "entity.slime.death");
		addSoundFromNow(ENTITY_SLIME_HURT, BigClientVersion.v1_9, "entity.slime.hurt");
		addSoundFromNow(ENTITY_SLIME_JUMP, BigClientVersion.v1_9, "entity.slime.jump");
		addSoundFromNow(ENTITY_SLIME_SQUISH, BigClientVersion.v1_9, "entity.slime.squish");
		addSoundFromNow(ENTITY_SMALL_MAGMACUBE_DEATH, BigClientVersion.v1_9, "entity.small_magmacube.death");
		addSoundFromNow(ENTITY_SMALL_MAGMACUBE_HURT, BigClientVersion.v1_9, "entity.small_magmacube.hurt");
		addSoundFromNow(ENTITY_SMALL_MAGMACUBE_SQUISH, BigClientVersion.v1_9, "entity.small_magmacube.squish");
		addSoundFromNow(ENTITY_SMALL_SLIME_DEATH, BigClientVersion.v1_9, "entity.small_slime.death");
		addSoundFromNow(ENTITY_SMALL_SLIME_HURT, BigClientVersion.v1_9, "entity.small_slime.hurt");
		addSoundFromNow(ENTITY_SMALL_SLIME_JUMP, BigClientVersion.v1_9, "entity.small_slime.jump");
		addSoundFromNow(ENTITY_SMALL_SLIME_SQUISH, BigClientVersion.v1_9, "entity.small_slime.squish");
		addSoundFromNow(ENTITY_SNOWBALL_THROW, BigClientVersion.v1_9, "entity.snowball.throw");
		addSoundFromNow(ENTITY_SNOWMAN_AMBIENT, BigClientVersion.v1_9, "entity.snowman.ambient");
		addSoundFromNow(ENTITY_SNOWMAN_DEATH, BigClientVersion.v1_9, "entity.snowman.death");
		addSoundFromNow(ENTITY_SNOWMAN_HURT, BigClientVersion.v1_9, "entity.snowman.hurt");
		addSoundFromNow(ENTITY_SNOWMAN_SHOOT, BigClientVersion.v1_9, "entity.snowman.shoot");
		addSoundFromNow(ENTITY_SPIDER_AMBIENT, BigClientVersion.v1_9, "entity.spider.ambient");
		addSoundFromNow(ENTITY_SPIDER_DEATH, BigClientVersion.v1_9, "entity.spider.death");
		addSoundFromNow(ENTITY_SPIDER_HURT, BigClientVersion.v1_9, "entity.spider.hurt");
		addSoundFromNow(ENTITY_SPIDER_STEP, BigClientVersion.v1_9, "entity.spider.step");
		addSoundFromNow(ENTITY_SPLASH_POTION_BREAK, BigClientVersion.v1_9, "entity.splash_potion.break");
		addSoundFromNow(ENTITY_SPLASH_POTION_THROW, BigClientVersion.v1_9, "entity.splash_potion.throw");
		addSoundFromNow(ENTITY_SQUID_AMBIENT, BigClientVersion.v1_9, "entity.squid.ambient");
		addSoundFromNow(ENTITY_SQUID_DEATH, BigClientVersion.v1_9, "entity.squid.death");
		addSoundFromNow(ENTITY_SQUID_HURT, BigClientVersion.v1_9, "entity.squid.hurt");
		addSoundFromNow(ENTITY_TNT_PRIMED, BigClientVersion.v1_9, "entity.tnt.primed");
		addSoundFromNow(ENTITY_VILLAGER_AMBIENT, BigClientVersion.v1_9, "entity.villager.ambient");
		addSoundFromNow(ENTITY_VILLAGER_DEATH, BigClientVersion.v1_9, "entity.villager.death");
		addSoundFromNow(ENTITY_VILLAGER_HURT, BigClientVersion.v1_9, "entity.villager.hurt");
		addSoundFromNow(ENTITY_VILLAGER_NO, BigClientVersion.v1_9, "entity.villager.no");
		addSoundFromNow(ENTITY_VILLAGER_TRADING, BigClientVersion.v1_9, "entity.villager.trading");
		addSoundFromNow(ENTITY_VILLAGER_YES, BigClientVersion.v1_9, "entity.villager.yes");
		addSoundFromNow(ENTITY_WITCH_AMBIENT, BigClientVersion.v1_9, "entity.witch.ambient");
		addSoundFromNow(ENTITY_WITCH_DEATH, BigClientVersion.v1_9, "entity.witch.death");
		addSoundFromNow(ENTITY_WITCH_DRINK, BigClientVersion.v1_9, "entity.witch.drink");
		addSoundFromNow(ENTITY_WITCH_HURT, BigClientVersion.v1_9, "entity.witch.hurt");
		addSoundFromNow(ENTITY_WITCH_THROW, BigClientVersion.v1_9, "entity.witch.throw");
		addSoundFromNow(ENTITY_WITHER_AMBIENT, BigClientVersion.v1_9, "entity.wither.ambient");
		addSoundFromNow(ENTITY_WITHER_BREAK_BLOCK, BigClientVersion.v1_9, "entity.wither.break_block");
		addSoundFromNow(ENTITY_WITHER_DEATH, BigClientVersion.v1_9, "entity.wither.death");
		addSoundFromNow(ENTITY_WITHER_HURT, BigClientVersion.v1_9, "entity.wither.hurt");
		addSoundFromNow(ENTITY_WITHER_SHOOT, BigClientVersion.v1_9, "entity.wither.shoot");
		addSoundFromNow(ENTITY_WITHER_SPAWN, BigClientVersion.v1_9, "entity.wither.spawn");
		addSoundFromNow(ENTITY_WOLF_AMBIENT, BigClientVersion.v1_9, "entity.wolf.ambient");
		addSoundFromNow(ENTITY_WOLF_DEATH, BigClientVersion.v1_9, "entity.wolf.death");
		addSoundFromNow(ENTITY_WOLF_GROWL, BigClientVersion.v1_9, "entity.wolf.growl");
		addSoundFromNow(ENTITY_WOLF_HOWL, BigClientVersion.v1_9, "entity.wolf.howl");
		addSoundFromNow(ENTITY_WOLF_HURT, BigClientVersion.v1_9, "entity.wolf.hurt");
		addSoundFromNow(ENTITY_WOLF_PANT, BigClientVersion.v1_9, "entity.wolf.pant");
		addSoundFromNow(ENTITY_WOLF_SHAKE, BigClientVersion.v1_9, "entity.wolf.shake");
		addSoundFromNow(ENTITY_WOLF_STEP, BigClientVersion.v1_9, "entity.wolf.step");
		addSoundFromNow(ENTITY_WOLF_WHINE, BigClientVersion.v1_9, "entity.wolf.whine");
		addSoundFromNow(ENTITY_ZOMBIE_AMBIENT, BigClientVersion.v1_9, "entity.zombie.ambient");
		addSoundFromNow(ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, BigClientVersion.v1_9, "entity.zombie.attack_door_wood");
		addSoundFromNow(ENTITY_ZOMBIE_ATTACK_IRON_DOOR, BigClientVersion.v1_9, "entity.zombie.attack_iron_door");
		addSoundFromNow(ENTITY_ZOMBIE_BREAK_DOOR_WOOD, BigClientVersion.v1_9, "entity.zombie.break_door_wood");
		addSoundFromNow(ENTITY_ZOMBIE_DEATH, BigClientVersion.v1_9, "entity.zombie.death");
		addSoundFromNow(ENTITY_ZOMBIE_HURT, BigClientVersion.v1_9, "entity.zombie.hurt");
		addSoundFromNow(ENTITY_ZOMBIE_INFECT, BigClientVersion.v1_9, "entity.zombie.infect");
		addSoundFromNow(ENTITY_ZOMBIE_STEP, BigClientVersion.v1_9, "entity.zombie.step");
		addSoundFromNow(ENTITY_ZOMBIE_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.zombie_horse.ambient");
		addSoundFromNow(ENTITY_ZOMBIE_HORSE_DEATH, BigClientVersion.v1_9, "entity.zombie_horse.death");
		addSoundFromNow(ENTITY_ZOMBIE_HORSE_HURT, BigClientVersion.v1_9, "entity.zombie_horse.hurt");
		addSoundFromNow(ENTITY_ZOMBIE_PIG_AMBIENT, BigClientVersion.v1_9, "entity.zombie_pig.ambient");
		addSoundFromNow(ENTITY_ZOMBIE_PIG_ANGRY, BigClientVersion.v1_9, "entity.zombie_pig.angry");
		addSoundFromNow(ENTITY_ZOMBIE_PIG_DEATH, BigClientVersion.v1_9, "entity.zombie_pig.death");
		addSoundFromNow(ENTITY_ZOMBIE_PIG_HURT, BigClientVersion.v1_9, "entity.zombie_pig.hurt");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_AMBIENT, BigClientVersion.v1_9, "entity.zombie_villager.ambient");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_CONVERTED, BigClientVersion.v1_9, "entity.zombie_villager.converted");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_CURE, BigClientVersion.v1_9, "entity.zombie_villager.cure");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_DEATH, BigClientVersion.v1_9, "entity.zombie_villager.death");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_HURT, BigClientVersion.v1_9, "entity.zombie_villager.hurt");
		addSoundFromNow(ENTITY_ZOMBIE_VILLAGER_STEP, BigClientVersion.v1_9, "entity.zombie_villager.step");
		addSoundFromNow(ITEM_ARMOR_EQUIP_CHAIN, BigClientVersion.v1_9, "item.armor.equip_chain");
		addSoundFromNow(ITEM_ARMOR_EQUIP_DIAMOND, BigClientVersion.v1_9, "item.armor.equip_diamond");
		addSoundFromNow(ITEM_ARMOR_EQUIP_GENERIC, BigClientVersion.v1_9, "item.armor.equip_generic");
		addSoundFromNow(ITEM_ARMOR_EQUIP_GOLD, BigClientVersion.v1_9, "item.armor.equip_gold");
		addSoundFromNow(ITEM_ARMOR_EQUIP_IRON, BigClientVersion.v1_9, "item.armor.equip_iron");
		addSoundFromNow(ITEM_ARMOR_EQUIP_LEATHER, BigClientVersion.v1_9, "item.armor.equip_leather");
		addSoundFromNow(ITEM_BOTTLE_FILL, BigClientVersion.v1_9, "item.bottle.fill");
		addSoundFromNow(ITEM_BOTTLE_FILL_DRAGONBREATH, BigClientVersion.v1_9, "item.bottle.fill_dragonbreath");
		addSoundFromNow(ITEM_BUCKET_EMPTY, BigClientVersion.v1_9, "item.bucket.empty");
		addSoundFromNow(ITEM_BUCKET_EMPTY_LAVA, BigClientVersion.v1_9, "item.bucket.empty_lava");
		addSoundFromNow(ITEM_BUCKET_FILL, BigClientVersion.v1_9, "item.bucket.fill");
		addSoundFromNow(ITEM_BUCKET_FILL_LAVA, BigClientVersion.v1_9, "item.bucket.fill_lava");
		addSoundFromNow(ITEM_CHORUS_FRUIT_TELEPORT, BigClientVersion.v1_9, "item.chorus_fruit.teleport");
		addSoundFromNow(ITEM_ELYTRA_FLYING, BigClientVersion.v1_9, "item.elytra.flying");
		addSoundFromNow(ITEM_FIRECHARGE_USE, BigClientVersion.v1_9, "item.firecharge.use");
		addSoundFromNow(ITEM_FLINTANDSTEEL_USE, BigClientVersion.v1_9, "item.flintandsteel.use");
		addSoundFromNow(ITEM_HOE_TILL, BigClientVersion.v1_9, "item.hoe.till");
		addSoundFromNow(ITEM_SHIELD_BLOCK, BigClientVersion.v1_9, "item.shield.block");
		addSoundFromNow(ITEM_SHIELD_BREAK, BigClientVersion.v1_9, "item.shield.break");
		addSoundFromNow(ITEM_SHOVEL_FLATTEN, BigClientVersion.v1_9, "item.shovel.flatten");
		addSoundFromNow(MUSIC_CREATIVE, BigClientVersion.v1_9, "music.creative");
		addSoundFromNow(MUSIC_CREDITS, BigClientVersion.v1_9, "music.credits");
		addSoundFromNow(MUSIC_DRAGON, BigClientVersion.v1_9, "music.dragon");
		addSoundFromNow(MUSIC_END, BigClientVersion.v1_9, "music.end");
		addSoundFromNow(MUSIC_GAME, BigClientVersion.v1_9, "music.game");
		addSoundFromNow(MUSIC_MENU, BigClientVersion.v1_9, "music.menu");
		addSoundFromNow(MUSIC_NETHER, BigClientVersion.v1_9, "music.nether");
		addSoundFromNow(RECORD_11, BigClientVersion.v1_9, "record.11");
		addSoundFromNow(RECORD_13, BigClientVersion.v1_9, "record.13");
		addSoundFromNow(RECORD_BLOCKS, BigClientVersion.v1_9, "record.blocks");
		addSoundFromNow(RECORD_CAT, BigClientVersion.v1_9, "record.cat");
		addSoundFromNow(RECORD_CHIRP, BigClientVersion.v1_9, "record.chirp");
		addSoundFromNow(RECORD_FAR, BigClientVersion.v1_9, "record.far");
		addSoundFromNow(RECORD_MALL, BigClientVersion.v1_9, "record.mall");
		addSoundFromNow(RECORD_MELLOHI, BigClientVersion.v1_9, "record.mellohi");
		addSoundFromNow(RECORD_STAL, BigClientVersion.v1_9, "record.stal");
		addSoundFromNow(RECORD_STRAD, BigClientVersion.v1_9, "record.strad");
		addSoundFromNow(RECORD_WAIT, BigClientVersion.v1_9, "record.wait");
		addSoundFromNow(RECORD_WARD, BigClientVersion.v1_9, "record.ward");
		addSoundFromNow(UI_BUTTON_CLICK, BigClientVersion.v1_9, "ui.button.click");
		addSoundFromNow(WEATHER_RAIN, BigClientVersion.v1_9, "weather.rain");
		addSoundFromNow(WEATHER_RAIN_ABOVE, BigClientVersion.v1_9, "weather.rain.above");

		//TODO implement sound for 1.8?
	}
	
	private static void addSoundFromNow(SoundEffect effect, BigClientVersion version, String id) {
		for(BigClientVersion versionentry : BigClientVersion.values()) {
			if(versionentry.getProtocollVersion().getProtocollVersion() >= version.getProtocollVersion().getProtocollVersion()) {
				effect.versions.put(versionentry, id);
			}
		}
	}
	
	private static void removeSoundFromNow(SoundEffect effect, BigClientVersion version) {
		for(BigClientVersion versionentry : BigClientVersion.values()) {
			if(versionentry.getProtocollVersion().getProtocollVersion() >= version.getProtocollVersion().getProtocollVersion()) {
				effect.versions.remove(versionentry);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static void changeSoundFromNow(SoundEffect effect, BigClientVersion version, String id) {
		for(BigClientVersion versionentry : BigClientVersion.values()) {
			if(versionentry.getProtocollVersion().getProtocollVersion() >= version.getProtocollVersion().getProtocollVersion()) {
				effect.versions.replace(versionentry, id);
			}
		}
	}

	private HashMap<BigClientVersion,String> versions;

	private SoundEffect() {
		versions = new HashMap<BigClientVersion,String>();
	}

	/**
	 * Get the Sound-String for the BigClientVersion
	 * @see BigClientVersion
	 *
	 * @param BigClientVersion version
	 * @return Sound-String
	 * */
	public String getId(BigClientVersion version) {
		return this.versions.get(version);
	}

	/**
	 * checks if the Sound is Aviarible for the specified version
	 * @see BigClientVersion
	 *
	 * @param BigClientVersion version
	 * @return true if its aviarible
	 * */
	public boolean isAvariable(BigClientVersion version) {
		return this.versions.containsKey(version);
	}
}
