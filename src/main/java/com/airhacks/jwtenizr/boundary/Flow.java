
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import com.airhacks.jwtenizr.control.Terminal;
import java.nio.charset.Charset;
import javax.json.JsonObject;

public interface Flow {

    static final String KEY_FILE_NAME = "jwtenizr";
    static final String PRIVATE_KEY_FILE_NAME = KEY_FILE_NAME + ".key";
    static final String PUBLIC_KEY_FILE_NAME = KEY_FILE_NAME + ".pub";
    static final String TOKEN_FILE_NAME = "jwt-token.json";
    static final String TOKEN_FILE = "token.jwt";

    static void establishPreconditions() throws Exception {
        if (FileManager.exists(KEY_FILE_NAME)) {
            Terminal.info(TOKEN_FILE_NAME + " does not exist, generating default one");
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(TOKEN_FILE_NAME, defaultToken);
        } else {
            Terminal.info("Token template " + TOKEN_FILE + " is used");
        }
        if (!FileManager.exists(PRIVATE_KEY_FILE_NAME) || !FileManager.exists(PUBLIC_KEY_FILE_NAME)) {
            Terminal.info("public or private key not exist -> generate new");
            KeyGenerator generator = new KeyGenerator(KEY_FILE_NAME);
            generator.generateKeys();
        } else {
            Terminal.info(PRIVATE_KEY_FILE_NAME + " " + PUBLIC_KEY_FILE_NAME + " are reused");
        }
    }

    public static void generateToken() throws Exception {
        establishPreconditions();
        String jwtToken = JwtTokenGenerator.generateJWTString(TOKEN_FILE_NAME, PRIVATE_KEY_FILE_NAME);
        Terminal.info("---jwt---");
        Terminal.info(jwtToken);
        Terminal.info("---------");
        FileManager.writeBytes(TOKEN_FILE, jwtToken.getBytes(Charset.defaultCharset()));
    }


}
