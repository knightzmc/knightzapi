package uk.knightz.knightzapi.communicationapi.server;

import uk.knightz.knightzapi.communicationapi.authorisation.NotAuthorisedException;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:30.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public abstract class ServerFactory {

    protected ServerFactory() {
    }

    public abstract Server getServer(InetSocketAddress address) throws NotAuthorisedException;
}
