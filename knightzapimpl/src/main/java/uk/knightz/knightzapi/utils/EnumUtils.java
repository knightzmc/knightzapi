package uk.knightz.knightzapi.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class was created by AlexL (Knightz) on 01/02/2018 at 20:43.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class EnumUtils {


    public static <T extends Enum<T>> T getRandom(Class<? extends T> enm, T... exclude) {
        T[] tEnum = enm.getEnumConstants();
        int size = tEnum.length;
        T t = tEnum[ThreadLocalRandom.current().nextInt(0, size - 1)];
        while (Arrays.asList(exclude).contains(t)) {
            t = tEnum[ThreadLocalRandom.current().nextInt(0, size - 1)];
        }
        return t;
    }

    public static <T extends Enum<T>> T[] getRandom(Class<? extends T> enm, int amount, T... exclude) {
        Set<T> list = new HashSet<>();
        int i = 0;
        while (list.size() > amount) {
            if (i > enm.getEnumConstants().length) {
                break;
            }
            list.add(getRandom(enm, exclude));
            i++;
        }
        return (T[]) list.toArray();
    }

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
