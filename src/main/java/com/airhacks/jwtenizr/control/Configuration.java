
package com.airhacks.jwtenizr.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 *
 * @author airhacks.com
 */
public interface Configuration {

    static final String KEY_NAME = "jwtenizr";
    static final String PRIVATE_KEY_FILE_NAME = KEY_NAME + ".key";
    static final String PUBLIC_KEY_FILE_NAME = KEY_NAME + ".pub";


    public final static String CONFIGURATION_FILE = "jwtenizr-config.json";

    public static JsonObjectBuilder load() throws FileNotFoundException {
        try (JsonReader reader = Json.createReader(new FileInputStream(CONFIGURATION_FILE))) {
            return Json.createObjectBuilder(reader.readObject());
        }
    }

    public static void write(JsonObject configuration) throws FileNotFoundException {
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(CONFIGURATION_FILE))) {
            writer.writeObject(configuration);
        }
    }

    public static JsonObjectBuilder writeDefaultIfNotExists() {
        if (!FileManager.exists(CONFIGURATION_FILE)) {
            JsonObject defaultConfiguration = Json.createObjectBuilder().
                    add("microprofile-config.properties_location", ".").
                    build();
            try {
                write(defaultConfiguration);
            } catch (FileNotFoundException ex) {
                throw new IllegalStateException("Cannot write defualt configuration " + defaultConfiguration + " to file " + CONFIGURATION_FILE);
            }
            Terminal.info(CONFIGURATION_FILE + " default configuration created");
        }
        try {
            return load();
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot find previously written default configuration " + CONFIGURATION_FILE);
        }
    }

    public static boolean keysExist() {
        try {
            JsonObjectBuilder load = load();
            JsonObject configuration = load.build();
            return configuration.isNull(PRIVATE_KEY_FILE_NAME) || configuration.isNull(PUBLIC_KEY_FILE_NAME);
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    public static void storeKeys(String privateKeyString, String publicKeyString) {
        JsonObjectBuilder configuration;
        try {
            configuration = load();
        } catch (FileNotFoundException ex) {
            configuration = writeDefaultIfNotExists();
        }
        configuration.add(PRIVATE_KEY_FILE_NAME, privateKeyString);
    }

}
