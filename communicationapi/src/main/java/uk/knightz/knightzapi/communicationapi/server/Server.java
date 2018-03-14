package uk.knightz.knightzapi.communicationapi.server;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:29.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public interface Server {

    /**
     * Get the IP address of this server.
     *
     * @return The IP address of this server
     * @apiNote Will possibly return null in future updates if server aliases are added
     */
    InetSocketAddress getAddress();

    /**
     * Send data to this server. It will be automatically encrypted, and any neccessary authorisation will also be done
     *
     * @param data The data to send
     */
    void sendData(String data);

    /**
     * Get the public RSA used for this Server to encrypt data.
     *
     * @return The public key in plain text format
     */
    String getPubKey();
}
