package com.cavetale.mirage;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.v1_13_R2.Packet;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

final class Players {
    private Players() { }

    static UUID createNpcUuid() {
        final Random random = ThreadLocalRandom.current();
        return new UUID(random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x0000000000002000L,
                        random.nextLong());
    }

    static void sendPacket(org.bukkit.entity.Player player, Packet packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
