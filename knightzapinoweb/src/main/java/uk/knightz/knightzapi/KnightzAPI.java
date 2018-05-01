package uk.knightz.knightzapi;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.knightz.knightzapi.event.PlayerBlockMoveEvent;
import uk.knightz.knightzapi.files.FilesManager;
import uk.knightz.knightzapi.files.MainFilesManager;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.HelpBuilder;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.user.User;
import uk.knightz.knightzapi.utils.VersionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:26.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class KnightzAPI extends JavaPlugin {
	/**
	 * Any plugins dependent on KnightzAPI - not completely accurate as goes by plugin.yml dependencies
	 */
	private static final Set<JavaPlugin> dependent = new HashSet<>();
	private static FileConfiguration config;
	private static KnightzAPI p;
	private boolean webAPIEnabled;
	private FilesManager filesManager;
	//Vault
	private Economy economy;
	private Chat chat;
	private Permission permission;

	public KnightzAPI() {
		super();
	}


	public static FileConfiguration getConfigFile() {
		return config;
	}

	public static KnightzAPI getP() {
		return p;
	}

	@Override

	public void onEnable() {
		p = this;
		VersionUtil.checkVersion();
		initManagers();
		config = new PluginFile(p);
		PlayerBlockMoveEvent.init();
		ConfigurationSerialization.registerClass(HelpBuilder.class);
		ConfigurationSerialization.registerClass(HelpBuilder.getHelpBuilderClass());
		webAPIEnabled = config.getBoolean("communication");
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin.getDescription().getDepend().contains("KnightzAPI") || plugin.getDescription().getSoftDepend().contains("KnightzAPI")) {
				dependent.add(JavaPlugin.getProvidingPlugin(plugin.getClass()));
			}
		}
		if (webAPIEnabled)
			try {
				Class webAPI = Class.forName("uk.knightz.knightzapi.KnightzWebAPI");
				Method init = webAPI.getDeclaredMethod("init", boolean.class);
				System.out.println(webAPIEnabled);
				init.invoke(null, webAPIEnabled);
				new BukkitCommandManager(KnightzAPI.getP()).registerCommand((BaseCommand) Class.forName("uk.knightz.knightzapi.Test").getConstructor().newInstance());
			} catch (ClassNotFoundException e) {
				Log.severe("You are trying to load in the communication API without the right file! Make sure you're using knightzapifull-*.jar+\n Disabling plugin...");
				Bukkit.getPluginManager().disablePlugin(this);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
				Log.severe("An error occured loading in the communication API! Enable debug mode in the config to view the stack trace.");
				if (Log.debug()) e.printStackTrace();
			}
		try {
			try {
				setupEconomy();
				setupChat();
				setupPermissions();
			} catch (NullPointerException e) {
				Log.warn("No Vault depending plugins (eg Chat, Economy) were found! This will likely cause problems, so installing an economy, chat, and permissions plugin is HIGHLY recommended!");
			}
		} catch (NoClassDefFoundError e) {
			Log.severe("Vault was not found! This will affect any plugins depending on it, so they have been disabled");
			for (JavaPlugin javaPlugin : dependent) {
				Log.debug("Disabled plugin " + javaPlugin.getName() + " due to no Vault found");
				Bukkit.getPluginManager().disablePlugin(javaPlugin);
			}
		}
	}

	@Override
	public void onDisable() {
		User.saveData();
		if (webAPIEnabled) try {
			Class webAPI = Class.forName("uk.knightz.knightzapi.KnightzWebAPI");
			Method init = webAPI.getDeclaredMethod("onDisable");
			init.invoke(null);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			Log.severe("THIS SHOULD NOT HAPPEN!");
			if (Log.debug()) e.printStackTrace();
		}
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
