package uk.knightz.knightzapi.communication.server;

import org.apache.commons.lang.Validate;
import spark.Spark;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.rsa.RSAIO;
import uk.knightz.knightzapi.communication.rsa.RSAKeyGen;
import uk.knightz.knightzapi.communication.server.authorisation.AuthFilter;
import uk.knightz.knightzapi.communication.server.authorisation.AuthMethod;
import uk.knightz.knightzapi.communication.server.authorisation.Whitelist;
import uk.knightz.knightzapi.communication.server.defaultmodules.ValidateModule;
import uk.knightz.knightzapi.communication.server.defaultmodules.controlpanel.LoginModule;
import uk.knightz.knightzapi.module.IncomingRequest;
import uk.knightz.knightzapi.module.Module;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.security.KeyPair;
import java.util.HashSet;
import java.util.Set;

import static spark.Spark.before;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 12:59.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * This class hosts the core functionality asynchronously. It runs the Spark webserver
 * manages modules, and loads RSA certificate information.
 **/
public class Webserver extends Thread {
    private static Webserver instance;
    private static boolean isInitalised = false;
    private final Set<Module> modules;
    private final KeyPair pair;
    private final AuthMethod auth;
    private Whitelist whitelist;

    private Webserver(AuthMethod auth) {
        this.auth = auth;
        KeyPair pair = null;
        try {
            File rsaDir = new File(KnightzAPI.getP().getDataFolder(), "/rsa");
            if (!rsaDir.exists()) {
                rsaDir.mkdir();
                pair = RSAKeyGen.generate(2048);
                RSAIO.save(rsaDir, pair);

            } else {
                pair = RSAIO.load(rsaDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pair = pair;
        new ValidateModule();
        new LoginModule();
        if (auth.equals(AuthMethod.WHITELIST) || auth.equals(AuthMethod.WHITEAUTH)) {
            whitelist = Whitelist.deserialize(KnightzAPI.getWebserverFile().getConfigurationSection("auth").getValues(true));
        }
        this.start();
        modules = new HashSet<>();
    }

    public static Webserver getInstance() {
        return instance;
    }

    static Webserver init(AuthMethod auth) {
        if (isInitalised) return instance;
        isInitalised = true;
        return instance = new Webserver(auth);
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
        Thread.currentThread().setContextClassLoader(KnightzAPI.class.getClassLoader());

        Spark.staticFiles.location("/public");
        before(new AuthFilter());
        WebModule.getAllModules().forEach(WebModule::exec);
        Spark.init();
        Log.normal("[KnightzAPI] Webserver successfully started up!");
    }

    public void registerModule(Module listener) {
        modules.add(listener);
    }

    public void registerModule(WebModule module) {
        Validate.notNull(module);
        module.exec();
    }

    public void callRequest(IncomingRequest request) {
        if (request == null) {
            return;
        }
        if (modules.stream().map(Module::getRequestID).anyMatch(i -> request.getId().equals(i))) {
            for (Module m : modules) {
                String id = m.getRequestID();
                if (request.getId().equals(id)) {
                    m.onIncomingRequest(request);
                    return;
                }
            }
        }
    }
}
