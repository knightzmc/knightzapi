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

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:26.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
