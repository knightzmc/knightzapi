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

package uk.knightz.knightzapi.communication.server;

import lombok.Data;
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
		System.out.println(port());
		Log.normal("[KnightzWebAPI] Webserver successfully started up!");
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
