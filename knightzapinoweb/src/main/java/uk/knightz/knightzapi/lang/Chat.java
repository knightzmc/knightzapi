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
	public static String decolor(String msg) {
		return ChatColor.stripColor(msg);
	}

	/**
	 * Color a list of Strings
	 * If the given List is null, then an empty List is returned
	 *
	 * @param msgs The list of Strings to colorize
	 * @return The List of Strings with each element colorized.
	 */
	public static List<String> color(List<String> msgs) {
		if (msgs == null) {
			return new ArrayList<>();
		}
		return msgs.stream().map(Chat::color).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Attempt to center a String of text in the default Minecraft Chat Window.
	 * Obviously, this doesn't take changed settings into account.
	 * @param text The text to center
	 * @return An attempt at centering the text.
	 */
	public static String center(String text) {
		int maxWidth = 80;
		int spaces = (int) Math.round(((double) maxWidth - 1.4 * (double) ChatColor.stripColor(text).length()) / 2.0);
		return StringUtils.repeat(" ", spaces) + text;
	}
}
