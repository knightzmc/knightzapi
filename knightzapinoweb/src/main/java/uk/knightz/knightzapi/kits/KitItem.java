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

package uk.knightz.knightzapi.kits;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * An Item in a Kit. The situation this was made for involved most items having on-click abilities, so
 * in order to instantiate {@link KitItem#onInteract(PlayerInteractEvent)} must be implemented
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KitItem)) return false;
        KitItem kitItem = (KitItem) o;
        return getSlot() == kitItem.getSlot() &&
                Objects.equals(getItem(), kitItem.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getSlot());
    }
}
