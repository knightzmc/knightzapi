package uk.knightz.knightzapi.communication.server.authorisation;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.communication.json.JSONMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 13:42.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Represents different methods ofGlobal authentication that a webserver should use.
 **/
public enum AuthMethod {
    /**
     * Least secure, anyone can send a request
     */
    NONE {
        @Override
        void validate(Request request, Response response) {
        }
    },
    /**
     * Username and Password Authentication
     */
    AUTH {
        @Override
        void validate(Request request, Response response) {
        }
    },

    /**
     * A whitelist ofGlobal IP addresses that are allowed to send requests to this server
     */
    WHITELIST {
        @Override
        void validate(Request request, Response response) {
            try {
                if (!Webserver.getInstance().getWhitelist().contains(InetAddress.getByName(request.ip()))) {
                    Spark.halt(401, new Gson().toJson(new JSONMessage(401, "Your IP is not whitelisted!")));
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    },

    /**
     * Username and Password, but also with a Whitelist that doesn't require login details
     */
    WHITEAUTH {
        @Override
        void validate(Request request, Response response) {
        }
    };

    abstract void validate(Request request, Response response);
}
