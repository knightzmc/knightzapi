package uk.knightz.knightzapi.communication.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * This class was created by AlexL (Knightz) on 15/02/2018 at 20:10.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
public class RSAKeyGen {

    public static KeyPair generate(int bits) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(bits);
        return generator.generateKeyPair();
    }
}
