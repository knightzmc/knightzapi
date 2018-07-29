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

package uk.knightz.knightzapi.module.defaultmodules;

import uk.knightz.knightzapi.module.IncomingRequest;
import uk.knightz.knightzapi.module.Module;
import uk.knightz.knightzapi.module.ModuleInfo;
import uk.knightz.knightzapi.module.ModuleManager;

import java.util.jar.JarFile;

/**
 * A default module that handles an incoming request that calls a Module by its name, not its request id.
 */
@ModuleInfo(name = "ModuleByName", requestID = "modulebyname")
public final class ModuleByName extends Module {
	protected ModuleByName(String requestID, String name, JarFile file, ModuleInfo info) {
		super(requestID, name, file, info);
	}

	@Override
	public void onIncomingRequest(IncomingRequest incomingRequest) {
		Module module = ModuleManager.forName(incomingRequest.getData());
		if (module == null) {
			return;
		}
		module.onIncomingRequest(incomingRequest);
	}
}
