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

package uk.knightz.knightzapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

/**
 * EventHandler and Listener related Utility class
 */
public class Listeners {

    private Listeners() {
    }

    /**
     * Register a listener if it is not already registered, to prevent duplicate event registering
     *
     * @param listener The listener to register
     * @param p The plugin that should register this listener
     */
    public static void registerOnce(Listener listener, Plugin p) {
        if (HandlerList.getRegisteredListeners(p).stream().map(RegisteredListener::getListener).map(Object::getClass).noneMatch(c -> c.equals(listener.getClass()))) {
            Bukkit.getPluginManager().registerEvents(listener, p);
        }
    }
}
