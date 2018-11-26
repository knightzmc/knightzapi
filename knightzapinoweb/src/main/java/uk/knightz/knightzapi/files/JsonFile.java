/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.files;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.knightz.knightzapi.KnightzAPI;

import java.io.*;
import java.util.UUID;

public class JsonFile {

    private File file;
    private String defaults;
    private JavaPlugin plugin;
    private String directory;
    @Getter
    @Setter
    private JsonElement parsed;

    /**
     * Creates new JsonFile, without defaults
     *
     * @param plugin   - Your plugin
     * @param fileName - Name of the file
     */
    public JsonFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    /**
     * Creates new JsonFile, with defaults
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     */
    public JsonFile(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    /**
     * Creates a new JsonFile in a directory
     *
     * @param plugin       - Your plugin
     * @param fileName     - Name of the file
     * @param defaultsName - Name of the defaults
     * @param directory    - Directory name
     */
    public JsonFile(JavaPlugin plugin, String fileName, String defaultsName, String directory) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.directory = directory;
        this.file = new File(plugin.getDataFolder() + File.separator + directory, fileName);
        reload();
    }


    /**
     * For internal use only
     * Generates a JSON file for a specific player.
     *
     * @param plugin     The KnightzAPI plugin
     * @param playerUUID The player's UUID
     */
    public JsonFile(JavaPlugin plugin, UUID playerUUID) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getParent() + File.separator + "KnightzAPI" + File.separator + "userdata" + File.separator + playerUUID + ".json");
        reload();
    }

    public boolean isEmpty() {
        return parsed == null;
    }

    /**
     * Reload configuration
     */
    public void reload() {
        load();
    }

    private void load() {
        try {
            if (defaults != null && !file.exists()) {
                InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                int read;
                while ((read = reader.read()) != -1) {
                    writer.write(read);
                }
                writer.flush();
                writer.close();
            } else if (!file.exists()) {
                if (file.getParentFile() != null)
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileReader reader = new FileReader(file);
            parsed = Streams.parse(new JsonReader(reader));
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());
        }
    }

    /**
     * Save configuration
     */
    public void save() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(KnightzAPI.GSON.toJson(parsed));
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }

    }

}
