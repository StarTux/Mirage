package com.cavetale.mirage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.packetlistener.PacketListenerAPI;

public final class MiragePlugin extends JavaPlugin {
    private MiragePacketHandler packetHandler = new MiragePacketHandler(this);

    @Override
    public void onEnable() {
        PacketListenerAPI.addPacketHandler(this.packetHandler);
    }

    @Override
    public void onDisable() {
        PacketListenerAPI.removePacketHandler(this.packetHandler);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player)sender;
        if (args.length != 0) return false;
        Mirage mirage = new Mirage(this);
        MirageData data = new MirageData();
        data.type = MirageType.MOB;
        data.entityType = org.bukkit.entity.EntityType.CREEPER;
        data.location = MirageData.Location.fromBukkitLocation(player.getLocation());
        mirage.setup(data);
        mirage.addObserver(player);
        return true;
    }
}
