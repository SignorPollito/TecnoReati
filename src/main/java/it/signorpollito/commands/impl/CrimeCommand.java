package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.crime.Crime;
import it.signorpollito.crime.CrimeCalculator;
import it.signorpollito.crime.CrimesContainer;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.repository.CrimeRepository;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrimeCommand implements Command {
    private final CrimeRepository crimeRepository = ServiciesManager.getInstance().getCrimeRepository();
    private final CommandHistory commandHistory = ServiciesManager.getInstance().getCommandHistory();

    @Override
    public String getDisplayName() {
        return "Calcola la pena di un soggetto";
    }

    @SneakyThrows
    @Override
    public void execute(Scanner scanner) {
        Utils.clearConsole();
        Utils.printHeader();

        CrimeCalculator crimeCalculator = new CrimeCalculator(InputUtils.requestString(scanner, "\nInserire nome soggetto: ", 3));

        if(!askCrimes(scanner, crimeCalculator))
            return;

        crimeCalculator.completeQuestions(scanner);

        //-------------------------------//

        var commands = crimeCalculator.getCrimeCommands();
        execute(crimeCalculator, commands);

        //-------------------------------//

        commandHistory.addHistory(new CommandHistory.Group(crimeCalculator.getName(), commands));

        System.out.println("\nTerminato, premere un tasto per continuare...");
        System.in.read(); //Wait for user input
    }

    @SneakyThrows
    private void execute(CrimeCalculator calculator, CrimesContainer container) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        var commands = container.getAll();

        for(int i=0; i<commands.size(); i++) {
            clipboard.setContents(new StringSelection(commands.get(i)), null);

            Utils.clearConsole();
            Utils.printHeader();

            printCrimes(calculator);
            printCommands(container);

            System.out.printf("\n> Comando n.%d copiato! <\n", i+1);
            System.out.println("Premi invio per copiare il possimo");

            int input;
            do { //To fix a Java bad design
                input = System.in.read();
            } while (input==10);
        }
    }

    private boolean askCrimes(Scanner scanner, CrimeCalculator crimeCalculator) {
        boolean crimeNotFound = false;

        while (true) {
            Utils.clearConsole();
            Utils.printHeader();

            System.out.println("- Per rimuovere reato inviare \"--rem <numero>\"");
            System.out.println("- Per uscire o annullare, inserire 'q'.\n");

            printCrimes(crimeCalculator);
            System.out.println();

            if(crimeNotFound) System.out.println("> Reato non trovato! <");
            crimeNotFound = false;

            String input = InputUtils.requestString(scanner, "Inserire nome reato: (y per confermare) ");

            if (input.startsWith("y"))
                break;

            if (input.startsWith("q"))
                return false;

            System.out.println(input);

            if(input.startsWith("--rem")) {
                String[] fields = input.split(" ");
                if(fields.length==1) continue;

                crimeCalculator.removeCrime(Math.max(0, Math.min(crimeCalculator.getCrimesCount(), Utils.parseInt(fields[1], 0)-1)));
                continue;
            }

            Crime crime = parseCrimeInput(scanner, input);

            if (crime == null) {
                crimeNotFound = true;
                continue;
            }

            crimeCalculator.addCrime(crime.commitCrime());
        }

        return true;
    }

    private Crime parseCrimeInput(Scanner scanner, String input) {
        List<Crime> matches = new ArrayList<>();
        for(var crime : crimeRepository.getCrimes())
            if(crime.match(input, true, true))
                matches.add(crime);

        if(matches.size()<=1) return matches.size()==0 ? null : matches.get(0);

        System.out.println("\nPiù occorrenze trovate! ");

        matches.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        int count = 1;
        for (var match : matches)
            System.out.printf("%d) %s%n", count++, match.getFullName());

        return matches.get(InputUtils.requestInteger(scanner, "Selezionare numero: ", 1, matches.size())-1);
    }

    private void printCrimes(CrimeCalculator crimeCalculator) {
        System.out.println("Reati commessi da ".concat(crimeCalculator.getName()));

        int count = 1;
        for(var crime : crimeCalculator.getCrimes())
            System.out.printf("%d) %s\n", count++, crime.getFullName());
    }

    private void printCommands(CrimesContainer container) {
        System.out.println("\n");
        container.printCharges();

        System.out.println();
        container.printDeclare();

        System.out.println();
        container.printArrest();
    }
}
