
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
public class KeyGenerator {

    private final String keyFile;


    public KeyGenerator(String keyFileWithoutEnding) {
        if (keyFileWithoutEnding.contains(".")) {
            throw new IllegalArgumentException("The file name " + keyFileWithoutEnding + " contains a .");
        }
        this.keyFile = keyFileWithoutEnding;
    }


    public KeyPair generateKeys() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        byte[] readable = makeWritable(privateKey);
        FileManager.writeBytes(this.keyFile + ".key", readable);
        Terminal.info("public key---");
        Terminal.info(readable);
        Terminal.info("\n---");

        readable = makeWritable(publicKey);
        FileManager.writeBytes(this.keyFile + ".pub", readable);
        return pair;
    }

    byte[] makeWritable(Key key) {
        byte[] encoded = key.getEncoded();
        return Base64.getEncoder().encode(encoded);
    }

}
