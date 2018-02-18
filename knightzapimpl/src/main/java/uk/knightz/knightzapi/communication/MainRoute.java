package uk.knightz.knightzapi.communication;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.knightz.knightzapi.communication.rsa.RSA;
import uk.knightz.knightzapi.communication.server.Webserver;

import java.util.Base64;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:13.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MainRoute implements Route {
    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response) {
        if (request.requestMethod().equals("POST")) {
            try {
                String message =
                        new String(
                                (RSA.decrypt(Base64.getDecoder().decode(request.queryParams("data").getBytes()),
                                        Base64.getDecoder().decode(request.queryParams("aes").getBytes()), Webserver.getInstance().getPair().getPrivate())));
                System.out.println("Message= " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
