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
 * Represents a .yml file stored in your plugins/[yourplugin] directory.
 * A clean and efficient way of handling many .yml files.
 */
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
     * @param plugin The KnightzAPI plugin
     */
    public PluginFile(JavaPlugin plugin) {
        this.plugin = plugin;
        this.defaults = "config.yml";
        this.file = new File(plugin.getDataFolder().getParent() + File.separator + plugin.getName() + File.separator + "config.yml");
        reload();
    }


    /**
     * For internal use only
     * Generates a yml file for a specific player.
     *
     * @param plugin     The KnightzAPI plugin
     * @param playerUUID The player's UUID
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
