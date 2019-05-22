
package com.airhacks.jwtenizr.control;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 *
 * @author airhacks.com
 */
public interface KeyGenerator {


    public static KeyPair generateKeys() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String privateKeyString = makeWritable(privateKey);
        String publicKeyString = makeWritable(publicKey);
        Terminal.info("public key---");
        Terminal.info(privateKeyString);
        Terminal.info("\n---");

        Configuration.storeKeys(privateKeyString, publicKeyString);

        MicroProfileConfiguration.generate(privateKeyString);
        return pair;
    }

    static String makeWritable(Key key) {
        byte[] encoded = key.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

}
