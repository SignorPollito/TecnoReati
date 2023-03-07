package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.crime.CrimesContainer;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.service.CopyService;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.Scanner;

public class PreventiveCommand implements Command {
    private final CommandHistory commandHistory = ServiciesManager.getInstance().getCommandHistory();

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

        String finalCommand = "/arresto add <NOME> 48h CAT1 0 Arresto preventivo".replace("<NOME>", name);

        System.out.println("\n");
        System.out.println(finalCommand);
        CopyService.copy(finalCommand);

        commandHistory.addHistory(new CommandHistory.Group(name, new CrimesContainer(
                "La dichiaro in arresto preventivo",
                finalCommand, Collections.emptyList())
        ));

        System.out.println("\nComando di arresto copiato, premere INVIO per continuare...");
        InputUtils.waitForEnter(scanner);
    }
}