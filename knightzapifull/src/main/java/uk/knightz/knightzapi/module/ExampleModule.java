package uk.knightz.knightzapi.module;

import java.util.jar.JarFile;

@ModuleInfo(name = "Example", requestID = "broadcast") //Tell the system the name of the Module, and ID that should be
//passed in the request in order to call the module's onIncomingRequest method
public class ExampleModule extends Module {
	protected ExampleModule(String requestID, String name, JarFile file, ModuleInfo info) {
		super(requestID, name, file, info);
	}
	@Override
	public void onIncomingRequest(IncomingRequest incomingRequest) {
		incomingRequest.getId(); //Will always be equal to the requestID defined in the @ModuleInfo
		incomingRequest.getData()
	}
}
