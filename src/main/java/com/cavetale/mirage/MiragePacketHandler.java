package com.cavetale.mirage;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

@RequiredArgsConstructor
final class MiragePacketHandler extends PacketHandler {
    private final MiragePlugin plugin;

    @Override
    public void onSend(SentPacket packet) {
    }

    @Override
    public void onReceive(ReceivedPacket packet) {
        if (packet.getPacketName().equals("PacketPlayInUseEntity")) {
            int id = (Integer)packet.getPacketValue(0);
            PacketPlayInUseEntity ppiue = (PacketPlayInUseEntity)packet.getPacket();
            PlayerUseEntityEvent.Hand hand;
            switch (ppiue.b()) {
            case INTERACT: case INTERACT_AT:
                hand = PlayerUseEntityEvent.Hand.RIGHT;
                break;
            case ATTACK: default:
                hand = PlayerUseEntityEvent.Hand.LEFT;
            }
            Player player = packet.getPlayer();
            PlayerUseEntityEvent event = new PlayerUseEntityEvent(player, id, hand);
            if (player != null) {
                Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().getPluginManager().callEvent(event));
            }
        }
    }
}
