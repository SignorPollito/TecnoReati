package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.site.Site;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class WebCommand implements Command {
    @Override
    public String getDisplayName() {
        return "Apri un codice (penale, civile o stradale)";
    }

    @Override
    public void execute(Scanner scanner) {
        List<Site> sites = ServiciesManager.getInstance().getSiteRepository().getSites();

        do {
            Utils.clearConsole();
            System.out.println("Ecco di codici disponibili:");

            for (int i = 0; i < sites.size(); i++)
                System.out.printf("%d) %s\n", i+1, sites.get(i).name());

            System.out.println();
            try {
                int selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, sites.size());

                if(Utils.openWebpage(new URL(sites.get(selection-1).url())))
                    continue; //All ok, looping

            } catch (MalformedURLException ignored) {}

            //Something went wrong
            System.out.println("Impossibile aprire il browser!");

        } while (InputUtils.requestYes(scanner, "Vorresti aprire un altro codice? (y/n) "));
    }
}
