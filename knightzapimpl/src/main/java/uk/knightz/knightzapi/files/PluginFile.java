package uk.knightz.knightzapi.files;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:16.
 * I DO NOT OWN RIGHTS TO THIS CLASS. I have been unable to find the authors on Bukkit (where I found it), but if you made it, don't
 * hesitate to contact me at @Knightz#0986 on Discord.
 **/
public class PluginFile extends YamlConfiguration {

    private File file;
    private String defaults;
    private JavaPlugin plugin;
    private String directory;

    /**
     * Creates new PluginFile, without defaults
     *
     * @param plugin   - Your plugin
     * @param fileName - Name of the file
     */
    public PluginFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    /**
     * Creates new PluginFile, with defaults
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     */
    public PluginFile(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    /**
     * Creates a new PluginFile in a directory
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     * @param directory    - Directory name
     */
    public PluginFile(JavaPlugin plugin, String fileName, String defaultsName, String directory) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.directory = directory;
        this.file = new File(plugin.getDataFolder() + File.separator + directory, fileName);
        reload();
    }

    /**
     * For internal use only. Don't use, all it will do is override the current KnightzAPI config.yml file
     *
     * @param plugin
     */
    public PluginFile(JavaPlugin plugin) {
        this.plugin = plugin;
        this.defaults = "config.yml";
        this.file = new File(plugin.getDataFolder().getParent() + File.separator + "KnightzAPI" + File.separator + "config.yml");
        reload();
    }


    /**
     * For internal use only
     * Generates a yml file for a specific player.
     *
     * @param plugin
     * @param playerUUID
     */
    public PluginFile(JavaPlugin plugin, UUID playerUUID) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getParent() + File.separator + "KnightzAPI" + File.separator + "userdata" + File.separator + playerUUID + ".yml");
        reload();
    }

    /**
     * Reload configuration
     */
    public void reload() {

        if (!file.exists()) {

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while creating file " + file.getName());
            }

        }

        try {
            load(file);

            if (defaults != null) {
                InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                reader.close();
                save();
            }

        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        }

    }

    /**
     * Save configuration
     */
    public void save() {
        try {
            options().indent(2);
            save(file);

        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }

    }

}
