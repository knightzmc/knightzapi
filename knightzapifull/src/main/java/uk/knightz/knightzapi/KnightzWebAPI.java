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

package uk.knightz.knightzapi;

import co.aikar.commands.BukkitCommandManager;
import spark.Spark;
import uk.knightz.knightzapi.communication.server.*;
import uk.knightz.knightzapi.communication.server.authorisation.AuthMethod;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.module.ModuleManager;

/**
 * Addon to KnightzAPI in charge of handling web communication API
 */
public class KnightzWebAPI {
	/**
	 * An instance of WebServer, running the main Spark server.
	 *
	 * @see Webserver
	 * @see Spark
	 */
	private static Webserver server;
	/**
	 * Instance of ServerManager used for managing the WebServer
	 *
	 * @see ServerManager
	 */
	private static ServerManager manager;
	/**
	 * The YAML file containing all configuration for the local web server
	 */
	private static PluginFile webserverFile;
	private static KnightzWebAPI webAPI;


	private KnightzWebAPI() {
	}
	public static PluginFile getWebserverFile() {
		return webserverFile;
	}
	/**
	 * Initialize the Web API
	 * Called by {@link KnightzAPI#onEnable()}, shouldn't be needed again
	 */
	public static void init() {
		webAPI = new KnightzWebAPI();
		new BukkitCommandManager(KnightzAPI.getP()).registerCommand(new FakeRequestCommand());
		ModuleManager.init(webAPI);
		webserverFile = new PluginFile(KnightzAPI.getP(), "webserver.yml", "webserver.yml");
		manager = ServerManager.getInstance();
		server = manager.initServer(AuthMethod.valueOf(webserverFile.getString("authtype")));
		ServerFactory.FactoryStorage.setInstance(address -> {
			try {
				return new SimpleServer(address);
			} catch (NotAServerException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	public static void onDisable() {
		manager.shutdown();
	}
}
