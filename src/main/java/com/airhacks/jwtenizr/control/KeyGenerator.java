
package com.airhacks.jwtenizr.control;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
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
        PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(publicKey.getEncoded());
        byte[] privateKeyString = makeWritable(privateKey);
        byte[] publicKeyString = makeWritable(encoded.getEncoded());
        Terminal.info("public key---");
        Terminal.info(publicKeyString);
        Terminal.info("\n---");

        Configuration.storeKeys(privateKeyString, publicKeyString);
        return pair;
    }

    static byte[] makeWritable(Key key) {
        byte[] encoded = key.getEncoded();
        return makeWritable(encoded);
    }

    static byte[] makeWritable(byte[] content) {
        return Base64.getEncoder().encode(content);
    }

}
