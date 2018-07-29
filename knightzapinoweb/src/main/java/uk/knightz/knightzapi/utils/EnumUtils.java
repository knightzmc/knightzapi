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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enum related utility class
 */
public class EnumUtils {


	/**
	 * Get a random element in the given enum, excluding any elements that 'exclude' includes (woo, confusing! :D)
	 * Typically used for ChatColor
	 *
	 * @param enm     The Enum
	 * @param exclude Any elements from this array will never be selected
	 * @param <T>     The type of Enum
	 * @return A random Element from the enum, will never be anything in exclude
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
	 * @param enm     The Enum
	 * @param exclude Any elements from this array will never be selected
	 * @param amount  How many elements to get. Will be shortened if not enough exist
	 * @param <T>     The type of Enum
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
}
