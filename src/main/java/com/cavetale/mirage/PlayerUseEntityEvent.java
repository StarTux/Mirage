package com.cavetale.mirage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called whenever a player interacts with an entity.  The entity may
 * or may not be a Mirage.
 */
@Getter @RequiredArgsConstructor
public final class PlayerUseEntityEvent extends Event {
    public enum Hand {
        LEFT, RIGHT;
    }

    private final Player player;
    private final int entityId;
    private final Hand hand;

    // Event

    @Getter private static HandlerList handlerList = new HandlerList();

    @Override public HandlerList getHandlers() {
        return handlerList;
    }
}
