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

import org.apache.commons.lang.Validate;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class related to Map operations
 */
public class MapUtils {
	private MapUtils() {
	}

	/**
	 * Return the first key of a Map with a value equal to the given value
	 * @param map The Map to check
	 * @param value The Value that should be equal
	 * @param <K> The Key type of the Map
	 * @param <V> The Value type of the Map
	 * @return the first key in the Map with a corresponding value equal to the provided value
	 */
	public static <K, V> K firstKeyOf(Map<K, V> map, V value) {
		for (K k : map.keySet()) {
			V v = map.getOrDefault(k, null);
			if (v != null && v.equals(value)) {
				return k;
			}
		}
		return null;
	}
	/**
	 * Convert a Map with Integer keys to an Array
	 * @param map The Map to Convert
	 * @param <T> The value type of the Map
	 * @return An array with each Map key as the index, containing the corresponding value from the Map
	 */
	public static <T> T[] mapToArray(Map<Integer, T> map) {
		Validate.notNull(map, "Map cannot be null");
		Set<Map.Entry<Integer, T>> entries = map.entrySet();
		entries = entries.stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).collect(Collectors.toCollection(LinkedHashSet::new));
		T[] arr = (T[]) new Object[entries.size()];
		entries.forEach(e -> arr[e.getKey()] = e.getValue());
		return arr;
	}
}
