package uk.knightz.knightzapi.communicationapi.server;

import uk.knightz.knightzapi.communicationapi.authorisation.NotAuthorisedException;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:30.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Creates a Server object. To avoid cyclic-depencencies, the only (current) implementation of this class is initialised in the KnightzAPI main class.
 *
 * @see uk.knightz.knightzapi.knightzapiimpl.KnightzAPI
 **/
public interface ServerFactory {

    Server getServer(InetSocketAddress address) throws NotAuthorisedException;
}
