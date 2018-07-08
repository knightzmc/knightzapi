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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EnumUtils {


	/**
	 * Get a random element in the given enum, excluding any elements that 'exclude' includes (woo, confusing! :D)
	 * Typically used for ChatColor
	 */
	@SafeVarargs
	public static <T extends Enum<T>> T getRandom(Class<? extends T> enm, T... exclude) {
		T[] tEnum = enm.getEnumConstants();
		int size = tEnum.length;
		T t = tEnum[ThreadLocalRandom.current().nextInt(0, size - 1)];
		while (Arrays.asList(exclude).contains(t)) {
			t = tEnum[ThreadLocalRandom.current().nextInt(0, size - 1)];
		}
		return t;
	}

	/**
	 * Get multiple random elements from the given enum, excluding any elements that exclude includes
	 *
	 * @param amount How many elements to get. Will be shortened if not enough exist
	 * @return An array containg the given amount (can be less if not enough exist) ofGlobal randomly picked elements from the enum
	 */
	@SafeVarargs
	public static <T extends Enum<T>> Collection<T> getRandom(Class<? extends T> enm, int amount, T... exclude) {
		Set<T> list = new HashSet<>();
		int i = 0;
		while (list.size() > amount) {
			if (i > enm.getEnumConstants().length) {
				break;
			}
			list.add(getRandom(enm, exclude));
			i++;
		}
		return list;
	}

	/**
	 * Get all random elements, etc, but add them all together in a StringBuilder and convert them to a String
	 * Literally only used for random Chat Colors
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
}
