package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.crime.CrimesContainer;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.Scanner;

public class PreventiveCommand implements Command {
    private final CommandHistory commandHistory = ServiciesManager.getInstance().getCommandHistory();

    private final String preventiveCommand = "/arresto add <NOME> 48h CAT1 0 Arresto preventivo";
    private final String declaration = "La dichiaro in arresto preventivo";

    @Override
    public String getDisplayName() {
        return "Arresto preventivo veloce";
    }

    @SneakyThrows
    @Override
    public void execute(Scanner scanner) {
        Utils.clearConsole();
        Utils.printHeader();

        String name = InputUtils.requestString(scanner, "\nInserire nome soggetto: ", 3);

        String finalCommand = preventiveCommand.replace("<NOME>", name);

        System.out.println();
        System.out.println(finalCommand);

        System.out.println();
        System.out.println(declaration);

        commandHistory.addHistory(new CommandHistory.Group(name, new CrimesContainer(declaration, finalCommand, Collections.emptyList())));

        System.in.read(); //Wait for user input
    }
}