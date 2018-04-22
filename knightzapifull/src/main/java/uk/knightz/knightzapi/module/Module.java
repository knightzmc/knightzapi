package uk.knightz.knightzapi.module;


import java.util.jar.JarFile;

/**
 * This class was created by AlexL (Knightz) on 14/03/2018 at 22:01.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Represents a Module, designed to add extra functionality to Request objects.
 * These are able to be installed from a central database, and made yourself.
 * NOTE THAT MODULES ARE NOT BUKKIT PLUGINS!! The Bukkit API will be available, but you should not be making a plugin.yml, extending JavaPlugin, etc
 * <p>
 * TODO: Wiki page on creating modules
 **/
public abstract class Module implements IncomingRequestListener {

    /**
     * The id that a request should send in order for it to be interpreted by this module.
     * It should be overridden in an implementation of the constructor, as it is null by default, causing the module to never be called
     */
    private final String requestID;
    private final String name;
    private final JarFile file;
    private final ModuleInfo info;

    protected Module(String requestID, String name, JarFile file, ModuleInfo info) {
        this.requestID = requestID;
        this.name = name;
        this.file = file;
        this.info = info;
    }

    public static Module forName(String name) {
        return ModuleManager.forName(name);
    }

    public JarFile getFile() {
        return file;
    }

    public ModuleInfo getInfo() {
        return info;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getName() {
        return name;
    }


}
