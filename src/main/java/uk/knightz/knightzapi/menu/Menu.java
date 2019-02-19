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

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.menu.button.BackPageButton;
import uk.knightz.knightzapi.menu.button.MenuButton;
import uk.knightz.knightzapi.menu.button.NextPageButton;
import uk.knightz.knightzapi.menu.page.Page;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.*;

public class Menu {

    private final LinkedList<Page> pages = new LinkedList<>();
    private final Map<Integer, MenuButton> buttons = new HashMap<>();
    protected Inventory inventory;

    public Menu(String title, int rows) {
        if (rows <= 6) {
            inventory = Bukkit.createInventory(new MenuHolder(this), rows * 9, Chat.color(title));
        } else {
            int extraRows = rows - 6;
            inventory = Bukkit.createInventory(new MenuHolder(this), 6 * 9, Chat.color(title));
            int extraPagesNeeded = extraRows % (54) + 1;
            for (int i = 0; i < extraPagesNeeded; i++) {
                addPage();
            }
        }
    }


    public void addButton(MenuButton b) {
        if (b == null) return;
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

    protected Page addPage() {
        if (inventory.getSize() == 0) {
            MenuHolder holder = (MenuHolder) inventory.getHolder();
            holder.getMenu().open(Bukkit.getOnlinePlayers().iterator().next());
        }
        Page p = new Page(inventory.getSize() / 9, ChatColor.GREEN + "Page " + pages.size(), this);
        p.addButton(new BackPageButton());

        Menu previous = pages.isEmpty() ? this : pages.get(0);
        int lastSlot = previous.inventory.firstEmpty();
        if (lastSlot == -1) lastSlot = previous.inventory.getSize() - 1;
        val lastButton = previous.getButtons().get(lastSlot);
        if (lastButton != null) {
            p.addButton(lastButton);
        }
        previous.addButton(new NextPageButton());
        pages.add(p);
        return p;
    }

    public void trim() {
        Optional<Integer> max = getButtons().keySet().stream().max(Integer::compare);
        if (!max.isPresent()) return; //if the inventory is empty do nothing
        resize(max.get());
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
        if (firstEmpty != -1) return firstEmpty;
        for (Page page : pages) {
            if (page.firstEmpty() != -1)
                return page.firstEmpty();
        }
        while ((firstEmpty = addPage().firstEmpty()) == -1) {
        }
        return firstEmpty;
    }

    public void open(Player whoClicked) {
        whoClicked.openInventory(inventory);
    }

    public List<Page> getPages() {
        return this.pages;
    }

    public Map<Integer, MenuButton> getButtons() {
        return this.buttons;
    }

    Inventory getInventory() {
        return inventory;
    }

    void setInventory(Inventory read) {
        this.inventory = read;
    }

    void addPage(Page page) {
        this.pages.add(page);
    }
}
