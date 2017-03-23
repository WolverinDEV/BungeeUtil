package dev.wolveringer.bungeeutil.sound;

import java.util.HashMap;

import dev.wolveringer.bungeeutil.player.ClientVersion.BigClientVersion;

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
	BLOCK_NOTE_HARP,
	BLOCK_NOTE_HAT,
	BLOCK_NOTE_PLING,
	BLOCK_NOTE_SNARE,
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
	ENTITY_ELDER_GUARDIAN_HURT,
	ENTITY_ELDER_GUARDIAN_HURT_LAND,
	ENTITY_ENDERDRAGON_AMBIENT,
	ENTITY_ENDERDRAGON_DEATH,
	ENTITY_ENDERDRAGON_FIREBALL_EXPLODE,
	ENTITY_ENDERDRAGON_FLAP,
	ENTITY_ENDERDRAGON_GROWL,
	ENTITY_ENDERDRAGON_HURT,
	ENTITY_ENDERDRAGON_SHOOT,
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
	ENTITY_MAGMACUBE_DEATH,
	ENTITY_MAGMACUBE_HURT,
	ENTITY_MAGMACUBE_JUMP,
	ENTITY_MAGMACUBE_SQUISH,
	ENTITY_MINECART_INSIDE,
	ENTITY_MINECART_RIDING,
	ENTITY_MOOSHROOM_SHEAR,
	ENTITY_MULE_AMBIENT,
	ENTITY_MULE_DEATH,
	ENTITY_MULE_HURT,
	ENTITY_PAINTING_BREAK,
	ENTITY_PAINTING_PLACE,
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
	ENTITY_VILLAGER_AMBIENT,
	ENTITY_VILLAGER_DEATH,
	ENTITY_VILLAGER_HURT,
	ENTITY_VILLAGER_NO,
	ENTITY_VILLAGER_TRADING,
	ENTITY_VILLAGER_YES,
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
	ITEM_ARMOR_EQUIP_GENERIC,
	ITEM_ARMOR_EQUIP_GOLD,
	ITEM_ARMOR_EQUIP_IRON,
	ITEM_ARMOR_EQUIP_LEATHER,
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
		//1.10
		addSound(AMBIENT_CAVE, BigClientVersion.v1_10, "ambient.cave");
		addSound(BLOCK_ANVIL_BREAK, BigClientVersion.v1_10, "block.anvil.break");
		addSound(BLOCK_ANVIL_DESTROY, BigClientVersion.v1_10, "block.anvil.destroy");
		addSound(BLOCK_ANVIL_FALL, BigClientVersion.v1_10, "block.anvil.fall");
		addSound(BLOCK_ANVIL_HIT, BigClientVersion.v1_10, "block.anvil.hit");
		addSound(BLOCK_ANVIL_LAND, BigClientVersion.v1_10, "block.anvil.land");
		addSound(BLOCK_ANVIL_PLACE, BigClientVersion.v1_10, "block.anvil.place");
		addSound(BLOCK_ANVIL_STEP, BigClientVersion.v1_10, "block.anvil.step");
		addSound(BLOCK_ANVIL_USE, BigClientVersion.v1_10, "block.anvil.use");
		addSound(BLOCK_BREWING_STAND_BREW, BigClientVersion.v1_10, "block.brewing_stand.brew");
		addSound(BLOCK_CHEST_CLOSE, BigClientVersion.v1_10, "block.chest.close");
		addSound(BLOCK_CHEST_LOCKED, BigClientVersion.v1_10, "block.chest.locked");
		addSound(BLOCK_CHEST_OPEN, BigClientVersion.v1_10, "block.chest.open");
		addSound(BLOCK_CHORUS_FLOWER_DEATH, BigClientVersion.v1_10, "block.chorus_flower.death");
		addSound(BLOCK_CHORUS_FLOWER_GROW, BigClientVersion.v1_10, "block.chorus_flower.grow");
		addSound(BLOCK_CLOTH_BREAK, BigClientVersion.v1_10, "block.cloth.break");
		addSound(BLOCK_CLOTH_FALL, BigClientVersion.v1_10, "block.cloth.fall");
		addSound(BLOCK_CLOTH_HIT, BigClientVersion.v1_10, "block.cloth.hit");
		addSound(BLOCK_CLOTH_PLACE, BigClientVersion.v1_10, "block.cloth.place");
		addSound(BLOCK_CLOTH_STEP, BigClientVersion.v1_10, "block.cloth.step");
		addSound(BLOCK_COMPARATOR_CLICK, BigClientVersion.v1_10, "block.comparator.click");
		addSound(BLOCK_DISPENSER_DISPENSE, BigClientVersion.v1_10, "block.dispenser.dispense");
		addSound(BLOCK_DISPENSER_FAIL, BigClientVersion.v1_10, "block.dispenser.fail");
		addSound(BLOCK_DISPENSER_LAUNCH, BigClientVersion.v1_10, "block.dispenser.launch");
		addSound(BLOCK_ENCHANTMENT_TABLE_USE, BigClientVersion.v1_10, "block.enchantment_table.use");
		addSound(BLOCK_END_GATEWAY_SPAWN, BigClientVersion.v1_10, "block.end_gateway.spawn");
		addSound(BLOCK_ENDERCHEST_CLOSE, BigClientVersion.v1_10, "block.enderchest.close");
		addSound(BLOCK_ENDERCHEST_OPEN, BigClientVersion.v1_10, "block.enderchest.open");
		addSound(BLOCK_FENCE_GATE_CLOSE, BigClientVersion.v1_10, "block.fence_gate.close");
		addSound(BLOCK_FENCE_GATE_OPEN, BigClientVersion.v1_10, "block.fence_gate.open");
		addSound(BLOCK_FIRE_AMBIENT, BigClientVersion.v1_10, "block.fire.ambient");
		addSound(BLOCK_FIRE_EXTINGUISH, BigClientVersion.v1_10, "block.fire.extinguish");
		addSound(BLOCK_FURNACE_FIRE_CRACKLE, BigClientVersion.v1_10, "block.furnace.fire_crackle");
		addSound(BLOCK_GLASS_BREAK, BigClientVersion.v1_10, "block.glass.break");
		addSound(BLOCK_GLASS_FALL, BigClientVersion.v1_10, "block.glass.fall");
		addSound(BLOCK_GLASS_HIT, BigClientVersion.v1_10, "block.glass.hit");
		addSound(BLOCK_GLASS_PLACE, BigClientVersion.v1_10, "block.glass.place");
		addSound(BLOCK_GLASS_STEP, BigClientVersion.v1_10, "block.glass.step");
		addSound(BLOCK_GRASS_BREAK, BigClientVersion.v1_10, "block.grass.break");
		addSound(BLOCK_GRASS_FALL, BigClientVersion.v1_10, "block.grass.fall");
		addSound(BLOCK_GRASS_HIT, BigClientVersion.v1_10, "block.grass.hit");
		addSound(BLOCK_GRASS_PLACE, BigClientVersion.v1_10, "block.grass.place");
		addSound(BLOCK_GRASS_STEP, BigClientVersion.v1_10, "block.grass.step");
		addSound(BLOCK_GRAVEL_BREAK, BigClientVersion.v1_10, "block.gravel.break");
		addSound(BLOCK_GRAVEL_FALL, BigClientVersion.v1_10, "block.gravel.fall");
		addSound(BLOCK_GRAVEL_HIT, BigClientVersion.v1_10, "block.gravel.hit");
		addSound(BLOCK_GRAVEL_PLACE, BigClientVersion.v1_10, "block.gravel.place");
		addSound(BLOCK_GRAVEL_STEP, BigClientVersion.v1_10, "block.gravel.step");
		addSound(BLOCK_IRON_DOOR_CLOSE, BigClientVersion.v1_10, "block.iron_door.close");
		addSound(BLOCK_IRON_DOOR_OPEN, BigClientVersion.v1_10, "block.iron_door.open");
		addSound(BLOCK_IRON_TRAPDOOR_CLOSE, BigClientVersion.v1_10, "block.iron_trapdoor.close");
		addSound(BLOCK_IRON_TRAPDOOR_OPEN, BigClientVersion.v1_10, "block.iron_trapdoor.open");
		addSound(BLOCK_LADDER_BREAK, BigClientVersion.v1_10, "block.ladder.break");
		addSound(BLOCK_LADDER_FALL, BigClientVersion.v1_10, "block.ladder.fall");
		addSound(BLOCK_LADDER_HIT, BigClientVersion.v1_10, "block.ladder.hit");
		addSound(BLOCK_LADDER_PLACE, BigClientVersion.v1_10, "block.ladder.place");
		addSound(BLOCK_LADDER_STEP, BigClientVersion.v1_10, "block.ladder.step");
		addSound(BLOCK_LAVA_AMBIENT, BigClientVersion.v1_10, "block.lava.ambient");
		addSound(BLOCK_LAVA_EXTINGUISH, BigClientVersion.v1_10, "block.lava.extinguish");
		addSound(BLOCK_LAVA_POP, BigClientVersion.v1_10, "block.lava.pop");
		addSound(BLOCK_LEVER_CLICK, BigClientVersion.v1_10, "block.lever.click");
		addSound(BLOCK_METAL_BREAK, BigClientVersion.v1_10, "block.metal.break");
		addSound(BLOCK_METAL_FALL, BigClientVersion.v1_10, "block.metal.fall");
		addSound(BLOCK_METAL_HIT, BigClientVersion.v1_10, "block.metal.hit");
		addSound(BLOCK_METAL_PLACE, BigClientVersion.v1_10, "block.metal.place");
		addSound(BLOCK_METAL_STEP, BigClientVersion.v1_10, "block.metal.step");
		addSound(BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_10, "block.metal_pressureplate.click_off");
		addSound(BLOCK_METAL_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_10, "block.metal_pressureplate.click_on");
		addSound(BLOCK_NOTE_BASEDRUM, BigClientVersion.v1_10, "block.note.basedrum");
		addSound(BLOCK_NOTE_BASS, BigClientVersion.v1_10, "block.note.bass");
		addSound(BLOCK_NOTE_HARP, BigClientVersion.v1_10, "block.note.harp");
		addSound(BLOCK_NOTE_HAT, BigClientVersion.v1_10, "block.note.hat");
		addSound(BLOCK_NOTE_PLING, BigClientVersion.v1_10, "block.note.pling");
		addSound(BLOCK_NOTE_SNARE, BigClientVersion.v1_10, "block.note.snare");
		addSound(BLOCK_PISTON_CONTRACT, BigClientVersion.v1_10, "block.piston.contract");
		addSound(BLOCK_PISTON_EXTEND, BigClientVersion.v1_10, "block.piston.extend");
		addSound(BLOCK_PORTAL_AMBIENT, BigClientVersion.v1_10, "block.portal.ambient");
		addSound(BLOCK_PORTAL_TRAVEL, BigClientVersion.v1_10, "block.portal.travel");
		addSound(BLOCK_PORTAL_TRIGGER, BigClientVersion.v1_10, "block.portal.trigger");
		addSound(BLOCK_REDSTONE_TORCH_BURNOUT, BigClientVersion.v1_10, "block.redstone_torch.burnout");
		addSound(BLOCK_SAND_BREAK, BigClientVersion.v1_10, "block.sand.break");
		addSound(BLOCK_SAND_FALL, BigClientVersion.v1_10, "block.sand.fall");
		addSound(BLOCK_SAND_HIT, BigClientVersion.v1_10, "block.sand.hit");
		addSound(BLOCK_SAND_PLACE, BigClientVersion.v1_10, "block.sand.place");
		addSound(BLOCK_SAND_STEP, BigClientVersion.v1_10, "block.sand.step");
		addSound(BLOCK_SLIME_BREAK, BigClientVersion.v1_10, "block.slime.break");
		addSound(BLOCK_SLIME_FALL, BigClientVersion.v1_10, "block.slime.fall");
		addSound(BLOCK_SLIME_HIT, BigClientVersion.v1_10, "block.slime.hit");
		addSound(BLOCK_SLIME_PLACE, BigClientVersion.v1_10, "block.slime.place");
		addSound(BLOCK_SLIME_STEP, BigClientVersion.v1_10, "block.slime.step");
		addSound(BLOCK_SNOW_BREAK, BigClientVersion.v1_10, "block.snow.break");
		addSound(BLOCK_SNOW_FALL, BigClientVersion.v1_10, "block.snow.fall");
		addSound(BLOCK_SNOW_HIT, BigClientVersion.v1_10, "block.snow.hit");
		addSound(BLOCK_SNOW_PLACE, BigClientVersion.v1_10, "block.snow.place");
		addSound(BLOCK_SNOW_STEP, BigClientVersion.v1_10, "block.snow.step");
		addSound(BLOCK_STONE_BREAK, BigClientVersion.v1_10, "block.stone.break");
		addSound(BLOCK_STONE_FALL, BigClientVersion.v1_10, "block.stone.fall");
		addSound(BLOCK_STONE_HIT, BigClientVersion.v1_10, "block.stone.hit");
		addSound(BLOCK_STONE_PLACE, BigClientVersion.v1_10, "block.stone.place");
		addSound(BLOCK_STONE_STEP, BigClientVersion.v1_10, "block.stone.step");
		addSound(BLOCK_STONE_BUTTON_CLICK_OFF, BigClientVersion.v1_10, "block.stone_button.click_off");
		addSound(BLOCK_STONE_BUTTON_CLICK_ON, BigClientVersion.v1_10, "block.stone_button.click_on");
		addSound(BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_10, "block.stone_pressureplate.click_off");
		addSound(BLOCK_STONE_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_10, "block.stone_pressureplate.click_on");
		addSound(BLOCK_TRIPWIRE_ATTACH, BigClientVersion.v1_10, "block.tripwire.attach");
		addSound(BLOCK_TRIPWIRE_CLICK_OFF, BigClientVersion.v1_10, "block.tripwire.click_off");
		addSound(BLOCK_TRIPWIRE_CLICK_ON, BigClientVersion.v1_10, "block.tripwire.click_on");
		addSound(BLOCK_TRIPWIRE_DETACH, BigClientVersion.v1_10, "block.tripwire.detach");
		addSound(BLOCK_WATER_AMBIENT, BigClientVersion.v1_10, "block.water.ambient");
		addSound(BLOCK_WATERLILY_PLACE, BigClientVersion.v1_10, "block.waterlily.place");
		addSound(BLOCK_WOOD_BREAK, BigClientVersion.v1_10, "block.wood.break");
		addSound(BLOCK_WOOD_FALL, BigClientVersion.v1_10, "block.wood.fall");
		addSound(BLOCK_WOOD_HIT, BigClientVersion.v1_10, "block.wood.hit");
		addSound(BLOCK_WOOD_PLACE, BigClientVersion.v1_10, "block.wood.place");
		addSound(BLOCK_WOOD_STEP, BigClientVersion.v1_10, "block.wood.step");
		addSound(BLOCK_WOOD_BUTTON_CLICK_OFF, BigClientVersion.v1_10, "block.wood_button.click_off");
		addSound(BLOCK_WOOD_BUTTON_CLICK_ON, BigClientVersion.v1_10, "block.wood_button.click_on");
		addSound(BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_10, "block.wood_pressureplate.click_off");
		addSound(BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_10, "block.wood_pressureplate.click_on");
		addSound(BLOCK_WOODEN_DOOR_CLOSE, BigClientVersion.v1_10, "block.wooden_door.close");
		addSound(BLOCK_WOODEN_DOOR_OPEN, BigClientVersion.v1_10, "block.wooden_door.open");
		addSound(BLOCK_WOODEN_TRAPDOOR_CLOSE, BigClientVersion.v1_10, "block.wooden_trapdoor.close");
		addSound(BLOCK_WOODEN_TRAPDOOR_OPEN, BigClientVersion.v1_10, "block.wooden_trapdoor.open");
		addSound(ENCHANT_THORNS_HIT, BigClientVersion.v1_10, "enchant.thorns.hit");
		addSound(ENTITY_ARMORSTAND_BREAK, BigClientVersion.v1_10, "entity.armorstand.break");
		addSound(ENTITY_ARMORSTAND_FALL, BigClientVersion.v1_10, "entity.armorstand.fall");
		addSound(ENTITY_ARMORSTAND_HIT, BigClientVersion.v1_10, "entity.armorstand.hit");
		addSound(ENTITY_ARMORSTAND_PLACE, BigClientVersion.v1_10, "entity.armorstand.place");
		addSound(ENTITY_ARROW_HIT, BigClientVersion.v1_10, "entity.arrow.hit");
		addSound(ENTITY_ARROW_HIT_PLAYER, BigClientVersion.v1_10, "entity.arrow.hit_player");
		addSound(ENTITY_ARROW_SHOOT, BigClientVersion.v1_10, "entity.arrow.shoot");
		addSound(ENTITY_BAT_AMBIENT, BigClientVersion.v1_10, "entity.bat.ambient");
		addSound(ENTITY_BAT_DEATH, BigClientVersion.v1_10, "entity.bat.death");
		addSound(ENTITY_BAT_HURT, BigClientVersion.v1_10, "entity.bat.hurt");
		addSound(ENTITY_BAT_LOOP, BigClientVersion.v1_10, "entity.bat.loop");
		addSound(ENTITY_BAT_TAKEOFF, BigClientVersion.v1_10, "entity.bat.takeoff");
		addSound(ENTITY_BLAZE_AMBIENT, BigClientVersion.v1_10, "entity.blaze.ambient");
		addSound(ENTITY_BLAZE_BURN, BigClientVersion.v1_10, "entity.blaze.burn");
		addSound(ENTITY_BLAZE_DEATH, BigClientVersion.v1_10, "entity.blaze.death");
		addSound(ENTITY_BLAZE_HURT, BigClientVersion.v1_10, "entity.blaze.hurt");
		addSound(ENTITY_BLAZE_SHOOT, BigClientVersion.v1_10, "entity.blaze.shoot");
		addSound(ENTITY_BOBBER_SPLASH, BigClientVersion.v1_10, "entity.bobber.splash");
		addSound(ENTITY_BOBBER_THROW, BigClientVersion.v1_10, "entity.bobber.throw");
		addSound(ENTITY_CAT_AMBIENT, BigClientVersion.v1_10, "entity.cat.ambient");
		addSound(ENTITY_CAT_DEATH, BigClientVersion.v1_10, "entity.cat.death");
		addSound(ENTITY_CAT_HISS, BigClientVersion.v1_10, "entity.cat.hiss");
		addSound(ENTITY_CAT_HURT, BigClientVersion.v1_10, "entity.cat.hurt");
		addSound(ENTITY_CAT_PURR, BigClientVersion.v1_10, "entity.cat.purr");
		addSound(ENTITY_CAT_PURREOW, BigClientVersion.v1_10, "entity.cat.purreow");
		addSound(ENTITY_CHICKEN_AMBIENT, BigClientVersion.v1_10, "entity.chicken.ambient");
		addSound(ENTITY_CHICKEN_DEATH, BigClientVersion.v1_10, "entity.chicken.death");
		addSound(ENTITY_CHICKEN_EGG, BigClientVersion.v1_10, "entity.chicken.egg");
		addSound(ENTITY_CHICKEN_HURT, BigClientVersion.v1_10, "entity.chicken.hurt");
		addSound(ENTITY_CHICKEN_STEP, BigClientVersion.v1_10, "entity.chicken.step");
		addSound(ENTITY_COW_AMBIENT, BigClientVersion.v1_10, "entity.cow.ambient");
		addSound(ENTITY_COW_DEATH, BigClientVersion.v1_10, "entity.cow.death");
		addSound(ENTITY_COW_HURT, BigClientVersion.v1_10, "entity.cow.hurt");
		addSound(ENTITY_COW_MILK, BigClientVersion.v1_10, "entity.cow.milk");
		addSound(ENTITY_COW_STEP, BigClientVersion.v1_10, "entity.cow.step");
		addSound(ENTITY_CREEPER_DEATH, BigClientVersion.v1_10, "entity.creeper.death");
		addSound(ENTITY_CREEPER_HURT, BigClientVersion.v1_10, "entity.creeper.hurt");
		addSound(ENTITY_CREEPER_PRIMED, BigClientVersion.v1_10, "entity.creeper.primed");
		addSound(ENTITY_DONKEY_AMBIENT, BigClientVersion.v1_10, "entity.donkey.ambient");
		addSound(ENTITY_DONKEY_ANGRY, BigClientVersion.v1_10, "entity.donkey.angry");
		addSound(ENTITY_DONKEY_CHEST, BigClientVersion.v1_10, "entity.donkey.chest");
		addSound(ENTITY_DONKEY_DEATH, BigClientVersion.v1_10, "entity.donkey.death");
		addSound(ENTITY_DONKEY_HURT, BigClientVersion.v1_10, "entity.donkey.hurt");
		addSound(ENTITY_EGG_THROW, BigClientVersion.v1_10, "entity.egg.throw");
		addSound(ENTITY_ELDER_GUARDIAN_AMBIENT, BigClientVersion.v1_10, "entity.elder_guardian.ambient");
		addSound(ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_10, "entity.elder_guardian.ambient_land");
		addSound(ENTITY_ELDER_GUARDIAN_CURSE, BigClientVersion.v1_10, "entity.elder_guardian.curse");
		addSound(ENTITY_ELDER_GUARDIAN_DEATH, BigClientVersion.v1_10, "entity.elder_guardian.death");
		addSound(ENTITY_ELDER_GUARDIAN_DEATH_LAND, BigClientVersion.v1_10, "entity.elder_guardian.death_land");
		addSound(ENTITY_ELDER_GUARDIAN_HURT, BigClientVersion.v1_10, "entity.elder_guardian.hurt");
		addSound(ENTITY_ELDER_GUARDIAN_HURT_LAND, BigClientVersion.v1_10, "entity.elder_guardian.hurt_land");
		addSound(ENTITY_ENDERDRAGON_AMBIENT, BigClientVersion.v1_10, "entity.enderdragon.ambient");
		addSound(ENTITY_ENDERDRAGON_DEATH, BigClientVersion.v1_10, "entity.enderdragon.death");
		addSound(ENTITY_ENDERDRAGON_FLAP, BigClientVersion.v1_10, "entity.enderdragon.flap");
		addSound(ENTITY_ENDERDRAGON_GROWL, BigClientVersion.v1_10, "entity.enderdragon.growl");
		addSound(ENTITY_ENDERDRAGON_HURT, BigClientVersion.v1_10, "entity.enderdragon.hurt");
		addSound(ENTITY_ENDERDRAGON_SHOOT, BigClientVersion.v1_10, "entity.enderdragon.shoot");
		addSound(ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, BigClientVersion.v1_10, "entity.enderdragon_fireball.explode");
		addSound(ENTITY_ENDEREYE_LAUNCH, BigClientVersion.v1_10, "entity.endereye.launch");
		addSound(ENTITY_ENDERMEN_AMBIENT, BigClientVersion.v1_10, "entity.endermen.ambient");
		addSound(ENTITY_ENDERMEN_DEATH, BigClientVersion.v1_10, "entity.endermen.death");
		addSound(ENTITY_ENDERMEN_HURT, BigClientVersion.v1_10, "entity.endermen.hurt");
		addSound(ENTITY_ENDERMEN_SCREAM, BigClientVersion.v1_10, "entity.endermen.scream");
		addSound(ENTITY_ENDERMEN_STARE, BigClientVersion.v1_10, "entity.endermen.stare");
		addSound(ENTITY_ENDERMEN_TELEPORT, BigClientVersion.v1_10, "entity.endermen.teleport");
		addSound(ENTITY_ENDERMITE_AMBIENT, BigClientVersion.v1_10, "entity.endermite.ambient");
		addSound(ENTITY_ENDERMITE_DEATH, BigClientVersion.v1_10, "entity.endermite.death");
		addSound(ENTITY_ENDERMITE_HURT, BigClientVersion.v1_10, "entity.endermite.hurt");
		addSound(ENTITY_ENDERMITE_STEP, BigClientVersion.v1_10, "entity.endermite.step");
		addSound(ENTITY_ENDERPEARL_THROW, BigClientVersion.v1_10, "entity.enderpearl.throw");
		addSound(ENTITY_EXPERIENCE_BOTTLE_THROW, BigClientVersion.v1_10, "entity.experience_bottle.throw");
		addSound(ENTITY_EXPERIENCE_ORB_PICKUP, BigClientVersion.v1_10, "entity.experience_orb.pickup");
		addSound(ENTITY_EXPERIENCE_ORB_TOUCH, BigClientVersion.v1_10, "entity.experience_orb.touch");
		addSound(ENTITY_FIREWORK_BLAST, BigClientVersion.v1_10, "entity.firework.blast");
		addSound(ENTITY_FIREWORK_BLAST_FAR, BigClientVersion.v1_10, "entity.firework.blast_far");
		addSound(ENTITY_FIREWORK_LARGE_BLAST, BigClientVersion.v1_10, "entity.firework.large_blast");
		addSound(ENTITY_FIREWORK_LARGE_BLAST_FAR, BigClientVersion.v1_10, "entity.firework.large_blast_far");
		addSound(ENTITY_FIREWORK_LAUNCH, BigClientVersion.v1_10, "entity.firework.launch");
		addSound(ENTITY_FIREWORK_SHOOT, BigClientVersion.v1_10, "entity.firework.shoot");
		addSound(ENTITY_FIREWORK_TWINKLE, BigClientVersion.v1_10, "entity.firework.twinkle");
		addSound(ENTITY_FIREWORK_TWINKLE_FAR, BigClientVersion.v1_10, "entity.firework.twinkle_far");
		addSound(ENTITY_GENERIC_BIG_FALL, BigClientVersion.v1_10, "entity.generic.big_fall");
		addSound(ENTITY_GENERIC_BURN, BigClientVersion.v1_10, "entity.generic.burn");
		addSound(ENTITY_GENERIC_DEATH, BigClientVersion.v1_10, "entity.generic.death");
		addSound(ENTITY_GENERIC_DRINK, BigClientVersion.v1_10, "entity.generic.drink");
		addSound(ENTITY_GENERIC_EAT, BigClientVersion.v1_10, "entity.generic.eat");
		addSound(ENTITY_GENERIC_EXPLODE, BigClientVersion.v1_10, "entity.generic.explode");
		addSound(ENTITY_GENERIC_EXTINGUISH_FIRE, BigClientVersion.v1_10, "entity.generic.extinguish_fire");
		addSound(ENTITY_GENERIC_HURT, BigClientVersion.v1_10, "entity.generic.hurt");
		addSound(ENTITY_GENERIC_SMALL_FALL, BigClientVersion.v1_10, "entity.generic.small_fall");
		addSound(ENTITY_GENERIC_SPLASH, BigClientVersion.v1_10, "entity.generic.splash");
		addSound(ENTITY_GENERIC_SWIM, BigClientVersion.v1_10, "entity.generic.swim");
		addSound(ENTITY_GHAST_AMBIENT, BigClientVersion.v1_10, "entity.ghast.ambient");
		addSound(ENTITY_GHAST_DEATH, BigClientVersion.v1_10, "entity.ghast.death");
		addSound(ENTITY_GHAST_HURT, BigClientVersion.v1_10, "entity.ghast.hurt");
		addSound(ENTITY_GHAST_SCREAM, BigClientVersion.v1_10, "entity.ghast.scream");
		addSound(ENTITY_GHAST_SHOOT, BigClientVersion.v1_10, "entity.ghast.shoot");
		addSound(ENTITY_GHAST_WARN, BigClientVersion.v1_10, "entity.ghast.warn");
		addSound(ENTITY_GUARDIAN_AMBIENT, BigClientVersion.v1_10, "entity.guardian.ambient");
		addSound(ENTITY_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_10, "entity.guardian.ambient_land");
		addSound(ENTITY_GUARDIAN_ATTACK, BigClientVersion.v1_10, "entity.guardian.attack");
		addSound(ENTITY_GUARDIAN_DEATH, BigClientVersion.v1_10, "entity.guardian.death");
		addSound(ENTITY_GUARDIAN_DEATH_LAND, BigClientVersion.v1_10, "entity.guardian.death_land");
		addSound(ENTITY_GUARDIAN_FLOP, BigClientVersion.v1_10, "entity.guardian.flop");
		addSound(ENTITY_GUARDIAN_HURT, BigClientVersion.v1_10, "entity.guardian.hurt");
		addSound(ENTITY_GUARDIAN_HURT_LAND, BigClientVersion.v1_10, "entity.guardian.hurt_land");
		addSound(ENTITY_HORSE_AMBIENT, BigClientVersion.v1_10, "entity.horse.ambient");
		addSound(ENTITY_HORSE_ANGRY, BigClientVersion.v1_10, "entity.horse.angry");
		addSound(ENTITY_HORSE_ARMOR, BigClientVersion.v1_10, "entity.horse.armor");
		addSound(ENTITY_HORSE_BREATHE, BigClientVersion.v1_10, "entity.horse.breathe");
		addSound(ENTITY_HORSE_DEATH, BigClientVersion.v1_10, "entity.horse.death");
		addSound(ENTITY_HORSE_EAT, BigClientVersion.v1_10, "entity.horse.eat");
		addSound(ENTITY_HORSE_GALLOP, BigClientVersion.v1_10, "entity.horse.gallop");
		addSound(ENTITY_HORSE_HURT, BigClientVersion.v1_10, "entity.horse.hurt");
		addSound(ENTITY_HORSE_JUMP, BigClientVersion.v1_10, "entity.horse.jump");
		addSound(ENTITY_HORSE_LAND, BigClientVersion.v1_10, "entity.horse.land");
		addSound(ENTITY_HORSE_SADDLE, BigClientVersion.v1_10, "entity.horse.saddle");
		addSound(ENTITY_HORSE_STEP, BigClientVersion.v1_10, "entity.horse.step");
		addSound(ENTITY_HORSE_STEP_WOOD, BigClientVersion.v1_10, "entity.horse.step_wood");
		addSound(ENTITY_HOSTILE_BIG_FALL, BigClientVersion.v1_10, "entity.hostile.big_fall");
		addSound(ENTITY_HOSTILE_DEATH, BigClientVersion.v1_10, "entity.hostile.death");
		addSound(ENTITY_HOSTILE_HURT, BigClientVersion.v1_10, "entity.hostile.hurt");
		addSound(ENTITY_HOSTILE_SMALL_FALL, BigClientVersion.v1_10, "entity.hostile.small_fall");
		addSound(ENTITY_HOSTILE_SPLASH, BigClientVersion.v1_10, "entity.hostile.splash");
		addSound(ENTITY_HOSTILE_SWIM, BigClientVersion.v1_10, "entity.hostile.swim");
		addSound(ENTITY_HUSK_AMBIENT, BigClientVersion.v1_10, "entity.husk.ambient");
		addSound(ENTITY_HUSK_DEATH, BigClientVersion.v1_10, "entity.husk.death");
		addSound(ENTITY_HUSK_HURT, BigClientVersion.v1_10, "entity.husk.hurt");
		addSound(ENTITY_HUSK_STEP, BigClientVersion.v1_10, "entity.husk.step");
		addSound(ENTITY_IRONGOLEM_ATTACK, BigClientVersion.v1_10, "entity.irongolem.attack");
		addSound(ENTITY_IRONGOLEM_DEATH, BigClientVersion.v1_10, "entity.irongolem.death");
		addSound(ENTITY_IRONGOLEM_HURT, BigClientVersion.v1_10, "entity.irongolem.hurt");
		addSound(ENTITY_IRONGOLEM_STEP, BigClientVersion.v1_10, "entity.irongolem.step");
		addSound(ENTITY_ITEM_BREAK, BigClientVersion.v1_10, "entity.item.break");
		addSound(ENTITY_ITEM_PICKUP, BigClientVersion.v1_10, "entity.item.pickup");
		addSound(ENTITY_ITEMFRAME_ADD_ITEM, BigClientVersion.v1_10, "entity.itemframe.add_item");
		addSound(ENTITY_ITEMFRAME_BREAK, BigClientVersion.v1_10, "entity.itemframe.break");
		addSound(ENTITY_ITEMFRAME_PLACE, BigClientVersion.v1_10, "entity.itemframe.place");
		addSound(ENTITY_ITEMFRAME_REMOVE_ITEM, BigClientVersion.v1_10, "entity.itemframe.remove_item");
		addSound(ENTITY_ITEMFRAME_ROTATE_ITEM, BigClientVersion.v1_10, "entity.itemframe.rotate_item");
		addSound(ENTITY_LEASHKNOT_BREAK, BigClientVersion.v1_10, "entity.leashknot.break");
		addSound(ENTITY_LEASHKNOT_PLACE, BigClientVersion.v1_10, "entity.leashknot.place");
		addSound(ENTITY_LIGHTNING_IMPACT, BigClientVersion.v1_10, "entity.lightning.impact");
		addSound(ENTITY_LIGHTNING_THUNDER, BigClientVersion.v1_10, "entity.lightning.thunder");
		addSound(ENTITY_LINGERINGPOTION_THROW, BigClientVersion.v1_10, "entity.lingeringpotion.throw");
		addSound(ENTITY_MAGMACUBE_DEATH, BigClientVersion.v1_10, "entity.magmacube.death");
		addSound(ENTITY_MAGMACUBE_HURT, BigClientVersion.v1_10, "entity.magmacube.hurt");
		addSound(ENTITY_MAGMACUBE_JUMP, BigClientVersion.v1_10, "entity.magmacube.jump");
		addSound(ENTITY_MAGMACUBE_SQUISH, BigClientVersion.v1_10, "entity.magmacube.squish");
		addSound(ENTITY_MINECART_INSIDE, BigClientVersion.v1_10, "entity.minecart.inside");
		addSound(ENTITY_MINECART_RIDING, BigClientVersion.v1_10, "entity.minecart.riding");
		addSound(ENTITY_MOOSHROOM_SHEAR, BigClientVersion.v1_10, "entity.mooshroom.shear");
		addSound(ENTITY_MULE_AMBIENT, BigClientVersion.v1_10, "entity.mule.ambient");
		addSound(ENTITY_MULE_DEATH, BigClientVersion.v1_10, "entity.mule.death");
		addSound(ENTITY_MULE_HURT, BigClientVersion.v1_10, "entity.mule.hurt");
		addSound(ENTITY_PAINTING_BREAK, BigClientVersion.v1_10, "entity.painting.break");
		addSound(ENTITY_PAINTING_PLACE, BigClientVersion.v1_10, "entity.painting.place");
		addSound(ENTITY_PIG_AMBIENT, BigClientVersion.v1_10, "entity.pig.ambient");
		addSound(ENTITY_PIG_DEATH, BigClientVersion.v1_10, "entity.pig.death");
		addSound(ENTITY_PIG_HURT, BigClientVersion.v1_10, "entity.pig.hurt");
		addSound(ENTITY_PIG_SADDLE, BigClientVersion.v1_10, "entity.pig.saddle");
		addSound(ENTITY_PIG_STEP, BigClientVersion.v1_10, "entity.pig.step");
		addSound(ENTITY_PLAYER_ATTACK_CRIT, BigClientVersion.v1_10, "entity.player.attack.crit");
		addSound(ENTITY_PLAYER_ATTACK_KNOCKBACK, BigClientVersion.v1_10, "entity.player.attack.knockback");
		addSound(ENTITY_PLAYER_ATTACK_NODAMAGE, BigClientVersion.v1_10, "entity.player.attack.nodamage");
		addSound(ENTITY_PLAYER_ATTACK_STRONG, BigClientVersion.v1_10, "entity.player.attack.strong");
		addSound(ENTITY_PLAYER_ATTACK_SWEEP, BigClientVersion.v1_10, "entity.player.attack.sweep");
		addSound(ENTITY_PLAYER_ATTACK_WEAK, BigClientVersion.v1_10, "entity.player.attack.weak");
		addSound(ENTITY_PLAYER_BIG_FALL, BigClientVersion.v1_10, "entity.player.big_fall");
		addSound(ENTITY_PLAYER_BREATH, BigClientVersion.v1_10, "entity.player.breath");
		addSound(ENTITY_PLAYER_BURP, BigClientVersion.v1_10, "entity.player.burp");
		addSound(ENTITY_PLAYER_DEATH, BigClientVersion.v1_10, "entity.player.death");
		addSound(ENTITY_PLAYER_HURT, BigClientVersion.v1_10, "entity.player.hurt");
		addSound(ENTITY_PLAYER_LEVELUP, BigClientVersion.v1_10, "entity.player.levelup");
		addSound(ENTITY_PLAYER_SMALL_FALL, BigClientVersion.v1_10, "entity.player.small_fall");
		addSound(ENTITY_PLAYER_SPLASH, BigClientVersion.v1_10, "entity.player.splash");
		addSound(ENTITY_PLAYER_SWIM, BigClientVersion.v1_10, "entity.player.swim");
		addSound(ENTITY_POLAR_BEAR_AMBIENT, BigClientVersion.v1_10, "entity.polar_bear.ambient");
		addSound(ENTITY_POLAR_BEAR_BABY_AMBIENT, BigClientVersion.v1_10, "entity.polar_bear.baby_ambient");
		addSound(ENTITY_POLAR_BEAR_DEATH, BigClientVersion.v1_10, "entity.polar_bear.death");
		addSound(ENTITY_POLAR_BEAR_HURT, BigClientVersion.v1_10, "entity.polar_bear.hurt");
		addSound(ENTITY_POLAR_BEAR_STEP, BigClientVersion.v1_10, "entity.polar_bear.step");
		addSound(ENTITY_POLAR_BEAR_WARNING, BigClientVersion.v1_10, "entity.polar_bear.warning");
		addSound(ENTITY_RABBIT_AMBIENT, BigClientVersion.v1_10, "entity.rabbit.ambient");
		addSound(ENTITY_RABBIT_ATTACK, BigClientVersion.v1_10, "entity.rabbit.attack");
		addSound(ENTITY_RABBIT_DEATH, BigClientVersion.v1_10, "entity.rabbit.death");
		addSound(ENTITY_RABBIT_HURT, BigClientVersion.v1_10, "entity.rabbit.hurt");
		addSound(ENTITY_RABBIT_JUMP, BigClientVersion.v1_10, "entity.rabbit.jump");
		addSound(ENTITY_SHEEP_AMBIENT, BigClientVersion.v1_10, "entity.sheep.ambient");
		addSound(ENTITY_SHEEP_DEATH, BigClientVersion.v1_10, "entity.sheep.death");
		addSound(ENTITY_SHEEP_HURT, BigClientVersion.v1_10, "entity.sheep.hurt");
		addSound(ENTITY_SHEEP_SHEAR, BigClientVersion.v1_10, "entity.sheep.shear");
		addSound(ENTITY_SHEEP_STEP, BigClientVersion.v1_10, "entity.sheep.step");
		addSound(ENTITY_SHULKER_AMBIENT, BigClientVersion.v1_10, "entity.shulker.ambient");
		addSound(ENTITY_SHULKER_CLOSE, BigClientVersion.v1_10, "entity.shulker.close");
		addSound(ENTITY_SHULKER_DEATH, BigClientVersion.v1_10, "entity.shulker.death");
		addSound(ENTITY_SHULKER_HURT, BigClientVersion.v1_10, "entity.shulker.hurt");
		addSound(ENTITY_SHULKER_HURT_CLOSED, BigClientVersion.v1_10, "entity.shulker.hurt_closed");
		addSound(ENTITY_SHULKER_OPEN, BigClientVersion.v1_10, "entity.shulker.open");
		addSound(ENTITY_SHULKER_SHOOT, BigClientVersion.v1_10, "entity.shulker.shoot");
		addSound(ENTITY_SHULKER_TELEPORT, BigClientVersion.v1_10, "entity.shulker.teleport");
		addSound(ENTITY_SHULKER_BULLET_HIT, BigClientVersion.v1_10, "entity.shulker_bullet.hit");
		addSound(ENTITY_SHULKER_BULLET_HURT, BigClientVersion.v1_10, "entity.shulker_bullet.hurt");
		addSound(ENTITY_SILVERFISH_AMBIENT, BigClientVersion.v1_10, "entity.silverfish.ambient");
		addSound(ENTITY_SILVERFISH_DEATH, BigClientVersion.v1_10, "entity.silverfish.death");
		addSound(ENTITY_SILVERFISH_HURT, BigClientVersion.v1_10, "entity.silverfish.hurt");
		addSound(ENTITY_SILVERFISH_STEP, BigClientVersion.v1_10, "entity.silverfish.step");
		addSound(ENTITY_SKELETON_AMBIENT, BigClientVersion.v1_10, "entity.skeleton.ambient");
		addSound(ENTITY_SKELETON_DEATH, BigClientVersion.v1_10, "entity.skeleton.death");
		addSound(ENTITY_SKELETON_HURT, BigClientVersion.v1_10, "entity.skeleton.hurt");
		addSound(ENTITY_SKELETON_SHOOT, BigClientVersion.v1_10, "entity.skeleton.shoot");
		addSound(ENTITY_SKELETON_STEP, BigClientVersion.v1_10, "entity.skeleton.step");
		addSound(ENTITY_SKELETON_HORSE_AMBIENT, BigClientVersion.v1_10, "entity.skeleton_horse.ambient");
		addSound(ENTITY_SKELETON_HORSE_DEATH, BigClientVersion.v1_10, "entity.skeleton_horse.death");
		addSound(ENTITY_SKELETON_HORSE_HURT, BigClientVersion.v1_10, "entity.skeleton_horse.hurt");
		addSound(ENTITY_SLIME_ATTACK, BigClientVersion.v1_10, "entity.slime.attack");
		addSound(ENTITY_SLIME_DEATH, BigClientVersion.v1_10, "entity.slime.death");
		addSound(ENTITY_SLIME_HURT, BigClientVersion.v1_10, "entity.slime.hurt");
		addSound(ENTITY_SLIME_JUMP, BigClientVersion.v1_10, "entity.slime.jump");
		addSound(ENTITY_SLIME_SQUISH, BigClientVersion.v1_10, "entity.slime.squish");
		addSound(ENTITY_SMALL_MAGMACUBE_DEATH, BigClientVersion.v1_10, "entity.small_magmacube.death");
		addSound(ENTITY_SMALL_MAGMACUBE_HURT, BigClientVersion.v1_10, "entity.small_magmacube.hurt");
		addSound(ENTITY_SMALL_MAGMACUBE_SQUISH, BigClientVersion.v1_10, "entity.small_magmacube.squish");
		addSound(ENTITY_SMALL_SLIME_DEATH, BigClientVersion.v1_10, "entity.small_slime.death");
		addSound(ENTITY_SMALL_SLIME_HURT, BigClientVersion.v1_10, "entity.small_slime.hurt");
		addSound(ENTITY_SMALL_SLIME_JUMP, BigClientVersion.v1_10, "entity.small_slime.jump");
		addSound(ENTITY_SMALL_SLIME_SQUISH, BigClientVersion.v1_10, "entity.small_slime.squish");
		addSound(ENTITY_SNOWBALL_THROW, BigClientVersion.v1_10, "entity.snowball.throw");
		addSound(ENTITY_SNOWMAN_AMBIENT, BigClientVersion.v1_10, "entity.snowman.ambient");
		addSound(ENTITY_SNOWMAN_DEATH, BigClientVersion.v1_10, "entity.snowman.death");
		addSound(ENTITY_SNOWMAN_HURT, BigClientVersion.v1_10, "entity.snowman.hurt");
		addSound(ENTITY_SNOWMAN_SHOOT, BigClientVersion.v1_10, "entity.snowman.shoot");
		addSound(ENTITY_SPIDER_AMBIENT, BigClientVersion.v1_10, "entity.spider.ambient");
		addSound(ENTITY_SPIDER_DEATH, BigClientVersion.v1_10, "entity.spider.death");
		addSound(ENTITY_SPIDER_HURT, BigClientVersion.v1_10, "entity.spider.hurt");
		addSound(ENTITY_SPIDER_STEP, BigClientVersion.v1_10, "entity.spider.step");
		addSound(ENTITY_SPLASH_POTION_BREAK, BigClientVersion.v1_10, "entity.splash_potion.break");
		addSound(ENTITY_SPLASH_POTION_THROW, BigClientVersion.v1_10, "entity.splash_potion.throw");
		addSound(ENTITY_SQUID_AMBIENT, BigClientVersion.v1_10, "entity.squid.ambient");
		addSound(ENTITY_SQUID_DEATH, BigClientVersion.v1_10, "entity.squid.death");
		addSound(ENTITY_SQUID_HURT, BigClientVersion.v1_10, "entity.squid.hurt");
		addSound(ENTITY_STRAY_AMBIENT, BigClientVersion.v1_10, "entity.stray.ambient");
		addSound(ENTITY_STRAY_DEATH, BigClientVersion.v1_10, "entity.stray.death");
		addSound(ENTITY_STRAY_HURT, BigClientVersion.v1_10, "entity.stray.hurt");
		addSound(ENTITY_STRAY_STEP, BigClientVersion.v1_10, "entity.stray.step");
		addSound(ENTITY_TNT_PRIMED, BigClientVersion.v1_10, "entity.tnt.primed");
		addSound(ENTITY_VILLAGER_AMBIENT, BigClientVersion.v1_10, "entity.villager.ambient");
		addSound(ENTITY_VILLAGER_DEATH, BigClientVersion.v1_10, "entity.villager.death");
		addSound(ENTITY_VILLAGER_HURT, BigClientVersion.v1_10, "entity.villager.hurt");
		addSound(ENTITY_VILLAGER_NO, BigClientVersion.v1_10, "entity.villager.no");
		addSound(ENTITY_VILLAGER_TRADING, BigClientVersion.v1_10, "entity.villager.trading");
		addSound(ENTITY_VILLAGER_YES, BigClientVersion.v1_10, "entity.villager.yes");
		addSound(ENTITY_WITCH_AMBIENT, BigClientVersion.v1_10, "entity.witch.ambient");
		addSound(ENTITY_WITCH_DEATH, BigClientVersion.v1_10, "entity.witch.death");
		addSound(ENTITY_WITCH_DRINK, BigClientVersion.v1_10, "entity.witch.drink");
		addSound(ENTITY_WITCH_HURT, BigClientVersion.v1_10, "entity.witch.hurt");
		addSound(ENTITY_WITCH_THROW, BigClientVersion.v1_10, "entity.witch.throw");
		addSound(ENTITY_WITHER_AMBIENT, BigClientVersion.v1_10, "entity.wither.ambient");
		addSound(ENTITY_WITHER_BREAK_BLOCK, BigClientVersion.v1_10, "entity.wither.break_block");
		addSound(ENTITY_WITHER_DEATH, BigClientVersion.v1_10, "entity.wither.death");
		addSound(ENTITY_WITHER_HURT, BigClientVersion.v1_10, "entity.wither.hurt");
		addSound(ENTITY_WITHER_SHOOT, BigClientVersion.v1_10, "entity.wither.shoot");
		addSound(ENTITY_WITHER_SPAWN, BigClientVersion.v1_10, "entity.wither.spawn");
		addSound(ENTITY_WITHER_SKELETON_AMBIENT, BigClientVersion.v1_10, "entity.wither_skeleton.ambient");
		addSound(ENTITY_WITHER_SKELETON_DEATH, BigClientVersion.v1_10, "entity.wither_skeleton.death");
		addSound(ENTITY_WITHER_SKELETON_HURT, BigClientVersion.v1_10, "entity.wither_skeleton.hurt");
		addSound(ENTITY_WITHER_SKELETON_STEP, BigClientVersion.v1_10, "entity.wither_skeleton.step");
		addSound(ENTITY_WOLF_AMBIENT, BigClientVersion.v1_10, "entity.wolf.ambient");
		addSound(ENTITY_WOLF_DEATH, BigClientVersion.v1_10, "entity.wolf.death");
		addSound(ENTITY_WOLF_GROWL, BigClientVersion.v1_10, "entity.wolf.growl");
		addSound(ENTITY_WOLF_HOWL, BigClientVersion.v1_10, "entity.wolf.howl");
		addSound(ENTITY_WOLF_HURT, BigClientVersion.v1_10, "entity.wolf.hurt");
		addSound(ENTITY_WOLF_PANT, BigClientVersion.v1_10, "entity.wolf.pant");
		addSound(ENTITY_WOLF_SHAKE, BigClientVersion.v1_10, "entity.wolf.shake");
		addSound(ENTITY_WOLF_STEP, BigClientVersion.v1_10, "entity.wolf.step");
		addSound(ENTITY_WOLF_WHINE, BigClientVersion.v1_10, "entity.wolf.whine");
		addSound(ENTITY_ZOMBIE_AMBIENT, BigClientVersion.v1_10, "entity.zombie.ambient");
		addSound(ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, BigClientVersion.v1_10, "entity.zombie.attack_door_wood");
		addSound(ENTITY_ZOMBIE_ATTACK_IRON_DOOR, BigClientVersion.v1_10, "entity.zombie.attack_iron_door");
		addSound(ENTITY_ZOMBIE_BREAK_DOOR_WOOD, BigClientVersion.v1_10, "entity.zombie.break_door_wood");
		addSound(ENTITY_ZOMBIE_DEATH, BigClientVersion.v1_10, "entity.zombie.death");
		addSound(ENTITY_ZOMBIE_HURT, BigClientVersion.v1_10, "entity.zombie.hurt");
		addSound(ENTITY_ZOMBIE_INFECT, BigClientVersion.v1_10, "entity.zombie.infect");
		addSound(ENTITY_ZOMBIE_STEP, BigClientVersion.v1_10, "entity.zombie.step");
		addSound(ENTITY_ZOMBIE_HORSE_AMBIENT, BigClientVersion.v1_10, "entity.zombie_horse.ambient");
		addSound(ENTITY_ZOMBIE_HORSE_DEATH, BigClientVersion.v1_10, "entity.zombie_horse.death");
		addSound(ENTITY_ZOMBIE_HORSE_HURT, BigClientVersion.v1_10, "entity.zombie_horse.hurt");
		addSound(ENTITY_ZOMBIE_PIG_AMBIENT, BigClientVersion.v1_10, "entity.zombie_pig.ambient");
		addSound(ENTITY_ZOMBIE_PIG_ANGRY, BigClientVersion.v1_10, "entity.zombie_pig.angry");
		addSound(ENTITY_ZOMBIE_PIG_DEATH, BigClientVersion.v1_10, "entity.zombie_pig.death");
		addSound(ENTITY_ZOMBIE_PIG_HURT, BigClientVersion.v1_10, "entity.zombie_pig.hurt");
		addSound(ENTITY_ZOMBIE_VILLAGER_AMBIENT, BigClientVersion.v1_10, "entity.zombie_villager.ambient");
		addSound(ENTITY_ZOMBIE_VILLAGER_CONVERTED, BigClientVersion.v1_10, "entity.zombie_villager.converted");
		addSound(ENTITY_ZOMBIE_VILLAGER_CURE, BigClientVersion.v1_10, "entity.zombie_villager.cure");
		addSound(ENTITY_ZOMBIE_VILLAGER_DEATH, BigClientVersion.v1_10, "entity.zombie_villager.death");
		addSound(ENTITY_ZOMBIE_VILLAGER_HURT, BigClientVersion.v1_10, "entity.zombie_villager.hurt");
		addSound(ENTITY_ZOMBIE_VILLAGER_STEP, BigClientVersion.v1_10, "entity.zombie_villager.step");
		addSound(ITEM_ARMOR_EQUIP_CHAIN, BigClientVersion.v1_10, "item.armor.equip_chain");
		addSound(ITEM_ARMOR_EQUIP_DIAMOND, BigClientVersion.v1_10, "item.armor.equip_diamond");
		addSound(ITEM_ARMOR_EQUIP_GENERIC, BigClientVersion.v1_10, "item.armor.equip_generic");
		addSound(ITEM_ARMOR_EQUIP_GOLD, BigClientVersion.v1_10, "item.armor.equip_gold");
		addSound(ITEM_ARMOR_EQUIP_IRON, BigClientVersion.v1_10, "item.armor.equip_iron");
		addSound(ITEM_ARMOR_EQUIP_LEATHER, BigClientVersion.v1_10, "item.armor.equip_leather");
		addSound(ITEM_BOTTLE_FILL, BigClientVersion.v1_10, "item.bottle.fill");
		addSound(ITEM_BOTTLE_FILL_DRAGONBREATH, BigClientVersion.v1_10, "item.bottle.fill_dragonbreath");
		addSound(ITEM_BUCKET_EMPTY, BigClientVersion.v1_10, "item.bucket.empty");
		addSound(ITEM_BUCKET_EMPTY_LAVA, BigClientVersion.v1_10, "item.bucket.empty_lava");
		addSound(ITEM_BUCKET_FILL, BigClientVersion.v1_10, "item.bucket.fill");
		addSound(ITEM_BUCKET_FILL_LAVA, BigClientVersion.v1_10, "item.bucket.fill_lava");
		addSound(ITEM_CHORUS_FRUIT_TELEPORT, BigClientVersion.v1_10, "item.chorus_fruit.teleport");
		addSound(ITEM_ELYTRA_FLYING, BigClientVersion.v1_10, "item.elytra.flying");
		addSound(ITEM_FIRECHARGE_USE, BigClientVersion.v1_10, "item.firecharge.use");
		addSound(ITEM_FLINTANDSTEEL_USE, BigClientVersion.v1_10, "item.flintandsteel.use");
		addSound(ITEM_HOE_TILL, BigClientVersion.v1_10, "item.hoe.till");
		addSound(ITEM_SHIELD_BLOCK, BigClientVersion.v1_10, "item.shield.block");
		addSound(ITEM_SHIELD_BREAK, BigClientVersion.v1_10, "item.shield.break");
		addSound(ITEM_SHOVEL_FLATTEN, BigClientVersion.v1_10, "item.shovel.flatten");
		addSound(MUSIC_CREATIVE, BigClientVersion.v1_10, "music.creative");
		addSound(MUSIC_CREDITS, BigClientVersion.v1_10, "music.credits");
		addSound(MUSIC_DRAGON, BigClientVersion.v1_10, "music.dragon");
		addSound(MUSIC_END, BigClientVersion.v1_10, "music.end");
		addSound(MUSIC_GAME, BigClientVersion.v1_10, "music.game");
		addSound(MUSIC_MENU, BigClientVersion.v1_10, "music.menu");
		addSound(MUSIC_NETHER, BigClientVersion.v1_10, "music.nether");
		addSound(RECORD_11, BigClientVersion.v1_10, "record.11");
		addSound(RECORD_13, BigClientVersion.v1_10, "record.13");
		addSound(RECORD_BLOCKS, BigClientVersion.v1_10, "record.blocks");
		addSound(RECORD_CAT, BigClientVersion.v1_10, "record.cat");
		addSound(RECORD_CHIRP, BigClientVersion.v1_10, "record.chirp");
		addSound(RECORD_FAR, BigClientVersion.v1_10, "record.far");
		addSound(RECORD_MALL, BigClientVersion.v1_10, "record.mall");
		addSound(RECORD_MELLOHI, BigClientVersion.v1_10, "record.mellohi");
		addSound(RECORD_STAL, BigClientVersion.v1_10, "record.stal");
		addSound(RECORD_STRAD, BigClientVersion.v1_10, "record.strad");
		addSound(RECORD_WAIT, BigClientVersion.v1_10, "record.wait");
		addSound(RECORD_WARD, BigClientVersion.v1_10, "record.ward");
		addSound(UI_BUTTON_CLICK, BigClientVersion.v1_10, "ui.button.click");
		addSound(WEATHER_RAIN, BigClientVersion.v1_10, "weather.rain");
		addSound(WEATHER_RAIN_ABOVE, BigClientVersion.v1_10, "weather.rain.above");

		//1.9
		addSound(AMBIENT_CAVE, BigClientVersion.v1_9, "ambient.cave");
		addSound(BLOCK_ANVIL_BREAK, BigClientVersion.v1_9, "block.anvil.break");
		addSound(BLOCK_ANVIL_DESTROY, BigClientVersion.v1_9, "block.anvil.destroy");
		addSound(BLOCK_ANVIL_FALL, BigClientVersion.v1_9, "block.anvil.fall");
		addSound(BLOCK_ANVIL_HIT, BigClientVersion.v1_9, "block.anvil.hit");
		addSound(BLOCK_ANVIL_LAND, BigClientVersion.v1_9, "block.anvil.land");
		addSound(BLOCK_ANVIL_PLACE, BigClientVersion.v1_9, "block.anvil.place");
		addSound(BLOCK_ANVIL_STEP, BigClientVersion.v1_9, "block.anvil.step");
		addSound(BLOCK_ANVIL_USE, BigClientVersion.v1_9, "block.anvil.use");
		addSound(BLOCK_BREWING_STAND_BREW, BigClientVersion.v1_9, "block.brewing_stand.brew");
		addSound(BLOCK_CHEST_CLOSE, BigClientVersion.v1_9, "block.chest.close");
		addSound(BLOCK_CHEST_LOCKED, BigClientVersion.v1_9, "block.chest.locked");
		addSound(BLOCK_CHEST_OPEN, BigClientVersion.v1_9, "block.chest.open");
		addSound(BLOCK_CHORUS_FLOWER_DEATH, BigClientVersion.v1_9, "block.chorus_flower.death");
		addSound(BLOCK_CHORUS_FLOWER_GROW, BigClientVersion.v1_9, "block.chorus_flower.grow");
		addSound(BLOCK_CLOTH_BREAK, BigClientVersion.v1_9, "block.cloth.break");
		addSound(BLOCK_CLOTH_FALL, BigClientVersion.v1_9, "block.cloth.fall");
		addSound(BLOCK_CLOTH_HIT, BigClientVersion.v1_9, "block.cloth.hit");
		addSound(BLOCK_CLOTH_PLACE, BigClientVersion.v1_9, "block.cloth.place");
		addSound(BLOCK_CLOTH_STEP, BigClientVersion.v1_9, "block.cloth.step");
		addSound(BLOCK_COMPARATOR_CLICK, BigClientVersion.v1_9, "block.comparator.click");
		addSound(BLOCK_DISPENSER_DISPENSE, BigClientVersion.v1_9, "block.dispenser.dispense");
		addSound(BLOCK_DISPENSER_FAIL, BigClientVersion.v1_9, "block.dispenser.fail");
		addSound(BLOCK_DISPENSER_LAUNCH, BigClientVersion.v1_9, "block.dispenser.launch");
		addSound(BLOCK_END_GATEWAY_SPAWN, BigClientVersion.v1_9, "block.end_gateway.spawn");
		addSound(BLOCK_ENDERCHEST_CLOSE, BigClientVersion.v1_9, "block.enderchest.close");
		addSound(BLOCK_ENDERCHEST_OPEN, BigClientVersion.v1_9, "block.enderchest.open");
		addSound(BLOCK_FENCE_GATE_CLOSE, BigClientVersion.v1_9, "block.fence_gate.close");
		addSound(BLOCK_FENCE_GATE_OPEN, BigClientVersion.v1_9, "block.fence_gate.open");
		addSound(BLOCK_FIRE_AMBIENT, BigClientVersion.v1_9, "block.fire.ambient");
		addSound(BLOCK_FIRE_EXTINGUISH, BigClientVersion.v1_9, "block.fire.extinguish");
		addSound(BLOCK_FURNACE_FIRE_CRACKLE, BigClientVersion.v1_9, "block.furnace.fire_crackle");
		addSound(BLOCK_GLASS_BREAK, BigClientVersion.v1_9, "block.glass.break");
		addSound(BLOCK_GLASS_FALL, BigClientVersion.v1_9, "block.glass.fall");
		addSound(BLOCK_GLASS_HIT, BigClientVersion.v1_9, "block.glass.hit");
		addSound(BLOCK_GLASS_PLACE, BigClientVersion.v1_9, "block.glass.place");
		addSound(BLOCK_GLASS_STEP, BigClientVersion.v1_9, "block.glass.step");
		addSound(BLOCK_GRASS_BREAK, BigClientVersion.v1_9, "block.grass.break");
		addSound(BLOCK_GRASS_FALL, BigClientVersion.v1_9, "block.grass.fall");
		addSound(BLOCK_GRASS_HIT, BigClientVersion.v1_9, "block.grass.hit");
		addSound(BLOCK_GRASS_PLACE, BigClientVersion.v1_9, "block.grass.place");
		addSound(BLOCK_GRASS_STEP, BigClientVersion.v1_9, "block.grass.step");
		addSound(BLOCK_GRAVEL_BREAK, BigClientVersion.v1_9, "block.gravel.break");
		addSound(BLOCK_GRAVEL_FALL, BigClientVersion.v1_9, "block.gravel.fall");
		addSound(BLOCK_GRAVEL_HIT, BigClientVersion.v1_9, "block.gravel.hit");
		addSound(BLOCK_GRAVEL_PLACE, BigClientVersion.v1_9, "block.gravel.place");
		addSound(BLOCK_GRAVEL_STEP, BigClientVersion.v1_9, "block.gravel.step");
		addSound(BLOCK_IRON_DOOR_CLOSE, BigClientVersion.v1_9, "block.iron_door.close");
		addSound(BLOCK_IRON_DOOR_OPEN, BigClientVersion.v1_9, "block.iron_door.open");
		addSound(BLOCK_IRON_TRAPDOOR_CLOSE, BigClientVersion.v1_9, "block.iron_trapdoor.close");
		addSound(BLOCK_IRON_TRAPDOOR_OPEN, BigClientVersion.v1_9, "block.iron_trapdoor.open");
		addSound(BLOCK_LADDER_BREAK, BigClientVersion.v1_9, "block.ladder.break");
		addSound(BLOCK_LADDER_FALL, BigClientVersion.v1_9, "block.ladder.fall");
		addSound(BLOCK_LADDER_HIT, BigClientVersion.v1_9, "block.ladder.hit");
		addSound(BLOCK_LADDER_PLACE, BigClientVersion.v1_9, "block.ladder.place");
		addSound(BLOCK_LADDER_STEP, BigClientVersion.v1_9, "block.ladder.step");
		addSound(BLOCK_LAVA_AMBIENT, BigClientVersion.v1_9, "block.lava.ambient");
		addSound(BLOCK_LAVA_EXTINGUISH, BigClientVersion.v1_9, "block.lava.extinguish");
		addSound(BLOCK_LAVA_POP, BigClientVersion.v1_9, "block.lava.pop");
		addSound(BLOCK_LEVER_CLICK, BigClientVersion.v1_9, "block.lever.click");
		addSound(BLOCK_METAL_BREAK, BigClientVersion.v1_9, "block.metal.break");
		addSound(BLOCK_METAL_FALL, BigClientVersion.v1_9, "block.metal.fall");
		addSound(BLOCK_METAL_HIT, BigClientVersion.v1_9, "block.metal.hit");
		addSound(BLOCK_METAL_PLACE, BigClientVersion.v1_9, "block.metal.place");
		addSound(BLOCK_METAL_STEP, BigClientVersion.v1_9, "block.metal.step");
		addSound(BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.metal_pressureplate.click_off");
		addSound(BLOCK_METAL_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.metal_pressureplate.click_on");
		addSound(BLOCK_NOTE_BASEDRUM, BigClientVersion.v1_9, "block.note.basedrum");
		addSound(BLOCK_NOTE_BASS, BigClientVersion.v1_9, "block.note.bass");
		addSound(BLOCK_NOTE_HARP, BigClientVersion.v1_9, "block.note.harp");
		addSound(BLOCK_NOTE_HAT, BigClientVersion.v1_9, "block.note.hat");
		addSound(BLOCK_NOTE_PLING, BigClientVersion.v1_9, "block.note.pling");
		addSound(BLOCK_NOTE_SNARE, BigClientVersion.v1_9, "block.note.snare");
		addSound(BLOCK_PISTON_CONTRACT, BigClientVersion.v1_9, "block.piston.contract");
		addSound(BLOCK_PISTON_EXTEND, BigClientVersion.v1_9, "block.piston.extend");
		addSound(BLOCK_PORTAL_AMBIENT, BigClientVersion.v1_9, "block.portal.ambient");
		addSound(BLOCK_PORTAL_TRAVEL, BigClientVersion.v1_9, "block.portal.travel");
		addSound(BLOCK_PORTAL_TRIGGER, BigClientVersion.v1_9, "block.portal.trigger");
		addSound(BLOCK_REDSTONE_TORCH_BURNOUT, BigClientVersion.v1_9, "block.redstone_torch.burnout");
		addSound(BLOCK_SAND_BREAK, BigClientVersion.v1_9, "block.sand.break");
		addSound(BLOCK_SAND_FALL, BigClientVersion.v1_9, "block.sand.fall");
		addSound(BLOCK_SAND_HIT, BigClientVersion.v1_9, "block.sand.hit");
		addSound(BLOCK_SAND_PLACE, BigClientVersion.v1_9, "block.sand.place");
		addSound(BLOCK_SAND_STEP, BigClientVersion.v1_9, "block.sand.step");
		addSound(BLOCK_SLIME_BREAK, BigClientVersion.v1_9, "block.slime.break");
		addSound(BLOCK_SLIME_FALL, BigClientVersion.v1_9, "block.slime.fall");
		addSound(BLOCK_SLIME_HIT, BigClientVersion.v1_9, "block.slime.hit");
		addSound(BLOCK_SLIME_PLACE, BigClientVersion.v1_9, "block.slime.place");
		addSound(BLOCK_SLIME_STEP, BigClientVersion.v1_9, "block.slime.step");
		addSound(BLOCK_SNOW_BREAK, BigClientVersion.v1_9, "block.snow.break");
		addSound(BLOCK_SNOW_FALL, BigClientVersion.v1_9, "block.snow.fall");
		addSound(BLOCK_SNOW_HIT, BigClientVersion.v1_9, "block.snow.hit");
		addSound(BLOCK_SNOW_PLACE, BigClientVersion.v1_9, "block.snow.place");
		addSound(BLOCK_SNOW_STEP, BigClientVersion.v1_9, "block.snow.step");
		addSound(BLOCK_STONE_BREAK, BigClientVersion.v1_9, "block.stone.break");
		addSound(BLOCK_STONE_FALL, BigClientVersion.v1_9, "block.stone.fall");
		addSound(BLOCK_STONE_HIT, BigClientVersion.v1_9, "block.stone.hit");
		addSound(BLOCK_STONE_PLACE, BigClientVersion.v1_9, "block.stone.place");
		addSound(BLOCK_STONE_STEP, BigClientVersion.v1_9, "block.stone.step");
		addSound(BLOCK_STONE_BUTTON_CLICK_OFF, BigClientVersion.v1_9, "block.stone_button.click_off");
		addSound(BLOCK_STONE_BUTTON_CLICK_ON, BigClientVersion.v1_9, "block.stone_button.click_on");
		addSound(BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.stone_pressureplate.click_off");
		addSound(BLOCK_STONE_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.stone_pressureplate.click_on");
		addSound(BLOCK_TRIPWIRE_ATTACH, BigClientVersion.v1_9, "block.tripwire.attach");
		addSound(BLOCK_TRIPWIRE_CLICK_OFF, BigClientVersion.v1_9, "block.tripwire.click_off");
		addSound(BLOCK_TRIPWIRE_CLICK_ON, BigClientVersion.v1_9, "block.tripwire.click_on");
		addSound(BLOCK_TRIPWIRE_DETACH, BigClientVersion.v1_9, "block.tripwire.detach");
		addSound(BLOCK_WATER_AMBIENT, BigClientVersion.v1_9, "block.water.ambient");
		addSound(BLOCK_WATERLILY_PLACE, BigClientVersion.v1_9, "block.waterlily.place");
		addSound(BLOCK_WOOD_BREAK, BigClientVersion.v1_9, "block.wood.break");
		addSound(BLOCK_WOOD_FALL, BigClientVersion.v1_9, "block.wood.fall");
		addSound(BLOCK_WOOD_HIT, BigClientVersion.v1_9, "block.wood.hit");
		addSound(BLOCK_WOOD_PLACE, BigClientVersion.v1_9, "block.wood.place");
		addSound(BLOCK_WOOD_STEP, BigClientVersion.v1_9, "block.wood.step");
		addSound(BLOCK_WOOD_BUTTON_CLICK_OFF, BigClientVersion.v1_9, "block.wood_button.click_off");
		addSound(BLOCK_WOOD_BUTTON_CLICK_ON, BigClientVersion.v1_9, "block.wood_button.click_on");
		addSound(BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, BigClientVersion.v1_9, "block.wood_pressureplate.click_off");
		addSound(BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, BigClientVersion.v1_9, "block.wood_pressureplate.click_on");
		addSound(BLOCK_WOODEN_DOOR_CLOSE, BigClientVersion.v1_9, "block.wooden_door.close");
		addSound(BLOCK_WOODEN_DOOR_OPEN, BigClientVersion.v1_9, "block.wooden_door.open");
		addSound(BLOCK_WOODEN_TRAPDOOR_CLOSE, BigClientVersion.v1_9, "block.wooden_trapdoor.close");
		addSound(BLOCK_WOODEN_TRAPDOOR_OPEN, BigClientVersion.v1_9, "block.wooden_trapdoor.open");
		addSound(ENCHANT_THORNS_HIT, BigClientVersion.v1_9, "enchant.thorns.hit");
		addSound(ENTITY_ARMORSTAND_BREAK, BigClientVersion.v1_9, "entity.armorstand.break");
		addSound(ENTITY_ARMORSTAND_FALL, BigClientVersion.v1_9, "entity.armorstand.fall");
		addSound(ENTITY_ARMORSTAND_HIT, BigClientVersion.v1_9, "entity.armorstand.hit");
		addSound(ENTITY_ARMORSTAND_PLACE, BigClientVersion.v1_9, "entity.armorstand.place");
		addSound(ENTITY_ARROW_HIT, BigClientVersion.v1_9, "entity.arrow.hit");
		addSound(ENTITY_ARROW_HIT_PLAYER, BigClientVersion.v1_9, "entity.arrow.hit_player");
		addSound(ENTITY_ARROW_SHOOT, BigClientVersion.v1_9, "entity.arrow.shoot");
		addSound(ENTITY_BAT_AMBIENT, BigClientVersion.v1_9, "entity.bat.ambient");
		addSound(ENTITY_BAT_DEATH, BigClientVersion.v1_9, "entity.bat.death");
		addSound(ENTITY_BAT_HURT, BigClientVersion.v1_9, "entity.bat.hurt");
		addSound(ENTITY_BAT_LOOP, BigClientVersion.v1_9, "entity.bat.loop");
		addSound(ENTITY_BAT_TAKEOFF, BigClientVersion.v1_9, "entity.bat.takeoff");
		addSound(ENTITY_BLAZE_AMBIENT, BigClientVersion.v1_9, "entity.blaze.ambient");
		addSound(ENTITY_BLAZE_BURN, BigClientVersion.v1_9, "entity.blaze.burn");
		addSound(ENTITY_BLAZE_DEATH, BigClientVersion.v1_9, "entity.blaze.death");
		addSound(ENTITY_BLAZE_HURT, BigClientVersion.v1_9, "entity.blaze.hurt");
		addSound(ENTITY_BLAZE_SHOOT, BigClientVersion.v1_9, "entity.blaze.shoot");
		addSound(ENTITY_BOBBER_SPLASH, BigClientVersion.v1_9, "entity.bobber.splash");
		addSound(ENTITY_BOBBER_THROW, BigClientVersion.v1_9, "entity.bobber.throw");
		addSound(ENTITY_CAT_AMBIENT, BigClientVersion.v1_9, "entity.cat.ambient");
		addSound(ENTITY_CAT_DEATH, BigClientVersion.v1_9, "entity.cat.death");
		addSound(ENTITY_CAT_HISS, BigClientVersion.v1_9, "entity.cat.hiss");
		addSound(ENTITY_CAT_HURT, BigClientVersion.v1_9, "entity.cat.hurt");
		addSound(ENTITY_CAT_PURR, BigClientVersion.v1_9, "entity.cat.purr");
		addSound(ENTITY_CAT_PURREOW, BigClientVersion.v1_9, "entity.cat.purreow");
		addSound(ENTITY_CHICKEN_AMBIENT, BigClientVersion.v1_9, "entity.chicken.ambient");
		addSound(ENTITY_CHICKEN_DEATH, BigClientVersion.v1_9, "entity.chicken.death");
		addSound(ENTITY_CHICKEN_EGG, BigClientVersion.v1_9, "entity.chicken.egg");
		addSound(ENTITY_CHICKEN_HURT, BigClientVersion.v1_9, "entity.chicken.hurt");
		addSound(ENTITY_CHICKEN_STEP, BigClientVersion.v1_9, "entity.chicken.step");
		addSound(ENTITY_COW_AMBIENT, BigClientVersion.v1_9, "entity.cow.ambient");
		addSound(ENTITY_COW_DEATH, BigClientVersion.v1_9, "entity.cow.death");
		addSound(ENTITY_COW_HURT, BigClientVersion.v1_9, "entity.cow.hurt");
		addSound(ENTITY_COW_MILK, BigClientVersion.v1_9, "entity.cow.milk");
		addSound(ENTITY_COW_STEP, BigClientVersion.v1_9, "entity.cow.step");
		addSound(ENTITY_CREEPER_DEATH, BigClientVersion.v1_9, "entity.creeper.death");
		addSound(ENTITY_CREEPER_HURT, BigClientVersion.v1_9, "entity.creeper.hurt");
		addSound(ENTITY_CREEPER_PRIMED, BigClientVersion.v1_9, "entity.creeper.primed");
		addSound(ENTITY_DONKEY_AMBIENT, BigClientVersion.v1_9, "entity.donkey.ambient");
		addSound(ENTITY_DONKEY_ANGRY, BigClientVersion.v1_9, "entity.donkey.angry");
		addSound(ENTITY_DONKEY_CHEST, BigClientVersion.v1_9, "entity.donkey.chest");
		addSound(ENTITY_DONKEY_DEATH, BigClientVersion.v1_9, "entity.donkey.death");
		addSound(ENTITY_DONKEY_HURT, BigClientVersion.v1_9, "entity.donkey.hurt");
		addSound(ENTITY_EGG_THROW, BigClientVersion.v1_9, "entity.egg.throw");
		addSound(ENTITY_ELDER_GUARDIAN_AMBIENT, BigClientVersion.v1_9, "entity.elder_guardian.ambient");
		addSound(ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_9, "entity.elder_guardian.ambient_land");
		addSound(ENTITY_ELDER_GUARDIAN_CURSE, BigClientVersion.v1_9, "entity.elder_guardian.curse");
		addSound(ENTITY_ELDER_GUARDIAN_DEATH, BigClientVersion.v1_9, "entity.elder_guardian.death");
		addSound(ENTITY_ELDER_GUARDIAN_DEATH_LAND, BigClientVersion.v1_9, "entity.elder_guardian.death_land");
		addSound(ENTITY_ELDER_GUARDIAN_HURT, BigClientVersion.v1_9, "entity.elder_guardian.hurt");
		addSound(ENTITY_ELDER_GUARDIAN_HURT_LAND, BigClientVersion.v1_9, "entity.elder_guardian.hurt_land");
		addSound(ENTITY_ENDERDRAGON_AMBIENT, BigClientVersion.v1_9, "entity.enderdragon.ambient");
		addSound(ENTITY_ENDERDRAGON_DEATH, BigClientVersion.v1_9, "entity.enderdragon.death");
		addSound(ENTITY_ENDERDRAGON_FLAP, BigClientVersion.v1_9, "entity.enderdragon.flap");
		addSound(ENTITY_ENDERDRAGON_GROWL, BigClientVersion.v1_9, "entity.enderdragon.growl");
		addSound(ENTITY_ENDERDRAGON_HURT, BigClientVersion.v1_9, "entity.enderdragon.hurt");
		addSound(ENTITY_ENDERDRAGON_SHOOT, BigClientVersion.v1_9, "entity.enderdragon.shoot");
		addSound(ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, BigClientVersion.v1_9, "entity.enderdragon_fireball.explode");
		addSound(ENTITY_ENDEREYE_LAUNCH, BigClientVersion.v1_9, "entity.endereye.launch");
		addSound(ENTITY_ENDERMEN_AMBIENT, BigClientVersion.v1_9, "entity.endermen.ambient");
		addSound(ENTITY_ENDERMEN_DEATH, BigClientVersion.v1_9, "entity.endermen.death");
		addSound(ENTITY_ENDERMEN_HURT, BigClientVersion.v1_9, "entity.endermen.hurt");
		addSound(ENTITY_ENDERMEN_SCREAM, BigClientVersion.v1_9, "entity.endermen.scream");
		addSound(ENTITY_ENDERMEN_STARE, BigClientVersion.v1_9, "entity.endermen.stare");
		addSound(ENTITY_ENDERMEN_TELEPORT, BigClientVersion.v1_9, "entity.endermen.teleport");
		addSound(ENTITY_ENDERMITE_AMBIENT, BigClientVersion.v1_9, "entity.endermite.ambient");
		addSound(ENTITY_ENDERMITE_DEATH, BigClientVersion.v1_9, "entity.endermite.death");
		addSound(ENTITY_ENDERMITE_HURT, BigClientVersion.v1_9, "entity.endermite.hurt");
		addSound(ENTITY_ENDERMITE_STEP, BigClientVersion.v1_9, "entity.endermite.step");
		addSound(ENTITY_ENDERPEARL_THROW, BigClientVersion.v1_9, "entity.enderpearl.throw");
		addSound(ENTITY_EXPERIENCE_BOTTLE_THROW, BigClientVersion.v1_9, "entity.experience_bottle.throw");
		addSound(ENTITY_EXPERIENCE_ORB_PICKUP, BigClientVersion.v1_9, "entity.experience_orb.pickup");
		addSound(ENTITY_EXPERIENCE_ORB_TOUCH, BigClientVersion.v1_9, "entity.experience_orb.touch");
		addSound(ENTITY_FIREWORK_BLAST, BigClientVersion.v1_9, "entity.firework.blast");
		addSound(ENTITY_FIREWORK_BLAST_FAR, BigClientVersion.v1_9, "entity.firework.blast_far");
		addSound(ENTITY_FIREWORK_LARGE_BLAST, BigClientVersion.v1_9, "entity.firework.large_blast");
		addSound(ENTITY_FIREWORK_LARGE_BLAST_FAR, BigClientVersion.v1_9, "entity.firework.large_blast_far");
		addSound(ENTITY_FIREWORK_LAUNCH, BigClientVersion.v1_9, "entity.firework.launch");
		addSound(ENTITY_FIREWORK_SHOOT, BigClientVersion.v1_9, "entity.firework.shoot");
		addSound(ENTITY_FIREWORK_TWINKLE, BigClientVersion.v1_9, "entity.firework.twinkle");
		addSound(ENTITY_FIREWORK_TWINKLE_FAR, BigClientVersion.v1_9, "entity.firework.twinkle_far");
		addSound(ENTITY_GENERIC_BIG_FALL, BigClientVersion.v1_9, "entity.generic.big_fall");
		addSound(ENTITY_GENERIC_BURN, BigClientVersion.v1_9, "entity.generic.burn");
		addSound(ENTITY_GENERIC_DEATH, BigClientVersion.v1_9, "entity.generic.death");
		addSound(ENTITY_GENERIC_DRINK, BigClientVersion.v1_9, "entity.generic.drink");
		addSound(ENTITY_GENERIC_EAT, BigClientVersion.v1_9, "entity.generic.eat");
		addSound(ENTITY_GENERIC_EXPLODE, BigClientVersion.v1_9, "entity.generic.explode");
		addSound(ENTITY_GENERIC_EXTINGUISH_FIRE, BigClientVersion.v1_9, "entity.generic.extinguish_fire");
		addSound(ENTITY_GENERIC_HURT, BigClientVersion.v1_9, "entity.generic.hurt");
		addSound(ENTITY_GENERIC_SMALL_FALL, BigClientVersion.v1_9, "entity.generic.small_fall");
		addSound(ENTITY_GENERIC_SPLASH, BigClientVersion.v1_9, "entity.generic.splash");
		addSound(ENTITY_GENERIC_SWIM, BigClientVersion.v1_9, "entity.generic.swim");
		addSound(ENTITY_GHAST_AMBIENT, BigClientVersion.v1_9, "entity.ghast.ambient");
		addSound(ENTITY_GHAST_DEATH, BigClientVersion.v1_9, "entity.ghast.death");
		addSound(ENTITY_GHAST_HURT, BigClientVersion.v1_9, "entity.ghast.hurt");
		addSound(ENTITY_GHAST_SCREAM, BigClientVersion.v1_9, "entity.ghast.scream");
		addSound(ENTITY_GHAST_SHOOT, BigClientVersion.v1_9, "entity.ghast.shoot");
		addSound(ENTITY_GHAST_WARN, BigClientVersion.v1_9, "entity.ghast.warn");
		addSound(ENTITY_GUARDIAN_AMBIENT, BigClientVersion.v1_9, "entity.guardian.ambient");
		addSound(ENTITY_GUARDIAN_AMBIENT_LAND, BigClientVersion.v1_9, "entity.guardian.ambient_land");
		addSound(ENTITY_GUARDIAN_ATTACK, BigClientVersion.v1_9, "entity.guardian.attack");
		addSound(ENTITY_GUARDIAN_DEATH, BigClientVersion.v1_9, "entity.guardian.death");
		addSound(ENTITY_GUARDIAN_DEATH_LAND, BigClientVersion.v1_9, "entity.guardian.death_land");
		addSound(ENTITY_GUARDIAN_FLOP, BigClientVersion.v1_9, "entity.guardian.flop");
		addSound(ENTITY_GUARDIAN_HURT, BigClientVersion.v1_9, "entity.guardian.hurt");
		addSound(ENTITY_GUARDIAN_HURT_LAND, BigClientVersion.v1_9, "entity.guardian.hurt_land");
		addSound(ENTITY_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.horse.ambient");
		addSound(ENTITY_HORSE_ANGRY, BigClientVersion.v1_9, "entity.horse.angry");
		addSound(ENTITY_HORSE_ARMOR, BigClientVersion.v1_9, "entity.horse.armor");
		addSound(ENTITY_HORSE_BREATHE, BigClientVersion.v1_9, "entity.horse.breathe");
		addSound(ENTITY_HORSE_DEATH, BigClientVersion.v1_9, "entity.horse.death");
		addSound(ENTITY_HORSE_EAT, BigClientVersion.v1_9, "entity.horse.eat");
		addSound(ENTITY_HORSE_GALLOP, BigClientVersion.v1_9, "entity.horse.gallop");
		addSound(ENTITY_HORSE_HURT, BigClientVersion.v1_9, "entity.horse.hurt");
		addSound(ENTITY_HORSE_JUMP, BigClientVersion.v1_9, "entity.horse.jump");
		addSound(ENTITY_HORSE_LAND, BigClientVersion.v1_9, "entity.horse.land");
		addSound(ENTITY_HORSE_SADDLE, BigClientVersion.v1_9, "entity.horse.saddle");
		addSound(ENTITY_HORSE_STEP, BigClientVersion.v1_9, "entity.horse.step");
		addSound(ENTITY_HORSE_STEP_WOOD, BigClientVersion.v1_9, "entity.horse.step_wood");
		addSound(ENTITY_HOSTILE_BIG_FALL, BigClientVersion.v1_9, "entity.hostile.big_fall");
		addSound(ENTITY_HOSTILE_DEATH, BigClientVersion.v1_9, "entity.hostile.death");
		addSound(ENTITY_HOSTILE_HURT, BigClientVersion.v1_9, "entity.hostile.hurt");
		addSound(ENTITY_HOSTILE_SMALL_FALL, BigClientVersion.v1_9, "entity.hostile.small_fall");
		addSound(ENTITY_HOSTILE_SPLASH, BigClientVersion.v1_9, "entity.hostile.splash");
		addSound(ENTITY_HOSTILE_SWIM, BigClientVersion.v1_9, "entity.hostile.swim");
		addSound(ENTITY_IRONGOLEM_ATTACK, BigClientVersion.v1_9, "entity.irongolem.attack");
		addSound(ENTITY_IRONGOLEM_DEATH, BigClientVersion.v1_9, "entity.irongolem.death");
		addSound(ENTITY_IRONGOLEM_HURT, BigClientVersion.v1_9, "entity.irongolem.hurt");
		addSound(ENTITY_IRONGOLEM_STEP, BigClientVersion.v1_9, "entity.irongolem.step");
		addSound(ENTITY_ITEM_BREAK, BigClientVersion.v1_9, "entity.item.break");
		addSound(ENTITY_ITEM_PICKUP, BigClientVersion.v1_9, "entity.item.pickup");
		addSound(ENTITY_ITEMFRAME_ADD_ITEM, BigClientVersion.v1_9, "entity.itemframe.add_item");
		addSound(ENTITY_ITEMFRAME_BREAK, BigClientVersion.v1_9, "entity.itemframe.break");
		addSound(ENTITY_ITEMFRAME_PLACE, BigClientVersion.v1_9, "entity.itemframe.place");
		addSound(ENTITY_ITEMFRAME_REMOVE_ITEM, BigClientVersion.v1_9, "entity.itemframe.remove_item");
		addSound(ENTITY_ITEMFRAME_ROTATE_ITEM, BigClientVersion.v1_9, "entity.itemframe.rotate_item");
		addSound(ENTITY_LEASHKNOT_BREAK, BigClientVersion.v1_9, "entity.leashknot.break");
		addSound(ENTITY_LEASHKNOT_PLACE, BigClientVersion.v1_9, "entity.leashknot.place");
		addSound(ENTITY_LIGHTNING_IMPACT, BigClientVersion.v1_9, "entity.lightning.impact");
		addSound(ENTITY_LIGHTNING_THUNDER, BigClientVersion.v1_9, "entity.lightning.thunder");
		addSound(ENTITY_LINGERINGPOTION_THROW, BigClientVersion.v1_9, "entity.lingeringpotion.throw");
		addSound(ENTITY_MAGMACUBE_DEATH, BigClientVersion.v1_9, "entity.magmacube.death");
		addSound(ENTITY_MAGMACUBE_HURT, BigClientVersion.v1_9, "entity.magmacube.hurt");
		addSound(ENTITY_MAGMACUBE_JUMP, BigClientVersion.v1_9, "entity.magmacube.jump");
		addSound(ENTITY_MAGMACUBE_SQUISH, BigClientVersion.v1_9, "entity.magmacube.squish");
		addSound(ENTITY_MINECART_INSIDE, BigClientVersion.v1_9, "entity.minecart.inside");
		addSound(ENTITY_MINECART_RIDING, BigClientVersion.v1_9, "entity.minecart.riding");
		addSound(ENTITY_MOOSHROOM_SHEAR, BigClientVersion.v1_9, "entity.mooshroom.shear");
		addSound(ENTITY_MULE_AMBIENT, BigClientVersion.v1_9, "entity.mule.ambient");
		addSound(ENTITY_MULE_DEATH, BigClientVersion.v1_9, "entity.mule.death");
		addSound(ENTITY_MULE_HURT, BigClientVersion.v1_9, "entity.mule.hurt");
		addSound(ENTITY_PAINTING_BREAK, BigClientVersion.v1_9, "entity.painting.break");
		addSound(ENTITY_PAINTING_PLACE, BigClientVersion.v1_9, "entity.painting.place");
		addSound(ENTITY_PIG_AMBIENT, BigClientVersion.v1_9, "entity.pig.ambient");
		addSound(ENTITY_PIG_DEATH, BigClientVersion.v1_9, "entity.pig.death");
		addSound(ENTITY_PIG_HURT, BigClientVersion.v1_9, "entity.pig.hurt");
		addSound(ENTITY_PIG_SADDLE, BigClientVersion.v1_9, "entity.pig.saddle");
		addSound(ENTITY_PIG_STEP, BigClientVersion.v1_9, "entity.pig.step");
		addSound(ENTITY_PLAYER_ATTACK_CRIT, BigClientVersion.v1_9, "entity.player.attack.crit");
		addSound(ENTITY_PLAYER_ATTACK_KNOCKBACK, BigClientVersion.v1_9, "entity.player.attack.knockback");
		addSound(ENTITY_PLAYER_ATTACK_NODAMAGE, BigClientVersion.v1_9, "entity.player.attack.nodamage");
		addSound(ENTITY_PLAYER_ATTACK_STRONG, BigClientVersion.v1_9, "entity.player.attack.strong");
		addSound(ENTITY_PLAYER_ATTACK_SWEEP, BigClientVersion.v1_9, "entity.player.attack.sweep");
		addSound(ENTITY_PLAYER_ATTACK_WEAK, BigClientVersion.v1_9, "entity.player.attack.weak");
		addSound(ENTITY_PLAYER_BIG_FALL, BigClientVersion.v1_9, "entity.player.big_fall");
		addSound(ENTITY_PLAYER_BREATH, BigClientVersion.v1_9, "entity.player.breath");
		addSound(ENTITY_PLAYER_BURP, BigClientVersion.v1_9, "entity.player.burp");
		addSound(ENTITY_PLAYER_DEATH, BigClientVersion.v1_9, "entity.player.death");
		addSound(ENTITY_PLAYER_HURT, BigClientVersion.v1_9, "entity.player.hurt");
		addSound(ENTITY_PLAYER_LEVELUP, BigClientVersion.v1_9, "entity.player.levelup");
		addSound(ENTITY_PLAYER_SMALL_FALL, BigClientVersion.v1_9, "entity.player.small_fall");
		addSound(ENTITY_PLAYER_SPLASH, BigClientVersion.v1_9, "entity.player.splash");
		addSound(ENTITY_PLAYER_SWIM, BigClientVersion.v1_9, "entity.player.swim");
		addSound(ENTITY_RABBIT_AMBIENT, BigClientVersion.v1_9, "entity.rabbit.ambient");
		addSound(ENTITY_RABBIT_ATTACK, BigClientVersion.v1_9, "entity.rabbit.attack");
		addSound(ENTITY_RABBIT_DEATH, BigClientVersion.v1_9, "entity.rabbit.death");
		addSound(ENTITY_RABBIT_HURT, BigClientVersion.v1_9, "entity.rabbit.hurt");
		addSound(ENTITY_RABBIT_JUMP, BigClientVersion.v1_9, "entity.rabbit.jump");
		addSound(ENTITY_SHEEP_AMBIENT, BigClientVersion.v1_9, "entity.sheep.ambient");
		addSound(ENTITY_SHEEP_DEATH, BigClientVersion.v1_9, "entity.sheep.death");
		addSound(ENTITY_SHEEP_HURT, BigClientVersion.v1_9, "entity.sheep.hurt");
		addSound(ENTITY_SHEEP_SHEAR, BigClientVersion.v1_9, "entity.sheep.shear");
		addSound(ENTITY_SHEEP_STEP, BigClientVersion.v1_9, "entity.sheep.step");
		addSound(ENTITY_SHULKER_AMBIENT, BigClientVersion.v1_9, "entity.shulker.ambient");
		addSound(ENTITY_SHULKER_CLOSE, BigClientVersion.v1_9, "entity.shulker.close");
		addSound(ENTITY_SHULKER_DEATH, BigClientVersion.v1_9, "entity.shulker.death");
		addSound(ENTITY_SHULKER_HURT, BigClientVersion.v1_9, "entity.shulker.hurt");
		addSound(ENTITY_SHULKER_HURT_CLOSED, BigClientVersion.v1_9, "entity.shulker.hurt_closed");
		addSound(ENTITY_SHULKER_OPEN, BigClientVersion.v1_9, "entity.shulker.open");
		addSound(ENTITY_SHULKER_SHOOT, BigClientVersion.v1_9, "entity.shulker.shoot");
		addSound(ENTITY_SHULKER_TELEPORT, BigClientVersion.v1_9, "entity.shulker.teleport");
		addSound(ENTITY_SHULKER_BULLET_HIT, BigClientVersion.v1_9, "entity.shulker_bullet.hit");
		addSound(ENTITY_SHULKER_BULLET_HURT, BigClientVersion.v1_9, "entity.shulker_bullet.hurt");
		addSound(ENTITY_SILVERFISH_AMBIENT, BigClientVersion.v1_9, "entity.silverfish.ambient");
		addSound(ENTITY_SILVERFISH_DEATH, BigClientVersion.v1_9, "entity.silverfish.death");
		addSound(ENTITY_SILVERFISH_HURT, BigClientVersion.v1_9, "entity.silverfish.hurt");
		addSound(ENTITY_SILVERFISH_STEP, BigClientVersion.v1_9, "entity.silverfish.step");
		addSound(ENTITY_SKELETON_AMBIENT, BigClientVersion.v1_9, "entity.skeleton.ambient");
		addSound(ENTITY_SKELETON_DEATH, BigClientVersion.v1_9, "entity.skeleton.death");
		addSound(ENTITY_SKELETON_HURT, BigClientVersion.v1_9, "entity.skeleton.hurt");
		addSound(ENTITY_SKELETON_SHOOT, BigClientVersion.v1_9, "entity.skeleton.shoot");
		addSound(ENTITY_SKELETON_STEP, BigClientVersion.v1_9, "entity.skeleton.step");
		addSound(ENTITY_SKELETON_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.skeleton_horse.ambient");
		addSound(ENTITY_SKELETON_HORSE_DEATH, BigClientVersion.v1_9, "entity.skeleton_horse.death");
		addSound(ENTITY_SKELETON_HORSE_HURT, BigClientVersion.v1_9, "entity.skeleton_horse.hurt");
		addSound(ENTITY_SLIME_ATTACK, BigClientVersion.v1_9, "entity.slime.attack");
		addSound(ENTITY_SLIME_DEATH, BigClientVersion.v1_9, "entity.slime.death");
		addSound(ENTITY_SLIME_HURT, BigClientVersion.v1_9, "entity.slime.hurt");
		addSound(ENTITY_SLIME_JUMP, BigClientVersion.v1_9, "entity.slime.jump");
		addSound(ENTITY_SLIME_SQUISH, BigClientVersion.v1_9, "entity.slime.squish");
		addSound(ENTITY_SMALL_MAGMACUBE_DEATH, BigClientVersion.v1_9, "entity.small_magmacube.death");
		addSound(ENTITY_SMALL_MAGMACUBE_HURT, BigClientVersion.v1_9, "entity.small_magmacube.hurt");
		addSound(ENTITY_SMALL_MAGMACUBE_SQUISH, BigClientVersion.v1_9, "entity.small_magmacube.squish");
		addSound(ENTITY_SMALL_SLIME_DEATH, BigClientVersion.v1_9, "entity.small_slime.death");
		addSound(ENTITY_SMALL_SLIME_HURT, BigClientVersion.v1_9, "entity.small_slime.hurt");
		addSound(ENTITY_SMALL_SLIME_JUMP, BigClientVersion.v1_9, "entity.small_slime.jump");
		addSound(ENTITY_SMALL_SLIME_SQUISH, BigClientVersion.v1_9, "entity.small_slime.squish");
		addSound(ENTITY_SNOWBALL_THROW, BigClientVersion.v1_9, "entity.snowball.throw");
		addSound(ENTITY_SNOWMAN_AMBIENT, BigClientVersion.v1_9, "entity.snowman.ambient");
		addSound(ENTITY_SNOWMAN_DEATH, BigClientVersion.v1_9, "entity.snowman.death");
		addSound(ENTITY_SNOWMAN_HURT, BigClientVersion.v1_9, "entity.snowman.hurt");
		addSound(ENTITY_SNOWMAN_SHOOT, BigClientVersion.v1_9, "entity.snowman.shoot");
		addSound(ENTITY_SPIDER_AMBIENT, BigClientVersion.v1_9, "entity.spider.ambient");
		addSound(ENTITY_SPIDER_DEATH, BigClientVersion.v1_9, "entity.spider.death");
		addSound(ENTITY_SPIDER_HURT, BigClientVersion.v1_9, "entity.spider.hurt");
		addSound(ENTITY_SPIDER_STEP, BigClientVersion.v1_9, "entity.spider.step");
		addSound(ENTITY_SPLASH_POTION_BREAK, BigClientVersion.v1_9, "entity.splash_potion.break");
		addSound(ENTITY_SPLASH_POTION_THROW, BigClientVersion.v1_9, "entity.splash_potion.throw");
		addSound(ENTITY_SQUID_AMBIENT, BigClientVersion.v1_9, "entity.squid.ambient");
		addSound(ENTITY_SQUID_DEATH, BigClientVersion.v1_9, "entity.squid.death");
		addSound(ENTITY_SQUID_HURT, BigClientVersion.v1_9, "entity.squid.hurt");
		addSound(ENTITY_TNT_PRIMED, BigClientVersion.v1_9, "entity.tnt.primed");
		addSound(ENTITY_VILLAGER_AMBIENT, BigClientVersion.v1_9, "entity.villager.ambient");
		addSound(ENTITY_VILLAGER_DEATH, BigClientVersion.v1_9, "entity.villager.death");
		addSound(ENTITY_VILLAGER_HURT, BigClientVersion.v1_9, "entity.villager.hurt");
		addSound(ENTITY_VILLAGER_NO, BigClientVersion.v1_9, "entity.villager.no");
		addSound(ENTITY_VILLAGER_TRADING, BigClientVersion.v1_9, "entity.villager.trading");
		addSound(ENTITY_VILLAGER_YES, BigClientVersion.v1_9, "entity.villager.yes");
		addSound(ENTITY_WITCH_AMBIENT, BigClientVersion.v1_9, "entity.witch.ambient");
		addSound(ENTITY_WITCH_DEATH, BigClientVersion.v1_9, "entity.witch.death");
		addSound(ENTITY_WITCH_DRINK, BigClientVersion.v1_9, "entity.witch.drink");
		addSound(ENTITY_WITCH_HURT, BigClientVersion.v1_9, "entity.witch.hurt");
		addSound(ENTITY_WITCH_THROW, BigClientVersion.v1_9, "entity.witch.throw");
		addSound(ENTITY_WITHER_AMBIENT, BigClientVersion.v1_9, "entity.wither.ambient");
		addSound(ENTITY_WITHER_BREAK_BLOCK, BigClientVersion.v1_9, "entity.wither.break_block");
		addSound(ENTITY_WITHER_DEATH, BigClientVersion.v1_9, "entity.wither.death");
		addSound(ENTITY_WITHER_HURT, BigClientVersion.v1_9, "entity.wither.hurt");
		addSound(ENTITY_WITHER_SHOOT, BigClientVersion.v1_9, "entity.wither.shoot");
		addSound(ENTITY_WITHER_SPAWN, BigClientVersion.v1_9, "entity.wither.spawn");
		addSound(ENTITY_WOLF_AMBIENT, BigClientVersion.v1_9, "entity.wolf.ambient");
		addSound(ENTITY_WOLF_DEATH, BigClientVersion.v1_9, "entity.wolf.death");
		addSound(ENTITY_WOLF_GROWL, BigClientVersion.v1_9, "entity.wolf.growl");
		addSound(ENTITY_WOLF_HOWL, BigClientVersion.v1_9, "entity.wolf.howl");
		addSound(ENTITY_WOLF_HURT, BigClientVersion.v1_9, "entity.wolf.hurt");
		addSound(ENTITY_WOLF_PANT, BigClientVersion.v1_9, "entity.wolf.pant");
		addSound(ENTITY_WOLF_SHAKE, BigClientVersion.v1_9, "entity.wolf.shake");
		addSound(ENTITY_WOLF_STEP, BigClientVersion.v1_9, "entity.wolf.step");
		addSound(ENTITY_WOLF_WHINE, BigClientVersion.v1_9, "entity.wolf.whine");
		addSound(ENTITY_ZOMBIE_AMBIENT, BigClientVersion.v1_9, "entity.zombie.ambient");
		addSound(ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, BigClientVersion.v1_9, "entity.zombie.attack_door_wood");
		addSound(ENTITY_ZOMBIE_ATTACK_IRON_DOOR, BigClientVersion.v1_9, "entity.zombie.attack_iron_door");
		addSound(ENTITY_ZOMBIE_BREAK_DOOR_WOOD, BigClientVersion.v1_9, "entity.zombie.break_door_wood");
		addSound(ENTITY_ZOMBIE_DEATH, BigClientVersion.v1_9, "entity.zombie.death");
		addSound(ENTITY_ZOMBIE_HURT, BigClientVersion.v1_9, "entity.zombie.hurt");
		addSound(ENTITY_ZOMBIE_INFECT, BigClientVersion.v1_9, "entity.zombie.infect");
		addSound(ENTITY_ZOMBIE_STEP, BigClientVersion.v1_9, "entity.zombie.step");
		addSound(ENTITY_ZOMBIE_HORSE_AMBIENT, BigClientVersion.v1_9, "entity.zombie_horse.ambient");
		addSound(ENTITY_ZOMBIE_HORSE_DEATH, BigClientVersion.v1_9, "entity.zombie_horse.death");
		addSound(ENTITY_ZOMBIE_HORSE_HURT, BigClientVersion.v1_9, "entity.zombie_horse.hurt");
		addSound(ENTITY_ZOMBIE_PIG_AMBIENT, BigClientVersion.v1_9, "entity.zombie_pig.ambient");
		addSound(ENTITY_ZOMBIE_PIG_ANGRY, BigClientVersion.v1_9, "entity.zombie_pig.angry");
		addSound(ENTITY_ZOMBIE_PIG_DEATH, BigClientVersion.v1_9, "entity.zombie_pig.death");
		addSound(ENTITY_ZOMBIE_PIG_HURT, BigClientVersion.v1_9, "entity.zombie_pig.hurt");
		addSound(ENTITY_ZOMBIE_VILLAGER_AMBIENT, BigClientVersion.v1_9, "entity.zombie_villager.ambient");
		addSound(ENTITY_ZOMBIE_VILLAGER_CONVERTED, BigClientVersion.v1_9, "entity.zombie_villager.converted");
		addSound(ENTITY_ZOMBIE_VILLAGER_CURE, BigClientVersion.v1_9, "entity.zombie_villager.cure");
		addSound(ENTITY_ZOMBIE_VILLAGER_DEATH, BigClientVersion.v1_9, "entity.zombie_villager.death");
		addSound(ENTITY_ZOMBIE_VILLAGER_HURT, BigClientVersion.v1_9, "entity.zombie_villager.hurt");
		addSound(ENTITY_ZOMBIE_VILLAGER_STEP, BigClientVersion.v1_9, "entity.zombie_villager.step");
		addSound(ITEM_ARMOR_EQUIP_CHAIN, BigClientVersion.v1_9, "item.armor.equip_chain");
		addSound(ITEM_ARMOR_EQUIP_DIAMOND, BigClientVersion.v1_9, "item.armor.equip_diamond");
		addSound(ITEM_ARMOR_EQUIP_GENERIC, BigClientVersion.v1_9, "item.armor.equip_generic");
		addSound(ITEM_ARMOR_EQUIP_GOLD, BigClientVersion.v1_9, "item.armor.equip_gold");
		addSound(ITEM_ARMOR_EQUIP_IRON, BigClientVersion.v1_9, "item.armor.equip_iron");
		addSound(ITEM_ARMOR_EQUIP_LEATHER, BigClientVersion.v1_9, "item.armor.equip_leather");
		addSound(ITEM_BOTTLE_FILL, BigClientVersion.v1_9, "item.bottle.fill");
		addSound(ITEM_BOTTLE_FILL_DRAGONBREATH, BigClientVersion.v1_9, "item.bottle.fill_dragonbreath");
		addSound(ITEM_BUCKET_EMPTY, BigClientVersion.v1_9, "item.bucket.empty");
		addSound(ITEM_BUCKET_EMPTY_LAVA, BigClientVersion.v1_9, "item.bucket.empty_lava");
		addSound(ITEM_BUCKET_FILL, BigClientVersion.v1_9, "item.bucket.fill");
		addSound(ITEM_BUCKET_FILL_LAVA, BigClientVersion.v1_9, "item.bucket.fill_lava");
		addSound(ITEM_CHORUS_FRUIT_TELEPORT, BigClientVersion.v1_9, "item.chorus_fruit.teleport");
		addSound(ITEM_ELYTRA_FLYING, BigClientVersion.v1_9, "item.elytra.flying");
		addSound(ITEM_FIRECHARGE_USE, BigClientVersion.v1_9, "item.firecharge.use");
		addSound(ITEM_FLINTANDSTEEL_USE, BigClientVersion.v1_9, "item.flintandsteel.use");
		addSound(ITEM_HOE_TILL, BigClientVersion.v1_9, "item.hoe.till");
		addSound(ITEM_SHIELD_BLOCK, BigClientVersion.v1_9, "item.shield.block");
		addSound(ITEM_SHIELD_BREAK, BigClientVersion.v1_9, "item.shield.break");
		addSound(ITEM_SHOVEL_FLATTEN, BigClientVersion.v1_9, "item.shovel.flatten");
		addSound(MUSIC_CREATIVE, BigClientVersion.v1_9, "music.creative");
		addSound(MUSIC_CREDITS, BigClientVersion.v1_9, "music.credits");
		addSound(MUSIC_DRAGON, BigClientVersion.v1_9, "music.dragon");
		addSound(MUSIC_END, BigClientVersion.v1_9, "music.end");
		addSound(MUSIC_GAME, BigClientVersion.v1_9, "music.game");
		addSound(MUSIC_MENU, BigClientVersion.v1_9, "music.menu");
		addSound(MUSIC_NETHER, BigClientVersion.v1_9, "music.nether");
		addSound(RECORD_11, BigClientVersion.v1_9, "record.11");
		addSound(RECORD_13, BigClientVersion.v1_9, "record.13");
		addSound(RECORD_BLOCKS, BigClientVersion.v1_9, "record.blocks");
		addSound(RECORD_CAT, BigClientVersion.v1_9, "record.cat");
		addSound(RECORD_CHIRP, BigClientVersion.v1_9, "record.chirp");
		addSound(RECORD_FAR, BigClientVersion.v1_9, "record.far");
		addSound(RECORD_MALL, BigClientVersion.v1_9, "record.mall");
		addSound(RECORD_MELLOHI, BigClientVersion.v1_9, "record.mellohi");
		addSound(RECORD_STAL, BigClientVersion.v1_9, "record.stal");
		addSound(RECORD_STRAD, BigClientVersion.v1_9, "record.strad");
		addSound(RECORD_WAIT, BigClientVersion.v1_9, "record.wait");
		addSound(RECORD_WARD, BigClientVersion.v1_9, "record.ward");
		addSound(UI_BUTTON_CLICK, BigClientVersion.v1_9, "ui.button.click");
		addSound(WEATHER_RAIN, BigClientVersion.v1_9, "weather.rain");
		addSound(WEATHER_RAIN_ABOVE, BigClientVersion.v1_9, "weather.rain.above");

		//TODO implement sound for 1.8?
	}

	private static void addSound(SoundEffect effect, BigClientVersion version, String id) {
		effect.versions.put(version, id);
	}

	private HashMap<BigClientVersion,String> versions = new HashMap<>();

	private SoundEffect() {}

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
