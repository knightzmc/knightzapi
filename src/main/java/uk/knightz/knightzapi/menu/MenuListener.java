/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.utils.Listeners;

/**
 * Listens to InventoryClickEvents and calls any MenuClickEvents if they are one
 */
public class MenuListener implements Listener {
    private MenuListener() {
        Listeners.registerOnce(this, KnightzAPI.getP());
    }

    public static void init() {
        new MenuListener();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e instanceof MenuClickEvent || !(e.getClickedInventory().getHolder() instanceof MenuHolder)) {
            return;
        }

        MenuClickEvent me = convert(e);

        Bukkit.getPluginManager().callEvent(me);
        if (me.getClickedButton() == null) {
            return;
        }
        me.getClickedButton().getOnClick().accept(me);

        if (me.isCancelled()) {
            e.setCancelled(true);
        }


    }

    private MenuClickEvent convert(InventoryClickEvent e) {
        MenuHolder holder = (MenuHolder) e.getClickedInventory().getHolder();
        Menu menu = holder.getMenu();
        return new MenuClickEvent(e, menu, menu.getButtons().get(e.getRawSlot()));
    }
}
