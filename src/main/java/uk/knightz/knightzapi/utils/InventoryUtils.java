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

package uk.knightz.knightzapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Inventory related utility class
 */
public class InventoryUtils {
	/**
	 * Shouldn't be instantiated
	 */
	private InventoryUtils() {
	}

	/**
	 * Checks if two Inventory objects are "equal"
	 * To be equal, the names, titles, contents (ie, InventoryClickEvent should be cancelled) and location (if applicable) must be equal.
	 *
	 * @param inv1 The first Inventory
	 * @param inv2 The second Inventory
	 * @return if the two Inventory objects are equal
	 */
	public static boolean equals(Inventory inv1, Inventory inv2) {
		return inv1 != null && inv2 != null
				&& Arrays.equals
				(inv1.getContents(), inv2.getContents())
				&& inv1.getTitle().equalsIgnoreCase(inv2.getTitle()) &&
				inv1.getType().equals(inv2.getType());
	}

	/**
	 * Check if two Inventory objects are equal, but not comparing their contents.
	 * @param inv1 The first Inventory
	 * @param inv2 The second Inventory
	 * @return true if both Inventory objects are not null, are of equal type, and have an equal title, otherwise false
	 */
	public static boolean equalsNoContents(Inventory inv1, Inventory inv2) {
		return inv1 != null && inv2 != null
				&& inv1.getTitle().equalsIgnoreCase(inv2.getTitle()) &&
				inv1.getType().equals(inv2.getType());
	}

	/**
	 * Remove a given ItemStack from a Player's Inventory
	 * @param p The Player to remove the items from
	 * @param item The ItemStack to remove
	 * @param quantity The amount of this ItemStack to remove
	 */
	public static void removeItemFromPlayer(Player p, ItemStack item, int quantity) {
		if (p.getInventory().containsAtLeast(item, quantity)) {
			int n = 0;
			for (ItemStack i : p.getInventory()) {
				if (i != null) {
					if (i.isSimilar(item)) {
						if (n + i.getAmount() > quantity || n > quantity) {
							i.setAmount(quantity - n);
							return;
						}
						n += i.getAmount();
						i.setType(Material.AIR);
					}
				}
			}
		}
	}
	/**
	 * Remove a given button from a Player's Inventory
	 * @param p The Player to remove the items from
	 * @param item The Material Type to remove
	 * @param quantity The amount of this Material to remove
	 */
	public static void removeItemFromPlayer(Player p, Material item, int quantity) {
		p.getInventory().removeItem(new ItemStack(item, quantity));
		//		if (p.getInventory().contains(button, quantity)) {
//			int n = 0;
//			for (ItemStack i : p.getInventory()) {
//				if (i != null) {
//					if (i
//							.getType() == button) {
//						if (n + i.getAmount() > quantity || n > quantity) {
//							i.setAmount(quantity - n);
//							return;
//						}
//						n += i.getAmount();
//						i.setType(Material.AIR);
//					}
//				}
//			}
//		}
	}

	/**
	 * Get the central slot in an inventory
	 *
	 * @param inv The Inventory
	 * @return the central slot id in the Inventory
	 */
	public static int getCenter(Inventory inv) {
		return (inv.getSize() / 2) - 1;
	}


	/**
	 * Get the first slot containing the given ItemStack in an inventory, ignoring button quantity
	 *
	 * @param inv The Inventory to get the button from
	 * @param it  The ItemStack to find
	 * @return The first slot containing the given ItemStack, or -1 if it doesn't exist or an exception occurs in BasecomponentReflection(unlikely)
	 */
	public static int firstIgnoreAmount(Inventory inv, ItemStack it) {
		try {
			Method firstIgnore;
			try {
				firstIgnore = inv.getClass().getDeclaredMethod("first", ItemStack.class, boolean.class);
			} catch (NoSuchMethodException e) {
				firstIgnore = inv.getClass().getSuperclass().getDeclaredMethod("first", ItemStack.class, boolean.class);
			}
			return (int) firstIgnore.invoke(inv, it, true);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
