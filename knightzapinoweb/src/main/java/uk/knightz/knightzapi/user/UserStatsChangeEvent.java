package uk.knightz.knightzapi.user;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class was created by AlexL (Knightz) on 15/02/2018 at 12:42.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Thrown when a User's stats (kills or deaths) changes.
 **/
public class UserStatsChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Type type;
    private final User user;

    public UserStatsChangeEvent(Type type, User user) {
        this.type = type;
        this.user = user;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Type getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public enum Type {
        KILLS, DEATHS
    }
}
