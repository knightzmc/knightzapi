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

package uk.knightz.knightzapi.menu.adapter.options;

import org.apache.commons.lang.Validate;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultUnfriendlyFilter;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.UnfriendlyFilter;

/**
 * Simple Builder Object for {@link Options}
 */
public class OptionsBuilder {
    private int modifierBlacklist;
    private ObjectTokenToItemStackAdapter objectTokenToItemStackAdapter = new DefaultObjectTokenToItemStackAdapter();
    private UnfriendlyFilter unfriendlyFilter = new DefaultUnfriendlyFilter();
    private int settings = 0;


    public OptionsBuilder unfriendlyFilter(UnfriendlyFilter filter) {
        Validate.notNull(filter);
        this.unfriendlyFilter = filter;
        return this;
    }

    public OptionsBuilder objectTokenToItemStackAdapter(ObjectTokenToItemStackAdapter adapter) {
        Validate.notNull(adapter);
        this.objectTokenToItemStackAdapter = adapter;
        return this;
    }

    public OptionsBuilder includeFields() {
        this.settings |= Settings.INCLUDE_FIELDS;
        return this;
    }

    public OptionsBuilder includeUnfriendly() {
        this.settings |= Settings.UNFRIENDLY;
        return this;
    }

    public OptionsBuilder includeStatic() {
        this.settings |= Settings.INCLUDE_STATIC;
        return this;
    }

    public OptionsBuilder modifierBlacklist(int modifierBlacklist) {
        this.modifierBlacklist = modifierBlacklist;
        return this;
    }

    public Options build() {
        return new Options(modifierBlacklist, objectTokenToItemStackAdapter, unfriendlyFilter, settings);
    }


}