package uk.knightz.knightzapi.utils;

import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:09.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class NotEmptyArrayList<T> extends ArrayList<T> {

    public NotEmptyArrayList(T initial) {
        this.ensureCapacity(1);
        Validate.notNull(initial);
        add(initial);
    }

    @SafeVarargs
    public NotEmptyArrayList(T... initial) {
        super(Arrays.stream(initial).collect(Collectors.toList()));
    }

    @SafeVarargs
    public static <T> NotEmptyArrayList<T> asNotEmptyArrayList(T... args) {
        return new NotEmptyArrayList<>(args);
    }

    public T getFirst() {
        return get(0);
    }
}
