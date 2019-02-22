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
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.menu.MenuClickEvent;
import uk.knightz.knightzapi.menu.page.Page;

/**
 * Built-in MenuButton to go to the next Page when clicked.
 * It's automatically added when a new Page is added in a Menu
 */
public final class NextPageButton extends MenuButton {


    private static final ItemStack defaultItem = new ItemBuilder()
            .setType(Material.EMERALD)
            .setName("&a&lNext")
            .build();

    static {
        ClickEventAliases.getInstance().add("next", NextPageButton::accept);
    }

    /**
     * Create a new Next Page Button
     *
     * @param itemStack The ItemStack that will be added to a Menu.
     */
    public NextPageButton(ItemStack itemStack) {
        super(itemStack, NextPageButton::accept);
    }

    /**
     * Create a new next Page Button with the default back build
     */
    public NextPageButton() {
        super(defaultItem, NextPageButton::accept);
    }

    private static void accept(MenuClickEvent e) {
        Menu clicked = e.getMenu(), toOpen;
        if (clicked instanceof Page) {
            int indexOf = e.getMenu().getPages().indexOf(clicked);
            toOpen = e.getMenu().getPages().get(indexOf + 1);
        } else {
            toOpen = e.getMenu().getPages().get(0);
        }
        toOpen.open(e.getWhoClicked());
    }
}
