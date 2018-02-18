package uk.knightz.knightzapi.communication.server;

import uk.knightz.knightzapi.communication.server.authorisaton.AuthMethod;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 11:32.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class ServerManager {

    private static ServerManager instance;
    private static Webserver webserver;

    private ServerManager() {
    }

    public static ServerManager getInstance() {
        if (instance == null) init();
        return instance;
    }

    public static ServerManager init() {
        return instance = new ServerManager();
    }

    public Webserver initServer(AuthMethod method) {
        if (webserver != null) {
            return webserver;
        }
        return Webserver.init(method);
    }
}
