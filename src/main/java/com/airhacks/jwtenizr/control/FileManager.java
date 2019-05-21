
package com.airhacks.jwtenizr.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author airhacks.com
 */
public interface FileManager {

    public static boolean exists(String fileName) {
        return Files.exists(Paths.get(fileName));
    }

    static String readString(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    public static String readString(String fileName) throws IOException {
        InputStream stream = new FileInputStream(fileName);
        return readString(stream);
    }

    public static void writeBytes(String fileName, byte[] content) throws IOException {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            out.write(content);
        }
    }


}
