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

package uk.knightz.knightzapi.menuold.button;

import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.lang.placeholder.Placeholder;
import uk.knightz.knightzapi.menuold.MenuClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DynamicDataButton extends MenuButton {

    private final ItemStack defaultValue;
    @NonNull
    private final List<Placeholder> placeholdersForItem = new ArrayList<>();

    @NonNull
    private final List<BiFunction<Player, String, String>> contextualPlaceholders = new ArrayList<>();
    @Setter
    private BiFunction<Player, ItemStack, ItemStack> editItem;

    /**
     * Create a new MenuButton that has its Metadata generated dynamically when an Inventory is rendered
     *
     * @param defaultValue The default value of the ItemStack that will be added to a menuold.
     *                     Typically, this has an ItemMeta using {@link String#format(String, Object...)}
     *                     compatible values, so they can be easily edited in {@link DynamicDataButton#editItem}
     * @param onClick      A consumer that will be called when the MenuButton is clicked by a user.
     */
    public DynamicDataButton(ItemStack defaultValue, Consumer<MenuClickEvent> onClick) {
        super(defaultValue, onClick);
        this.defaultValue = defaultValue;
    }


    public ItemStack applyDynData(Player menuUser, Object... format) {
        ItemStack temp = new ItemStack(getItemStack());
        if (editItem != null) {
            temp = editItem.apply(menuUser, getItemStack());
        }
        val meta = getItemStack().getItemMeta();
        contextualPlaceholders.forEach(f -> {
            meta.setDisplayName(f.apply(menuUser, meta.getDisplayName()));
            meta.setLore(meta.getLore().stream().map(s -> s = f.apply(menuUser, s)).collect(Collectors.toList()));

        });
        placeholdersForItem.forEach(p -> {
            meta.setDisplayName(String.format(p.replace(meta.getDisplayName()), format));
            val tempList = meta.getLore().stream()
                    .map(p::replace)
                    .map(s1 -> String.format(s1, format))
                    .collect(Collectors.toList());
            meta.setLore(tempList);
        });
        temp.setItemMeta(meta);
        return temp;
    }

    public void addPlaceholder(Placeholder p) {
        placeholdersForItem.add(p);
    }

    public void addContextualPlaceholder(BiFunction<Player, String, String> s) {
        contextualPlaceholders.add(s);
    }
}
