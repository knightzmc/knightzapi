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

package uk.knightz.knightzapi.menu.adapter;

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.adapter.options.Options;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.factory.TokenFactory;

import java.util.function.Function;

/**
 * A class that uses Reflection to convert all the public data of an object to an ItemStack
 * Being an ItemStack, it has no interactivity and is only really suitable for displaying primitives
 *
 * It is however used by {@link CollectionToMenuAdapter}, which creates an interactive Menu based on a Collection of data
 */
public class ObjectToItemStackAdapter<T> implements Function<T, ItemStack> {

    public ItemStack apply(T o) {
        return adapt(o, Options.DEFAULT_OPTIONS);
    }

    public ItemStack adapt(T t, Options options) {
        TokenFactory<T> factory = new TokenFactory<>();
        ObjectToken<T> generate = factory.generate(t, options);

        return options.getObjectTokenToItemStackAdapter().adapt(t, generate, null);
    }

}
