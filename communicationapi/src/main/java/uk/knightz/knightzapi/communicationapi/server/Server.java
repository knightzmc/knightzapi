package uk.knightz.knightzapi.communicationapi.server;

import org.apache.http.auth.UsernamePasswordCredentials;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:29.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public interface Server {

    InetSocketAddress getAddress();

    void sendData(String data);

    String getPubKey();

    void auth(UsernamePasswordCredentials e);
}
