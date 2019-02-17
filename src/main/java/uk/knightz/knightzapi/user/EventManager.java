/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.user;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.reflections.Reflections;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.ReflectionUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * In charge of handling user events.
 * Can also provide a Set of all Bukkit event classes {@link EventManager#getBukkitEventClasses()}
 */
public class EventManager implements Listener {
    public static final EventManager inst = new EventManager();
    public static final String EVENT_PACKAGE = "org.bukkit.event";
    public static final String HANDLER_LIST_METHOD = "getHandlerList";
    public static final EventExecutor CANCEL_EXECUTOR = (listener, event) -> {
        PlayerEvent p = (PlayerEvent) event;
        User.valueOf(p.getPlayer()).playerAction(p);
    };
    /**
     * An immutable Set of all Classes that are part of Bukkit's Event API, and are valid (that is, have a getHandlerList method and are not abstract)
     */
    public static Set<Class<? extends Event>> bukkitEventClasses;

    private EventManager() {
        Reflections reflections = new Reflections(EVENT_PACKAGE);
        bukkitEventClasses = ImmutableSet.copyOf(reflections.getSubTypesOf(Event.class).stream().filter(c ->
                ReflectionUtil.classHasMethod(c, HANDLER_LIST_METHOD)).collect(Collectors.toSet()
        ));


    }

    public static Set<Class<? extends Event>> getBukkitEventClasses() {
        return bukkitEventClasses;
    }

    public void addClassToBlock(Class<? extends PlayerEvent> clazz) {
        registerBlockedEvent(clazz);
    }

    private void registerBlockedEvent(Class<? extends PlayerEvent> clazz) {
        Bukkit.getPluginManager().registerEvent(clazz, inst, EventPriority.LOWEST, CANCEL_EXECUTOR, KnightzAPI.getP());
    }
}
