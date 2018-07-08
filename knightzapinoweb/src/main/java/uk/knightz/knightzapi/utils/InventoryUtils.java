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

package uk.knightz.knightzapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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

	public static boolean equalsNoContents(Inventory inv1, Inventory inv2) {
		return inv1 != null && inv2 != null
				&& inv1.getTitle().equalsIgnoreCase(inv2.getTitle()) &&
				inv1.getType().equals(inv2.getType());
	}

	public static void removeItemFromPlayer(Player p, ItemStack item, int quantity) {
		if (p.getInventory().containsAtLeast(item, quantity)) {
			int n = 0;
			for (ItemStack i : p.getInventory()) {
				if (i != null) {
					if (i
							.isSimilar(item)) {
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

	public static void removeItemFromPlayer(Player p, Material item, int quantity) {
		p.getInventory().removeItem(new ItemStack(item, quantity));
		//		if (p.getInventory().contains(item, quantity)) {
//			int n = 0;
//			for (ItemStack i : p.getInventory()) {
//				if (i != null) {
//					if (i
//							.getType() == item) {
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
	 * @param inv
	 * @return the central slot id in the Inventory
	 */
	public static int getCenter(Inventory inv) {
		return (inv.getSize() / 2) - 1;
	}


	/**
	 * Get the first slot containing the given ItemStack in an inventory, ignoring item quantity
	 *
	 * @param inv The Inventory to get the item from
	 * @param it  The ItemStack to find
	 * @return The first slot containing the given ItemStack, or -1 if it doesn't exist or an exception occurs in Reflection(unlikely)
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
