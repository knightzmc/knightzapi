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

package uk.knightz.knightzapi.module;

import com.google.common.collect.Sets;
import org.xeustechnologies.jcl.JarClassLoader;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.KnightzWebAPI;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * An async class in charge of loading Modules into the JVM
 */
class ModuleLoader extends Thread {


	/**
	 * Initialised by ModuleManager;
	 */
	private static ModuleLoader loader;
	private final ModuleManager manager;
	private final KnightzWebAPI knightzWebAPI;
	private final JarClassLoader jcl;
	private final Map<JarFile, Set<Class<? extends Module>>> modClassesForFile = new ConcurrentHashMap<>();
	private Set<Module> all;

	private ModuleLoader(ModuleManager manager, KnightzWebAPI m) {
		this.manager = manager;
		knightzWebAPI = m;
		jcl = new JarClassLoader();
	}

	static ModuleLoader initLoader(ModuleManager manager, KnightzWebAPI main) {
		if (loader == null) {
			return loader = new ModuleLoader(manager, main);
		}
		return loader;
	}

	public void run() {
		loadModules();
	}
	synchronized Set<Module> loadModules() {
		File modulesDir = new File(KnightzAPI.getP().getDataFolder(), "/modules");
		if (!modulesDir.exists()) modulesDir.mkdir();
		all = Sets.newHashSet();
		if (Objects.requireNonNull(modulesDir.listFiles()).length == 0) {
			return all;
		}
		try {
			load(new File(KnightzWebAPI.class.getProtectionDomain().getCodeSource().getLocation().getFile()));
		} catch (ModuleException e) {
			e.printStackTrace();
		}
		for (File file : Objects.requireNonNull(modulesDir.listFiles())) {
			try {
				load(file);
			} catch (ModuleException ex) {
				ex.printStackTrace();
			}
		}
		return all;
	}

	private List<String> getClassesInJar(File f) {
		try {
			List<String> classNames = new ArrayList<>();
			if (!f.exists()) f.createNewFile();
			ZipInputStream zip = new ZipInputStream(
					new FileInputStream(f));
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

	private synchronized Module loadModuleClass(JarFile f, Class<? extends Module> modClazz) throws ModuleException {
		Module module;
		try {
			Constructor constructor = modClazz.getDeclaredConstructor(String.class, String.class, JarFile.class, ModuleInfo.class);
			constructor.setAccessible(true);
			if (modClazz.isAnnotationPresent(ModuleInfo.class)) {
				ModuleInfo annotation = modClazz.getAnnotation(ModuleInfo.class);
				if (annotation.requestID().equals("")) {
					throw new ModuleException(modClazz.getName() + "'s ModuleInfo does not define a requestID!");
				}
				if (annotation.name().equals("Default Module Name")) {
					throw new ModuleException(modClazz.getName() + "'s ModuleInfo does not define a name for the Module!");
				}
				module = (Module) constructor.newInstance(annotation.requestID(), annotation.name(), f, annotation);
				all.add(module);
				Log.normal("Module " + module.getName() + " successfully loaded!");
			} else {
				throw new ModuleException(modClazz.getName() + " does not have a ModuleInfo annotation!");
			}
		} catch (ReflectiveOperationException ex) {
			if (Log.debug()) {
				ex.printStackTrace();
			}
			module = null;
		}
		return module;
	}


	private synchronized void load(File f) throws ModuleException {
		try {
			JarFile jf = new JarFile(f);
			jcl.add(new FileInputStream(f));
			List<String> classes = getClassesInJar(f);
			List<Class> clazzes = new ArrayList<>();
			if (classes == null) {
				return;
			}
			classes.forEach(n -> {
				try {
					clazzes.add(jcl.loadClass(n));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
			clazzes.sort(((o1, o2) -> {
				if (o1.equals(o2)) {
					return 0;
				}
				return Module.class.isAssignableFrom(o1) && o1 != Module.class ? -1 : 1;
			}));
			for (Class clazz : clazzes) {
				if (clazz == null) {
					Log.severe("Module " + f.getName() + " failed to be loaded into the server! Please contact @Knightz#0986 on Discord for assistance!");
					continue;
				}
				if (Module.class.isAssignableFrom(clazz)) {
					Class<? extends Module> modClazz = (Class<? extends Module>) clazz;

					//User might've shaded in API or other causes
					if (modClazz.equals(Module.class)) {
						continue;
					}
					Set<Class<? extends Module>> set = modClassesForFile.getOrDefault(jf, ConcurrentHashMap.newKeySet());
					set.add(clazz);
					modClassesForFile.put(jf, set);
					Module m = loadModuleClass(jf, modClazz);
					if (m == null) {
						System.out.println("Module with class name " + modClazz.getName() + " could not be loaded! Enable debug mode in KnightzAPI config to view stack-trace.");
					}
				}
				if (WebModule.class.isAssignableFrom(clazz)) {
					if (!(clazz).equals(WebModule.class)) {
						try {
							Constructor<? extends WebModule> c = ((Class<? extends WebModule>) clazz).getDeclaredConstructor();
							WebModule webModule = c.newInstance();
						} catch (NoSuchMethodException e) {
							Log.severe("Unable to load in WebModule " + clazz.getSimpleName() + " from Module " + f.getName() +
									" as it lacks a no-argument constructor. Contact its developer");
						} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			Log.severe("An error occurred loading in the a module from " + f.getName() + "Enable debug mode in KnightzAPI to view stacktrace.");
			if (Log.debug())
				e.printStackTrace();
		}
	}
}

