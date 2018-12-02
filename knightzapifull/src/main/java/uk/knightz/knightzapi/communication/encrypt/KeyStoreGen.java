package uk.knightz.knightzapi.communication.encrypt;

import lombok.val;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import uk.knightz.knightzapi.communication.server.Webserver;
import uk.knightz.knightzapi.lang.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.sql.Date;
import java.time.Instant;
import java.time.ZonedDateTime;

public class KeyStoreGen {

    public static KeyStore generate(char[] password, KeyPair pair) {
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

                keyStore.setKeyEntry("post", pair.getPrivate(), password, generateCertificate(pair));
                keyStore.store(new FileOutputStream(file), password);
            }
        } catch (Exception e) {
            Log.severe("Failed to save Keystore file!");
            if (Log.debug()) e.printStackTrace();
        }
        return keyStore;
    }

    private static Certificate[] generateCertificate(KeyPair keyPair) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        val certificates = new Certificate[1];
        X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        cert.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number
        cert.setSubjectDN(new X509Principal("CN=localhost"));  //see examples to add O,OU etc
        cert.setIssuerDN(new X509Principal("CN=localhost")); //same since it is self-signed
        cert.setPublicKey(keyPair.getPublic());
        cert.setNotBefore(Date.from(Instant.now()));
        cert.setNotAfter(Date.from(ZonedDateTime.now().plusYears(1).toInstant()));
        cert.setSignatureAlgorithm("SHA1WithRSAEncryption");
        PrivateKey signingKey = keyPair.getPrivate();
        try {
            certificates[0] = cert.generate(signingKey, "BC");
        } catch (CertificateEncodingException | NoSuchProviderException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return certificates;
    }
}
