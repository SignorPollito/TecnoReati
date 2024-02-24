package it.signorpollito.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class Utils {
    private Utils() {}

    /**
     * Clears the console.
     */
    public static void clearConsole(){
        try {
            ProcessBuilder processBuilder;

            if(System.getProperty("os.name").contains("Windows")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                processBuilder = new ProcessBuilder("clear");
            }

            processBuilder.inheritIO().start().waitFor();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes the round brackets and its content from the provided string.
     *
     * @param string The string
     * @return The string without round brackets
     */
    public static String removeBrackets(String string) {
        return string.split("\\(")[0].strip();
    }

    /**
     * Gets the content inside round brackets of the provided string.
     *
     * @param string The string
     * @return The content inside round brackets
     */
    public static String getBracketsContent(String string) {
        return string.substring(string.indexOf("(")+1, Math.max(0, string.indexOf(")")));
    }

    /**
     * Parses a string to an integer.
     *
     * @param input The input string
     * @param fallbackValue The value that will be returned if the string is not an integer
     * @return The parsed integer
     */
    public static int parseInt(String input, int fallbackValue) {
        try {
            return Integer.parseInt(input);

        } catch (NumberFormatException ignored) {}

        return fallbackValue;
    }

    /**
     * Opens a web page by providing the URL.
     *
     * @param url The URL
     * @return True, if the webpage was successfully opened, otherwise false
     */
    public static boolean openWebpage(URL url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE))
            return false;

        try {
            desktop.browse(url.toURI());
            return true;
        } catch (IOException | RuntimeException | URISyntaxException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void printHeader() {
        System.out.println("---------------------------------------------------");
        System.out.println("|       TecnoReati v2.3.0b - by SMD for SMD       |");
        System.out.println("|  Made with love by SignorPollito - @SirPollito  |");
        System.out.println("---------------------------------------------------\n");
    }
}
