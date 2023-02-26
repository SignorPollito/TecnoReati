package it.signorpollito.utils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class Utils {
    private Utils() {}

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

    public static int parseInt(String input, int fallbackValue) {
        try {
            return Integer.parseInt(input);

        } catch (NumberFormatException ignored) {}

        return fallbackValue;
    }

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
        System.out.println("TecnoReati v2.0.0 - by SMD for SMD");
        System.out.println("Made with love by SignorPollito - @SirPollito\n");
    }
}
