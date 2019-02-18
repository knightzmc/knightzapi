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
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.knightz.knightzapi.event.Events;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.HelpBuilder;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.lang.fancy.FancyCommand;
import uk.knightz.knightzapi.menu.MenuListener;
import uk.knightz.knightzapi.menu.adapter.CollectionToMenuAdapter;
import uk.knightz.knightzapi.menuold.Menu;
import uk.knightz.knightzapi.serializers.InventorySerializer;
import uk.knightz.knightzapi.serializers.ItemStackJsonSerializer;
import uk.knightz.knightzapi.serializers.MenuSerializer;
import uk.knightz.knightzapi.user.User;
import uk.knightz.knightzapi.utils.VersionUtil;

import java.lang.reflect.Field;
import java.util.Collection;

public class KnightzAPI extends JavaPlugin implements Listener {
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(ItemStack.class, new ItemStackJsonSerializer())
            .registerTypeAdapter(Inventory.class, new InventorySerializer())
            .registerTypeAdapter(Menu.class, new MenuSerializer()).create();


    private static KnightzAPI plugin;
    private static FileConfiguration config;


    @Getter
    private Economy economy;
    @Getter
    private Chat chat;
    @Getter
    private Permission permission;

    public static FileConfiguration getConfigFile() {
        return config;
    }

    public static KnightzAPI getP() {
        if (plugin == null)
            throw new NullPointerException("KnightzAPI instance is null! A plugin has tried to reference it before it's been loaded in");
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this);
        VersionUtil.checkVersion();


        Events.init();
        MenuListener.init();
//        Listeners.registerOnce(new ObjectiveListener(), this);
        ConfigurationSerialization.registerClass(HelpBuilder.class);
        ConfigurationSerialization.registerClass(HelpBuilder.getHelpMessageClass());

        /*
        This method would register a Listener for every PlayerEvent class in the Classpath
        obviously taking a heavy performance toll. Instead, they are registered on-demand

        configureUserEventBlocker();
        */

        configureVault();
        registerFancyMessagesCommand();


        getCommand("test").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                new CollectionToMenuAdapter<Player>().adapt((Collection<Player>) Bukkit.getOnlinePlayers()).open((Player) sender);
            }
            return true;
        });
    }

    @Override
    public void onDisable() {
        User.saveData();
        plugin = null;
    }

    private void registerFancyMessagesCommand() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register("knightzapi", new FancyCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //========================
    //    VAULT SETUP
    //========================
    private void configureVault() {
        Log.normal("Configuring Vault integration. Warnings are common, and if you are sure you have compatible plugins installed then feel free to just ignore them.");
        try {
            try {
                setupEconomy();
            } catch (NullPointerException e) {
                Log.warn("No Economy plugin was found! This will likely cause problems with dependent plugins, so installing an economy plugin such as Essentials is recommended.");
            }
            try {
                setupChat();
            } catch (NullPointerException e) {
                Log.warn("No Chat plugin was found! This will likely cause problems with dependent plugins, so installing an chat plugin such as EssentialsChat is recommended.");
            }
            try {
                setupPermissions();
            } catch (NullPointerException e) {
                Log.warn("No Permissions plugin was found! This will likely cause problems with dependent plugins, so installing an Permissions plugin such as LuckPerms is recommended.");
            }
        } catch (NoClassDefFoundError e) {
            Log.severe("Vault was not found! KnightzAPI has been disabled as it requires it");
            Bukkit.getPluginManager().disablePlugin(this);
        }
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
