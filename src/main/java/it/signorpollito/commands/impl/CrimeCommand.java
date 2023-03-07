package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.crime.Crime;
import it.signorpollito.crime.CrimeCalculator;
import it.signorpollito.crime.CrimesContainer;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.repository.CrimeRepository;
import it.signorpollito.service.CopyService;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;
import org.apache.commons.lang3.StringUtils;

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
        copyCommands(scanner, crimeCalculator, commands);

        //-------------------------------//

        commandHistory.addHistory(new CommandHistory.Group(crimeCalculator.getName(), commands));

        System.out.println("\nTerminato, premere INVIO per continuare...");
        InputUtils.waitForEnter(scanner);
    }

    private void copyCommands(Scanner scanner, CrimeCalculator calculator, CrimesContainer container) {
        new CopyService(container.getCommandList()).copyEach(phrase -> {
            Utils.clearConsole();
            Utils.printHeader();

            System.out.println("Comandi da eseguire:");
            printCommands(container);
            System.out.println("\n|---------------------------------------------------|\n");
            printCrimes(calculator);

            System.out.printf("\n\n%s\n", StringUtils.abbreviate(phrase, 50));
            System.out.println("Frase copiata, premi INVIO per la prossima!");

            InputUtils.waitForEnter(scanner);
        });
    }

    private boolean askCrimes(Scanner scanner, CrimeCalculator crimeCalculator) {
        boolean crimeNotFound = false;

        while (true) {
            Utils.clearConsole();
            Utils.printHeader();

            System.out.println("- Per rimuovere reato inviare \"--rem <numero>\"");
            System.out.println("- Per uscire o annullare, inserire 'q'.\n\n");

            printCrimes(crimeCalculator);
            System.out.println();

            if(crimeNotFound) System.out.println("> Reato non trovato! <");
            crimeNotFound = false;

            String input = InputUtils.requestString(scanner, "Inserire nome reato: (y per confermare) ");

            if (input.startsWith("y")) break;
            if (input.startsWith("q")) return false;

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

        var crimes = crimeCalculator.getCrimes();
        if(crimes.size()==0) {
            System.out.println("> Nessun crimine selezionato <");
            return;
        }

        for (int i = 0; i < crimes.size(); i++)
            System.out.printf("%d) %s\n", i+1, crimes.get(i).getFullName());

    }

    private void printCommands(CrimesContainer container) {
        container.printCharges();

        System.out.println();
        container.printDeclare();

        System.out.println();
        container.printArrest();
    }
}
