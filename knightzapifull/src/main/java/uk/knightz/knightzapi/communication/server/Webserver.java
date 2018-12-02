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


import lombok.Getter;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import spark.Spark;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.KnightzWebAPI;
import uk.knightz.knightzapi.communication.MainRoute;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.encrypt.KeyStoreGen;
import uk.knightz.knightzapi.communication.encrypt.RSAIO;
import uk.knightz.knightzapi.communication.encrypt.RSAKeyGen;
import uk.knightz.knightzapi.communication.server.authorisation.AuthFilter;
import uk.knightz.knightzapi.communication.server.authorisation.AuthMethod;
import uk.knightz.knightzapi.communication.server.authorisation.ValidateModule;
import uk.knightz.knightzapi.communication.server.authorisation.Whitelist;
import uk.knightz.knightzapi.files.PluginFile;
import uk.knightz.knightzapi.lang.Log;
import uk.knightz.knightzapi.module.IncomingRequest;
import uk.knightz.knightzapi.module.Module;
import uk.knightz.knightzapi.module.ModuleManager;
import uk.knightz.knightzapi.utils.Struct;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static spark.Spark.*;
import static uk.knightz.knightzapi.communication.server.authorisation.AuthMethod.WHITEAUTH;
import static uk.knightz.knightzapi.communication.server.authorisation.AuthMethod.WHITELIST;

/**
 * The Main Web server of the Web API. It wraps a Spark Web server and handles encryption, modulation, and all other aspects of the Web API
 */
@Getter
public class Webserver extends Thread {
    public static final File SECURE_DIR = new File(KnightzAPI.getP().getDataFolder(), "/secure");
    private static final String README_FILENAME = "README.txt";
    private static final List<String> README_LINES = Arrays.asList(
            "This folder contains your public and private keys for encrypting and decrypting data",
            "as said in the name, the private key SHOULD BE KEPT PRIVATE AT ALL TIMES!",
            "exposing it in ANY way will pose a security risk as any data sent to your server can be easily decrypted and read as plain text.",
            "If you have lost yours or suspect it has been discovered, immediately delete public.key and private.key, and restart your server");
    private static Webserver instance;
    private static boolean isInitialised = false;
    private final Set<Module> modules;
    private final KeyPair pair;
    private final KeyStore keyStore;
    private final AuthMethod auth;
    private Whitelist whitelist;


    private Webserver(AuthMethod auth) {
        this.setName("KnightzAPI Web server");
        this.auth = auth;
        //RSA Keys and Keystore
        val struct = loadEncryptionData();
        this.pair = struct.getA();
        this.keyStore = struct.getB();


        new ValidateModule();
        if (auth.equals(WHITELIST) || auth.equals(WHITEAUTH)) {
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

    private Struct<KeyPair, KeyStore> loadEncryptionData() {
        KeyPair pair = null;
        try {
            if (!SECURE_DIR.exists() || Objects.requireNonNull(SECURE_DIR.listFiles()).length == 0) {
                SECURE_DIR.mkdir();
                pair = RSAKeyGen.generate(2048);
                RSAIO.save(SECURE_DIR, pair);

                File readme = new File(SECURE_DIR, README_FILENAME);
                if (!readme.exists())
                    readme.createNewFile();

                Files.write(Paths.get(readme.toURI()), README_LINES);
            } else {
                pair = RSAIO.load(SECURE_DIR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyStore keyStore = loadKeystore(pair);
        return new Struct<>(pair, keyStore);


    }

    private KeyStore loadKeystore(KeyPair pair) {
        val config = KnightzWebAPI.getWebserverFile();
        KeyStore keyStore;
        char[] keystorePass = getKeystorePassword(config);
        keyStore = KeyStoreGen.generate(keystorePass, pair);
        return keyStore;
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

        Spark.secure(Webserver.SECURE_DIR.getPath() + "/keystore.jks", new String(getKeystorePassword()), null, null);
        staticFiles.location("/public");
        post("/requests", new MainRoute());
        before(new AuthFilter());
        WebModule.getAllModules().forEach(WebModule::exec);

//        Bukkit.getScheduler().runTaskAsynchronously(KnightzAPI.getP(), ()->{
//            Spark.awaitInitialization();
//            Log.normal("Webserver successfully started up! Running on port " + port());
//        });
    }


    private char[] getKeystorePassword() {
        return getKeystorePassword(KnightzWebAPI.getWebserverFile());
    }

    @NotNull
    private char[] getKeystorePassword(PluginFile config) {
        String keystorePass = config.getString("keystore-password");
        if (keystorePass == null) {
            Log.normal("No Keystore password found. Generating random password");
            String random = RandomStringUtils.randomAscii(15);
            Log.normal("Keystore password set to " + random);
            config.set("keystore-password", random);
            config.save();
            keystorePass = random;
        }
        return keystorePass.toCharArray();
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


    /**
     * Check if a given object is equal to this one.
     * Webserver is a singleton, so equality can be checked with a simple instanceof check
     *
     * @param o An object
     * @return If the given object is equal to this webserver
     */
    public boolean equals(Object o) {
        return o instanceof Webserver;
    }
}
