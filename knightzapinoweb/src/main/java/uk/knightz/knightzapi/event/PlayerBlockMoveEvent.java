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

package uk.knightz.knightzapi.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.Listeners;

/**
 * An event called when a player moves, but only if they have moved to a different block from their previous one.
 */
public class PlayerBlockMoveEvent extends PlayerMoveEvent {
    /**
     * Create a new PlayerBlockMoveEvent.
     *
     * @param player The player who moved
     * @param from   Their original Location
     * @param to     Their current Location
     */
    private PlayerBlockMoveEvent(Player player, Location from, Location to) {
        super(player, from, to);
    }

    /**
     * Register the PlayerMoveEvent Listener that checks when a block was moved.
     * Will not register more than once
     */
    public static void init() {
//        TODO Disabled as interferes with JRebel, should be enabled in production
        Listeners.registerOnce(new MoveListener(), KnightzAPI.getP());
    }

    private static class MoveListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onMove(PlayerMoveEvent ex) {
            if (ex instanceof PlayerBlockMoveEvent) {
                return;
            }
            Location f = ex.getFrom();
            Location t = ex.getTo();
            if (!f.getBlock().getLocation().equals(t.getBlock().getLocation())) {
                Bukkit.getPluginManager().callEvent(new PlayerBlockMoveEvent(ex.getPlayer(), ex.getFrom(), ex.getTo()));
            }
        }
    }
}
