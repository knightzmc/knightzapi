package uk.knightz.knightzapi.module.defaultmodules;

import uk.knightz.knightzapi.module.IncomingRequest;
import uk.knightz.knightzapi.module.Module;
import uk.knightz.knightzapi.module.ModuleInfo;
import uk.knightz.knightzapi.module.ModuleManager;

import java.util.jar.JarFile;

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
