package uk.knightz.knightzapi.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 20:43.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A class consisting ofGlobal various utility functions for operating on enums
 **/
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
