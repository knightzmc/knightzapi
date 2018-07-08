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

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class MenuListener implements Listener {

	private static final Set<Menu> allMenus = ConcurrentHashMap.newKeySet();

	private static final MenuListener instance = new MenuListener();

	private MenuListener() {
		Bukkit.getPluginManager().registerEvents(this, KnightzAPI.getP());
	}

	public static void register(Menu me) {
		if (me != null)
			allMenus.add(me);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e instanceof MenuClickEvent)) {
			if (e.getWhoClicked() instanceof Player) {
				if (e.getClickedInventory() != null) {
					for (Menu m : allMenus) {
						if (InventoryUtils.equalsNoContents(m.getInv(), e.getClickedInventory())) {
							if (e.getCurrentItem() != null) {
								e.setCancelled(true);
								if (m.getItems().containsKey(e.getSlot())) {
									val event = convertEvent(m, e);
									Bukkit.getPluginManager().callEvent(event);
									m.getItems().get(e.getSlot()).onClick(event);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	private MenuClickEvent convertEvent(Menu m, InventoryClickEvent e) {
		if (e.getClickedInventory() != null) {
			AtomicReference<Menu> clicked = new AtomicReference<>();
			if (InventoryUtils.equalsNoContents(m.getInv(), e.getClickedInventory())) {
				clicked.set(m);
			} else {
				List<Menu> menus = recursiveCheckChildren(e, m);
				clicked.set(menus.get(0));
			}
			if (clicked.get() == null) {
				Log.warn("No Menu matched " + e.getClickedInventory().toString() + " but it was registered as a Menu!");
				return null;
			}
			return new MenuClickEvent(e, clicked.get());
		}
		throw new IllegalArgumentException("InventoryClickEvent does not refer to a Menu being clicked!");
	}

	private List<Menu> recursiveCheckChildren(InventoryClickEvent e, Menu menu) {
		List<Menu> childList = new ArrayList<>();
		for (SubMenu sub : menu.getChildren()) {
			childList.addAll(recursiveCheckChildren(e, menu));
		}
		return childList;
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!(e instanceof MenuCloseEvent)) {
			if (e.getInventory() != null)
				for (Menu m : allMenus) {
					if (InventoryUtils.equalsNoContents(m.getInv(), e.getInventory())) {
						MenuCloseEvent event = new MenuCloseEvent(e.getView(), m);
						Bukkit.getPluginManager().callEvent(event);
						if (event.isCancelled()) {
							Bukkit.getScheduler().runTaskLater(KnightzAPI.getP(), () ->
									e.getPlayer().openInventory(e.getInventory()), 1L);
						} else if (m.isDestroyWhenClosed()) {
							allMenus.remove(m);
							break;
						}
					}
				}
		}
	}
}
