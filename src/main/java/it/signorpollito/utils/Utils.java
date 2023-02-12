package it.signorpollito.utils;

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

    public static void printHeader() {
        System.out.println("TecnoReati v1.0 - by SMD for SMD");
        System.out.println("Made with love by SignorPollito - @SirPollito\n");
    }
}
