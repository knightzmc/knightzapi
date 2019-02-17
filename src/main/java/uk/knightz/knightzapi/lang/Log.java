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

package uk.knightz.knightzapi.lang;

import uk.knightz.knightzapi.KnightzAPI;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Utility class for logging KnightzAPI related things to the console.
 * As such, it likely won't be used by users much.
 */

public class Log {
    private static final Logger plugin = KnightzAPI.getP().getLogger();

    private Log() {
    }

    public static boolean debug() {
        return KnightzAPI.getConfigFile().getBoolean("debug", false);
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
