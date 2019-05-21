package com.airhacks.jwtenizr.control;

import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import static com.nimbusds.jwt.JWTClaimsSet.parse;
import com.nimbusds.jwt.SignedJWT;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.json.Json;
import javax.json.JsonObject;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import static net.minidev.json.parser.JSONParser.DEFAULT_PERMISSIVE_MODE;
import org.eclipse.microprofile.jwt.Claims;

public interface JwtTokenGenerator {

    public static String generateJWTString(String tokenFile, String privateKeyFile) throws Exception {
        Terminal.info("Reading token: " + tokenFile);
        Terminal.info("Using private key: " + privateKeyFile);
        String token = FileManager.readString(tokenFile);
        JSONParser parser = new JSONParser(DEFAULT_PERMISSIVE_MODE);
        JSONObject jwtJson = (JSONObject) parser.parse(token);
        
        long currentTimeInSecs = (System.currentTimeMillis() / 1000);
        long expirationTime = currentTimeInSecs + 1000;
       
        jwtJson.put(Claims.iat.name(), currentTimeInSecs);
        jwtJson.put(Claims.auth_time.name(), currentTimeInSecs);
        jwtJson.put(Claims.exp.name(), expirationTime);

        JWSHeader header = new JWSHeader.Builder(RS256)
                .keyID("jwt.key").
                type(JWT).
                build();

        JWTClaimsSet claimSet = parse(jwtJson);
        SignedJWT signedJWT = new SignedJWT(header, claimSet);
        PrivateKey privateKey = readPrivateKey(privateKeyFile);
        RSASSASigner signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        
        return signedJWT.serialize();
    }
    
    public static PrivateKey readPrivateKey(String privateKeyFile) throws Exception {
        String readableStream = FileManager.readString(privateKeyFile);

        byte[] decodedKey = Base64.getDecoder().decode(readableStream);
        
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

    static JsonObject createDefaultToken() {
        return Json.createObjectBuilder().
                add("iss", "airhacks").
                add("jti", "42").
                add("sub", "duke").
                add("upn", "duke").
                add("groups", Json.createArrayBuilder().
                        add("chief").
                        add("hacker").
                        build()).
                build();
    }
}
