
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.Configuration;
import com.airhacks.jwtenizr.control.Curl;
import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import com.airhacks.jwtenizr.control.Terminal;
import java.nio.charset.Charset;
import javax.json.JsonObject;

public interface Flow {

    static final String TOKEN_FILE_NAME = "jwt-token.json";
    static final String TOKEN_FILE = "token.jwt";

    static void establishPreconditions() throws Exception {

        if (FileManager.exists(TOKEN_FILE_NAME)) {
            Terminal.info(TOKEN_FILE_NAME + " does not exist, generating default token template");
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(TOKEN_FILE_NAME, defaultToken);
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
        establishPreconditions();
        String privateKey = Configuration.loadPrivateKey();
        String jwtToken = JwtTokenGenerator.generateJWTString(TOKEN_FILE_NAME, privateKey);
        Terminal.info("---jwt---");
        Terminal.info(jwtToken);
        Terminal.info("---------");
        FileManager.writeBytes(TOKEN_FILE, jwtToken.getBytes(Charset.defaultCharset()));
        Terminal.info("---");
        String command = Curl.generate(uri, jwtToken);
        Terminal.info(command);

    }


}
