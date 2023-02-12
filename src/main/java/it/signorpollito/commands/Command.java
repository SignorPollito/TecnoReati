package it.signorpollito.commands;

import java.util.Scanner;

public interface Command {

    String getDisplayName();

    void execute(Scanner scanner);

}
