package uk.knightz.knightzapi.lang;

import uk.knightz.knightzapi.KnightzAPI;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This class was created by AlexL (Knightz) on 03/02/2018 at 21:03.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A utility class made for easily sending Debug messages, warnings, and normal messages.
 **/
public class Log {
    private static final Logger plugin = KnightzAPI.getP().getLogger();

    private Log() {
    }

    public static void normal(String string) {
        plugin.info(string);
    }

    /**
     * Send a debug message
     *
     * @param obj The message to send, by default Object#toString is called
     */
    public static void debug(Object obj) {
        if (KnightzAPI.getConfigFile().getBoolean("debug")) {
            plugin.info("[DEBUG] " + obj);
        }
    }

    /**
     * Send multiple debug messages
     *
     * @param objs The messages to send
     * @see Log#debug(Object)
     */
    public static void debug(Object[] objs) {
        Arrays.stream(objs).forEach(Log::debug);
    }

    /**
     * Wrapper for <code>KnightzAPI#getLogger#severe</code>
     *
     * @param string The message to log severely
     */
    public static void severe(String string) {
        plugin.severe(string);
    }

    /**
     * Wrapper for <code>KnightzAPI#getLogger#warning</code>
     *
     * @param string The message to log as a warning
     */
    public static void warn(String string) {
        plugin.warning(string);
    }
}
