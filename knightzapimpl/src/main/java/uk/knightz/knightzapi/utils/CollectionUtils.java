package uk.knightz.knightzapi.utils;

import java.util.*;
import java.util.function.Function;

/**
 * This class was created by AlexL (Knightz) on 03/02/2018 at 23:16.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class CollectionUtils {

    public static <Type, Return> boolean collectionContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
        for (Type type : collection) {
            if (function.apply(type).equals(check)) {
                return true;
            }
        }
        return false;
    }

    public static <Type, Return> Type getIfContains(Collection<Type> collection, Function<Type, Return> function, Return check) {
        return getIfContains(collection, function, check, false);
    }

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

    @SuppressWarnings("unchecked")
    public static <Type, Return> Collection<Return> applyToEach(Collection<Type> collection, Function<Type, Return> toExec) {
        if (collection instanceof HashSet) {
            HashSet set = new HashSet();
            collection.forEach(t -> set.add(toExec.apply(t)));
            return set;
        }
        Collection temp = collection;
        temp.clear();
        collection.forEach(t -> temp.add(toExec.apply(t)));
        return (Collection) temp;
    }

    public static <Type, Return> List<Return> changeListType(List<Type> list, Function<Type, Return> toExec) {
        List<Return> newList = new ArrayList<>();
        list.forEach(type -> newList.add(toExec.apply(type)));
        return newList;
    }

    public static <Type, Return> Set<Return> changeSetType(Set<Type> list, Function<Type, Return> toExec) {
        Set<Return> newList = new HashSet<>();
        list.forEach(type -> newList.add(toExec.apply(type)));
        return newList;
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        java.util.Collections.sort(list);
        return list;
    }

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
}
