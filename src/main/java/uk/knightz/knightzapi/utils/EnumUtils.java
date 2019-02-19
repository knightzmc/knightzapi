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

package uk.knightz.knightzapi.utils;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Enum related utility class
 */
public class EnumUtils {


    /**
     * Get a random element in the given enum, excluding any elements that a given 'exclude' includes  (confusing!)
     * Typically used for ChatColor
     *
     * @param enm     The Enum
     * @param exclude Any elements from this array will never be selected.
     * @param <T>     The type of Enum
     * @return A random Element from the enum that will never be anything in exclude unless include contains all of the Enum constants, in which case null will be returned
     */
    @SafeVarargs
    public static <T extends Enum<T>> T getRandom(Class<? extends T> enm, T... exclude) {
        return getRandom(enm, () -> null, exclude);
    }

    /**
     * Get a random element in the given enum, excluding any elements that a given 'exclude' includes  (confusing!)
     * Typically used for ChatColor
     *
     * @param enm     The Enum
     * @param exclude Any elements from this array will never be selected.
     * @param ifFull  A Supplier that will be called if there are no enum entries that aren't excluded
     * @param <T>     The type of Enum
     * @return A random Element from the enum that will never be anything in exclude unless include contains all of the Enum constants, in which case the returned value from the Supplier will be returned
     */
    @SafeVarargs
    public static <T extends Enum<T>> T getRandom(Class<? extends T> enm, Supplier<T> ifFull, T... exclude) {
        T[] tEnum = enm.getEnumConstants();
        if (tEnum.length == exclude.length) return ifFull.get();
        List<T> excluded = Arrays.asList(exclude);
        T random;
        while (excluded.contains(random = getRandom(enm))) {
        }
        return random;
    }

    private static <T extends Enum<T>> T getRandom(Class<? extends T> enm) {
        T[] tEnum = enm.getEnumConstants();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return tEnum[random.nextInt(tEnum.length)];
    }

    /**
     * Get multiple random elements from the given enum, excluding any elements that exclude includes
     *
     * @param enm     The Enum
     * @param exclude Any elements from this array will never be selected
     * @param amount  How many elements to get. Will be shortened if not enough exist
     * @param <T>     The type of Enum
     * @return An array containing the given amount (may be less if not enough exist) of randomly picked elements from the enum
     */
    @SafeVarargs
    public static <T extends Enum<T>> Collection<T> getRandom(Class<? extends T> enm, int amount, T... exclude) {
        Set<T> list = new HashSet<>();
        int i = 0;
        T[] enumConstants = enm.getEnumConstants();
        if (enumConstants.length < amount) {
            amount = enumConstants.length;
        }
        while (list.size() < amount)
            for (int j = 0; j < amount; j++) {
                list.add(getRandom(enm, exclude));
            }
        return list;
    }

    /**
     * Get all random elements, etc, but add them all together in a StringBuilder and convert them to a String
     * Literally only used for random Chat Colors
     *
     * @param amount  The amount of random elements to generate
     * @param enm     The enum
     * @param exclude Any elements from this array will never be selected
     * @param <T>     The type of Enum
     * @return A String contain all of the random elements appended to each other
     */
    @SafeVarargs
    public static <T extends Enum<T>> String getRandomFormatted(Class<? extends T> enm, int amount, T... exclude) {
        Set<T> list = new HashSet<>();
        int i = 0;
        while (list.size() < amount) {
            if (i > enm.getEnumConstants().length) {
                break;
            }
            list.add(getRandom(enm, exclude));
            i++;
        }
        StringBuilder builder = new StringBuilder();
        for (T t : list) {
            builder.append(t);
        }
        return builder.toString();
    }


    public static String getFriendlyName(Enum e) {
        return StringUtils.capitalize(e.name().toLowerCase().replace("_", " "));
    }
}
