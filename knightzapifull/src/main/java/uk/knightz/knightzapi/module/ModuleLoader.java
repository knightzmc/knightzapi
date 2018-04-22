package uk.knightz.knightzapi.module;

import com.google.common.collect.Sets;
import org.xeustechnologies.jcl.JarClassLoader;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.KnightzWebAPI;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
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

	@SuppressWarnings ("ConstantConditions")
	public synchronized Set<Module> loadModules() {
		File modulesDir = new File(KnightzAPI.getP().getDataFolder(), "/modules");
		if (!modulesDir.exists()) modulesDir.mkdir();
		all = Sets.newHashSet();
		if (modulesDir.listFiles().length == 0) {
			return all;
		}
		for (File file : modulesDir.listFiles()) {
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
				Log.normal("Module " + module.getName() + " sucessfully loaded!");
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


	private synchronized Module load(File f) throws ModuleException {
		try {
			JarFile jf = new JarFile(f);
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
			}
		} catch (IOException e) {
			System.out.println("An error occured loading in the a module from " + f.getName() + "Enable debug mode in KnightzAPI to view stacktrace.");
			if (Log.debug())
				e.printStackTrace();
		}
		return null;
	}
}

