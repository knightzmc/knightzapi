package uk.knightz.knightzapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

/**
 * This class was created by AlexL (Knightz) on 17/03/2018 at 17:46.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Listeners {

    private Listeners() {
    }

    /**
     * Register a listener if it is not already registered, to prevent duplicate event registering
     *
     * @param listener The listener to register
     * @param p
     */
    public static void registerOnce(Listener listener, Plugin p) {
        if (HandlerList.getRegisteredListeners(p).stream().map(RegisteredListener::getListener).map(Object::getClass).noneMatch(c -> c.equals(listener.getClass()))) {
            Bukkit.getPluginManager().registerEvents(listener, p);
        }
    }
}
