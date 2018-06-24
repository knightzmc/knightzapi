package uk.knightz.knightzapi.user;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.reflections.Reflections;
import uk.knightz.knightzapi.KnightzAPI;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class EventManager implements Listener {
    @Getter
    public static final EventManager inst = new EventManager();

    private EventManager() {
        EventExecutor executor = (listener, event) -> {
            PlayerEvent p = (PlayerEvent) event;
            User.valueOf(p.getPlayer()).playerAction(p);
        };
        for (Class<? extends PlayerEvent> e : new Reflections("org.bukkit.event").getSubTypesOf(PlayerEvent.class)) {
            Bukkit.getPluginManager().registerEvent(e, this, EventPriority.LOWEST, executor, KnightzAPI.getP());
        }
    }
}
