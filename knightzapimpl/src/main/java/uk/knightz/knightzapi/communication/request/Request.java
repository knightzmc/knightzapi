package uk.knightz.knightzapi.communication.request;

import uk.knightz.knightzapi.communicationapi.server.Server;

/**
 * This class was created by AlexL (Knightz) on 16/02/2018 at 13:16.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class Request {
    private final RequestAction action;

    public Request(RequestAction action) {
        this.action = action;
    }

    public void sendToServer(Server server) {
    }
}
