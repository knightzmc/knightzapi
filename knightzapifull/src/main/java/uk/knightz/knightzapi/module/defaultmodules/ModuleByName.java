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
