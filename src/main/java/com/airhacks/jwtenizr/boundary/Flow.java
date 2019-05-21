
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import com.airhacks.jwtenizr.control.Terminal;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.json.JsonObject;

public interface Flow {

    static final String KEY_FILE_NAME = "jwtenizr";
    static final String PRIVATE_KEY_FILE_NAME = KEY_FILE_NAME + ".key";
    static final String TOKEN_FILE_NAME = "jwt-token.json";
    static final String TOKEN_FILE = "token.jwt";

    static void establishPreconditions() throws IOException {
        if (!Files.exists(Paths.get(TOKEN_FILE_NAME))) {
            Terminal.info(TOKEN_FILE_NAME + " does not exist");
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(TOKEN_FILE_NAME, defaultToken);
        }
    }

    public static void generateToken() throws Exception {
        establishPreconditions();
        KeyGenerator generator = new KeyGenerator(KEY_FILE_NAME);
        generator.generateKeys();
        String jwtToken = JwtTokenGenerator.generateJWTString(TOKEN_FILE_NAME, PRIVATE_KEY_FILE_NAME);
        Terminal.info(jwtToken);
        FileManager.writeBytes(TOKEN_FILE, jwtToken.getBytes(Charset.defaultCharset()));
    }


}
