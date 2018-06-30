package uk.knightz.knightzapi.communication.rsa;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Static RSA utility methods for encrypting and decrypting blocks ofGlobal
 * information.
 *
 * @author Blake Beaupain
 */
public class RSA {

    /**
     * Encrypts a block ofGlobal data.
     *
     * @param data The data to encrypt
     * @param key  The key to encrypt with
     * @return The encrypted data
     * @throws Exception If an error occurs
     */
    public static Holder encrypt(String data, PublicKey key) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(data.getBytes());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PUBLIC_KEY, key);
        return new Holder(byteCipherText, cipher.doFinal(secKey.getEncoded()));
    }

    /**
     * Decrypts a block ofGlobal data.
     *
     * @param byteCipherText The data to be sent
     * @param encryptedKey   The encrypted AES key used
     * @param key            The key to decrypt with
     * @return The decrypted data
     * @throws Exception If an error occurs
     */
    public static byte[] decrypt(byte[] byteCipherText, byte[] encryptedKey, PrivateKey key) throws Exception {
        //Decrypt RSA encrypted AES data
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, key);
        byte[] decryptedKey = cipher.doFinal(encryptedKey);
        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        return (aesCipher.doFinal((byteCipherText)));
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        String clear = new String(Base64.getDecoder().decode(key64));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear.getBytes());
        KeyFactory fact = KeyFactory.getInstance("DSA");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear.getBytes(), (byte) 0);
        return priv;
    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }

    public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
                PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = new String(Base64.getEncoder().encode(packed));
        Arrays.fill(packed, (byte) 0);
        return key64;
    }

    public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ,
                X509EncodedKeySpec.class);
        return new String(Base64.getEncoder().encode(spec.getEncoded()));
    }

    public static class Holder {
        private final byte[] byteCipherText;
        private final byte[] encryptedKey;

        public Holder(byte[] byteCipherText, byte[] aesKey) {
            this.byteCipherText = byteCipherText;
            this.encryptedKey = aesKey;

        }

        public byte[] getByteCipherText() {
            return byteCipherText;
        }

        public byte[] getEncryptedKey() {
            return encryptedKey;
        }
    }

}
