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


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.Validate;
import spark.Spark;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.KnightzWebAPI;
import uk.knightz.knightzapi.communication.MainRoute;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.rsa.RSAIO;
import uk.knightz.knightzapi.communication.rsa.RSAKeyGen;
import uk.knightz.knightzapi.communication.server.authorisation.AuthFilter;
import uk.knightz.knightzapi.communication.server.authorisation.AuthMethod;
import uk.knightz.knightzapi.communication.server.authorisation.ValidateModule;
import uk.knightz.knightzapi.communication.server.authorisation.Whitelist;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.module.IncomingRequest;
import uk.knightz.knightzapi.module.Module;
import uk.knightz.knightzapi.module.ModuleManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static spark.Spark.*;

/**
 * The Main Webserver of the Web API. It wraps a Spark Webserver and handles encryption, modulation, and all other aspects of the Web API
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Webserver extends Thread {
	private static Webserver instance;
	private static boolean isInitialised = false;
	private final Set<Module> modules;
	private final KeyPair pair;
	private final AuthMethod auth;
	private Whitelist whitelist;


	private Webserver(AuthMethod auth) {
		this.setName("KnightzAPI Webserver");
		this.auth = auth;
		//RSA Keys
		{
			KeyPair pair = null;
			try {
				File rsaDir = new File(KnightzAPI.getP().getDataFolder(), "/rsa");
				if (!rsaDir.exists() || Objects.requireNonNull(rsaDir.listFiles()).length == 0) {
					rsaDir.mkdir();
					pair = RSAKeyGen.generate(2048);
					RSAIO.save(rsaDir, pair);
					new File(rsaDir, "README.txt").createNewFile();
					Files.write(Paths.get(new File(rsaDir, "README.txt").toURI()),
							Arrays.asList("This folder contains your public and private keys for encrypting and decrypting data",
									"as said in the name, the private key SHOULD BE KEPT PRIVATE AT ALL TIMES!",
									"exposing it in ANY way will pose a security risk as any data sent to your server can be easily decrypted and read as plain text."
									, "If you have lost yours or suspect it has been discovered, immediately delete public.key and private.key, and restart your server"));
				} else {
					pair = RSAIO.load(rsaDir);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.pair = pair;
		}


		new ValidateModule();
		if (auth.equals(AuthMethod.WHITELIST) || auth.equals(AuthMethod.WHITEAUTH)) {
			whitelist = Whitelist.deserialize(KnightzWebAPI.getWebserverFile().getConfigurationSection("auth").getValues(true));
		}
		this.start();
		modules = ModuleManager.getManager().getAllModules();
	}

	public static Webserver getInstance() {
		return instance;
	}

	static Webserver init(AuthMethod auth) {
		if (isInitialised) return instance;
		isInitialised = true;
		return instance = new Webserver(auth);
	}

	public void shutdown() {
		instance = null;
		Spark.stop();
	}

	public Set<Module> getModules() {
		return modules;
	}

	public Whitelist getWhitelist() {
		return whitelist;
	}

	public AuthMethod getAuth() {
		return auth;
	}

	public KeyPair getPair() {
		return pair;
	}

	@Override
	public void run() {
		//Allow Spark to load static files from Bukkit
		setContextClassLoader(KnightzWebAPI.class.getClassLoader());
		staticFiles.location("/public");
		post("/requests", new MainRoute());
		before(new AuthFilter());
		WebModule.getAllModules().forEach(WebModule::exec);
		Spark.init();
		Log.normal("[KnightzWebAPI] Webserver successfully started up! Running on port " + port());
	}

	public void registerModule(WebModule module) {
		Validate.notNull(module);
		module.exec();
	}

	public void callRequest(IncomingRequest request) {
		if (request == null) {
			return;
		}
		for (Module m : modules) {
			String id = m.getRequestID();
			if (request.getId().equals(id)) {
				m.onIncomingRequest(request);
				return;
			}
		}
	}

}
