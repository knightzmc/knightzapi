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

package uk.knightz.knightzapi.menu.adapter.iface;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.item.ItemBuilder;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.Token.DataToken;
import uk.knightz.knightzapi.utils.ColorUtils;
import uk.knightz.knightzapi.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultObjectTokenToItemStackAdapter<T> implements ObjectTokenToItemStackAdapter<T> {
    private static final String FORMAT = "%s%s = %s";

    @Override
    public ItemStack adapt(T data, ObjectToken<T> token, String source) {
        List<DataToken> allData =
                Stream.concat(token.getFieldTokens().stream(),
                        token.getMethodTokens().stream()).collect(Collectors.toList());

        String name = ColorUtils.colorOfClass(data.getClass()) + (source == null ? data.getClass().getSimpleName() : source);
        Material type = attemptExtractType(data, allData);
        List<String> dataStrings = new ArrayList<>();
        allData.forEach(f -> {
            String string = colorString(f);
            dataStrings.add(string);
        });
        dataStrings.removeIf(Objects::isNull);
        dataStrings.removeIf(String::isEmpty);
        return new ItemBuilder().setType(type).setName(name).setLore(dataStrings).build();
    }

    private Material attemptExtractType(Object t, List<DataToken> tokens) {
        if (t instanceof ItemStack) {
            return ((ItemStack) t).getType();
        }
        for (DataToken f : tokens) {
            if (f.getValue() instanceof Material) {
                return (Material) f.getValue();
            }
        }

        return EnumUtils.getRandom(Material.class);

    }

    private String colorString(DataToken token) {
        if (token.getValue() == null) return null;
        ChatColor chatColor = ColorUtils.colorOfClass(token.getType());
        return String.format(FORMAT, chatColor, token.getFriendlyDataName(), token.getValue().toString());
    }


}
