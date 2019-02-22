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
 * Built-in MenuButton to go to the previous Page when clicked.
 * It's automatically added when a new Page is added in a Menu
 */
public final class BackPageButton extends MenuButton {


    private static final ItemStack defaultItem = new ItemBuilder()
            .setType(Material.REDSTONE)
            .setName("&c&lBack")
            .build();

    static {
        ClickEventAliases.getInstance().add("back", BackPageButton::accept);
    }

    /**
     * Create a new Back Page Button
     *
     * @param itemStack The ItemStack that will be added to a Menu.
     */
    public BackPageButton(ItemStack itemStack) {
        super(itemStack, BackPageButton::accept);
    }

    /**
     * Create a new BackPageButton with the default back build ItemStack
     */
    public BackPageButton() {
        super(defaultItem, BackPageButton::accept);
    }

    private static void accept(MenuClickEvent e) {
        if (e.getMenu() instanceof Page) {
            Menu toOpen;
            Page current = (Page) e.getMenu();
            int indexOf = e.getMenu().getPages().indexOf(current);
            if (indexOf == -1) {
                toOpen = current.getParent();
            } else {
                toOpen = e.getMenu().getPages().get(indexOf);
            }
            toOpen.open(e.getWhoClicked());
        }
    }
}
