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

package uk.knightz.knightzapi.menuold;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.annotation.Dangerous;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.menuold.button.BackPageButton;
import uk.knightz.knightzapi.menuold.button.DynamicDataButton;
import uk.knightz.knightzapi.menuold.button.MenuButton;
import uk.knightz.knightzapi.menuold.button.NextPageButton;
import uk.knightz.knightzapi.utils.Functions;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.*;
import java.util.function.Consumer;

import static org.bukkit.Material.AIR;

/**
 * An Inventory utility for easily creating interactive menus
 */
@Data
@Getter
@Setter
public class Menu {


    public static final Consumer<MenuClickEvent> doNothing = (e) -> {
    };
    public static final Consumer<MenuClickEvent> cancel = (e) -> e.setCancelled(true);
    public static final Menu EMPTY_MENU = new Menu("", 0);
    private static final int MAX_SIZE = 54;
    private static final Inventory EMPTY = Bukkit.createInventory(null, 0);

    private final Set<SubMenu> children = new HashSet<>();
    private final Map<Integer, MenuButton> items = new HashMap<>();
    private final Map<Integer, Consumer<MenuClickEvent>> clickMappings = new HashMap<>();
    private final LinkedList<Page> pages = new LinkedList<>();
    /**
     * Page 1 of the Inventory, and the main page.
     * Any additional pages will be in {@link Menu#pages}
     */
    private Inventory inv;
    private Sound onClick;
    private MenuButton backgroundItem;
    @Setter
    private Consumer<MenuCloseEvent> onClose;
    /**
     * Indicate if the Menu should be "destroyed" when it is closed by a player.
     * Bear in mind that "destroyed" means the Menu will be removed from the Collection of all in-use
     * menus and no click-events will be fired, however, there is no guarantee that it will be garbage
     * collected by the JVM, as it may be referred to elsewhere.
     */
    @Setter
    private boolean destroyWhenClosed = true;

    /**
     * Create a new Menu
     *
     * @param title The title of the Inventory
     * @param rows  The amount of rows in the Inventory
     */
    public Menu(String title, int rows) {
        if (rows * 9 > MAX_SIZE) {
            inv = Bukkit.createInventory(null, MAX_SIZE, Chat.color(title));
            rows -= MAX_SIZE / 9;
            do {
                rows -= addPage().getSize() / 9;
            }
            while (rows > 0);
        } else {
            inv = Bukkit.createInventory(null, rows * 9, Chat.color(title));
        }
        if (inv == null) {
            throw new NullPointerException("Inventory is null!");
        }
        MenuListener.register(this);
    }

    public int getFullSize() {
        return inv.getSize() + pages.stream().mapToInt(Page::getSize).sum();
    }

    public int getSize() {
        return inv.getSize();
    }


    /**
     * Get the page that the given slot refers to
     *
     * @param slot An integer slot that shouldn't be bigger than {@link Menu#getFullSize()}
     * @return The Inventory that contains this slot
     */
    private Menu indexOf(int slot) {
        if (slot < 0) {
            throw new IndexOutOfBoundsException("Slot is less than 0!");
        }
        if (slot > getFullSize()) {
            throw new IndexOutOfBoundsException(String.format("Slot is more than full size of Inventory! (%d)", getFullSize()));
        }
        if (slot < inv.getSize()) {
            return this;
        }
        int allSize = inv.getSize();
        for (Page v : getPages()) {
            if (allSize <= slot && slot <= (allSize += v.getSize())) {
                return v;
            }
        }
        throw new IndexOutOfBoundsException("Slot does not exist in Menu");
    }


    /**
     * Bring a "global" slot (one that is more than
     * a singular Inventory's size to refer to a next page, to a "local" slot (a
     * slot that refers to an actual slot on a certain page)
     * <p>
     * For example, a slot of 103 would refer to slot 49 of page 2
     *
     * @param allInvSlot The "global" slot
     * @param inv        The Inventory that the "global" slot refers to,
     *                   obtained by calling {@link Menu#indexOf(int)}
     * @return The "local" slot. If the given "global" slot would not be in the given Menu, the global slot is returned
     */
    private int localize(int allInvSlot, Menu inv) {
        if (allInvSlot < inv.getSize() || allInvSlot > getFullSize()) {
            return allInvSlot;
        }
        allInvSlot -= this.inv.getSize();
        for (Page p : pages) {
            if (allInvSlot <= p.getSize()) {
                return allInvSlot;
            }
            allInvSlot -= p.getSize();
        }
        return allInvSlot;
    }

    public int trueFirstEmpty() {
        if (inv.firstEmpty() != -1) {
            return inv.firstEmpty();
        }
        for (Page p : pages) {
            Inventory i = p.getInv();
            if (i.firstEmpty() != -1) {
                return i.firstEmpty();
            }
        }
        return -1;
    }

