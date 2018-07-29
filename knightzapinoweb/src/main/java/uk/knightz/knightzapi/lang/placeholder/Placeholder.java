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

package uk.knightz.knightzapi.lang.placeholder;

/**
 * A Placeholder that can replace text placeholders with dynamic values.
 * Check the GitHub Wiki for examples.
 */
public class Placeholder {
    /**
     * The placeholder string that will be replaced, eg "%username%"
     */
    private final String placeholder;

    /**
     * The method(s) that will be called when replacing the placeholder, eg "User::getName"
     */
    private Replacement replacement;

    public Placeholder(String placeholder, Replacement replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String getPlaceholder() {
        return placeholder;
    }


    public Replacement replacement() {
        return replacement;
    }


    /**
     * Apply the placeholders to the given String
     *
     * @param toReplace The string to replace any instances ofGlobal Placeholder#getPlaceholder with Placeholder#getReplacement
     * @return The formatted String
     */
    public String replace(String toReplace) {
        if (toReplace == null) {
            return null;
        }
        return toReplace.replace(getPlaceholder().toLowerCase(), replacement.getReplacement());
    }

    public interface Replacement {
        String getReplacement();
    }
}
