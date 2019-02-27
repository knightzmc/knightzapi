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

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Utility class of various Chat related functions
 */
public class Chat {
    private Chat() {
    }

    /**
     * Apply color codes to a given String
     *
     * @param msg The String to colorize
     * @return The colorized String
     */
    public static String color(String msg) {
        if (msg == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Strip colors from a given String
     *
     * @param msg The colored String
     * @return The String with all colors removed
     */
    public static String stripColor(String msg) {
        return ChatColor.stripColor(msg);
    }

    /**
     * Color a list of Strings
     * If the given List is null, then an empty List is returned
     *
     * @param messages The list of Strings to colorize
     * @return The List of Strings with each element colorized.
     */
    public static List<String> color(List<String> messages) {
        if (messages == null) {
            return new ArrayList<>();
        }
        return messages.stream().map(Chat::color).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Attempt to center a String of text in the default Minecraft Chat Window.
     * This doesn't take changed settings into account as the client doesn't send this data
     *
     * @param text The text to center
     * @return An attempt at centering the text.
     */
    public static String center(String text) {
        int maxWidth = 80;
        int spaces = (int) Math.round(((double) maxWidth - 1.4 * (double) ChatColor.stripColor(text).length()) / 2.0);
        return StringUtils.repeat(" ", spaces) + text;
    }
}
