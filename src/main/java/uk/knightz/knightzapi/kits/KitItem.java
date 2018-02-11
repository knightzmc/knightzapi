package uk.knightz.knightzapi.kits;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:12.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public abstract class KitItem {
    private final ItemStack item;
    private int slot;

    protected KitItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public KitItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public abstract void onInteract(PlayerInteractEvent e);

    public void onClickInInv(InventoryClickEvent e) {
    }
}
