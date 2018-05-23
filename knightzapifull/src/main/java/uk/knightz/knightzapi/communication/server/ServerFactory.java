package uk.knightz.knightzapi.communication.server;

import uk.knightz.knightzapi.communication.server.authorisation.NotAuthorisedException;

import java.net.InetSocketAddress;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 14:30.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * Creates a Server object. To avoid cyclic-dependencies, the only (current) implementation of this class is initialised in the KnightzWebAPI main class.
 **/
public interface ServerFactory {
	static ServerFactory getInstance() {
		return FactoryStorage.getInstance();
	}

	Server getServer(InetSocketAddress address) throws NotAuthorisedException;


	/**
	 * Stores the current ServerFactory instance
	 */
	class FactoryStorage {
		private static ServerFactory instance;

		public static ServerFactory getInstance() {
			return instance;
		}

		public static void setInstance(ServerFactory factory) {
			instance = factory;
		}
	}
}
