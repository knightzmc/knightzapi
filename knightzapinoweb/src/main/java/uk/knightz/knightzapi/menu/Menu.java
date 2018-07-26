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
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		ItemStack[] items = inv.getContents();
		inv = Bukkit.createInventory(null, inv.getSize(), Chat.color(title));
		inv.setContents(items);
	}


	public void addSubMenu(SubMenu subMenu) {
		if (subMenu != null)
			children.add(subMenu);
	}

	public void addButton(int slot, MenuButton button) {
		Validate.notNull(button, "Button is null");
		if (slot > inv.getSize())
			throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size ofGlobal Inventory %d", slot, inv.getSize()));
		if (button != null) {
			items.put(slot, button);
			clickMappings.put(slot, button.getOnClick());
			inv.setItem(slot, button.getItemStack());
		}
	}

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

	public void setRows(int rows) {
		adjustSize(rows * 9);
	}

	public void mapButton(int slot, Consumer<MenuClickEvent> e) {
		Validate.notNull(e);
		if (slot > inv.getSize())
			throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size ofGlobal Inventory %d", slot, inv.getSize()));

		clickMappings.put(slot, e);
	}


	public Inventory getInv() {
		return inv;
	}

	public void trim() {
		adjustSize(items.size());
	}


	//Dangerous Methods
	//These will likely break things unless you know what you're doing
	//Use at your own risk!

	/**
	 * @param o
	 * @deprecated Dangerous
	 */
	@Deprecated
	public void setButtons(Map<Integer, MenuButton> o) {
		items.clear();
		items.putAll(o);

	}
}

