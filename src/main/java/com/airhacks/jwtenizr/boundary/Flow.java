
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.Configuration;
import com.airhacks.jwtenizr.control.Curl;
import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import com.airhacks.jwtenizr.control.MicroProfileConfiguration;
import com.airhacks.jwtenizr.control.Terminal;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import javax.json.JsonObject;

public interface Flow {

    static final String TOKEN_TEMPLATE_FILE_NAME = "jwt-token.json";
    static final String TOKEN_FILE = "token.jwt";

    static void establishPreconditions() throws Exception {

        if (!FileManager.exists(TOKEN_TEMPLATE_FILE_NAME)) {
            Terminal.info(TOKEN_TEMPLATE_FILE_NAME + " does not exist, generating default token template");
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(TOKEN_TEMPLATE_FILE_NAME, defaultToken);
        } else {
            Terminal.info("Token template " + TOKEN_FILE + " is used");
        }

        if (!Configuration.keysExist()) {
            Terminal.info("public or private key not exist -> generate new");
            KeyGenerator.generateKeys();
        } else {
            Terminal.info("Keys from are reused from the configuration file");
        }
    }

    public static void generateToken(String uri) throws Exception {
        if (uri == null) {
            uri = "http://localhost:8080";

        }
        printWelcomeMessage();
        establishPreconditions();
        String privateKey = Configuration.loadPrivateKey();
        String publicKey = Configuration.loadPublicKey();
        String jwtToken = JwtTokenGenerator.generateJWTString(TOKEN_TEMPLATE_FILE_NAME, privateKey);
        Terminal.info("---jwt---");
        Terminal.info(jwtToken);
        Terminal.info("---------");
        FileManager.writeBytes(TOKEN_FILE, jwtToken.getBytes(Charset.defaultCharset()));
        Terminal.info("---");
        MicroProfileConfiguration.generate(publicKey);
        Terminal.info("---mp configuration written");
        Terminal.info("---");

        Terminal.userInfo("Enable verbose output with java -Dverbose -jar jwtenizr.jar");
        Terminal.userInfo("The generated token contains information loaded from: " + TOKEN_TEMPLATE_FILE_NAME);
        Terminal.userInfo("Adjust the groups[] to configure roles and upn to change the principal, then re-execute JWTenizr");
        Terminal.userInfo("The iss has to correspond with the mp.jwt.verify.issuer in microprofile-config.properties");
        Terminal.userInfo("Use the following command to send a HTTP request containing the JWT generated token: " + TOKEN_FILE);
        Terminal.userInfo("Copy the microprofile-config.properties to your WAR/src/main/resources/META-INF");
        Terminal.userInfo("Use the following command for testing:");
        String command = Curl.command(uri, jwtToken);
        Terminal.userInfo(command);

    }

    static void printWelcomeMessage() throws IOException {
        try (InputStream resourceAsStream = Flow.class.
                getClassLoader().
                getResourceAsStream("META-INF/maven/com.airhacks/jwtenizr/pom.properties")) {
            if (resourceAsStream == null) {
                return;
            }
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            String app = properties.getProperty("artifactId");
            String version = properties.getProperty("version");
            System.out.println(app + " " + version);
        }
    }
    
}
