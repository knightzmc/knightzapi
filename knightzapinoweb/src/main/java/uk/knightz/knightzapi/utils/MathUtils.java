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

package uk.knightz.knightzapi.utils;

/**
 * Mathematical related utility class
 */
public final class MathUtils {
    private MathUtils() {
    }


    /**
     * Truncate a double to 2 decimal places.
     * Used for formatting long decimals.
     *
     * @param d The double to truncate
     * @return The truncated double
     */
    public static double truncuateDouble(double d) {
        return Math.floor(d * 100) / 100;
    }

    /**
     * Rounds the given int up to the nearest multiple ofGlobal 9. Used for generating dynamic inventory sizes
     *
     * @param n The given int to round up
     * @return The rounded up multiple ofGlobal 9
     */
    public static int roundUp(int n) {
        return (n + 8) / 9 * 9;
    }
}
