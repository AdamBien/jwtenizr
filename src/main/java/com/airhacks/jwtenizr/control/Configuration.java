
package com.airhacks.jwtenizr.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    static final String MP_CONFIG_LOCATION_FOLDER = "microprofile-config.properties_location";
    static final String KEY_NAME = "jwtenizr";
    static final String PRIVATE_KEY_NAME = KEY_NAME + ".key";
    static final String PUBLIC_KEY_NAME = KEY_NAME + ".pub";


    public final static String CONFIGURATION_FILE = "jwtenizr-config.json";

    public static JsonObjectBuilder load() throws FileNotFoundException {
        try (JsonReader reader = Json.createReader(new FileInputStream(CONFIGURATION_FILE))) {
            return Json.createObjectBuilder(reader.readObject());
        }
    }

    public static void write(JsonObject configuration) {
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(CONFIGURATION_FILE))) {
            writer.writeObject(configuration);
        } catch (FileNotFoundException ex) {
            throw new JWTenizrException("cannot write configuration to: " + CONFIGURATION_FILE);
        }
    }

    public static JsonObjectBuilder writeDefaultIfNotExists() {
        if (!FileManager.exists(CONFIGURATION_FILE)) {
            JsonObject defaultConfiguration = Json.createObjectBuilder().
                    add(MP_CONFIG_LOCATION_FOLDER, ".").
                    build();
            write(defaultConfiguration);
            Terminal.info(CONFIGURATION_FILE + " default configuration created");
        }
        try {
            return load();
        } catch (FileNotFoundException ex) {
            throw new JWTenizrException("Cannot find previously written default configuration " + CONFIGURATION_FILE);
        }
    }

    public static boolean keysExist() {
        try {
            JsonObjectBuilder load = load();
            JsonObject configuration = load.build();

            return configuration.containsKey(PRIVATE_KEY_NAME) && configuration.containsKey(PUBLIC_KEY_NAME);
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
        JsonObject updatedConfiguration = configuration.add(PRIVATE_KEY_NAME, privateKeyString).
                add(PUBLIC_KEY_NAME, publicKeyString).build();
        write(updatedConfiguration);
    }

    public static String loadPrivateKey() throws FileNotFoundException {
        return getValue(PRIVATE_KEY_NAME);
    }

    static void delete() throws IOException {
        Files.deleteIfExists(Paths.get(CONFIGURATION_FILE));
    }

    public static String getValue(String key) {
        JsonObject configuration = null;
        try {
            configuration = load().build();
        } catch (FileNotFoundException ex) {
            Terminal.info(ex.getMessage());
            throw new JWTenizrException("Configuration entry for " + key + " not found in: " + configuration);
        }
        return configuration.getString(key);
    }

    public static String mpConfigurationLocation() {
        return getValue(MP_CONFIG_LOCATION_FOLDER);
    }

}
