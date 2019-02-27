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

import org.bukkit.inventory.ItemStack;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;

/**
 * Interface responsible for converting an ObjectToken to an ItemStack
 * Default implementation: {@link DefaultObjectTokenToItemStackAdapter}
 *
 * @param <T> The type of ObjectToken and data
 */
public interface ObjectTokenToItemStackAdapter<T> {

    /**
     * Convert an ObjectToken into an ItemStack
     *
     * @param data             The actual Object to convert
     * @param token            The ObjectToken of this Object
     * @param source A nullable String that essentially says where this method was called from. EG it might be "getChild" if we are currently converting a TestData object that was obtained by calling TestData#getChild
     * @return An ItemStack representing the given ObjectToken
     */
    ItemStack adapt(T data, ObjectToken<T> token, String source);

}
