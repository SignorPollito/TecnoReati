package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.Utils;
import lombok.SneakyThrows;

import java.util.Comparator;
import java.util.Scanner;

public class HistoryCommand implements Command {
    private final CommandHistory commandHistory = ServiciesManager.getInstance().getCommandHistory();

    @Override
    public String getDisplayName() {
        return "Vedi storico arresti/multe";
    }

    @SneakyThrows
    @Override
    public void execute(Scanner scanner) {
        Utils.clearConsole();
        Utils.printHeader();

        var groups = commandHistory.getGroups();
        groups.sort(Comparator.comparing(CommandHistory.Group::getDate));

        groups.forEach(group -> {
            var container = group.getContainer();
            System.out.printf("<-> Comandi su %s, ore %s <->\n", group.getCriminal(), group.getFormattedTime());

            System.out.println("\n");
            container.printCharges();

            System.out.println();
            container.printDeclare();

            System.out.println();
            container.printArrest();

            System.out.print("\n\n\n");
        });

        System.in.read(); //Wait for user input
    }
}
