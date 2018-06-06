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

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
