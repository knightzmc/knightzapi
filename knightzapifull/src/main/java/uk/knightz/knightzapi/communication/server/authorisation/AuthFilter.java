package uk.knightz.knightzapi.communication.server.authorisation;

import spark.Filter;
import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.server.Webserver;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 17:11.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class AuthFilter implements Filter {
    @Override
    public void handle(Request request, Response response) {
        try {
            Webserver.getInstance().getAuth()
                    .validate( request
                            , response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
