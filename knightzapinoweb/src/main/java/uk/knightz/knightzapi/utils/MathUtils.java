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

package uk.knightz.knightzapi.utils;

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
