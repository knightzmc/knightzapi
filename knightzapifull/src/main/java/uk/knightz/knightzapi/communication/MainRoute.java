package uk.knightz.knightzapi.communication;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.module.IncomingRequest;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:13.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class MainRoute implements Route {

    @Override
    public Object handle(Request request, Response response) {
        if (request.requestMethod().equals("POST")) {
            try {
                IncomingRequest ir = new IncomingRequest(request, response);
                System.out.println(ir.getId());
                Webserver.getInstance().callRequest(ir);
                return null;
            } catch (Exception e) {
                throw new RuntimeException("Error reading Incoming Request", e);
            }
        }
        return null;
    }
}
