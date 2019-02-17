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

package uk.knightz.knightzapi.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
public class MenuButton {

    @NonNull
    private final transient Consumer<MenuClickEvent> onClick;
    @NonNull
    private final Map<String, Object> injectedData = new HashMap<>();

    @NonNull
    private final ItemStack item;

    public MenuButton(ItemStack item, Consumer<MenuClickEvent> onClick) {
        this.item = item;
        this.onClick = onClick;
    }

    public MenuButton addDataToInject(String key, Object value) {
        injectedData.put(key, value);
        return this;
    }

    public void injectDataFrom(MenuButton b) {
        injectDataFrom(b.injectedData);
    }

    public void injectDataFrom(Map<String, Object> map) {
        ItemBuilder builder = new ItemBuilder(item);
        builder.setName(parse(map, builder.getName()));


        val lore = builder.getLore();
        builder.getLore().clear();
        lore.forEach(s -> builder.addLore(parse(map, s)));
        item.setItemMeta(builder.build().getItemMeta());
    }

    private String parse(Map<String, Object> data, String s) {
        AtomicReference<String> str = new AtomicReference<>(s);
        data.forEach((k, v) -> str.set(str.get().replace(k, v.toString())));
        return str.get();
    }

}