    /**
     * Set a slot of the inventory to the given button
     *
     * @param slot   The slot to set
     * @param button The button to add
     * @throws IndexOutOfBoundsException if the given slot is not in the Inventory
     */

    public void addButton(int slot, MenuButton button) {
        Validate.notNull(button);

        Menu toAdd = indexOf(slot);

        if (toAdd == null) return;
        toAdd.getInv().setItem(localize(slot, toAdd), button.getItemStack());
        items.put(slot, button);
        clickMappings.put(slot, button.onClickAlias == null ? button.getOnClick() : ClickEventAliases.getInstance().get(button.onClickAlias));
    }

    /**
     * Set the background button of the Menu, which will take up all non-inhabited slots
     * It does nothing when clicked
     *
     * @param backgroundItem The background button to set
     */
    public void setBackgroundItem(ItemStack backgroundItem) {
        this.backgroundItem = new MenuButton(backgroundItem, Functions.emptyConsumer());
        addBackgroundItems();
    }

    /**
     * Adds a MenuButton to the Inventory without adding it to the internal items map,
     * so it is treated as if it doesn't exist (but will still fire events when clicked on).
     * Used so background items are not detected by {@link Menu#trim()}
     *
     * @param slot   The slot of the button
     * @param button The button to add
     */
    private void addButtonWithoutMap(int slot, MenuButton button) {
        Validate.notNull(button, "Button is null");
        if (slot > getFullSize())
            throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size of Inventory %d", slot, inv.getSize()));
        clickMappings.put(slot, button.getOnClick());
        inv.setItem(slot, button.getItemStack());
    }

    /**
     * Set the title of the Inventory
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        ItemStack[] items = inv.getContents();
        inv = Bukkit.createInventory(null, inv.getSize(), Chat.color(title));
        inv.setContents(items);
    }


    /**
     * Register a SubMenu.
     * This is called in the constructor of {@link SubMenu}, so usually will not need to be called
     *
     * @param subMenu The SubMenu to register
     */
    public void addSubMenu(SubMenu subMenu) {
        if (subMenu != null)
            children.add(subMenu);
    }


    private void addBackgroundItems() {
        if (this.backgroundItem != null) {
            for (int slot = 0; slot < getInv().getSize(); slot++) {
                if (!items.containsKey(slot) || items.get(slot) == null) {
                    addButtonWithoutMap(slot, this.backgroundItem);
                }
            }
        }
    }

    /**
     * Add a MenuButton to the first empty slot in the Inventory
     *
     * @param button The button to add
     * @throws IndexOutOfBoundsException if there are no more free slots
     */
    public void addButton(MenuButton button) {
        if (backgroundItem != null) {
            //If there's a background item, firstEmpty will be -1, temporarily remove them
            for (int x = 0; x < inv.getContents().length; x++) {
                ItemStack i = inv.getContents()[x];
                if (i != null && i.isSimilar(backgroundItem.getItemStack())) {
                    inv.setItem(x, new ItemStack(AIR));
                }
            }
            for (Page p : pages) {
                Inventory inv = p.getInv();
                for (int x = 0; x < inv.getContents().length; x++) {
                    ItemStack i = inv.getContents()[x];
                    if (i != null && i.isSimilar(backgroundItem.getItemStack())) {
                        inv.setItem(x, new ItemStack(AIR));
                    }
                }
            }
        }
        int trueFirstEmpty = trueFirstEmpty();
        if (trueFirstEmpty == -1) {
            addPage();
            trueFirstEmpty = trueFirstEmpty();
        }
        addButton(trueFirstEmpty, button);
        if (backgroundItem != null)
            addBackgroundItems();
    }

    private void adjustSize(int size) {
        if (size == 0) {
            inv = EMPTY;
            return;
        }
        if (size >= MAX_SIZE) {
            Menu menu = indexOf(size);
            menu.adjustSize(localize(size, menu));
            return;
        }
        if (size > inv.getSize()) {
            Inventory tempInv = Bukkit.createInventory(null, MathUtils.roundUp(size), inv.getTitle());
            tempInv.setContents(inv.getContents());
            inv.setMaxStackSize(inv.getMaxStackSize());
            inv = tempInv;
            addBackgroundItems();
        } else {
            Inventory tempInv = Bukkit.createInventory(null, MathUtils.roundUp(size), inv.getTitle());
            tempInv.setContents(Arrays.copyOf(inv.getContents(), size));
            inv.setMaxStackSize(inv.getMaxStackSize());
            inv = tempInv;
        }
    }


