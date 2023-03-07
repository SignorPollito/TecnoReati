package it.signorpollito.commands;

import it.signorpollito.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandService {

    private final List<Command> commands;
    private final Scanner scanner;

    public CommandService() {
        this.commands = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    private void executeCommand(int index) {
        this.commands.get(index).execute(scanner);
    }

    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    public void requestCommand() {
        System.out.println("Lista dei comandi disponibili.");

        int count = 1;
        for(var command : commands)
            System.out.printf("%d) %s\n", count++, command.getDisplayName());

        executeCommand(InputUtils.requestInteger(scanner, "Selezionare comando: ", 1, commands.size()) - 1);
    }
}
