package uk.knightz.knightzapi.communication.server.authorisation;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.json.JSONMessage;
import uk.knightz.knightzapi.communication.rsa.RSA;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.security.GeneralSecurityException;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:44.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Provides a client with the Public RSA key ofGlobal this server, in order to sucessfully encrypt messages
 **/
public class ValidateModule extends WebModule {


    public ValidateModule() {
        super("Validate", "/validate", Verb.GET);
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            return new Gson().toJson
                    (new JSONMessage(200,
                            RSA.savePublicKey
                                    (Webserver.getInstance().getPair().getPublic())
                    ));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "Exception in decoding public key!";
    }
}
