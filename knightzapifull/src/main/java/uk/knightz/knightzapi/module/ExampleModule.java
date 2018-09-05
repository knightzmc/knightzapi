package uk.knightz.knightzapi.module;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uk.knightz.knightzapi.communication.json.JSONMessage;

import java.util.jar.JarFile;

@ModuleInfo(name = "Example", requestID = "broadcast") //Tell the system the name of the Module, and ID that should be
//passed in the request in order to call the module's onIncomingRequest method
public class ExampleModule extends Module {
    protected ExampleModule(String requestID, String name, JarFile file, ModuleInfo info) {
        super(requestID, name, file, info);
    }

    //Method called when an HTTP request is sent to this server, and the requestID matches the module's
    @Override
    public void onIncomingRequest(IncomingRequest incomingRequest) {
        incomingRequest.getId(); //Will always be equal to the requestID defined in the @ModuleInfo
        String message = incomingRequest.getData(); //the message to broadcast. In order to keep
        //text formatting correct, the module will typically provide some form of encapsulation so the
        //sent data is always correct.

        message = ChatColor.translateAlternateColorCodes('&', message); //Color it
        Bukkit.broadcastMessage(message); //Broadcast the message
        incomingRequest.getResponse() //Get the HTTP Response object that will be returned
                .body( //Set the HTML body
                        new JSONMessage(
                                200, //HTTP status code for "all ok"
                                "Message Broadcast").toJson()); //Easy JSON status messages
    }
}
