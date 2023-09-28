
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

    static void establishPreconditions(String tokenTemplateFilename, String tokenFilename) throws Exception {

        if (!FileManager.exists(tokenTemplateFilename)) {
            Terminal.info(tokenTemplateFilename + " does not exist, generating default token template");
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(tokenTemplateFilename, defaultToken);
        } else {
            Terminal.info("Token template " + tokenFilename + " is used");
        }

        if (!Configuration.keysExist()) {
            Terminal.info("public or private key not exist -> generate new");
            KeyGenerator.generateKeys();
        } else {
            Terminal.info("Keys from are reused from the configuration file");
        }
    }

    public static void generateToken(String uri, String overrideTemplateFilename) throws Exception {
        if (uri == null) {
            uri = "http://localhost:8080";
        }
        String tokenFilename ;
        if (overrideTemplateFilename == null) {
          overrideTemplateFilename = TOKEN_TEMPLATE_FILE_NAME;
          tokenFilename = TOKEN_FILE;
        } else {
            int dotIndex = overrideTemplateFilename.lastIndexOf('.');
            tokenFilename = (dotIndex == -1) ? overrideTemplateFilename : overrideTemplateFilename.substring(0, dotIndex);
            tokenFilename += ".jwt";
        }
        Terminal.printWelcomeMessage();
        establishPreconditions(overrideTemplateFilename, tokenFilename);
        String privateKey = Configuration.loadPrivateKey();
        String publicKey = Configuration.loadPublicKey();
        String jwtToken = JwtTokenGenerator.generateJWTString(overrideTemplateFilename, privateKey);
        Terminal.info("---jwt---");
        Terminal.info(jwtToken);
        Terminal.info("---------");
        FileManager.writeBytes(tokenFilename, jwtToken.getBytes(Charset.defaultCharset()));
        Terminal.info("---");
        MicroProfileConfiguration.generate(publicKey);
        Terminal.info("---mp configuration written");
        Terminal.info("---");

        Terminal.printUserInfo("Enable verbose output with java -Dverbose -jar jwtenizr.jar [optional: an URI for the generated curl]");
        Terminal.printUserInfo("Use custom token filename with java -DtokenSourceFile=\"filename.json\" -jar jwtenizr.jar [optional: an URI for the generated curl]");
        Terminal.printUserInfo("The generated token " + tokenFilename + " contains information loaded from: " + overrideTemplateFilename);
        Terminal.printUserInfo("Adjust the groups[] to configure roles and upn to change the principal in " + overrideTemplateFilename + " then re-execute JWTenizr");
        Terminal.printUserInfo("The iss in " + overrideTemplateFilename + " has to correspond with the mp.jwt.verify.issuer in microprofile-config.properties");
        Terminal.printUserInfo("Copy the microprofile-config.properties to your WAR/src/main/resources/META-INF");
        Terminal.printUserInfo("Use the following command for testing:");
        String command = Curl.command(uri, jwtToken);
        Terminal.printCommand(command);
        Terminal.printDoneMessage();
    }
    
}
