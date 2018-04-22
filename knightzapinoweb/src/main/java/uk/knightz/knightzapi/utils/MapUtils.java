package uk.knightz.knightzapi.utils;

import java.util.Map;

/**
 * This class was created by AlexL (Knightz) on 31/01/2018 at 21:04.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MapUtils {
    private MapUtils() {
    }

    public static <K, V> K firstKeyOf(Map<K, V> map, V value) {
        for (K k : map.keySet()) {
            V v = map.getOrDefault(k, null);
            if (v != null && v.equals(value)) {
                return k;
            }
        }
        return null;
    }
}
