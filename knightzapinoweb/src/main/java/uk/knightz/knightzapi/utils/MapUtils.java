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
