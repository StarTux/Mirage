package com.cavetale.mirage;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketPlayOutAnimation;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntity;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_13_R2.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Runtime instance of a single mirage.
 */
@Getter @Setter @RequiredArgsConstructor
public final class Mirage {
    private final JavaPlugin plugin;
    private MirageData data;
    private Entity entity;
    private boolean onGround = true;
    private final EnumMap<DataVar, Object> metadata = new EnumMap<>(DataVar.class);
    private final Set<UUID> observers = new HashSet<>();
    private final EnumMap<EnumItemSlot, ItemStack> equipment = new EnumMap<>(EnumItemSlot.class);

    /**
     * This method must be called before any others.  It will ensure
     * completeness of {@code data} an prepare the fake entity for
     * use.
     * @param mirageData The MirageData.
     */
    public void setup(MirageData mirageData) {
        if (this.plugin == null) throw new NullPointerException("plugin cannot be null");
        if (mirageData == null) throw new NullPointerException("mirageData cannot be null");
        if (mirageData.type == null) throw new NullPointerException("mirageData.type cannot be null");
        if (mirageData.location == null) throw new NullPointerException("mirageData.location cannot be null");
        this.data = mirageData;
        final MinecraftServer minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
        final WorldServer worldServer = ((CraftWorld)mirageData.location.bukkitWorld()).getHandle();
        switch (mirageData.type) {
        case PLAYER: {
            if (mirageData.playerName == null) throw new NullPointerException("mirageData.playerName cannot be null");
            if (mirageData.playerSkin == null) throw new NullPointerException("mirageData.playerSkin cannot be null");
            UUID uuid = mirageData.uuid != null ? mirageData.uuid : Players.createNpcUuid();
            final GameProfile profile = new GameProfile(uuid, mirageData.playerName);
            final EntityPlayer entityPlayer = new EntityPlayer(minecraftServer, worldServer, profile, new PlayerInteractManager(worldServer));
            this.entity = entityPlayer;
            profile.getProperties().put("texture", new Property("textures", mirageData.playerSkin.texture, mirageData.playerSkin.signature));
            // Set all skin layers visible
            this.metadata.put(DataVar.PLAYER_SKIN_PARTS, (byte)EntityFlag.PLAYER_SKIN_ALL.bitMask);
            break;
        }
        case MOB: {
            if (mirageData.entityType == null) throw new NullPointerException("mirageData.entityType cannot be null");
            this.entity = EntityTypes.a(this.data.entityType.getName()).a(worldServer);
            break;
        }
        case ITEM: {
            break;
        }
        case BLOCK: {
            break;
        }
        default: throw new IllegalArgumentException("type cannot be " + mirageData.type);
        }
        this.entity.setPositionRotation(mirageData.location.x, mirageData.location.y, mirageData.location.z, mirageData.location.yaw, mirageData.location.pitch);
        this.entity.setHeadRotation(mirageData.location.headYaw);
        if (this.data.debug) debug("Finished setup with data: \"" + new Gson().toJson(this.data) + "\"");
    }

    /**
     * Get the entity id.
     * @return The entity id.
     */
    public int getEntityId() {
        if (this.entity == null) throw new NullPointerException("entity cannot be null");
        return this.entity.getId();
    }

    // --- Observers

    /**
     * Send a packet to all registered observers.
     * @param packet The packet.
     */
    public void sendObservers(Packet packet) {
        for (UUID observer: this.observers) {
            Player player = Bukkit.getPlayer(observer);
            if (player != null) Players.sendPacket(player, packet);
        }
    }

    /**
     * Update observers to contain everybody who is in the same world
     * within {@code data.chunkActivationRange} and nobdoy who is not
     * in the same world or not within {@code data.chunkViewDistance},
     * measured in
     * chunk distance.
     */
    public void updateObserverList() {
        Location location = this.data.location.bukkitLocation();
        if (location == null) throw new NullPointerException("invalid location");
        int cx = location.getBlockX() >> 4;
        int cz = location.getBlockZ() >> 4;
        Set<UUID> done = new HashSet<>();
        for (UUID observer: new ArrayList<>(this.observers)) {
            done.add(observer);
            Player player = Bukkit.getPlayer(observer);
            if (player == null) {
                if (this.data.debug) debug("Removing null observer " + observer);
                this.observers.remove(observer);
                continue;
            }
            Location pl = player.getLocation();
            if (!pl.getWorld().equals(location.getWorld())) {
                if (this.data.debug) debug("Removing observer who left world: " + player.getName());
                this.observers.remove(observer);
                continue;
            }
            int pcx = pl.getBlockX() >> 4;
            int pcz = pl.getBlockZ() >> 4;
            if (Math.max(Math.abs(pcx - cx), Math.abs(pcz - cz)) > this.data.chunkViewDistance) {
                removeObserver(player);
                continue;
            }
        }
        for (Player player: location.getWorld().getPlayers()) {
            if (done.contains(player.getUniqueId())) continue;
            Location pl = player.getLocation();
            int pcx = pl.getBlockX() >> 4;
            int pcz = pl.getBlockZ() >> 4;
            if (Math.max(Math.abs(pcx - cx), Math.abs(pcz - cz)) <= this.data.chunkActivationRange) {
                addObserver(player);
            }
        }
    }

