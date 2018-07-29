/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.communication.server;

import uk.knightz.knightzapi.communication.server.authorisation.NotAuthorisedException;

import java.net.InetSocketAddress;

/**
 * An abstract Factory responsible for creating new {@link Server} objects.
 * Until additional functionality is added, it will always create a new {@link SimpleServer}
 */
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
