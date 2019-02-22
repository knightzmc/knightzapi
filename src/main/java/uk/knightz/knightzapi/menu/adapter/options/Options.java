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

import lombok.Data;
import uk.knightz.knightzapi.menu.adapter.ObjectToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultUnfriendlyFilter;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.UnfriendlyFilter;

/**
 * Options for Reflection and Adapter operations
 */
@Data
public class Options {

    /**
     * Default Options settings. This is used if no Options is provided.
     * It has no settings, no blacklisted modifiers, and uses the default implementations of all interfaces
     */
    public static final Options DEFAULT_OPTIONS = new Options(
            0,
            new DefaultObjectTokenToItemStackAdapter(),
            new DefaultUnfriendlyFilter(),
            0
    );
    /**
     * A bitmasked- blacklist of any modifiers (eg public, private, transient) that if a method or field has any of these, will
     * be excluded from any friendly Reflexive operations
     */
    private final int modifierBlacklist;
    /**
     * Implementation of {@link ObjectToItemStackAdapter}
     */
    private final ObjectTokenToItemStackAdapter objectTokenToItemStackAdapter;
    /**
     * Implementation of {@link UnfriendlyFilter}
     */
    private final UnfriendlyFilter filter;
    /**
     * Bitmask-ed integer representing any of {@link Settings} to use
     */
    private final int settings;


    /**
     * Construct a bitmask-ed settings int from a series of ints
     * These ints should be obtained from {@link Settings}, however can be used for any bitmasking
     *
     * @param settings An array of integers to bitmask
     * @return A bitmask-ed integer representing the given settings
     */
    public static int constructSettings(int... settings) {
        int total = 0;
        for (int setting : settings) {
            total |= setting;
        }
        return total;
    }


    public boolean isAllowSettingValues() {
        return (settings & Settings.ALLOW_SETTING_VALUES) != 0;
    }

    public boolean isIncludeFields() {
        return (settings & Settings.INCLUDE_FIELDS) != 0;
    }

    public boolean isUnfriendly() {
        return (settings & Settings.UNFRIENDLY) != 0;
    }

    public boolean includeStatic() {
        return (settings & Settings.INCLUDE_STATIC) != 0;
    }
}
