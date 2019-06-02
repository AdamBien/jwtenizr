
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.Configuration;
import com.airhacks.jwtenizr.control.Curl;
import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import com.airhacks.jwtenizr.control.MicroProfileConfiguration;
import com.airhacks.jwtenizr.control.Terminal;
import java.nio.charset.Charset;
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
        Terminal.printWelcomeMessage();
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

        Terminal.printUserInfo("Enable verbose output with java -Dverbose -jar jwtenizr.jar [optional: an URI for the generated curl]");
        Terminal.printUserInfo("The generated token " + TOKEN_FILE + " contains information loaded from: " + TOKEN_TEMPLATE_FILE_NAME);
        Terminal.printUserInfo("Adjust the groups[] to configure roles and upn to change the principal in " + TOKEN_TEMPLATE_FILE_NAME + " then re-execute JWTenizr");
        Terminal.printUserInfo("The iss in " + TOKEN_TEMPLATE_FILE_NAME + " has to correspond with the mp.jwt.verify.issuer in microprofile-config.properties");
        Terminal.printUserInfo("Copy the microprofile-config.properties to your WAR/src/main/resources/META-INF");
        Terminal.printUserInfo("Use the following command for testing:");
        String command = Curl.command(uri, jwtToken);
        Terminal.printCommand(command);
        Terminal.printDoneMessage();
    }
    
}
