package com.cavetale.mirage;

import java.util.UUID;
import lombok.Data;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * JSON-serializable Mirage configuration. See {@link Mirage#setup(MirageData)}.
 */
public final class MirageData {
    // Generic
    public MirageType type;
    public Location location;
    // Specific
    public String blockData;
    public ItemData itemData;
    // Player Specific
    public String playerName;
    public PlayerSkin playerSkin;
    public UUID uuid;
    // Entity specific
    public EntityType entityType;
    /**
     * Players will start observing this entity once they are within
     * {@code chunkActivationRange} and stop once they are not within
     * {@code chunkViewDistance}, masured in distance in chunks (16
     * blocks).  This is optional and only effective when {@link
     * Mirage#updateObserverList()} is called.
     */
    public int chunkViewDistance = 3;
    /**
     * See {@link MirageData#chunkViewDistance}.
     */
    public int chunkActivationRange = 2;
    // Spam debug info
    public boolean debug = false;
    public String debugName;

    public static final class Location {
        public String world;
        public double x, y, z;
        public float pitch, yaw;
        public float headYaw;

        public org.bukkit.Location bukkitLocation() {
            var w = Bukkit.getWorld(this.world);
            return w == null ? null : new org.bukkit.Location(w, x, y, z, yaw, pitch);
        }

        public World bukkitWorld() {
            return Bukkit.getWorld(this.world);
        }

        public static Location fromBukkitLocation(org.bukkit.Location bl) {
            Location result = new Location();
            result.world = bl.getWorld().getName();
            result.x = bl.getX();
            result.y = bl.getY();
            result.z = bl.getZ();
            result.pitch = bl.getPitch();
            result.yaw = bl.getYaw();
            result.headYaw = result.yaw;
            return result;
        }
    }

    @Data
    static final class Equipment {
        public String head, chest, legs, feet, hand, offHand;
    }
}
