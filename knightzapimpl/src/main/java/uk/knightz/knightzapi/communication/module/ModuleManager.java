package uk.knightz.knightzapi.communication.module;

import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.communicationapi.module.Module;

import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 14/03/2018 at 22:11.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/

public class ModuleManager {
    private static ModuleManager manager;

    private final KnightzAPI api;
    private final ModuleLoader loader;
    private final Set<Module> allModules;

    private ModuleManager(KnightzAPI api) {
        this.api = api;
        loader = ModuleLoader.initLoader(this, api);
        allModules = loader.loadModules();
    }

    public static ModuleManager getManager() {
        return manager;
    }

    public static ModuleManager init(KnightzAPI main) {
        if (manager == null) {
            return manager = new ModuleManager(main);
        }
        return manager;
    }

    public static Module forName(String name) {
        if (name == null) return null;
        ModuleManager inst =
                getManager();
        if (inst == null) return null;
        return inst.allModules.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
    }

    public Set<Module> getAllModules() {
        return allModules;
    }

    public ModuleLoader getLoader() {
        return loader;
    }

    public void addModule(Module m) {
        allModules.add(m);
    }
}
