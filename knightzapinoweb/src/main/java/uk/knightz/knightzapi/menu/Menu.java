package uk.knightz.knightzapi.menu;

import lombok.Data;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import uk.knightz.knightzapi.lang.Chat;
import uk.knightz.knightzapi.menu.item.MenuButton;
import uk.knightz.knightzapi.utils.MathUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
@Data
@Getter
public class Menu {
	private static final int MAX_SIZE = 54;
	private static final Inventory EMPTY = Bukkit.createInventory(null, 0);
	private final Set<SubMenu> children = ConcurrentHashMap.newKeySet();
	private final Map<Integer, MenuButton> items;
	private Inventory inv;

	private boolean destroyWhenClosed = true;

	public Menu(String title, int rows) {
		inv = Bukkit.createInventory(null, rows * 9, Chat.color(title));
		items = new ConcurrentHashMap<>(rows * 9);
		MenuListener.register(this);
	}

	public void addSubMenu(SubMenu subMenu) {
		if (subMenu != null)
			children.add(subMenu);
	}

	public void addButton(int slot, MenuButton button) {
		if (slot > inv.getSize())
			throw new IndexOutOfBoundsException(String.format("%d exceeds maximum size of Inventory %d", slot, inv.getSize()));
		if (button != null) {
			items.put(slot, button);
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


	public Inventory getInv() {
		return inv;
	}

	public void trim() {
		adjustSize(items.size());
	}


}
