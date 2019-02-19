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

package uk.knightz.knightzapi.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.Menu;

import java.util.Collections;

/**
 * Built-in implementation of MenuButton that opens a different, provided Menu when clicked
 */
public class OpenMenuButton extends MenuButton {


    private static final ItemStack DEFAULT_ITEM = new ItemBuilder()
            .setType(Material.BEACON)
            .setName("&a&lOpen Other Menu")
            .setLore(Collections.singletonList("&aClick to go to another Menu"))
            .build();

    public OpenMenuButton(Menu toOpen) {
        this(DEFAULT_ITEM, toOpen);
    }

    public OpenMenuButton(ItemStack item, Menu toOpen) {
        super(item, e -> {
            e.setCancelled(true);
            toOpen.open(e.getWhoClicked());
        });
    }
}
