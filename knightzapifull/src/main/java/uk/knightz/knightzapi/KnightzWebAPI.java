/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi;

import co.aikar.commands.BukkitCommandManager;
import spark.Spark;
import uk.knightz.knightzapi.communication.server.ServerFactory;
import uk.knightz.knightzapi.communication.server.ServerManager;
import uk.knightz.knightzapi.communication.server.SimpleServer;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.communication.server.authorisation.AuthMethod;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.module.ModuleManager;

public class KnightzWebAPI {
	/**
	 * An instance ofGlobal WebServer, running the main Spark server.
	 *
	 * @see Webserver
	 * @see Spark
	 */
	private static Webserver server;
	/**
	 * Instance ofGlobal ServerManager used for managing the WebServer
	 *
	 * @see ServerManager
	 */
	private static ServerManager manager;
	/**
	 * The YAML file containing all configuration for the local web server
	 */
	private static PluginFile webserverFile;
	private static KnightzWebAPI webAPI;


	public static PluginFile getWebserverFile() {
		return webserverFile;
	}

	public static void init(boolean selfServer) {
		if (selfServer) {
			webAPI = new KnightzWebAPI();
			new BukkitCommandManager(KnightzAPI.getP()).registerCommand(new FakeRequestCommand());
			ModuleManager.init(webAPI);
			webserverFile = new PluginFile(KnightzAPI.getP(), "webserver.yml", "webserver.yml");
			manager = ServerManager.getInstance();
			server = manager.initServer(AuthMethod.valueOf(webserverFile.getString("authtype")));
		}
		ServerFactory.FactoryStorage.setInstance(SimpleServer::new);
	}

	private KnightzWebAPI() {
	}

	public static void onDisable() {
		manager.shutdown();
	}
}
