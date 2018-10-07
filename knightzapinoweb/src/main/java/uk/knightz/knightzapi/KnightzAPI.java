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

package uk.knightz.knightzapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import uk.knightz.knightzapi.event.PlayerBlockMoveEvent;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.HelpBuilder;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.menu.Menu;
import uk.knightz.knightzapi.serializers.InventorySerializer;
import uk.knightz.knightzapi.serializers.ItemStackJsonSerializer;
import uk.knightz.knightzapi.serializers.MenuSerializer;
import uk.knightz.knightzapi.user.User;
import uk.knightz.knightzapi.user.UserEventBlocker;
import uk.knightz.knightzapi.utils.VersionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class KnightzAPI extends JavaPlugin implements Listener {
    public static final Gson gson = new GsonBuilder().disableHtmlEscaping()
            .setPrettyPrinting().registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer())
            .registerTypeAdapter(Inventory.class, new InventorySerializer())
            .registerTypeAdapter(Menu.class, new MenuSerializer()).create();
    /**
     * Any plugins dependent on KnightzAPI - not completely accurate as goes by plugin.yml dependencies
     */
    private static final Set<JavaPlugin> dependent = new HashSet<>();
    private static KnightzAPI plugin;
    private static FileConfiguration config;
    private boolean webAPIEnabled;
    //Vault
    private Economy economy;
    private Chat chat;
    private Permission permission;

    public static FileConfiguration getConfigFile() {
        return config;
    }

    public static KnightzAPI getP() {
        return plugin;
    }

    @Override

    public void onEnable() {
        VersionUtil.checkVersion();

        UserEventBlocker userEventBlocker = new UserEventBlocker();
        EventExecutor ev = (listener, event) -> userEventBlocker.event(event);
        for (Class<? extends PlayerEvent> e : new Reflections("org.bukkit.event").getSubTypesOf(PlayerEvent.class)) {
            if (!e.getName().equals(PlayerEvent.class.getName())) {
                try {
                    Bukkit.getPluginManager().registerEvent(e, userEventBlocker, EventPriority.NORMAL,
                            ev,
                            this, true);
                } catch (Exception ex) {
                    System.out.println(e.getName());
                }
            }
        }

        config = new PluginFile(this);
        PlayerBlockMoveEvent.init();
        Bukkit.getPluginManager().registerEvents(this, this);
        ConfigurationSerialization.registerClass(HelpBuilder.class);
        ConfigurationSerialization.registerClass(HelpBuilder.getHelpMessageClass());
        webAPIEnabled = config.getBoolean("communication");
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains("KnightzAPI") || plugin.getDescription().getSoftDepend().contains("KnightzAPI")) {
                dependent.add(JavaPlugin.getProvidingPlugin(plugin.getClass()));
            }
        }
        if (webAPIEnabled)
            try {
                Class webAPI = Class.forName("uk.knightz.knightzapi.KnightzWebAPI");
                Method init = webAPI.getDeclaredMethod("init");
                init.invoke(null);

            } catch (ClassNotFoundException e) {
                Log.severe("You are trying to load in the communication API without the right file! Make sure you're using knightzapifull-*.jar\n Disabling plugin...");
                if (Log.debug()) {
                    e.printStackTrace();
                }
                Bukkit.getPluginManager().disablePlugin(this);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Log.severe("An error occured loading in the communication API! Enable debug mode in the config to view the stack trace.");
                if (Log.debug()) e.printStackTrace();
            }
        try {
            try {
                setupEconomy();
            } catch (NullPointerException e) {
                Log.warn("No Vault Economy plugin was found! This will likely cause problems with dependent plugins, so installing an economy plugin (such as Essentials) is highly recommended.");
            }
            try {
                setupChat();
            } catch (NullPointerException e) {
                Log.warn("No Vault Chat plugin was found! This will likely cause problems with dependent plugins, so installing an chat plugin (such as EssentialsChat) is highly recommended.");
            }
            try {
                setupPermissions();
            } catch (NullPointerException e) {
                Log.warn("No Vault Permissions plugin was found! This will likely cause problems with dependent plugins, so installing an Permissions plugin (such as LuckPerms) is highly recommended.");
            }
        } catch (NoClassDefFoundError e) {
            Log.severe("Vault was not found! This will affect any plugins depending on it, so they have been disabled");
            for (JavaPlugin javaPlugin : dependent) {
                Log.debug("Disabled plugin " + javaPlugin.getName() + " due to no Vault found");
                Bukkit.getPluginManager().disablePlugin(javaPlugin);
            }
        }
        plugin = this;
    }

    @Override
    public void onDisable() {
        try {
            User.saveData();
        } catch (NoClassDefFoundError e) {
            //TODO FIX
        }
        if (webAPIEnabled) try {
            Class webAPI = Class.forName("uk.knightz.knightzapi.KnightzWebAPI");
            Method init = webAPI.getDeclaredMethod("onDisable");
            init.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.severe("THIS SHOULD NOT HAPPEN!");
            if (Log.debug()) e.printStackTrace();
        }
        plugin = null;
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        User.saveData();
    }

    public Economy getEconomy() {
        return economy;
    }

    public Chat getChat() {
        return chat;
    }

    public Permission getPermission() {
        return permission;
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }
}
