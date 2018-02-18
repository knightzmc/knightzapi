package uk.knightz.knightzapi.lang;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:12.
 * Copyright Knightz  2018 For assistance using this class, or for permission to use it in any way, contact
 *
 * @Knightz#0986 on Discord.
 **/
public class HelpBuilder {
    private final List<HelpMessage> messages = new ArrayList<>();
    private String title = "";
    private ChatColor primary = ChatColor.WHITE;
    private ChatColor secondary = ChatColor.WHITE;

    public HelpBuilder setTitle(String title) {
        this.title = Chat.color(title);
        return this;
    }

    public HelpBuilder setSecondary(ChatColor secondary) {
        this.secondary = secondary;
        return this;
    }

    public HelpBuilder setPrimary(ChatColor primary) {
        this.primary = primary;
        return this;
    }

    public HelpBuilder addHelpMessage(String main, String description) {
        this.messages.add(new HelpMessage(Chat.color(main), Chat.color(description)));
        return this;
    }

    public HelpBuilder addPermissionDependentHelpMessage(String name, String description, String permission) {
        this.messages.add(new HelpMessage(Chat.color(name), Chat.color(description), permission));
        return this;
    }

    public String[] build() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(Chat.center(this.primary + "" + ChatColor.BOLD + this.title));
        temp.add(this.primary + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        this.messages.forEach(s -> temp.add(this.primary + s.getMain() + " \u00a77-\u00a7r " + this.secondary + s.getDescription())
        );
        return temp.toArray(new String[temp.size()]);
    }

    public void sendToPlayer(CommandSender... toSendTo) {
        for (CommandSender p : toSendTo) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(Chat.center(this.primary + "" + ChatColor.BOLD + this.title));
            temp.add(this.primary + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
            this.messages.forEach(s -> {
                        if (s.hasPermission()) {
                            if (p.hasPermission(s.getPermission())) {
                                temp.add(this.primary + s.getMain() + " §7-§r " + this.secondary + s.getDescription());
                            }
                        } else {
                            temp.add(this.primary + s.getMain() + " §7-§r " + this.secondary + s.getDescription());
                        }
                    }
            );
            p.sendMessage(temp.toArray(new String[temp.size()]));
        }
    }

    public static class HelpMessage {
        private final String main;
        private final String description;
        private String permission;

        HelpMessage(String mainString, String description) {
            this.main = mainString;
            this.description = description;
        }

        HelpMessage(String mainString, String description, String permission) {
            this.main = mainString;
            this.description = description;
            this.permission = permission;
        }

        String getDescription() {
            return this.description;
        }

        String getMain() {
            return this.main;
        }

        boolean hasPermission() {
            return this.permission != null;
        }

        String getPermission() {
            return this.permission;
        }
    }

}

