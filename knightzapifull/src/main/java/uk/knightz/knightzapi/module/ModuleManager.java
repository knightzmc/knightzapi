/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.module;

import uk.knightz.knightzapi.KnightzWebAPI;

import java.util.Set;

/**
 * Handles all Modules
 */
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

	/**
	 * @param name The name of the Module
	 * @return a Module matching the given name, or null if none matches
	 */
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

//	public void loadModuleClass(Class<? extends Module> clazz) {
//		try {
//			Method load = ModuleLoader.class.getDeclaredMethod("loadModuleClass", JarFile.class, Class.class);
//
//			mh = MethodHandles.lookup().findVirtual(ModuleLoader.class, "loadModuleClass", MethodType.methodType(Module.class, JarFile.class, Class.class));
//			mh.invokeExact(loader, clazz);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
}
