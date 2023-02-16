package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebCommand implements Command {
    private final Site[] sites = {
            new Site("Codice penale", "https://docs.google.com/document/d/11n607oa4jbNTADLznxBjV80b3Kuaj4dr0y4MxDakvBE"),
            new Site("Codice civile", "https://docs.google.com/document/d/1JRMVVJn5JRsmwF3gl0zHjlhu6gUMsqAh5Q4cCFQdvCM"),
            new Site("Codice stradale", "https://docs.google.com/document/d/148aM4VqIpN3jfodK8Dr6pExzYMUaB_byy0S17w_YzcE"),
            new Site("Lista reati", "https://docs.google.com/spreadsheets/d/1CzQoZbGMeN9Z59ZeZU9No8actcZneGEIgevOao9zEFs")
    };

    @Override
    public String getDisplayName() {
        return "Apri un codice (penale, civile o stradale)";
    }

    @Override
    public void execute(Scanner scanner) {
        do {
            Utils.clearConsole();
            System.out.println("Ecco di codici disponibili:");

            for (int i = 0; i < sites.length; i++)
                System.out.printf("%d) %s\n", i+1, sites[i].name());

            System.out.println();
            try {
                int selection = InputUtils.requestInteger(scanner, "Selezionare opzione: ", 1, sites.length);

                if(Utils.openWebpage(new URL(sites[selection-1].url())))
                    continue; //All ok, looping

            } catch (MalformedURLException ignored) {}

            //Something went wrong
            System.out.println("Impossibile aprire il browser!");

        } while (InputUtils.requestYes(scanner, "Vorresti aprirne un altro? (y/n) "));
    }


    record Site(String name, String url) { }
}
