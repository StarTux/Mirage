package com.cavetale.mirage;

import java.util.Optional;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.EnumDirection;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.Vector3f;

public enum DataVar {
    ENTITY_FLAGS(0, DataType.BYTE, (byte)0), //mask
    ENTITY_AIR(1, DataType.INTEGER, (int)300),
    ENTITY_CUSTOM_NAME(2, DataType.OPT_CHAT, Optional.empty()),
    ENTITY_CUSTOM_NAME_VISIBLE(3, DataType.BOOLEAN, false),
    ENTITY_SILENT(4, DataType.BOOLEAN, false),
    ENTITY_NO_GRAVITY(5, DataType.BOOLEAN, false),
    POTION_TYPE(6, DataType.ITEM_STACK, ItemStack.a),
    FALLING_BLOCK_SPAWN_POSITION(6, DataType.POSITION, BlockPosition.ZERO),
    AREA_EFFECT_CLOUD_RADIUS(6, DataType.FLOAT, 0.5F),
    AREA_EFFECT_CLOUD_COLOR(7, DataType.INTEGER, 0),
    AREA_EFFECT_CLOUD_POINT(8, DataType.BOOLEAN, false),
    AREA_EFFECT_CLOUD_PARTICLE(9, DataType.PARTICLE_PARAM, Particles.s),
    FISHING_HOOK_ENTITY(6, DataType.INTEGER, 0),
    ARROW_FLAGS(6, DataType.BYTE, (byte)0), //mask
    ARROW_SHOOTER(7, DataType.OPT_UUID, Optional.empty()),
    TIPPED_ARROW_COLOR(8, DataType.INTEGER, -1),
    TRIDENT_LOYALTY(8, DataType.INTEGER, 0),
    BOAT_HIT_TIME(6, DataType.INTEGER, 0),
    BOAT_FORWARD(7, DataType.INTEGER, 0),
    BOAT_DAMAGE(8, DataType.FLOAT, 0.0F),
    BOAT_TYPE(9, DataType.INTEGER, 0),
    BOAT_RIGHT_PADDLE(10, DataType.BOOLEAN, false),
    BOAT_LEFT_PADDLE(11, DataType.BOOLEAN, false),
    BOAT_SPLASH_TIMER(12, DataType.INTEGER, 0),
    ENDER_CRYSTAL_BEAM_TARGET(6, DataType.OPT_POSITION, Optional.empty()),
    ENDER_CRYSTAL_SHOW_BOTTOM(7, DataType.BOOLEAN, false),
    WITHER_SKULL_INVULNERABLE(6, DataType.BOOLEAN, false),
    FIREWORKS_INFO(6, DataType.ITEM_STACK, ItemStack.a),
    FIREWORKS_SHOOTER(7, DataType.INTEGER, 0),
    ITEM_FRAME_ITEM(6, DataType.ITEM_STACK, ItemStack.a),
    ITEM_FRAME_ROTATION(7, DataType.INTEGER, 0),
    ITEM_ITEM(6, DataType.ITEM_STACK, ItemStack.a),
    LIVING_HAND_FLAGS(6, DataType.BYTE, (byte)0), //mask
    LIVING_HEALTH(7, DataType.FLOAT, 0F),
    LIVING_POTION_EFFECT_COLOR(8, DataType.INTEGER, 0),
    LIVING_POTION_EFFECT_AMBIENT(9, DataType.BOOLEAN, false),
    LIVING_ARROWS(10, DataType.INTEGER, 0),
    PLAYER_ADDITIONAL_HEARTS(11, DataType.FLOAT, 0.0f),
    PLAYER_SCORE(12, DataType.INTEGER, 0),
    PLAYER_SKIN_PARTS(13, DataType.BYTE, (byte)0),
    PLAYER_MAIN_HAND(14, DataType.BYTE, (byte)1),
    PLAYER_LEFT_SHOULDER(15, DataType.COMPOUND, new NBTTagCompound()),
    PLAYER_RIGHT_SHOULDER(16, DataType.COMPOUND, new NBTTagCompound()),
    ARMOR_STAND_FLAGS(11, DataType.BYTE, (byte)0), //mask
    ARMOR_STAND_HEAD_ROTATION(12, DataType.ROTATION, new Vector3f(0, 0, 0)),
    ARMOR_STAND_BODY_ROTATION(13, DataType.ROTATION, new Vector3f(0, 0, 0)),
    ARMOR_STAND_LEFT_ARM_ROTATION(14, DataType.ROTATION, new Vector3f(0, 0, 0)),
    ARMOR_STAND_RIGHT_ARM_ROTATION(15, DataType.ROTATION, new Vector3f(0, 0, 0)),
    ARMOR_STAND_LEFT_LEG_ROTATION(16, DataType.ROTATION, new Vector3f(0, 0, 0)),
    ARMOR_STAND_RIGHT_LEG_ROTATION(17, DataType.ROTATION, new Vector3f(0, 0, 0)),
    INSENTIENT_FLAGS(11, DataType.BYTE, (byte)0), //mask
    BAT_FLAGS(12, DataType.BYTE, (byte)0), //mask
    AGEABLE_BABY(12, DataType.BOOLEAN, false),
    ABSTRACT_HORSE_FLAGS(13, DataType.BYTE, (byte)0), //mask
    ABSTRACT_HORSE_OWNER(14, DataType.OPT_UUID, Optional.empty()),
    HORSE_VARIANT(15, DataType.INTEGER, 0),
    HORSE_ARMOR(16, DataType.INTEGER, 0),
    HORSE_ARMOR_ITEM(17, DataType.ITEM_STACK, ItemStack.a),
    CHESTED_HORSE_HAS_CHEST(15, DataType.BOOLEAN, false),
    LLAMA_STRENGTH(16, DataType.INTEGER, 0),
    LLAMA_CARPET_COLOR(17, DataType.INTEGER, -1),
    LLAMA_VARIANT(18, DataType.INTEGER, 0),
    PIG_SADDLE(13, DataType.BOOLEAN, false),
    PIG_BOOST(14, DataType.INTEGER, 0),
    RABBIT_TYPE(13, DataType.INTEGER, 0),
    POLAR_BEAR_STANDING(13, DataType.BOOLEAN, false),
    SHEEP_FLAGS(13, DataType.BYTE, (byte)0), //mask
    TAMEABLE_FLAGS(13, DataType.BYTE, (byte)0), //mask
    TAMEABLE_OWNER(14, DataType.OPT_UUID, Optional.empty()),
    OCELOT_TYPE(15, DataType.INTEGER, 0),
    WOLF_DAMAGE_TAKEN(15, DataType.FLOAT, 1.0f),
    WOLF_BEGGING(16, DataType.BOOLEAN, false),
    WOLF_COLLAR_COLOR(17, DataType.INTEGER, 14),
    PARROT_VARIANT(15, DataType.INTEGER, 0),
    VILLAGER_PROFESSION(13, DataType.INTEGER, 0),
    IRON_GOLEM_FLAGS(12, DataType.BYTE, (byte)0), //mask
    SNOWMAN_FLAGS(12, DataType.BYTE, (byte)0x10), //mask
    SHULKER_DIRECTION(12, DataType.DIRECTION, EnumDirection.DOWN),
    SHULKER_ATTACHMENT(13, DataType.OPT_POSITION, Optional.empty()),
    SHULKER_SHIELD_HEIGHT(14, DataType.BYTE, (byte)0),
    SHULKER_COLOR(15, DataType.BYTE, (byte)10),
    BLAZE_FLAGS(12, DataType.BYTE, (byte)0), //mask
    CREEPER_STATE(12, DataType.INTEGER, -1),
    CREEPER_CHARGED(13, DataType.BOOLEAN, false),
    CREEPER_IGNITED(14, DataType.BOOLEAN, false),
    GUARDIAN_RETRACTING_SPIKES(12, DataType.BOOLEAN, false),
    GUARDIAN_TARGET(13, DataType.INTEGER, 0),
    ABSTRACT_ILLAGER_FLAGS(12, DataType.BYTE, (byte)0), //mask
    SPELLCASTER_ILLAGER_SPELL(13, DataType.BYTE, (byte)0),
    VEX_FLAGS(12, DataType.BYTE, (byte)0), //mask
    ABSTRACT_SKELETON_SWINGING_ARMS(12, DataType.BOOLEAN, false),
    SPIDER_FLAGS(12, DataType.BYTE, (byte)0), //mask
    WITCH_DRINKING_POTION(12, DataType.BOOLEAN, false),
    WITHER_CENTER_HEAD_TARGET(12, DataType.INTEGER, 0),
    WITHER_LEFT_HEAD_TARGET(13, DataType.INTEGER, 0),
    WITHER_RIGHT_HEAD_TARGET(14, DataType.INTEGER, 0),
    WITHER_INVULNERABLE_TIME(15, DataType.INTEGER, 0),
    PHANTOM_SIZE(12, DataType.INTEGER, 0),
    DOLPHIN_TREASURE_POSITION(12, DataType.POSITION, BlockPosition.ZERO),
    DOLPHIN_CAN_FIND_TREASURE(13, DataType.BOOLEAN, false),
    DOLPHIN_HAS_FISH(14, DataType.BOOLEAN, false),
    FISH_FROM_BUCKET(12, DataType.BOOLEAN, false),
    PUFFERFISH_PUFF(13, DataType.INTEGER, 0),
    TROPICAL_FISH_VARIANT(13, DataType.INTEGER, 0),
    TURTLE_HOME_POS(13, DataType.POSITION, BlockPosition.ZERO),
    TURTLE_HAS_EGG(14, DataType.BOOLEAN, false),
    TURTLE_LAYING_EGG(15, DataType.BOOLEAN, false),
    TURTLE_TRAVEL_POS(16, DataType.POSITION, BlockPosition.ZERO),
    TURTLE_GOING_HOME(17, DataType.BOOLEAN, false),
    TURTLE_TRAVELING(18, DataType.BOOLEAN, false),
    ZOMBIE_IS_BABY(12, DataType.BOOLEAN, false),
    ZOMBIE_UNUSED_TYPE(13, DataType.INTEGER, 0),
    ZOMBIE_HANDS_UP(14, DataType.BOOLEAN, false),
    ZOMBIE_BECOMING_DROWNED(15, DataType.BOOLEAN, false),
    ZOMBIE_VILLAGER_CONVERTING(15, DataType.BOOLEAN, false),
    ZOMBIE_VILLAGER_PROFESSION(16, DataType.INTEGER, 0),
    ENDERMAN_CARRIED_BLOCK(12, DataType.OPT_BLOCK, Optional.empty()),
    ENDERMAN_SCREAMING(13, DataType.BOOLEAN, false),
    ENDER_DRAGON_PHASE(12, DataType.INTEGER, 10),
    GHAST_ATTACKING(12, DataType.BOOLEAN, false),
    SLIME_SIZE(12, DataType.INTEGER, 1),
    MINECART_SHAKING_POWER(6, DataType.INTEGER, 0),
    MINECART_SHAKING_DIRECTION(7, DataType.INTEGER, 1),
    MINECART_SHAKING_MULTIPLIER(8, DataType.FLOAT, 0.0f),
    MINECART_CUSTOM_BLOCK_ID_DMG(9, DataType.INTEGER, 0),
    MINECART_CUSTOM_BLOCK_Y(10, DataType.INTEGER, 6),
    MINECART_SHOW_CUSTOM_BLOCK(11, DataType.BOOLEAN, false),
    MINECART_FURNACE_POWERED(12, DataType.BOOLEAN, false),
    MINECART_COMMAND_COMMAND(12, DataType.STRING, ""),
    MINECART_LAST_OUTPUT(13, DataType.CHAT, new ChatComponentText("")),
    TNT_PRIMED_FUSE_TIME(6, DataType.INTEGER, 80);

    public final DataType dataType;
    public final int index;
    public final Object defaultValue;
    public final DataWatcherObject dataWatcherObject;

    <T> DataVar(int index, DataType<T> dataType, T defaultValue) {
        this.index = index;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.dataWatcherObject = new DataWatcherObject(index, dataType.serializer);
    }
}
