package uk.knightz.knightzapi.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import uk.knightz.knightzapi.lang.Chat;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Menu {
	private final Inventory inventory;
	private final Set<SubMenu> menus = ConcurrentHashMap.newKeySet();

	public Menu(String title) {this(title, 54);}

	public Menu(String title, int size) {
		inventory = Bukkit.createInventory(null, size, Chat.color(title));
	}

	public void addSubMenu(SubMenu subMenu) {
		if (subMenu != null)
			menus.add(subMenu);
	}
}

