package uk.knightz.knightzapi;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.knightz.knightzapi.files.FilesManager;
import uk.knightz.knightzapi.files.MainFilesManager;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:26.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class KnightzAPI extends JavaPlugin {
    private static final Set<JavaPlugin> dependent = new HashSet<>();
    private static FileConfiguration config;
    private static KnightzAPI p;
    private FilesManager filesManager;
    private Economy economy;
    private Chat chat;
    private Permission permission;

    public static FileConfiguration getConfigFile() {
        return config;
    }

    public static KnightzAPI getP() {
        return p;
    }

    @Override
    public void onEnable() {
        p = this;
        initManagers();
        config = new PluginFile(p);
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getDescription().getDepend().contains("KnightzAPI") || plugin.getDescription().getSoftDepend().contains("KnightzAPI")) {
                dependent.add(JavaPlugin.getProvidingPlugin(plugin.getClass()));
            }
        }
        try {
            try {
                setupEconomy();
                setupChat();
                setupPermissions();
            } catch (NullPointerException e) {
                Log.warn("KnightzAPI - No Vault depending plugins (eg Chat, Economy) were found! This will likely cause problems, so installing an economy, chat, and permissions plugin is HIGHLY recommended!");
            }
        } catch (NoClassDefFoundError e) {
            Log.severe("KnightzAPI - Vault was not found! This will affect any plugins depending on it, so they have been disabled");
            for (JavaPlugin javaPlugin : dependent) {
                Log.debug("Disabled plugin " + javaPlugin.getName() + " due to no Vault found");
                Bukkit.getPluginManager().disablePlugin(javaPlugin);
            }
        }
    }

    @Override
    public void onDisable() {
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

    public FilesManager getFilesManager() {
        return filesManager;
    }

    private void initManagers() {
        filesManager = new MainFilesManager(this);
    }

    private boolean setupEconomy() {
        if (p.getServer().getPluginManager().getPlugin("Vault") == null) {
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
