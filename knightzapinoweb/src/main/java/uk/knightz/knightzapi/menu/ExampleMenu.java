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

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.item.BackMenuButton;
import uk.knightz.knightzapi.menu.item.CloseButton;
import uk.knightz.knightzapi.menu.item.MenuButton;
import uk.knightz.knightzapi.menu.item.OpenSubMenuButton;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.*;

public class ExampleMenu {
	private static final Map<String, ExampleMenu> ourMap = new HashMap<>();
	Menu menu;
	SubMenu subMenu;
	SubMenu second;

	private ExampleMenu(Player p) {
		menu = new Menu("&6Hi", 6);
		subMenu = new SubMenu("&9&lSubMenu", 3, menu);
		menu.addButton(new CloseButton());
		menu.addButton(new OpenSubMenuButton(
				new ItemStack(GLASS),
				subMenu));
		subMenu.addButton(new CloseButton());
		menu.addButton(new MenuButton(new ItemStack(STONE), e -> {
			menu.trim();
			p.openInventory(menu.getInv());
		}));
		subMenu.addButton(new BackMenuButton(new ItemStack(WOOL)));
		second = new SubMenu("&6&lDeep", 1, subMenu);
		second.addButton(6, new MenuButton(new ItemStack(PURPLE_GLAZED_TERRACOTTA), e -> e.getWhoClicked().sendMessage("Hi")));
		subMenu.addButton(new OpenSubMenuButton(new ItemStack(BARRIER), second));
		p.sendMessage("new inv made");
		ourMap.put(p.getName(), this);
	}

	public static ExampleMenu forPlayer(Player p) {
		ExampleMenu orDefault;
		if (ourMap.containsKey(p.getName())) orDefault = ourMap.get(p.getName());
		else
			orDefault = new ExampleMenu(p);
		p.openInventory(orDefault.menu.getInv());
		return orDefault;
	}
}
