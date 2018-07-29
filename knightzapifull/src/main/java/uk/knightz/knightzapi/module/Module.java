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


import java.util.jar.JarFile;

/**
 * A Module is a separate Jar file. It can be extended to add additional functionality to the Web API.
 * Every extension class must have a {@link ModuleInfo} annotation at the start of the class or it will not be loaded
 * See the GitHub Wiki for more details on Modules
 */
public abstract class Module implements IncomingRequestListener {

	/**
	 * The id that a request should send in order for it to be interpreted by this module.
	 * Along with {@link Module#name} it is loaded by the Module Loader from the values of {@link ModuleInfo}
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
