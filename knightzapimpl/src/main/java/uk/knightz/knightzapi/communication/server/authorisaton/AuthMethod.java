package uk.knightz.knightzapi.communication.server.authorisaton;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.communicationapi.json.JSONMessage;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 13:42.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Represents different methods of authentication that a webserver should use.
 **/
public enum AuthMethod {
    /**
     * Least secure, anyone can send a request
     */
    NONE {
        @Override
        boolean validate(Request request, Response response) {
            return true;
        }
    },
    /**
     * Username and Password Authentication
     */
    AUTH {
        @Override
        boolean validate(Request request, Response response) {
            return false;
        }
    },

    /**
     * A whitelist of IP addresses that are allowed to send requests to this server
     */
    WHITELIST {
        @Override
        boolean validate(Request request, Response response) {
            try {
                if (Webserver.getInstance().getWhitelist().contains((Inet4Address) Inet4Address.getByName(request.ip()))) {
                    return true;
                } else {
                    response.status(401);
                    response.body(new Gson().toJson(new JSONMessage(401, "Your IP is not whitelisted!")));
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return false;
        }
    },

    /**
     * Username and Password, but also with a Whitelist that doesn't require login details
     */
    WHITEAUTH {
        @Override
        boolean validate(Request request, Response response) {
            return false;
        }
    };

    abstract boolean validate(Request request, Response response);
}
