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
