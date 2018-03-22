package uk.knightz.knightzapi.utils;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
        return
                Arrays.equals(inv1.getContents(), inv2.getContents())
                        && inv1.getName().equals(inv2.getName())
                        && inv1.getTitle().equals(inv2.getTitle())
                        && inv1.getType().equals(inv2.getType())
                        && (inv1.getLocation() == null
                        || (inv2.getLocation() == null
                        || inv1.getLocation().equals(inv2.getLocation())));
    }
}
