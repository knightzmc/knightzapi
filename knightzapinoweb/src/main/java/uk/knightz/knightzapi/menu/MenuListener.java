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

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onClick(InventoryClickEvent e) {
		//Avoid repeating calls
		if (!(e instanceof MenuClickEvent)) {
			//Menu API only supports player clicks
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
									if (m.getOnClick() != null) {
										val onClickSound = m.getItems().get(e.getSlot()).getOnClickSound();
										if (onClickSound != null)
											event.getWhoClicked().playSound(event.getWhoClicked().getLocation(), onClickSound, 1, 1);
										else
											event.getWhoClicked().playSound(event.getWhoClicked().getLocation(), m.getOnClick(), 1, 1);
									}
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
