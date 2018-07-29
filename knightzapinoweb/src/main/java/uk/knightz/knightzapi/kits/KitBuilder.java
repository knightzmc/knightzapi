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

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.utils.NotEmptyArrayList;

/**
 * A builder object that shortens and cleans up the process of creating a new {@link Kit}
 */
public class KitBuilder {
    private String name = "Default";
    private ItemStack icon;
    private NotEmptyArrayList<KitItem> items;

    public KitBuilder(KitItem only) {
        this.items = new NotEmptyArrayList<>(only);
        this.icon = items.getFirst().getItem();
    }

    public KitBuilder(NotEmptyArrayList<KitItem> items) {
        this.items = items;
        this.icon = items.getFirst().getItem();
    }

    public KitBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public KitBuilder setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public KitBuilder setItems(NotEmptyArrayList<KitItem> items) {
        this.items = items;
        return this;
    }

    public KitBuilder addItem(KitItem item) {
        items.add(item);
        return this;
    }

    public Kit build() {
        return new Kit(name, icon, items);
    }
}
