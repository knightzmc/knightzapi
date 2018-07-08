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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private Chat() {
    }

    public static String color(String msg) {
        if (msg == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String decolor(String msg) {
        return ChatColor.stripColor(msg);
    }

    public static void tell(Player p, String msg) {
        p.sendMessage(Chat.color(msg));
    }

    public static void tell(String msg, Player... ps) {
        for (Player p : ps) {
            p.sendMessage(Chat.color(msg));
        }
    }

    public static List<String> color(List<String> msgs) {
        if (msgs == null) {
            return new ArrayList<>();
        }
        ArrayList<String> newmsgs = new ArrayList<>();
        msgs.forEach(s -> newmsgs.add(Chat.color(s)));
        return newmsgs;
    }

    public static void broadcast(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Chat.color(msg));
        }
    }

    public static void broadcast(World world, String msg) {
        for (Player p : world.getPlayers()) {
            p.sendMessage(Chat.color(msg));
        }
    }

    public static String center(String text) {
        int maxWidth = 80;
        int spaces = (int) Math.round(((double) maxWidth - 1.4 * (double) ChatColor.stripColor(text).length()) / 2.0);
        return StringUtils.repeat(" ", spaces) + text;
    }
}
