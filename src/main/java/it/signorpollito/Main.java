package it.signorpollito;

import it.signorpollito.service.ServiciesManager;
import it.signorpollito.startup.BasicStartup;
import it.signorpollito.utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.printHeader();

        BasicStartup.start();
        Utils.clearConsole();

        while (true) {
            Utils.printHeader();
            ServiciesManager.getInstance().getCommandService().requestCommand();
            Utils.clearConsole();
        }
    }
}