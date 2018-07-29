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

package uk.knightz.knightzapi.menu;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.menu.item.MenuButton;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * An Inventory utility for easily creating interactive menus
 */
@Data
@Getter
@Setter
public class Menu {
	private static final int MAX_SIZE = 54;
	private static final Inventory EMPTY = Bukkit.createInventory(null, 0);
	private final Set<SubMenu> children = ConcurrentHashMap.newKeySet();
	private final Map<Integer, MenuButton> items;
	private final Map<Integer, Consumer<MenuClickEvent>> clickMappings;
	private Inventory inv;
	private Sound onClick;

	@Setter
	private Consumer<MenuCloseEvent> onClose;
	@Setter
	private boolean destroyWhenClosed = true;

	/**
	 * Create a new Menu
	 *
	 * @param title The title of the Inventory
	 * @param rows  The amount of rows in the Inventory
	 */
	public Menu(String title, int rows) {
		inv = Bukkit.createInventory(null, rows * 9, Chat.color(title));
		items = new ConcurrentHashMap<>(rows * 9);
		clickMappings = new ConcurrentHashMap<>(rows * 9);
		MenuListener.register(this);
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

	/**
	 * Set a slot of the inventory to the given button
	 *
	 * @param slot   The slot to set
	 * @param button The button to add
	 * @throws IndexOutOfBoundsException if the given slot is not in the Inventory
	 */
	public void addButton(int slot, MenuButton button) {
		Validate.notNull(button, "Button is null");
		if (slot > inv.getSize())
			throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size of Inventory %d", slot, inv.getSize()));
		items.put(slot, button);
		clickMappings.put(slot, button.getOnClick());
		inv.setItem(slot, button.getItemStack());
	}

	/**
	 * Add a MenuButton to the first empty slot in the Inventory
	 *
	 * @param button The button to add
	 * @throws IndexOutOfBoundsException if there are no more free slots
	 */
	public void addButton(MenuButton button) {
		addButton(inv.firstEmpty(), button);
	}

	private void adjustSize(int size) {
		if (size == 0) {
			inv = EMPTY;
			return;
		}
		if (size >= MAX_SIZE) {
			//TODO pagination
			return;
		}
		if (size > inv.getSize()) {
			val tempInv = Bukkit.createInventory(null, MathUtils.roundUp(size), inv.getTitle());
			tempInv.setContents(inv.getContents());
			inv.setMaxStackSize(inv.getMaxStackSize());
			inv = tempInv;
		} else {
			val tempInv = Bukkit.createInventory(null, MathUtils.roundUp(size), inv.getTitle());
			tempInv.setContents(Arrays.copyOf(inv.getContents(), size));
			inv.setMaxStackSize(inv.getMaxStackSize());
			inv = tempInv;
		}
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
	 * Set the Event listener for a certain slot. This can override the event handler of a button
	 *
	 * @param slot The slot to bind the event to
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


	//Dangerous Methods
	//These will likely break things unless you know what you're doing
	//Use at your own risk!

	/**
	 * @param o The map of slots and buttons to set
	 * @deprecated Dangerous
	 */
	@Deprecated
	public void setButtons(Map<Integer, MenuButton> o) {
		items.clear();
		items.putAll(o);

	}
}

