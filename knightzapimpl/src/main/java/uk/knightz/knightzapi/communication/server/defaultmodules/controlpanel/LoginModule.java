package uk.knightz.knightzapi.communication.server.defaultmodules.controlpanel;

import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.WebModule;

/**
 * This class was created by AlexL (Knightz) on 22/02/2018 at 21:52.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class LoginModule extends WebModule {
    public LoginModule() {
        super("login", "cp/login", Verb.GET);
    }

    @Override
    public Object handle(Request request, Response response) {
        return "Hi!";
    }
}
