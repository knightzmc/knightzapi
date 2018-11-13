package uk.knightz.knightzapi.utils;

import org.bukkit.ChatColor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.bukkit.ChatColor.*;

public class ColorUtils {

    /**
     * Array of ChatColors that are deemed unfriendly for typical user use
     */
    private static final ChatColor[] unfriendly = {BLACK, MAGIC, BOLD, ITALIC, UNDERLINE, STRIKETHROUGH, RESET};
    private static final Map<Class, ChatColor> colorsOfClasses = new ConcurrentHashMap<>();

    public static ChatColor random() {
        return random(true);
    }

    public static ChatColor random(boolean friendly) {
        return EnumUtils.getRandom(ChatColor.class, friendly ? unfriendly : new ChatColor[1]);
    }

    public static ChatColor colorOfClass(Class clazz) {
        colorsOfClasses.putIfAbsent(clazz, EnumUtils.getRandom(ChatColor.class,
                ChatColor.BOLD,
                ChatColor.BLACK,
                ChatColor.MAGIC,
                ChatColor.ITALIC,
                ChatColor.UNDERLINE,
                ChatColor.STRIKETHROUGH
        ));
        return colorsOfClasses.get(clazz);
    }
}
