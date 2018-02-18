package uk.knightz.knightzapi.communication.server;

import org.apache.commons.lang.Validate;
import spark.Spark;
import uk.knightz.knightzapi.KnightzAPI;
import uk.knightz.knightzapi.communication.MainRoute;
import uk.knightz.knightzapi.communication.WebModule;
import uk.knightz.knightzapi.communication.rsa.RSAIO;
import uk.knightz.knightzapi.communication.rsa.RSAKeyGen;
import uk.knightz.knightzapi.communication.server.authorisaton.AuthFilter;
import uk.knightz.knightzapi.communication.server.authorisaton.AuthMethod;
import uk.knightz.knightzapi.communication.server.authorisaton.Whitelist;
import uk.knightz.knightzapi.communication.server.defaultmodules.ValidateModule;

import java.io.File;
import java.security.KeyPair;

/**
 * This class was created by AlexL (Knightz) on 14/02/2018 at 12:59.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * This class hosts the core functionality asyncronously. It runs the Spark webserver
 * manages modules, and loads RSA certificate information.
 **/
public class Webserver extends Thread {
    private static Webserver instance;
    private static boolean isInitalised = instance != null;
    private final KeyPair pair;
    private final AuthMethod auth;
    private Whitelist whitelist;
    public Whitelist getWhitelist() {
        return whitelist;
    }

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
        registerModule(new ValidateModule());
        if (auth.equals(AuthMethod.WHITELIST) || auth.equals(AuthMethod.WHITEAUTH)) {
            whitelist = Whitelist.deserialize(KnightzAPI.getWebserverFile().getConfigurationSection("auth").getValues(true));

        }
        this.start();
    }

    public static Webserver getInstance() {
        return instance;
    }

    static Webserver init(AuthMethod auth) {
        if (isInitalised) return instance;
        isInitalised = true;
        return instance = new Webserver(auth);
    }

    public AuthMethod getAuth() {
        return auth;
    }

    public KeyPair getPair() {
        return pair;
    }

    @Override
    public void run() {
        Spark.init();
        Spark.before(new AuthFilter());
        Spark.get("/", new MainRoute());
        Spark.post("/", new MainRoute());
        WebModule.getAllModules().forEach(WebModule::exec);
    }

    public void registerModule(WebModule module) {
        Validate.notNull(module);
        module.exec();
    }
}
