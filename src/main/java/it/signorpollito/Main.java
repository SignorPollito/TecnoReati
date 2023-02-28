package it.signorpollito;

import it.signorpollito.service.ServiciesManager;
import it.signorpollito.startup.Startup;
import it.signorpollito.utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.clearConsole();
        Utils.printHeader();

        Startup.start();
        Utils.clearConsole();

        while (true) {
            Utils.printHeader();
            ServiciesManager.getInstance().getCommandService().requestCommand();
            Utils.clearConsole();
        }
    }
}