package uk.knightz.knightzapi.communicationapi.module;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private String name = "Default Module Name";

    {
        //Using reflection to avoid cyclic-dependencies
        try {
            Class<?> webClass = Class.forName("uk.knightz.knightzapi.communication.server.Webserver");
            Method getInstance = webClass.getMethod("getInstance");
            Object webserver = getInstance.invoke(null);
            Method register = webClass.getMethod("registerModule", Module.class);
            register.invoke(webserver, this);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ModuleException(e);
        }
    }

    /**
     * Override requestID in this to your desired ID
     */
    public Module() throws ModuleException {
        requestID = null;
    }


    public static Module forName(String name) {
        try {
            Class manager = Class.forName("uk.knightz.knightzapi.communication.module.ModuleManager");
            Method forName = manager.getMethod("forName", String.class);
            return (Module) forName.invoke(null, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getName() {
        return name;
    }

    protected void setName(String s) {
        this.name = s;
    }

}
