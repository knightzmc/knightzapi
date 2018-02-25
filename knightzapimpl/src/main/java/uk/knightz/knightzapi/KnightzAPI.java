package uk.knightz.knightzapi;

import io.sentry.Sentry;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import spark.Spark;
import uk.knightz.knightzapi.commands.CommTestCommand;
import uk.knightz.knightzapi.communication.server.ServerManager;
import uk.knightz.knightzapi.communication.server.SimpleServer;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.communication.server.authorisaton.AuthMethod;
import uk.knightz.knightzapi.communicationapi.authorisation.NotAuthorisedException;
import uk.knightz.knightzapi.communicationapi.json.JSONMessage;
import uk.knightz.knightzapi.communicationapi.server.Server;
import uk.knightz.knightzapi.communicationapi.server.ServerFactory;
import uk.knightz.knightzapi.files.FilesManager;
import uk.knightz.knightzapi.files.MainFilesManager;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.user.User;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:26.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class KnightzAPI extends JavaPlugin {
    private static final Set<JavaPlugin> dependent = new HashSet<>();
    private static final ServerFactory factory = new ServerFactory() {
        @Override
        public Server getServer(InetSocketAddress address) throws NotAuthorisedException {
            return new SimpleServer(address);
        }
    };
    private static FileConfiguration config;
    private static KnightzAPI p;
    private static Webserver server;
    private static ServerManager manager;
    private static PluginFile webserverFile;
    private FilesManager filesManager;
    private Economy economy;
    private Chat chat;
    private Permission permission;

    public static ServerFactory getServerFactory() {
        return factory;
    }

    public static FileConfiguration getConfigFile() {
        return config;
    }

    public static KnightzAPI getP() {
        return p;
    }

    public static ServerFactory getFactory() {
        return factory;
    }

    public static Webserver getWebServer() {
        return server;
    }

    public static PluginFile getWebserverFile() {
        return webserverFile;
    }

    public static String renderFile(String fileName) {
        try {
            URL url = KnightzAPI.class.getResource(fileName);
            String answer = new String
                    (Files.readAllBytes
                            (Paths.get(
                                    url
                                            .toURI())),
                            Charset.defaultCharset());
            System.out.println(answer);
            return answer;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return new JSONMessage(403, "Internal Error Occured").toJson();
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
//        if (config.getBoolean("report")) {
//            Sentry.init("https://e09a0fbf45294cc0bde0d5d337f4477b:45ddd955e60b4a7b895aa8c23ac31f55@sentry.io/288131");
//            Thread.setDefaultUncaughtExceptionHandler((t, e) -> Sentry.capture(e));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            TestPrintStream ps = new TestPrintStream(baos);
//            System.setErr(ps);
//        }
        if (config.getBoolean("communication")) {
            webserverFile = new PluginFile(this, "webserver.yml", "webserver.yml");
            manager = ServerManager.getInstance();
            server = manager.initServer(AuthMethod.valueOf(webserverFile.getString("authtype")));
        }
        getCommand("test").setExecutor(new CommTestCommand());
    }

    @Override
    public void onDisable() {
        if (config.getBoolean("report")) {
            Sentry.close();
        }
        if (config.getBoolean("communication")) {
            Spark.stop();
        }
        User.saveData();
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

    private static class TestPrintStream extends PrintStream {

        public TestPrintStream(@NotNull OutputStream out) {
            super(out);
        }

        @Override
        public void println(Object o) {
            String s = String.valueOf(o);
            synchronized (this) {
                if (o instanceof Exception) {
                    boolean us = false;
                    for (StackTraceElement stackTraceElement : ((Throwable) o).getStackTrace()) {
                        if (stackTraceElement.getClassName().contains("knightzapi")) {
                            us = true;
                            break;
                        }
                    }
                    if (us) {
                        Sentry.capture((Throwable) o);
                        System.out.println("An error has occurred within KnightzAPI! It has been automatically logged!");
                    }
                    Log.debug(((Throwable) o).getStackTrace());
                }
                if (o instanceof StackTraceElement) {
                    System.out.println(o);
                }
            }
        }

        @Override
        public void println(String o) {
            String s = String.valueOf(o);
            synchronized (this) {
                System.out.println(o);
            }
        }
    }
}
