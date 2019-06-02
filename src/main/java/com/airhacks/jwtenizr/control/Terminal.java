
package com.airhacks.jwtenizr.control;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import java.util.Properties;
import jwtenizr.App;

/**
 *
 * @author airhacks.com
 */
public interface Terminal {

    public static void printUserInfo(String message) {
        System.out.printf("%s%s%s\n", TerminalColors.INFO.value(), message, TerminalColors.RESET.value());
    }

    public static void info(String message) {
        if (isVerbose()) {
            System.out.println(message);
        }
    }
    public static void info(byte[] message) {
        if (isVerbose()) {
            System.out.write(message, 0, message.length);
        }
    }

    public static void error(String error) {
        System.err.println(error);
    }

    public static boolean isVerbose() {
        return System.getProperties().containsKey("verbose");
    }

    static void printTime() {
        System.out.printf("[%s%s%s]", TerminalColors.TIME.value(), currentFormattedTime(), TerminalColors.RESET.value());
    }

    public static void printWelcomeMessage() throws IOException {
        try (InputStream resourceAsStream = App.class.
                getClassLoader().
                getResourceAsStream("META-INF/maven/com.airhacks/wad/pom.properties")) {
            if (resourceAsStream == null) {
                System.out.println("\njwtenizr - unknown version\n");
                return;
            }
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            String jwtenizr = properties.getProperty("artifactId");
            String version = properties.getProperty("version");
            printTime();
            System.out.printf("%s %s\n\n", jwtenizr, version);
        }
    }

    static void printDoneMessage() {
        printTime();
        System.out.println("JWT generated");
    }


    static String currentFormattedTime() {
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .toFormatter();

        return LocalTime.now().format(timeFormatter);
    }

    public static void printCommand(String command) {
        System.out.printf("\n\n%s%s%s\n\n", TerminalColors.COMMAND.value(), command, TerminalColors.RESET.value());
    }

}
