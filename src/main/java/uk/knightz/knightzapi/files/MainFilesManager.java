package uk.knightz.knightzapi.files;

import uk.knightz.knightzapi.KnightzAPI;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:34.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MainFilesManager implements FilesManager {
    private static MainFilesManager instance;
    private final KnightzAPI api;

    public MainFilesManager(KnightzAPI api) {
        if (instance != null) {
            this.api = instance.api;
        } else {
            this.api = api;
        }
    }

    @Override
    public PluginFile createPluginFile(String fileName) {
        return new PluginFile(api.getP(), fileName);
    }

    @Override
    public PluginFile createPluginFile(String fileName, String defaultsName) {
        return new PluginFile(api.getP(), fileName, defaultsName);
    }

    @Override
    public PluginFile createPluginFile(String fileName, String defaultsName, String directory) {
        return new PluginFile(api.getP(), fileName, defaultsName, directory);
    }
}
