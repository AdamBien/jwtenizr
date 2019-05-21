
package com.airhacks.jwtenizr.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author airhacks.com
 */
public interface ConfigurationLoader {

    public static JsonObject loadConfiguration(String configuration) throws FileNotFoundException {
        try (JsonReader reader = Json.createReader(new FileInputStream(configuration))) {
            return reader.readObject();
        }
    }

}
