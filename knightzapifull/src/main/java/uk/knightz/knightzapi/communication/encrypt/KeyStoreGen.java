package uk.knightz.knightzapi.communication.encrypt;

import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeyStoreGen {

    public static KeyStore generate(char[] password) {
        KeyStore keyStore = null;
        try {
            File file = new File(Webserver.SECURE_DIR, "keystore.jks");
            keyStore = KeyStore.getInstance("JKS");
            if (file.exists()) {
                // if exists, load
                keyStore.load(new FileInputStream(file), password);
            } else {
                //if doesn't exist, create
                file.createNewFile();
                keyStore.load(null, password);
                keyStore.store(new FileOutputStream(file), password);
            }
        } catch (Exception e) {
            Log.severe("Failed to save Keystore file!");
            if (Log.debug()) e.printStackTrace();
        }
        return keyStore;
    }
}
