package uk.knightz.knightzapi.communication.module;

import com.google.common.collect.Sets;
import org.xeustechnologies.jcl.JarClassLoader;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class was created by AlexL (Knightz) on 14/03/2018 at 22:02.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Resonsible for loading in all modules on server load (or when manually queried)
 **/
class ModuleLoader extends Thread {


    /**
     * Initialised by ModuleManager;
     */
    private static ModuleLoader loader;
    private final ModuleManager manager;
    private final KnightzAPI knightzAPI;
    private final JarClassLoader jcl;
    private Set<Module> all;

    private ModuleLoader(ModuleManager manager, KnightzAPI m) {
        this.manager = manager;
        knightzAPI = m;
        jcl = new JarClassLoader();
        start();
    }

    static ModuleLoader initLoader(ModuleManager manager, KnightzAPI main) {
        if (loader == null) {
            return loader = new ModuleLoader(manager, main);
        }
        return loader;
    }

    public void run() {
        loadModules();
    }

    @SuppressWarnings("ConstantConditions")
    public synchronized Set<Module> loadModules() {
        File modulesDir = new File(knightzAPI.getDataFolder(), "/modules");
        if (modulesDir.listFiles() == null || modulesDir.listFiles().length == 0) {
            return Sets.newHashSet();
        }
        all = new HashSet<>();
        Arrays.stream(modulesDir.listFiles()).forEach(this::load);
        return all;
    }

    private List<String> getClassesInJar(File f) {
        try {
            List<String> classNames = new ArrayList<>();
            ZipInputStream zip = new ZipInputStream(new FileInputStream(f.getName()));
            ZipEntry entry = zip.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.');
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
                entry = zip.getNextEntry();
            }
            return classNames;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private synchronized Module load(File f) {
        try {
            jcl.add(new FileInputStream(f));
            List<String> classes = getClassesInJar(f);
            List<Class> clazzes = new ArrayList<>();
            if (classes == null) {
                return null;
            }
            classes.forEach(n -> {
                try {
                    clazzes.add(jcl.loadClass(n));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            clazzes.sort(((o1, o2) -> Module.class.isAssignableFrom(o1) && o1 != Module.class ? -1 : 1));
            boolean contains = false;
            Module module;
            for (Class clazz : clazzes) {
                if (clazz == null) {
                    Log.severe("Module " + f.getName() + " failed to be loaded into the server! Please contact @Knightz#0986 on discord for assistance!");
                    continue;
                }
                if (Module.class.isAssignableFrom(clazz)) {
                    if (contains) {
                        throw new ModuleException("Only one class should extend Module in module " + f.getName());
                    }
                    Class<? extends Module> modClazz = (Class<? extends Module>) clazz;
                    if (modClazz.equals(Module.class)) {
                        continue;
                    }
                    Constructor constructor = modClazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    module = (Module) constructor.newInstance();
                    System.out.println("Module " + module.getName() + " sucessfully loaded!");
                    ModuleManager.getManager().addModule(module);
                    contains = true;
                }
            }
        } catch (IOException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

