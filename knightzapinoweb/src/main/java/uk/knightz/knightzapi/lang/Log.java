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
