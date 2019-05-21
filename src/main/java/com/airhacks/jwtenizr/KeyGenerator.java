
package com.airhacks.jwtenizr;

import java.io.FileOutputStream;
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
        this.keyFile = keyFileWithoutEnding;
    }


    public KeyPair generateKeys() throws IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        try (FileOutputStream out = new FileOutputStream(this.keyFile + ".key")) {
            byte[] readable = makeReadable(privateKey);
            out.write(readable);

        }

        try (FileOutputStream out = new FileOutputStream(this.keyFile + ".pub")) {
            byte[] readable = makeReadable(publicKey);
            out.write(readable);
        }
        return pair;
    }

    byte[] makeReadable(Key key) {
        byte[] encoded = key.getEncoded();
        return Base64.getEncoder().encode(encoded);
    }

}
