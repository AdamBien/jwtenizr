package com.airhacks.jwtenizr.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

/**
 *
 * @author airhacks.com
 */
public class Configuration {

    public String privateKey;
    public String publicKey;
    public String mpConfiguratioFolder;
    public String mpConfigIssuer;

    private final static String CONFIGURATION_FILE = "jwtenizr-config.json";

    protected Configuration() {
        this.mpConfiguratioFolder = ".";
        this.mpConfigIssuer = "airhacks";
    }

    public void save() {
        Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
        String serialized = jsonb.toJson(this);
        try {
            FileManager.writeBytes(CONFIGURATION_FILE, serialized.getBytes());
        } catch (IOException ex) {
            throw new JWTenizrException("Cannot save configuration " + CONFIGURATION_FILE + " Reason: " + ex.getMessage());

        }
        Terminal.info(CONFIGURATION_FILE + " configuration saved");
    }

    public static Configuration writeDefaultIfNotExists() throws IOException {
        if (!FileManager.exists(CONFIGURATION_FILE)) {
            new Configuration().save();
        }
        return load();
    }

    public static boolean keysExist() {
        if (!FileManager.exists(CONFIGURATION_FILE)) {
            return false;
        }
        Configuration loaded = load();
        return loaded.privateKey != null && loaded.publicKey != null;
    }

    static void delete() throws IOException {
        Files.deleteIfExists(Paths.get(CONFIGURATION_FILE));
    }

    public static String loadPrivateKey() throws FileNotFoundException {
        return load().privateKey;
    }

    public static String loadPublicKey() throws FileNotFoundException {
        return load().publicKey;
    }

    public static String mpConfigurationLocation() {
        return load().mpConfiguratioFolder;
    }

    public static String issuer() {
        return load().mpConfigIssuer;
    }

    static void storeKeys(byte[] privateKeyAsBytes, byte[] publicKeyAsBytes) {
        Configuration loaded = load();
        loaded.privateKey = new String(privateKeyAsBytes);
        loaded.publicKey = new String(publicKeyAsBytes);
        loaded.save();

    }


    public static Configuration load() {
        try {
            String serializedFormat = FileManager.readString(CONFIGURATION_FILE);
            return JsonbBuilder.create().fromJson(serializedFormat, Configuration.class);
        } catch (IOException ex) {
            throw new JWTenizrException("Configuration does not exist " + CONFIGURATION_FILE);

        }
    }

}
