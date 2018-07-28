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

import uk.knightz.knightzapi.KnightzWebAPI;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.jar.JarFile;

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