    private Page addPage() {
        val lastInv = pages.isEmpty() ? this : pages.peekLast();
        MenuButton copyOldButton = null;
        if (lastInv.getInv().firstEmpty() == -1) {
            copyOldButton = lastInv.getItems().get(lastInv.getInv().getSize());
        }

        lastInv.addButton(lastInv.getSize() - 1, new NextPageButton());

        Page newPage = new Page(this);
        newPage.addButton(new BackPageButton());
        if (copyOldButton != null) {
            newPage.addButton(copyOldButton);
        }
        pages.add(newPage);
        return newPage;
    }

    /**
     * Set the amount of rows in the Inventory. If decreasing the rows amount, some items may be removed
     *
     * @param rows The amount of rows to set.
     */
    public void setRows(int rows) {
        adjustSize(rows * 9);
    }

    /**
     * Set the Event listener for a certain slot. This can override the cancelEvent handler of a button
     *
     * @param slot The slot to bind the cancelEvent to
     * @param e    Called when the slot is clicked on by a player
     */
    public void mapButton(int slot, Consumer<MenuClickEvent> e) {
        Validate.notNull(e);
        if (slot > inv.getSize())
            throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size of Inventory %d", slot, inv.getSize()));

        clickMappings.put(slot, e);
    }


    /**
     * @return The Bukkit Inventory of this Menu
     */
    public Inventory getInv() {
        return inv;
    }

    /**
     * Removes any trailing rows of empty slots in the Inventory, rounding up to the nearest whole row if necessary
     */
    public void trim() {
        adjustSize(items.size());
    }


    /**
     * Clear the Menu, removing all buttons but not executing {@link #trim()}
     */
    public void clear() {
        inv.clear();
        items.clear();
        clickMappings.clear();
    }


    /**
     * Open the Menu for a Player. Note that MenuButton permissions will only be used if the Inventory
     * is opened this way.
     */
    public void open(Player p, Object... dynData) {
        HashMap<Integer, MenuButton> itemMap = new HashMap<>(getItems());
        itemMap.entrySet().removeIf(m -> {
            val menuButton = m.getValue();
            if (menuButton.hasPermission())
                return !p.hasPermission(menuButton.getPermission());
            return false;
        });
        val inv = cloneInventory();
        itemMap.forEach((integer, menuButton) -> inv.setItem(integer, menuButton.getItemStack()));
        itemMap.forEach((integer, menuButton) -> {
            if (menuButton instanceof DynamicDataButton) {
                val i = ((DynamicDataButton) menuButton).applyDynData(p, dynData);
                inv.setItem(integer, i);
            }
        });
        p.openInventory(inv);
    }


    //Dangerous Methods
    //These will likely break things unless you know what you're doing
    //Use at your own risk!

    /**
     * @param o The map of slots and buttons to set
     * @deprecated Dangerous
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @Dangerous
    public void setButtons(Map<Integer, MenuButton> o) {
        items.clear();
        items.putAll(o);
    }


    /**
     * Centre all buttons in the Menu vertically and horizontally.
     * This operation will break predefined button order.
     */
    public void centreButtons() {
        int totalSlots = inv.getSize();
        int occupiedSlots;
        if (backgroundItem == null) {
            occupiedSlots = (int) getItems().values().stream().filter(m -> m.getItemStack().getType() != AIR).count();
        } else {
            occupiedSlots = (int) getItems().values().stream().filter(m -> m.getItemStack().isSimilar(backgroundItem.getItemStack())).count();
        }

		/*
		0 0 0 0
		0 1 2 0    3 items, round up to square of 2^2
		3 0 0 0
		0 0 0 0
		 */
        val root_slots = Math.sqrt(occupiedSlots);
        val floorRootSlots = Math.floor(root_slots);
        val ceilRootSlots = floorRootSlots + 1;
        val ceilRootSquare = Math.pow(ceilRootSlots, 2); //In example, expected result should be 4


    }

    /**
     * Create a copy of the internal Bukkit Inventory that is used.
     *
     * @return A copy of {@link Menu#inv}
     */
    private Inventory cloneInventory() {
        val inventory = Bukkit.createInventory(null, inv.getSize(), inv.getTitle());
        inventory.setContents(inv.getContents());
        return inventory;
    }

    /**
     * Make a copy of this Menu. It will be functionally equal, in that it {@code originalMenu#equals( originalMenu.copy)} will return
     * {@code true}, but {@code originalMenu == originalMenu.copy} will return false.
     *
     * @return A functionally equal copy of this Menu.
     */
    public Menu copy() {
        Menu copy = new Menu(getInv().getTitle(), getInv().getSize() / 9);
        //noinspection deprecation
        copy.setButtons(this.items);
        copy.backgroundItem = this.backgroundItem;
        copy.onClick = this.onClick;
        copy.inv = this.inv;
        copy.onClose = this.onClose;
        this.children.forEach(copy::addSubMenu);
        copy.destroyWhenClosed = this.destroyWhenClosed;
        return copy;
    }
}

