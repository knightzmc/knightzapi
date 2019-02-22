/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.menu.button;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.ClickEventAliases;
import uk.knightz.knightzapi.menu.MenuClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A Button that is placed in a Menu. It has an ItemStack that users view, and can interact with a MenuClickEvent when it's clicked
 */
@Getter
public class MenuButton {

    private final String onClickAlias;
    private final Map<String, Object> injectedData = new HashMap<>();
    private final ItemStack item;

    public MenuButton(@NonNull ItemStack item, @NonNull Consumer<MenuClickEvent> onClick) {
        this.item = item;
        String random = UUID.randomUUID().toString();
        this.onClickAlias = random;
        ClickEventAliases.getInstance().add(random, onClick);
    }

    public Consumer<MenuClickEvent> getOnClick() {
        return ClickEventAliases.getInstance().get(onClickAlias);
    }


    public MenuButton addDataToInject(String key, Object value) {
        injectedData.put(key, value);
        return this;
    }

    public void injectData(Map<String, Object> map) {
        ItemBuilder builder = new ItemBuilder(item);
        builder.setName(parse(map, builder.getName()));

        val lore = builder.getLore();
        builder.getLore().clear();
        lore.forEach(s -> builder.addLore(parse(map, s)));
        item.setItemMeta(builder.build().getItemMeta());
    }

    private String parse(Map<String, Object> data, String s) {
        String str = s;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            str = str.replace(key, value.toString());
        }
        return str;
    }

}