    /**
     * Add an observer to this mirage and send all the packets which
     * are necessary according to the {@code type}.  Observers will
     * automatically receive packets about changes to entity metadata
     * or location.
     * @param player The player.
     */
    public void addObserver(Player player) {
        if (this.observers.contains(player.getUniqueId())) return;
        if (this.data.debug) debug("Add observer " + player.getName());
        this.observers.add(player.getUniqueId());
        switch (this.data.type) {
        case PLAYER: {
            Players.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, (EntityPlayer)entity));
            Players.sendPacket(player, new PacketPlayOutNamedEntitySpawn((EntityPlayer)this.entity));
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    Players.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, (EntityPlayer)entity));
                }, 20L);
            break;
        }
        case MOB: {
            Players.sendPacket(player, new PacketPlayOutSpawnEntityLiving((EntityLiving)this.entity));
            break;
        }
        default: throw new IllegalArgumentException("mirageData.type cannot be " + this.data.type);
        }
        Players.sendPacket(player, entityMetadataPacket());
        Players.sendPacket(player, new PacketPlayOutEntityTeleport(this.entity));
        if (!this.equipment.isEmpty()) {
            for (PacketPlayOutEntityEquipment packet: entityEquipmentPackets()) Players.sendPacket(player, packet);
        }
    }

    /**
     * Remove a player from the observer list and remove this Mirage
     * from their world.
     * @param player The player.
     */
    public void removeObserver(Player player) {
        if (!this.observers.contains(player.getUniqueId())) return;
        if (this.data.debug) this.debug("Remove observer " + player.getName());
        this.observers.remove(player.getUniqueId());
        Players.sendPacket(player, new PacketPlayOutEntityDestroy(new int[] {this.entity.getId()}));
    }

    /**
     * Remove all observers and remove this Mirage from their worlds.
     */
    public void removeAllObservers() {
        for (UUID observer: this.observers) {
            Player player = Bukkit.getPlayer(observer);
            if (player!= null) {
                Players.sendPacket(player, new PacketPlayOutEntityDestroy(new int[] {this.entity.getId()}));
            }
        }
        this.observers.clear();
    }

    // --- Movement

    /**
     * Teleport this mirage to a new location and notify observers.
     * If observers are present, the location must be in the same
     * world or else the result is undefined.
     * @param bl The locationl
     */
    public void teleport(org.bukkit.Location bl) {
        MirageData.Location location = MirageData.Location.fromBukkitLocation(bl);
        this.data.location = location;
        entity.setPositionRotation(location.x, location.y, location.z, location.yaw, location.pitch);
        entity.setHeadRotation(location.headYaw);
        if (!this.observers.isEmpty()) sendObservers(new PacketPlayOutEntityTeleport(entity));
    }

    /**
     * Move an entity to a new location and notify observers via a
     * relative move packet.
     * @param bl The location.  Only x, y, z will be used.
     */
    public void relMove(org.bukkit.Location bl) {
        MirageData.Location location = MirageData.Location.fromBukkitLocation(bl);
        double dx = location.x - this.data.location.x;
        double dy = location.y - this.data.location.y;
        double dz = location.z - this.data.location.z;
        this.data.location.x = location.x;
        this.data.location.y = location.y;
        this.data.location.z = location.z;
        entity.setPosition(location.x, location.y, location.z);
        if (!this.observers.isEmpty()) {
            sendObservers(new PacketPlayOutEntity
                          .PacketPlayOutRelEntityMove(this.entity.getId(),
                                                      (long)(dx * 32.0 * 128.0),
                                                      (long)(dy * 32.0 * 128.0),
                                                      (long)(dz * 32.0 * 128.0),
                                                      this.onGround));
        }
    }

    /**
     * Move an entity to a new location and notify observers via a
     * relative move packet.
     * @param bl The location.
     */
    public void relMoveLook(org.bukkit.Location bl) {
        MirageData.Location location = MirageData.Location.fromBukkitLocation(bl);
        double dx = location.x - this.data.location.x;
        double dy = location.y - this.data.location.y;
        double dz = location.z - this.data.location.z;
        this.data.location = location;
        entity.setPositionRotation(location.x, location.y, location.z, location.yaw, location.pitch);
        entity.setHeadRotation(location.headYaw);
        if (!this.observers.isEmpty()) {
            sendObservers(new PacketPlayOutEntity
                          .PacketPlayOutRelEntityMoveLook(this.entity.getId(),
                                                          (long)(dx * 32.0 * 128.0),
                                                          (long)(dy * 32.0 * 128.0),
                                                          (long)(dz * 32.0 * 128.0),
                                                          (byte)((int)(location.yaw * 256.0f / 360.0f)),
                                                          (byte)((int)(location.pitch * 256.0f / 360.0f)),
                                                          this.onGround));
        }
    }

    /**
     * Update yaw and pitch and notify observers.
     * @param yaw The yaw.
     * @param pitch The pitch.
     */
    public void look(float yaw, float pitch) {
        this.data.location.yaw = yaw;
        this.data.location.pitch = pitch;
        this.data.location.headYaw = yaw;
        entity.setPositionRotation(this.data.location.x, this.data.location.y, this.data.location.z, yaw, pitch);
        entity.setHeadRotation(yaw);
        if (!this.observers.isEmpty()) {
            sendObservers(new PacketPlayOutEntity
                          .PacketPlayOutEntityLook(entity.getId(),
                                                   (byte)((int)(yaw * 256.0f / 360.0f)),
                                                   (byte)((int)(yaw * 256.0f / 360.0f)),
                                                   onGround));
        }
    }

    // --- Equipment

    /**
     * Make a list of entity equipment packets, if any.
     * @return The packet list.  May be empty but not null.
     */
    public List<PacketPlayOutEntityEquipment> entityEquipmentPackets() {
        List<PacketPlayOutEntityEquipment> result = new ArrayList<>();
        for (Map.Entry<EnumItemSlot, ItemStack> entry: this.equipment.entrySet()) {
            result.add(new PacketPlayOutEntityEquipment(this.entity.getId(), entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * Set the item contained in an equipment slot and notify
     * observers.
     * @param slot The slot.
     * @param item The item.
     */
    public void setEquipment(EquipmentSlot slot, org.bukkit.inventory.ItemStack item) {
        EnumItemSlot eis;
        switch (slot) {
        case OFF_HAND: eis = EnumItemSlot.OFFHAND; break;
        case HEAD: eis = EnumItemSlot.HEAD; break;
        case CHEST: eis = EnumItemSlot.CHEST; break;
        case LEGS: eis = EnumItemSlot.LEGS; break;
        case FEET: eis = EnumItemSlot.FEET; break;
        case HAND: default: eis = EnumItemSlot.MAINHAND;
        }
        ItemStack is = CraftItemStack.asNMSCopy(item);
        this.equipment.put(eis, is);
        if (!this.observers.isEmpty()) sendObservers(new PacketPlayOutEntityEquipment(this.entity.getId(), eis, is));
    }

    // --- Metadata

    /**
     * Create a metadata packet for all the present metadata.
     * @return The packet.
     */
    public PacketPlayOutEntityMetadata entityMetadataPacket() {
        List<DataWatcher.Item<?>> list = new ArrayList<>();
        for (Map.Entry<DataVar, Object> entry: this.metadata.entrySet()) {
            list.add(new DataWatcher.Item(entry.getKey().dataWatcherObject, entry.getValue()));
        }
        DummyDataWatcher dummy = new DummyDataWatcher(list);
        return new PacketPlayOutEntityMetadata(this.entity.getId(), dummy, false);
    }

    /**
     * Create a metadata packet for a given key value pair.
     * @param key The key.
     * @param value The value.
     * @return The packet.
     */
    public PacketPlayOutEntityMetadata entityMetadataPacket(DataVar key, Object value) {
        DummyDataWatcher dummy = new DummyDataWatcher(Arrays.asList(new DataWatcher.Item(key.dataWatcherObject, value)));
        return new PacketPlayOutEntityMetadata(this.entity.getId(), dummy, false);
    }

    /**
     * Set entity metadata and notify observers.  Data types are
     * currently NOT checked for correctness.  Incorrect use will lead
     * to player disconnects.
     * @param key The DatVar key.
     * @param value The metadata value.
     */
    public void setMetadata(DataVar key, Object value) {
        this.metadata.put(key, value);
        if (!this.observers.isEmpty()) sendObservers(entityMetadataPacket(key, value));
    }

    /**
     * Set the custom name and notify observers.
     * Calls {@link setMetadata(DataVar, Object)}.
     * @param customName The custom name.
     */
    public void setCustomName(String customName) {
        setMetadata(DataVar.ENTITY_CUSTOM_NAME, Optional.of(new ChatComponentText(customName)));
    }

    // --- Debug

    private void debug(String msg) {
        this.plugin.getLogger().info("[Mirage " + this.getEntityId() + "] (" + this.data.debugName + ") " + msg);
    }
}
