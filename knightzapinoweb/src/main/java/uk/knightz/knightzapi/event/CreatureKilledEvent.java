/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.Listeners;

import java.util.List;

/**
 * Extention of {@link org.bukkit.event.entity.EntityDeathEvent}, but will only be thrown when a Creature is killed by a Player
 */
public class CreatureKilledEvent extends EntityDeathEvent {
    @Getter
    private final Player killer;

    public CreatureKilledEvent(LivingEntity entity, List<ItemStack> drops, Player killer) {
        super(entity, drops);
        this.killer = killer;
    }

    public CreatureKilledEvent(LivingEntity what, List<ItemStack> drops, int droppedExp, Player killer) {
        super(what, drops, droppedExp);
        this.killer = killer;
    }

    /**
     * Register the PlayerMoveEvent Listener that checks when a block was moved.
     * Will not register more than once
     */
    public static void init() {
        Listeners.registerOnce(new KillListener(), KnightzAPI.getP());
    }

    private static class KillListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onMove(EntityDeathEvent ex) {
            if (ex instanceof CreatureKilledEvent) return;

            if (ex.getEntity().getKiller() != null) {
                Bukkit.getPluginManager().callEvent(new CreatureKilledEvent(ex.getEntity(), ex.getDrops(), ex.getDroppedExp(), ex.getEntity().getKiller()));
            }
        }
    }
}
