
package com.airhacks.jwtenizr.boundary;

import com.airhacks.jwtenizr.control.FileManager;
import com.airhacks.jwtenizr.control.JwtTokenGenerator;
import com.airhacks.jwtenizr.control.KeyGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.json.JsonObject;

public interface Flow {

    static final String KEY_FILE_NAME = "jwtenizr";
    static final String PRIVATE_KEY_FILE_NAME = KEY_FILE_NAME + ".key";
    static final String TOKEN_FILE_NAME = "jwt-token.json";

    static void establishPreconditions() throws IOException {
        if (!Files.exists(Paths.get(TOKEN_FILE_NAME))) {
            JsonObject defaultToken = JwtTokenGenerator.createDefaultToken();
            FileManager.write(KEY_FILE_NAME, defaultToken);
        }
    }

    public static void generatedToken() throws Exception {
        establishPreconditions();
        KeyGenerator generator = new KeyGenerator(KEY_FILE_NAME);
        generator.generateKeys();
        JwtTokenGenerator.generateJWTString(TOKEN_FILE_NAME, PRIVATE_KEY_FILE_NAME);
    }


}
