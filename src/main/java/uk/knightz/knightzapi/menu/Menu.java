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

package uk.knightz.knightzapi.menu;

import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.menu.button.BackPageButton;
import uk.knightz.knightzapi.menu.button.NextPageButton;
import uk.knightz.knightzapi.menu.page.Page;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.*;

public class Menu {

    @Getter
    private final List<Page> pages = new LinkedList<>();
    @Getter
    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    protected Inventory inventory;

    public Menu(String title, int rows) {
        inventory = Bukkit.createInventory(new MenuHolder(this), rows * 9, Chat.color(title));
    }


    public void addButton(MenuButton b) {
        addButton(firstEmpty(), b);
    }

    public void addButton(int slot, MenuButton button) {
        if (slot < 0) throw new IndexOutOfBoundsException("Slot is less than 0");

        if (slot < inventory.getSize()) {
            inventory.setItem(slot, button.getItem());
            buttons.put(slot, button);
        } else {
            int fullSize = getFullSize();
            if (slot < fullSize) {
                Page lastPage;
                do {
                    lastPage = addPage();
                    fullSize += lastPage.getSize();
                } while (slot < fullSize);
                lastPage.addButton(slot, button);
            }
        }
        inventory.getViewers().forEach(h -> {
            if (h instanceof Player) ((Player) h).updateInventory();
        });
    }

    public int getSize() {
        return inventory.getSize();
    }

    private int getFullSize() {
        return inventory.getSize() + pages.stream().mapToInt(Page::getSize).sum();
    }

    private Page addPage() {
        Page p = new Page(inventory.getSize() / 9, ChatColor.GREEN + "Page " + pages.size(), this);

        p.addButton(new BackPageButton());

        Menu previous = pages.isEmpty() ? this : pages.get(0);
        val lastButton = previous.getButtons().get(previous.firstEmpty());
        if (lastButton != null) {
            p.addButton(lastButton);
        }
        previous.addButton(new NextPageButton());
        pages.add(p);
        return p;
    }

    public void trim() {
        if (inventory.firstEmpty() == 0) //if the inventory is empty do nothing
            return;
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        int lastItemSlot = getButtons().keySet().stream().max(Integer::compare).get();
        resize(lastItemSlot);
    }

    public void resize(int size) {
        Inventory old = inventory;
        Inventory newInv = Bukkit.createInventory(old.getHolder(), MathUtils.roundUp(size), old.getTitle());
        newInv.setMaxStackSize(old.getMaxStackSize());
        newInv.setContents(Arrays.copyOf(old.getContents(), size));
        this.inventory = newInv;
        inventory.getViewers().forEach(h -> ((Player) h).updateInventory());
    }

    public void removeButton(int slot) {
        buttons.put(slot, null);
        inventory.getViewers().forEach(h -> {
            if (h instanceof Player) ((Player) h).updateInventory();
        });
    }

    public void removeButton(MenuButton button) {
        buttons.forEach((key, value) -> {
            if (value.equals(button)) {
                removeButton(key);
            }
        });
    }

    public int firstEmpty() {
        int firstEmpty = inventory.firstEmpty();
        if (firstEmpty > 0 || pages.isEmpty()) return firstEmpty;

        for (Page p : pages) {
            firstEmpty = p.firstEmpty();
            if (firstEmpty != -1) return firstEmpty;
        }
        return -1;
    }

    public void open(Player whoClicked) {
        whoClicked.openInventory(inventory);
    }
}
