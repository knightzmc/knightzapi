package uk.knightz.knightzapi.lang;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:13.
 * Copyright Knightz 2018 #else2018-2018 For assistance using this class, or for permission to use it in any way, contact
 *
 * @Knightz#0986 on Discord.
 **/
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
