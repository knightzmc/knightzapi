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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A utility class for handling Collection objects
 * @deprecated Stream API has all of these functions
 */
@Deprecated
public class CollectionUtils {

	/**
	 * @param collection The collection to check
	 * @param check      Perform this check on each element in the collection
	 * @param <I>        Element type of the collection
	 * @return If every element in the collection returns true after being tested by check
	 */
	public static <I> boolean contentsMatches(Collection<I> collection, Predicate<I> check) {
		for (I i : collection) {
			if (!check.test(i)) return false;
		}
		return true;
	}
	/**
	 * @param arr   The array to check
	 * @param check Perform this check on each element in the array
	 * @param <I>   Element type of the array
	 * @return If every element in the array returns true after being tested by check
	 */
	public static <I> boolean contentsMatches(I[] arr, Predicate<I> check) {
		for (I i : arr) {
			if (!check.test(i)) return false;
		}
		return true;
	}

	/**
	 * Evaluates if the given Collection contains any elements, which after having the given function run on them,
	 * are equal to the given check parameter
	 *
	 * @param collection The Collection to check
	 * @param function   The Function to check each element
	 * @param check      The expected result after the application of the Function
	 * @param <Type>     the type of the Collection
	 * @param <Return>   the return type of the Function
	 * @return if the Collection contains any elements that are equal to the check parameter after having the given Function applied to them
	 */
	public static <Type, Return> boolean collectionContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
		return collection.stream().anyMatch(type -> function.apply(type).equals(check));
	}

	/**
	 * Get the first element in the given Collection which, after having the given function run on it, is equal to the given check parameter
	 *
	 * @param collection The Collection to check
	 * @param function   The Function to check each element
	 * @param check      The expected result after the application of the Function
	 * @param <Type>     the type of the Collection
	 * @param <Return>   if it is an instanceof String, String#equals will be checked, rather than String#equalsIgnoreCase
	 * @see CollectionUtils#getIfContains(Collection, Function, Object, boolean)
	 * @return if the Collection contains any elements that are equal to the check parameter after having the given Function applied to them
	 */
	public static <Type, Return> Type getIfContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
		return getIfContains(collection, function, check, false);
	}

	/**
	 * Get the first element in the given Collection which, after having the given function run on it, is equal to the given check parameter
	 *
	 * @param collection       The Collection to check
	 * @param function         The Function to check each element
	 * @param check            The expected result after the application of the Function
	 * @param <Type>           the type of the Collection
	 * @param <Return>         the return type of the Function
	 * @param stringIgnoreCase if Return is an instanceof String, then String#equalsIgnoreCase will be run over String#equals depending on this parameter
	 * @return if the Collection contains any elements that are equal to the check parameter after having the given Function applied to them
	 */

	public static <Type, Return> Type getIfContains(Collection<Type> collection, Function<Type, Return> function, Return check, boolean stringIgnoreCase) {
		for (Type type : collection) {
			Return returned = function.apply(type);
			if (returned instanceof String) {
				if (stringIgnoreCase && ((String) returned).equalsIgnoreCase((String) check)) {
					return type;
				}
			}
			if (returned.equals(check)) {
				return type;
			}
		}
		return null;
	}

//	/**
//	 * Essentially just a wrapper for Stream#map, implemented messier because I forgot Stream#map exists
//	 * {@link java.util.stream.Stream#map(Function)}
//	 *
//	 * @deprecated Use Stream#map, this is buggy and likely will not be used. Kept for old version compatibility
//	 */
//	@Deprecated
//	public static <Type, Return> Collection<Return> applyToEach(Collection<Type> collection, Function<Type, Return> toExec) {
//		if (collection instanceof HashSet) {
//			HashSet set = new HashSet();
//			collection.forEach(t -> set.add(toExec.generateMenu(t)));
//			return set;
//		}
//		//NOT UNNECESSARY - REQUIRED TO ENSURE THE ORIGINAL COLLECTION IS NOT CLEARED
//		//noinspection UnnecessaryLocalVariable
//		Collection temp = collection;
//		temp.clear();
//		collection.forEach(t -> temp.add(toExec.generateMenu(t)));
//		return (Collection) temp;
//	}

	/**
	 * Sort the given Collection through {@link Collections#sort}
	 *
	 * @param c   The Collection to sort
	 * @param <T> the type of the Collection
	 * @return The sorted Collection as a List
	 */
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<>(c);
		Collections.sort(list);
		return list;
	}

	/**
	 * Get an Iterable of the given Enumeration
	 *
	 * @param enumeration The Enumeration to make an Iterable of
	 * @param <E>         the type of Enumeration and Iterable
	 * @return An Iterable of the given Enumeration
	 */
	public static <E> Iterable<E> iterable(final Enumeration<E> enumeration) {
		if (enumeration == null) {
			throw new NullPointerException();
		}
		return () -> new Iterator<E>() {
			public boolean hasNext() {
				return enumeration.hasMoreElements();
			}

			public E next() {
				return enumeration.nextElement();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Get a random element in the given collection
	 *
	 * @param toRandom The collection to get a random element from
	 * @param <T>      Default type parameter ofGlobal the given collection
	 * @return A random element from the given collection
	 */
	public static <T> T getRandom(Collection<T> toRandom) {
		if (toRandom.size() <= 0) {
			return null;
		}
		int i = ThreadLocalRandom.current().nextInt(toRandom.size());
		T temp;
		Iterator<T> iterator = toRandom.iterator();
		int x = 0;
		do {
			temp = iterator.next();
		}
		while (x++ < i);
		return temp;
	}

}
