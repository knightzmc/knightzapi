package uk.knightz.knightzapi.files;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:32.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public interface FilesManager {

    PluginFile createPluginFile(String fileName);

    PluginFile createPluginFile(String fileName, String defaultsName);

    PluginFile createPluginFile(String fileName, String defaultsName, String directory);


}
