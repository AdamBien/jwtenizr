package com.airhacks.jwtenizr.control;

import com.nimbusds.jose.jwk.RSAKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public interface JwkConfiguration {
    final static String JWK_FILENAME = "jwk.json";

    static void generate(String publicKey) {
        try {
            RSAPublicKey key = readPublicKey(publicKey);
            RSAKey jwk = new RSAKey.Builder(key)
                    .keyID("jwt.key")
                    .build();
            byte[] jwkBytes = jwk.toJSONString()
                    .getBytes();
            String location = Configuration.mpConfigurationLocation();
            Path path = Paths.get(location, JWK_FILENAME);
            Terminal.info("Writing public to: " + JWK_FILENAME);
            Files.write(path, jwkBytes);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
            throw new JWTenizrException("Couldn't write " + JWK_FILENAME + ":" + ex.getMessage());
        }
    }

    static RSAPublicKey readPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = Base64.getDecoder().decode(publicKey);
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodedKey));
    }

}
