package uk.knightz.knightzapi.lang;

import org.bukkit.Bukkit;
import uk.knightz.knightzapi.KnightzAPI;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class was created by AlexL (Knightz) on 03/02/2018 at 21:03.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Log {
    private static final Logger bukkit = Bukkit.getLogger();

    private Log() {
    }

    public static void normal(String string) {
        bukkit.info(string);
    }

    public static void debug(Object obj) {
        if (KnightzAPI.getConfigFile().getBoolean("debug")) {
            bukkit.info("[DEBUG] " + obj);
        }
    }

    public static void debug(Object[] objs) {
        if (KnightzAPI.getConfigFile().getBoolean("debug")) {
            bukkit.info("[DEBUG]");
            Arrays.stream(objs).map(Object::toString).forEach(bukkit::info);
        }
    }

    public static void severe(String string) {
        bukkit.severe(string);
    }

    public static void warn(String string) {
        bukkit.warning(string);
    }
}
