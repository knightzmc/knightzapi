package uk.knightz.knightzapi.utils;

import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class was created by AlexL (Knightz) on 03/02/2018 at 23:16.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Various utility methods for Collections
 **/
public class CollectionUtils {

    public static <I> boolean contentsMatches(Collection<I> collection, Predicate<I> check) {
        for (I i : collection) {
            if (!check.test(i)) return false;
        }
        return true;
    }

    public static <I> boolean contentsMatches(I[] arr, Predicate<I> check) {
        for (I i : arr) {
            if (!check.test(i)) return false;
        }
        return true;
    }

    /**
     * Evaluates if the given Collection contains any elements, which after having the given function run on them,
     * are equal to the given check parameter
     */
    public static <Type, Return> boolean collectionContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
        return collection.stream().anyMatch(type -> function.apply(type).equals(check));
    }

    /**
     * Get the first element in the given Collection which, after having the given function run on it, is equal to the given check parameter
     *
     * @param <Return> if it is an instanceof String, String#equals will be checked, rather than String#equalsIgnoreCase
     * @see CollectionUtils#getIfContains(Collection, Function, Object, boolean)
     */
    public static <Type, Return> Type getIfContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
        return getIfContains(collection, function, check, false);
    }

    /**
     * Get the first element in the given Collection which, after having the given function run on it, is equal to the given check parameter
     *
     * @param stringIgnoreCase if <Return> is an instanceof String, then String#equalsIgnoreCase will be run over String#equals depending on this parameter
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

    /**
     * Essentially just a wrapper for Stream#map, implemented messier because I forgot Stream#map exists
     *
     * @deprecated Use Stream#map, this is buggy and likely will not be used. Kept for old version compatibility
     */
    @Deprecated
    public static <Type, Return> Collection<Return> applyToEach(Collection<Type> collection, Function<Type, Return> toExec) {
        if (collection instanceof HashSet) {
            HashSet set = new HashSet();
            collection.forEach(t -> set.add(toExec.apply(t)));
            return set;
        }
        //NOT UNNECESSARY - REQUIRED TO ENSURE THE ORIGINAL COLLECTION IS NOT CLEARED
        //noinspection UnnecessaryLocalVariable
        Collection temp = collection;
        temp.clear();
        collection.forEach(t -> temp.add(toExec.apply(t)));
        return (Collection) temp;
    }

    /**
     * Sort the given Collection through Collections#sort
     *
     * @return The sorted Collection as a List
     */
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        Collections.sort(list);
        return list;
    }

    /**
     * Get an Iterable ofGlobal the given Enumeration
     *
     * @return An Iterable ofGlobal the given Enumeration
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

    public static <T> T[] mapToArray(Map<Integer, T> map) {
        Validate.notNull(map, "Map cannot be null");
        Set<Map.Entry<Integer, T>> entries = map.entrySet();
        entries = entries.stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).collect(Collectors.toSet());
        T[] arr = (T[]) new Object[entries.size()];
        entries.forEach(e -> arr[e.getKey()] = e.getValue());
        return arr;
    }
}
