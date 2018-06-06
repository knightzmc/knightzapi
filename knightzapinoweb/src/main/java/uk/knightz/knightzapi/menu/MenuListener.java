package uk.knightz.knightzapi.menu;

import org.bukkit.Bukkit;
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
		if (e.getClickedInventory() != null) {
			for (Menu m : allMenus) {
				if (InventoryUtils.equalsNoContents(m.getInv(), e.getClickedInventory()))
					if (e.getCurrentItem() != null) {
						e.setCancelled(true);
						if (m.getItems().containsKey(e.getSlot())) {
							m.getItems().get(e.getSlot()).onClick(convertEvent(m, e));
							break;
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
		if (e.getInventory() != null)
			for (Menu m : allMenus) {
				if (InventoryUtils.equalsNoContents(m.getInv(), e.getInventory())) {
					if (m.isDestroyWhenClosed()) {
						allMenus.remove(m);
						break;
					}
				}
			}
	}
}
