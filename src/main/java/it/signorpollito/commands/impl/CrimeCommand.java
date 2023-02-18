package it.signorpollito.commands.impl;

import it.signorpollito.commands.Command;
import it.signorpollito.crime.Crime;
import it.signorpollito.crime.CrimeCalculator;
import it.signorpollito.repository.CommandHistory;
import it.signorpollito.repository.CrimeRepository;
import it.signorpollito.service.ServiciesManager;
import it.signorpollito.utils.InputUtils;
import it.signorpollito.utils.Utils;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public void execute(Scanner scanner) {
        Utils.clearConsole();
        Utils.printHeader();

        CrimeCalculator crimeCalculator = new CrimeCalculator(InputUtils.requestString(scanner, "\nInserire nome soggetto: ", 3));

        askCrimes(scanner, crimeCalculator);

        crimeCalculator.completeQuestions(scanner);

        var commands = crimeCalculator.getCommands();
        String arrestDeclare = crimeCalculator.getArrestDeclare();

        System.out.println();
        commands.forEach(System.out::println);

        System.out.println();
        System.out.println(arrestDeclare);

        commandHistory.addHistory(new CommandHistory.Group(crimeCalculator.getName(), commands, arrestDeclare));

        System.in.read(); //Wait for user input
    }

    private void askCrimes(Scanner scanner, CrimeCalculator crimeCalculator) {
        boolean crimeNotFound = false;

        while (true) {
            Utils.clearConsole();
            Utils.printHeader();

            printCrimes(crimeCalculator, crimeCalculator.getName());

            if(crimeNotFound) System.out.println("> Reato non trovato! <");
            crimeNotFound = false;

            String input = InputUtils.requestString(scanner, "Inserire nome reato: (y per confermare) ");

            if (input.startsWith("y"))
                break;

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
    }

    private Crime parseCrimeInput(Scanner scanner, String input) {
        List<Crime> matches = new ArrayList<>();
        for(var crime : crimeRepository.getCrimes())
            if(StringUtils.containsIgnoreCase(crime.getName(), input))
                matches.add(crime);

        if(matches.size()<=1) return matches.size()==0 ? null : matches.get(0);

        System.out.println("\nPiù occorrenze trovate! ");

        matches.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        int count = 1;
        for (var match : matches)
            System.out.printf("%d) %s%n", count++, match.getName());

        return matches.get(InputUtils.requestInteger(scanner, "Selezionare numero: ", 1, matches.size())-1);
    }

    private void printCrimes(CrimeCalculator crimeCalculator, String criminalName) {
        System.out.println("Reati commessi da ".concat(criminalName));

        int count = 1;
        for(var crime : crimeCalculator.getCrimes())
            System.out.printf("%d) %s\n", count++, crime.getName());

        System.out.println("Per rimuovere reato inviare \"--rem <numero>\"\n");
    }
}
