package uk.knightz.knightzapi.module;

import uk.knightz.knightzapi.KnightzWebAPI;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Set;

/**
 * This class was created by AlexL (Knightz) on 14/03/2018 at 22:11.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/

public class ModuleManager {
    private static ModuleManager manager;

    private final KnightzWebAPI api;
    private final ModuleLoader loader;
    private final Set<Module> allModules;

    private ModuleManager(KnightzWebAPI api) {
        manager = this;
        this.api = api;
        loader = ModuleLoader.initLoader(this, api);
        allModules = loader.loadModules();
    }

    public static ModuleManager getManager() {
        return manager;
    }

    public static ModuleManager init(KnightzWebAPI main) {
        if (manager == null) {
            return manager = new ModuleManager(main);
        }
        return manager;
    }

    public static Module forName(String name) {
        if (name == null) return null;
        ModuleManager inst = getManager();
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

    public void loadModuleClass(Class<? extends Module> clazz) {
        try {
            MethodHandle mh;
            mh = MethodHandles.lookup().findVirtual(ModuleLoader.class, "loadModuleClass", MethodType.methodType(Module.class, Class.class));
            mh.invokeExact(loader, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
